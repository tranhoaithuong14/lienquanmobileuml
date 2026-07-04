# 0003 — Hero full combat-stats modeling (research deliverable)

## Status

**Superseded by implementation under `com.moba.combat`** — research deliverable retained as historical record of the primary-source findings that shaped the value-object decomposition (`BaseStats` = `CoreStats` + `OffensiveStats` + `AttackRange`). The implementation kept the directional semantics (pen subtracts before mitigation; life steal is post-mitigation; critDamage ≥ 1.0; cooldownReduction ∈ [0, 0.40]) and dropped the multi-client cross-checks that were useful during research but have no place in the teaching artifact.

> Note on language: prose is English for density; domain terms use the ubiquitous language from `CONTEXT.md` (Hero, Enemy, currentHp, maxHp, alive, Position, TargetSelector, NearestEnemy, LowestHP). ADR-0001 and ADR-0002 remain the binding constraints.

## Context

The original `CombatStats` (later renamed during the rename pass) is **only an HP state machine**: `maxHp`, `currentHp`, `alive` + `takeDamage/heal/respawn/isAlive`. It holds no attack, armor, mana, crit, or range data. A MOBA hero detail screen shows roughly 16 stats. This ADR investigates how to model the full sheet.

Reference stat sheet — a ranged marksman archetype, values typical for the genre:

| Group | Stat | Value |
|---|---|---|
| Core | maxHp | ~3600 |
| Core | armor | ~140 (+0%) |
| Core | attackDamage | ~174 |
| Core | magicDefense | ~80 (+0%) |
| Core | abilityPower | 0 |
| Core | maxMana | ~440 |
| Offensive | movementSpeed | 360 |
| Offensive | attackSpeed bonus | +0% |
| Offensive | armorPen (flat / %) | 0 / 0% |
| Offensive | magicPen (flat / %) | 0 / 0% |
| Offensive | critChance | 0% |
| Offensive | critDamage | 200% |
| Offensive | lifeSteal | 0% |
| Offensive | spellVamp | 0% |
| Offensive | cooldownReduction | 0% |
| Offensive | attackRange | RANGED |

> **Cross-check note.** A second source confirmed the *field structure* but listed different *numbers* (different region/patch/level snapshot). **Conclusion: model the field structure, not the literal numbers — the numbers are per-hero data that lives in a catalog, not in code.** The cross-source also surfaced two stats absent from the primary screenshot but present in the client: **HP/5s** and **Mana/5s** (regeneration), which the model should leave room for.

## Investigation

### R1 — Which stats are pure values, which are derived, which are stateful?

**Primary-source findings.**

- *Value Object* (Evans, *Domain-Driven Design*, 2003, Ch. 5): an object that "describes some characteristic of a thing" and has **no conceptual identity** — it is defined only by its attributes and should be treated as **immutable**. A hero's base attack of 174 is not a thing with a lifecycle; it is a value.
- *Minimize mutability* (Bloch, *Effective Java*, 3rd ed., Item 17): "Classes should be immutable unless there's a very good reason to make them mutable." Immutable objects are simple, thread-safe, and freely shareable. The static stat sheet is a textbook immutable-value case.
- *Entity* (Evans, Ch. 5): defined by a **thread of continuity and identity**, not attributes. A `Hero` is an Entity (it has identity "Yorn", persists through HP changes). `currentHp` is the mutable state *of* that Entity — which is exactly why ADR-0002 kept it mutable in a state machine.

**Classification.**

- **Pure values (candidates for `final` fields on an immutable Value Object):** maxHp, armor, attackDamage, magicDefense, abilityPower, maxMana, movementSpeed, attackSpeed bonus, armorPen (flat + %), magicPen (flat + %), critChance, critDamage, lifeSteal, spellVamp, cooldownReduction, attackRange, hp/5s, mana/5s.
- **Derived (computed, never stored):** effective armor after pen (`armor − flatPen`, then `× (1 − %pen)`); physical damage reduction fraction; effective HP; post-mitigation damage; attacks-per-second from attackSpeed bonus; effective cooldown from cooldownReduction; expected crit value from critChance × critDamage.
- **Stateful / mutable (belongs on the Entity, not the Value Object):** currentHp, currentMana, `alive`, cooldown timers, temporary buff stacks. These already live (for HP) in the existing `CombatStats` state machine per ADR-0002.

**Recommendation.** Keep two clearly separated concepts: an **immutable base-stat Value Object** (the sheet) and the **existing mutable vitals state machine** (`CombatStats`, HP/mana + `alive`). Do not fold static values into the mutable class; do not store derived values at all. This preserves ADR-0002 (HP is `float`, `maxHp` final) unchanged.

### R2 — Cleanest decomposition

- *SRP* (Martin, *Clean Code*, 2008, Ch. 10): "A class should have one, and only one, reason to change."
- *Records are Value Objects* (Bloch Item 17 + JLS records): a Java `record` is shallowly immutable with generated `equals`/`hashCode`/`toString` — the language's built-in Value Object.
- *Introduce Parameter Object / small grouped values* (Evans Ch. 5; Fowler, *Refactoring*).

**Recommendation.** An immutable `BaseStats` (or equivalent) **record** composed of a few cohesive Value Objects — `CoreStats` (maxHp, attackDamage, abilityPower, armor, magicDefense, maxMana), `OffensiveStats` (movementSpeed, armorPen, magicPen, attackSpeed, crit, lifeSteal, spellVamp, cooldownReduction, attackRange), `AttackRange` enum. Keep the existing mutable `CombatStats` as the **vitals runtime state** (currentHp/currentMana/alive); do not merge the two. Model armor / magic defense and each pen as flat + bonus% / flat + pct Value Objects to kill data clumps.

### R3 — How to model `attackRange`

- *Enums instead of int/String constants* (Bloch Item 34).
- The detail screen and second-source cross-check both show attack range as a **discrete label** ("Đánh xa" / Ranged) — categorical, not numeric.

**Recommendation.** Model the sheet field as an `enum AttackRange { MELEE, RANGED }` (Bloch Item 34). Do **not** use a `String`. Any numeric reach belongs in a separate `float` field added later.

### R4 — Derived stats without exploding into 50 getters

**Recommendation.** (a) Put **self-contained** derivations as methods on the relevant Value Object. (b) Put **cross-entity** damage math in a `DamageCalculator` service that takes attacker offense + defender defense and delegates the mitigation curve to a **`DamageType` Strategy** (physical/magic/true) — extends ADR-0001's pattern. (c) Reach for **Specification** only when a real conditional passive appears (defer until passives are in scope).

### R5 — Does the `Enemy` interface hold up? ISP audit.

**Finding.** Adding AD/armor/attackRange to `Enemy` **would** violate ISP: target-selectors would then depend on offensive stats they never read. Even today `Enemy` slightly over-serves — each selector uses one of its two data methods (`getName` is only for display/debug).

**Recommendation.** Do **not** add combat stats to the targeting-facing `Enemy` interface. Combat stats used by the `DamageCalculator` travel through a **separate** interface/Value Object, never through the selector-facing surface. ADR-0001's "public seam" stays narrow and testable.

### R6 — Patterns for the hero catalog

- Heroes differ by **data, not behavior**, so Factory Method / Abstract Factory are the wrong axis — they would reintroduce per-hero subclassing ADR-0001 rejected.
- The catalog is a **Repository/Registry** of immutable `BaseStats` templates.
- Construction of each entry uses a **Builder** (Bloch Item 2) for readability and validation.
- Per-hero **passives** are behavior ⇒ a **Strategy** (consistent with ADR-0001) or a **Specification**, not a subclass.
- **Temporary buffs** ⇒ **Decorator** wrapping the stat source at runtime.

**Recommendation.** `HeroCatalog` as a **Repository/Registry** of immutable `BaseStats`; per-hero passives as **Strategy/Specification**; buffs (later) as **Decorator**. No per-hero subclasses. Fully consistent with ADR-0001.

### R7 — The damage formula (most concrete deliverable)

Qualitative mechanics **confirmed** by reproduced client tooltip text:

- **Armor** — "Reduces physical damage taken."
- **Armor Pen** — "Causes a target to take damage **as if they had less Armor**." (⇒ flat pen subtracts from the defender's armor value before mitigation.)
- **Magic Defense / Magic Pen** — symmetric.
- **Critical Damage** — base is 200%; no documented cap (the cap that exists in some derived clients is not a baseline rule of the genre).
- **Life Steal** — "A percentage of your basic attacks will recover your HP. This applies to damage done **after** a target's damage reduction (Armor) is calculated." (⇒ life steal = post-mitigation damage × lifeSteal%.)
- **Spell Vamp** — same, applied after Magic Defense.

**Stat caps confirmed by client text:**

- Cooldown Reduction capped at **40%**; Attack Speed / Movement Speed / Armor Pierce have **no cap** (per in-game FAQ).

**The exact mitigation coefficient: NO PRIMARY SOURCE FOUND.**

The specific curve `physicalDamage = AD × (1 − armor / (armor + 600))` **could not be confirmed from any primary source.** The in-game tooltips describe armor only qualitatively ("Reduces physical damage taken") and never state the `600` (or any) coefficient. Google and the official publisher sites were unreachable from this environment.

**Fallback (secondary, clearly marked).** Community / data-mined wikis commonly cite the reduced-damage-taken curve `1 − DEF / (DEF + 600)`, with flat pen subtracting from `DEF` and % pen scaling it, matching the *directional* tooltip semantics above. **This coefficient is unverified and must not be hard-coded as fact.** For the model this is fine: R4 isolates the mitigation curve behind a `DamageType` Strategy, so the exact coefficient is a single patch-tunable constant in one class, swappable when an authoritative value is obtained.

**Directionally-confirmed formula skeleton (safe to model; coefficient TBD):**

```
effectiveArmor      = max(0, armor − flatArmorPen) × (1 − pctArmorPen)
mitigationMultiplier = f(effectiveArmor)         // shape confirmed ↓ with armor; coefficient UNVERIFIED
rawPhysical         = AD (+ ability AD ratios)
postMitigation      = rawPhysical × mitigationMultiplier
onCrit              = postMitigation × critDamage   // critDamage base 200%
healFromLifeSteal   = postMitigation × lifeSteal%    // post-mitigation, per in-game tooltip
trueDamage          = rawTrue                        // ignores armor by definition
```

**Recommendation.** Model the **skeleton and the confirmed semantics** (pen subtracts before mitigation; life steal is post-mitigation; crit multiplies; true damage bypasses defense; caps as documented). Keep the **mitigation coefficient behind the `DamageType` Strategy** and annotate it as UNVERIFIED until a primary source is found.

## Consequences

**Easier.**

- Adding a new derived stat or damage type becomes a new class (Strategy / method on a Value Object), not an edit across call sites — OCP, consistent with ADR-0001.
- Immutable base-stat Value Objects are thread-safe and freely shareable across the catalog.
- Target-selectors stay decoupled from combat stats via narrow role interfaces (ISP), so `NearestEnemy` / `LowestHP` remain unit-testable in isolation.
- ADR-0002 is untouched: HP stays a `float` mutable state machine; static values live elsewhere.

**Harder / costs.**

- More types — a navigation cost, and a real risk of premature generalization if introduced before it pays for itself.
- The mitigation coefficient is unverified; any balance-accurate simulation is blocked until an authoritative value is sourced.
- Flat/bonus and flat/% pairs add small Value Objects that must be kept consistent.

**Deliberately deferred.**

- Actual code (this is research only; follow-up ADRs + TDD implement).
- Specification-based passives and Decorator buffs — until passives/buffs are in scope.
- Regen (HP/5s, Mana/5s) — present in some clients, not on the primary screenshot, deferred.
- Settling the exact role-interface names and stat terms in `CONTEXT.md`.

## Open questions

1. **Mitigation coefficient** — can an authoritative source (patch notes, official forum, client data-mine with provenance) confirm the `DEF/(DEF+k)` curve and `k`? Until then it stays behind a Strategy, marked UNVERIFIED.
2. **Split granularity (R2)** — one `BaseStats` record now, or `CoreStats`/`OffensiveStats` split from the start? Depends on how soon consumers need narrow slices.
3. **Role-interface vocabulary (R5)** — names for the position-only and hp-only interfaces; must be added to `CONTEXT.md` with `_Avoid_` aliases before coding.
4. **Where do the bonus channels come from** — items, runes, level? Modeling the *source* of bonuses (not just the total) may pull in the Decorator/aggregation design earlier than expected.
5. **Regen & energy** — do we model HP/5s, Mana/5s, and energy-type heroes now, or defer?

## Sources

- Gamma, Helm, Johnson, Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software* (1994) — pattern intents (Strategy, Composite, Factory Method, Abstract Factory, Decorator, Memento).
- Bloch, *Effective Java*, 3rd ed. — Item 2 (Builder), Item 17 (Minimize mutability), Item 20 (Prefer interfaces), Item 34 (Enums over int constants), Item 64 (Refer to objects by their interfaces).
- Martin, *Clean Code* (2008) — Ch. 10 (Classes, SRP, small classes); ISP/OCP from Martin's SOLID.
- Evans, *Domain-Driven Design* (2003) — Ch. 5 (Entity vs Value Object), Ch. 6 (Repository, Factory), Ch. 9 (Specification).
- [refactoring.guru — Strategy](https://refactoring.guru/design-patterns/strategy) — repo-blessed reference.
- Repo primary sources: `CONTEXT.md`, `docs/adr/0001-strategy-pattern-for-target-selection.md`, `docs/adr/0002-hero-combat-semantics.md`, `src/main/java/com/moba/combat/*.java`, `src/main/java/com/moba/strategy/*.java`.