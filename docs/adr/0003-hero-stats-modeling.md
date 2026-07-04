# 0003 — Hero full combat-stats modeling (research deliverable)

## Status

**Proposed** — research deliverable, not yet a decision. This ADR gathers primary-source
findings so a follow-up ADR can make the actual modeling decision. No Java was written or changed.

> Note on language: prose is English for density; domain terms use the ubiquitous language from
> `CONTEXT.md` (Hero, Enemy, currentHp, maxHp, active, Position, TargetSelector, NearestEnemy,
> LowestHP). ADR-0001 and ADR-0002 remain the binding constraints.

## Context

`CombatStats` today (`src/main/java/com/lqm/combat/CombatStats.java`, verified present) is **only an
HP state machine**: `maxHp`, `currentHp`, `active` + `takeDamage/heal/respawn/isAlive`. It holds no
attack, armor, mana, crit, or range data. The hero detail screen in Liên Quân Mobile shows ~16 stats.
This ADR investigates how to model the full sheet.

Reference stat sheet — **Yorn** (from the Liên Quân Mobile / Vietnamese detail screen supplied in the
research brief):

| Group | Stat (VN) | English | Value |
|---|---|---|---|
| Cơ bản | Máu | HP (max) | 3582 |
| Cơ bản | Giáp | Armor (physical defense) | 140 (+0%) |
| Cơ bản | Công vật lý | Attack Damage (AD) | 174 |
| Cơ bản | Giáp phép | Magic Defense | 80 (+0%) |
| Cơ bản | Công phép | Ability Power (AP) | 0 |
| Cơ bản | Năng lượng tối đa | Max Mana | 440 |
| Tấn công | Tốc chạy | Movement Speed | 360.0 |
| Tấn công | Tốc đánh | Attack Speed bonus | +0% |
| Tấn công | Xuyên giáp | Armor Pen (flat \| %) | 0 \| 0% |
| Tấn công | Xuyên giáp phép | Magic Pen (flat \| %) | 0 \| 0% |
| Tấn công | Tỷ lệ chí mạng | Critical Chance | 0% |
| Tấn công | Sát thương chí mạng | Critical Damage | 200% |
| Tấn công | Hút máu | Life Steal | 0% |
| Tấn công | Hút máu phép | Spell Vamp (magic life steal) | 0% |
| Tấn công | Giảm hồi chiêu | Cooldown Reduction | 0% |
| Tấn công | Tầm đánh | Attack Range | "Đánh xa" (ranged) |

> **Cross-check with a second primary snapshot.** The Arena of Valor wiki Yorn infobox (data mirrored
> from the game client) lists the same *field set* but different *numbers*: `maxhp = 3401`,
> `armor = 88 / 12.7%`, `attackdamage = 174`, `abilitypower = 0`, `maxmana = 440`,
> `movementspeed = 340`, `magicpierce = 0 / 0%`, `attackspeed = 0`, `criticalchance = 0`,
> `criticaldamage = 200`, `lifesteal = 0`, `magiclifesteal = 0`, `cooldownspeed = 0`,
> `attackrange = Long Range`, plus `hpper5seconds = 42`, `manaper5seconds = 15`
> ([Arena of Valor Wiki — Yorn](https://arenaofvalor.fandom.com/wiki/Yorn), pageid 695, fetched via
> the fandom MediaWiki API 2026-07-04). The number differences are region/patch/level differences
> (Liên Quân VN vs the international AoV client). **Conclusion: model the field structure, not the
> literal numbers — the numbers are per-hero data that lives in a catalog, not in code.** The AoV
> infobox also reveals two stats absent from the supplied screenshot but present in the client:
> **HP/5s** and **Mana/5s** (regeneration), which the model should leave room for.

## Investigation

### R1 — Which stats are pure values, which are derived, which are stateful?

**Question.** Classify each Yorn stat as an immutable value, a computed/derived quantity, or mutable
runtime state.

**Primary-source findings.**

- *Value Object* (Evans, *Domain-Driven Design*, 2003, Ch. 5): an object that "describes some
  characteristic of a thing" and has **no conceptual identity** — it is defined only by its
  attributes and should be treated as **immutable**. A hero's base attack of 174 is not a thing with
  a lifecycle; it is a value.
- *Minimize mutability* (Bloch, *Effective Java*, 3rd ed., Item 17): "Classes should be immutable
  unless there's a very good reason to make them mutable." Immutable objects are simple,
  thread-safe, and freely shareable. The static stat sheet is a textbook immutable-value case.
- *Entity* (Evans, Ch. 5): defined by a **thread of continuity and identity**, not attributes. A
  `Hero` is an Entity (it has identity "Yorn", persists through HP changes). `currentHp` is the
  mutable state *of* that Entity — which is exactly why ADR-0002 kept it mutable in a state machine.
- *In-game semantics* confirm which quantities are computed from others (see R7): Life Steal
  "applies to damage done **after** a target's damage reduction (Armor) is calculated"; Armor Pierce
  "causes a target to take damage **as if** they had less Armor" ([Arena of Valor Wiki — New Player
  FAQs](https://arenaofvalor.fandom.com/wiki/New_Player_FAQs), pageid 3075, in-game tooltip text
  reproduced; fetched via fandom API 2026-07-04). "After" and "as if" are the language of derived
  quantities.

**Classification for Yorn's sheet.**

- **Pure values (candidates for `final` fields on an immutable Value Object):** maxHp, armor (flat +
  bonus%), attackDamage, magicDefense (flat + bonus%), abilityPower, maxMana, movementSpeed,
  attackSpeed bonus%, armorPen (flat + %), magicPen (flat + %), critChance, critDamage, lifeSteal,
  spellVamp, cooldownReduction, attackRange, hp/5s, mana/5s.
- **Derived (computed, never stored):** effective armor after pen (`armor − flatPen`, then `× (1 −
  %pen)`); physical damage reduction fraction; effective HP; post-mitigation damage; attacks-per-
  second from attackSpeed bonus; effective cooldown from cooldownReduction; expected crit value from
  critChance × critDamage. Each "(+0%)" on the sheet is itself a derived total: `displayed = flatBase
  × (1 + bonus%)`.
- **Stateful / mutable (belongs on the Entity, not the Value Object):** currentHp, currentMana,
  `active`, cooldown timers, and any temporary buff stacks. These already live (for HP) in the
  existing `CombatStats` state machine per ADR-0002.

**Recommendation for this repo.** Keep two clearly separated concepts: an **immutable base-stat
Value Object** (the sheet) and the **existing mutable vitals state machine** (`CombatStats`, HP/mana
+ `active`). Do not fold static values into the mutable class; do not store derived values at all.
This preserves ADR-0002 (HP is `float`, `maxHp` final) unchanged.

### R2 — Cleanest decomposition of `CombatStats`

**Question.** One fat record, or split into Offensive/Defensive/Vitals, or a Value-Object container?

**Primary-source findings.**

- *SRP* (Martin, *Clean Code*, 2008, Ch. 10): "A class should have one, and only one, reason to
  change." "Classes should be small… The first rule is that they should be small. The second rule is
  that they should be smaller than that" — smallness measured by **responsibilities**, not lines.
- *Records are Value Objects* (Bloch Item 17 + JLS records): a Java `record` is shallowly immutable
  with generated `equals`/`hashCode`/`toString` — the language's built-in Value Object, already used
  in this repo for `Position` (`src/main/java/com/lqm/combat/Position.java`).
- *Introduce Parameter Object / small grouped values* (Evans Ch. 5 on Value Objects; Fowler,
  *Refactoring*): cohesive attributes that travel together and change together should be grouped into
  their own Value Object.
- *Data clumps* smell (refactoring.guru catalog): flat + bonus% for armor, and flat + % for pen, are
  clumps that repeat — each pair is a candidate micro Value Object (e.g. an `Armor(flat, bonusPct)`).

**Evaluation.**

- *One fat record with ~16 fields:* defensible because base stats **change together per balance
  patch** (one reason to change → SRP-compatible), but it reads poorly and buries the flat/bonus and
  flat/% pairs as loose primitives (primitive-obsession smell).
- *Split into `Offense` / `Defense` / `Vitals`:* improves readability and lets `LowestHP`-style
  consumers depend on a narrow slice (ties into R5). Each sub-object is an immutable Value Object.
- *GoF Composite:* **rejected.** Composite's intent is "compose objects into tree structures to
  represent part-whole hierarchies [so] clients treat individual objects and compositions uniformly"
  (Gang of Four 1995, Structural patterns). Stats are not a recursive tree with a uniform leaf/node
  interface, so Composite does not apply. What we want is *Value Object composition*, not Composite.

**Recommendation for this repo.** An immutable `HeroStats` (or `BaseStats`) **record** composed of a
few cohesive Value Objects — e.g. `Offense` (AD, AP, attackSpeed, crit, pen, lifeSteal, spellVamp,
cdr, attackRange), `Defense` (armor, magicDefense), `Vitals` (maxHp, maxMana, regen). Keep the
existing mutable `CombatStats` as the **vitals runtime state** (currentHp/currentMana/active); do
not merge the two. Model armor/magic-defense and each pen as small `flat + bonus`/`flat + pct`
Value Objects to kill the data clumps. Start with the split only if it earns its keep; a single flat
record is an acceptable first step (avoid premature generalization — Clean Code Ch. 10 warns against
over-design as much as under-design).

### R3 — How to model `attackRange` ("Đánh xa" / "Cận chiến")

**Question.** String, enum, or numeric threshold?

**Primary-source findings.**

- *Enums instead of int/String constants* (Bloch Item 34): the int/String enum patterns are "not
  type-safe" and "provide no easy way to… iterate"; a proper `enum` is the right tool for a fixed set
  of constants. A `String` "Đánh xa" is stringly-typed and forbidden by the `_Avoid_` discipline in
  CONTEXT.md.
- *Primary game data:* the detail screen and the AoV client both show attack range as a **discrete
  label**, not a number — VN "Đánh xa" / "Cận chiến"; AoV infobox `attackrange = Long Range`
  ([Yorn](https://arenaofvalor.fandom.com/wiki/Yorn)). So the *sheet field* is categorical.

**Recommendation for this repo.** Model the sheet field as an `enum AttackRange { MELEE, RANGED }`
(Bloch Item 34). Do **not** use a `String`. The *categorical* label (what the screen shows) and any
future *numeric reach in map units* are different concepts: if hit-detection later needs a distance,
add a separate `float` reach — do not overload the enum with a magic number now (premature
generalization). This mirrors how `Position.distanceTo` already keeps geometry numeric and separate.

### R4 — Derived stats without exploding into 50 getters

**Question.** Where do computed quantities (effective damage after armor, effective HP, attack-speed
cap) live?

**Primary-source findings.**

- *Strategy intent* (Gang of Four 1995, Behavioral): "Define a family of algorithms, encapsulate each
  one, and make them interchangeable. Strategy lets the algorithm vary independently from clients that
  use it" (also stated by [refactoring.guru — Strategy](https://refactoring.guru/design-patterns/strategy):
  "lets you define a family of algorithms, put each into a separate class, and make their objects
  interchangeable"). This is already the repo's chosen seam (ADR-0001).
- *SRP* (Clean Code Ch. 10): a cross-entity calculation (attacker's AD vs defender's armor) is a
  responsibility of neither the attacker's nor the defender's Value Object; giving `CombatStats`
  knowledge of an attacker would add a second reason to change.
- *Specification* (Evans Ch. 9): a Specification is "a predicate that determines if an object does or
  does not satisfy some criteria" — the natural home for conditional passives ("if target HP < 50%,
  add bonus").

**Evaluation.**

- *Method on the Value Object* — good **only** for computations that depend on that object alone
  (e.g. `attacksPerSecond()`, `displayedArmor()`). This keeps behavior with data and avoids an
  anemic getter-bag (Clean Code's warning against data classes with no behavior).
- *Separate `DamageCalculator` domain service using Strategy* — right for cross-entity math. Physical
  vs magic vs true damage are a **family of algorithms** ⇒ a `DamageType`/mitigation Strategy, which
  extends the pattern already blessed in ADR-0001 instead of introducing a new vocabulary.
- *Specification for conditional bonuses* — right for passives with a boolean trigger; keeps the
  calculator open for extension (OCP) without editing it per passive.

**Recommendation for this repo.** (a) Put **self-contained** derivations as methods on the relevant
Value Object. (b) Put **cross-entity** damage math in a `DamageCalculator` service that takes
attacker offense + defender defense and delegates the mitigation curve to a **`DamageType` Strategy**
(physical/magic/true) — a direct, idiomatic extension of ADR-0001. (c) Reach for **Specification**
only when a real conditional passive appears (defer until Yorn's `Fierce Shot`-style passives are in
scope). This answers OCP: a new derived stat or damage type is a new class, not an edit to every call
site.

### R5 — Does the `Enemy` interface hold up? ISP audit.

**Question.** When AD/armor/attackRange arrive, does `Enemy` (currently `getPosition`,
`getCurrentHp`, `getName`) violate ISP? What is the minimum surface the strategies need?

**Primary-source findings.**

- *Interface Segregation Principle* (Martin): "Clients should not be forced to depend upon interfaces
  that they do not use." Fat interfaces should be split into role interfaces.
- *Refer to objects by their interfaces* / *prefer interfaces* (Bloch Items 64, 20).
- *Actual usage in this repo (verified):* `NearestEnemy.select` calls **only**
  `attacker.getPosition()` and `e.getPosition()`; `LowestHP.select` calls **only** `Enemy::getCurrentHp`
  (`src/main/java/com/lqm/strategy/NearestEnemy.java`, `LowestHP.java`). `MinSelector` is generic and
  needs nothing from `Enemy`. So the true minimum surfaces are **disjoint**: NearestEnemy → position
  only; LowestHP → current HP only.

**Finding.** Adding AD/armor/attackRange to `Enemy` **would** violate ISP: target-selectors would
then depend on offensive stats they never read. Even today `Enemy` slightly over-serves — each
selector uses one of its two data methods (`getName` is only for display/debug).

**Recommendation for this repo.** Do **not** add combat stats to the targeting-facing `Enemy`
interface. Introduce narrow role interfaces — e.g. `Positioned { getPosition() }` and
`HasHealth { getCurrentHp() }` (names to be settled in CONTEXT.md) — and let `NearestEnemy` depend on
`Positioned`, `LowestHP` on `HasHealth`. `Hero` implements all roles. Combat stats used by the
`DamageCalculator` (R4) travel through a **separate** interface/Value Object, never through the
selector-facing surface. This keeps ADR-0001's "public seam" narrow and testable.

### R6 — Patterns for the hero catalog (Yorn, Tulen, Butterfly…)

**Question.** Factory Method vs Abstract Factory vs Builder; Registry/Repository; where do per-hero
overrides live?

**Primary-source findings.**

- *Builder* (Bloch Item 2): "Consider a builder when faced with many constructor parameters." With
  ~16 stats, telescoping constructors and a plain constructor (`Hero(name, position, maxHp,
  selector)` today) do not scale; a Builder gives readable, validated construction.
- *Factory Method* (Gang of Four 1995, Creational): "Define an interface for creating an object, but
  let subclasses decide which class to instantiate." *Abstract Factory:* "provide an interface for
  creating families of related or dependent objects without specifying their concrete classes." Both
  presuppose **behavioral subtypes** to instantiate.
- *Repository* (Evans Ch. 6): "provides the illusion of an in-memory collection of all objects of
  that type," decoupling clients from storage. A static set of heroes with fixed base stats is
  exactly a read-only Repository/Registry of `HeroStats` templates.
- *Strategy vs inheritance* (ADR-0001, this repo): inheritance per hero was already **rejected**
  ("Inheritance (`NearestEnemyHero extends Hero`): combinatorial explosion").
- *Decorator* (Gang of Four 1995, Structural): "Attach additional responsibilities to an object
  dynamically. Decorators provide a flexible alternative to subclassing for extending functionality"
  — the intent-match for **temporary buffs** that wrap a hero's stats at runtime.
- *Memento* (Gang of Four 1995, Behavioral): "capture and externalize an object's internal state so
  that the object can be restored to this state later," without violating encapsulation — an **undo**
  mechanism.

**Evaluation.**

- Heroes differ by **data, not behavior**, so Factory Method / Abstract Factory (which switch on
  *class*) are the wrong axis — they would reintroduce the per-hero subclassing ADR-0001 rejected.
- The catalog itself is a **Repository/Registry** of immutable `HeroStats`.
- Construction of each entry is a **Builder** (Bloch Item 2).
- Per-hero **passives** (e.g. Yorn's `Fierce Shot`) are behavior ⇒ a **Strategy** (consistent with
  ADR-0001) or a **Specification** (R4), not a subclass.
- **Temporary buffs** ⇒ **Decorator** wrapping the stat source at runtime.
- **Memento for "level-up delta": rejected.** Level-up is a forward transform, not an undo; Memento's
  intent is state *restoration*. A level-up is better modeled as producing a new immutable `HeroStats`
  (values are immutable per R1). Memento *would* fit a future "restore combat state" (e.g. replay),
  but not leveling.

**Recommendation for this repo.** `HeroCatalog` as a **Repository/Registry** of immutable `HeroStats`
built with a **Builder**; per-hero passives as **Strategy/Specification**; buffs (later) as
**Decorator**. No per-hero subclasses. This is fully consistent with ADR-0001.

### R7 — The exact Liên Quân Mobile damage formula (most concrete deliverable)

**Question.** What is the exact physical/magic/true/crit/life-steal formula, per a primary source?

**Primary-source findings (in-game tooltip text, reproduced on official-data wikis).**

Qualitative mechanics **are** confirmed by primary in-game tooltip text
([Arena of Valor Wiki — New Player FAQs](https://arenaofvalor.fandom.com/wiki/New_Player_FAQs),
pageid 3075, "What are some of the attributes shown…"; fetched via fandom API 2026-07-04):

- **Armor** — "Reduces physical damage taken."
- **Armor Pierce** — "Causes a target to take damage **as if they had less Armor**." (⇒ flat pen
  subtracts from the defender's armor value before mitigation.)
- **Magic Defense** — "Reduces magic damage taken." **Magic Pierce** — "take magic damage as if their
  Magic Defense was reduced."
- **Critical Damage** — "The base amount for Critical Damage is **200%**." (⇒ a crit multiplies by
  critDamage; matches Yorn's 200%.)
- **Life Steal** — "A percentage of your basic attacks will recover your HP. This applies to damage
  done **after** a target's damage reduction (Armor) is calculated." (⇒ lifesteal = post-mitigation
  damage × lifeSteal%.)
- **Magic Life Steal (spell vamp)** — same, applied after Magic Defense.

**Stat caps** confirmed by primary reproduced client data:

- Cooldown Reduction capped at **40%**; Attack Speed / Movement Speed / Armor Pierce have **no cap**
  (AoV New Player FAQs, in-game text).
- Honor of Kings client (the Chinese original) Equipment screen: **Critical Damage 250%**,
  **Attack Speed 200%**, **Cooldown Reduction 40%**, **Movement Speed 800**
  ([Honor of Kings Wiki — Equipment](https://honor-of-kings.fandom.com/wiki/Equipment), pageid 1408,
  "Stat Limits"; fetched via fandom API 2026-07-04). (Note the crit-damage *base* differs between
  clients: AoV base 200%, HoK cap 250% — evidence that caps/coefficients are client/patch data, not
  universal constants to hard-code.)

**The exact mitigation coefficient: NO PRIMARY SOURCE FOUND.**

The specific curve hypothesized in the brief — `physicalDamage = AD × (1 − armor / (armor + 600))` —
**could not be confirmed from any primary/official source.** The in-game tooltips describe armor only
qualitatively ("Reduces physical damage taken") and never state the `600` (or any) coefficient. What
I searched (all 2026-07-04):

- `arenaofvalor.fandom.com` and `honor-of-kings.fandom.com` via the MediaWiki API (direct page GETs
  return HTTP 403; the `api.php` endpoint works). Searched: "armor damage reduction formula",
  "physical defense reduction 600", "Physical Defense mechanics", "damage reduction percentage
  defense value", "insipid damage reduction". Read in full: **New Player FAQs** (in-game attribute
  tooltips — qualitative only), **Equipment** (caps only), **Arcana** (stat vocabulary only), **Yorn**
  (infobox field set). No page states the mitigation coefficient.
- `en.wikipedia.org/wiki/Design_Patterns` (pattern catalog) and `refactoring.guru/design-patterns/strategy`
  (pattern intents) — not game sources; used for the pattern citations above.
- Google web search and direct `lienquan.garena.vn` / `arenaofvalor.com` were **not reachable** from
  this environment (Google returned a bot-check page; the official Garena/Tencent sites and any
  Garena-forum dev post could not be fetched). I did **not** find a Garena official forum post or a
  Tencent dev blog stating the coefficient.

**Fallback (secondary, clearly marked).** Community/data-mined wikis for Honor of Kings (the Chinese
original) and AoV commonly cite the reduced-damage-taken curve `1 − DEF / (DEF + 600)` (equivalently
damage-taken multiplier `600 / (600 + DEF)`), with flat pen subtracting from `DEF` and % pen scaling
it, matching the *directional* tooltip semantics confirmed above. **This coefficient is unverified by
a primary source and must not be hard-coded as fact.** For the model this is fine: R4 already isolates
the mitigation curve behind a `DamageType` Strategy, so the exact coefficient is a single
patch-tunable constant in one class, swappable when an authoritative value is obtained.

**Directionally-confirmed formula skeleton (safe to model; coefficient TBD):**

```
effectiveArmor      = max(0, armor − flatArmorPen) × (1 − pctArmorPen)
mitigationMultiplier = f(effectiveArmor)         // shape confirmed ↓ with armor; coefficient UNVERIFIED
rawPhysical         = AD (+ ability AD ratios)
postMitigation      = rawPhysical × mitigationMultiplier
onCrit              = postMitigation × critDamage   // critDamage base 200% (AoV), cap 250% (HoK)
healFromLifeSteal   = postMitigation × lifeSteal%    // post-mitigation, per in-game tooltip
trueDamage          = rawTrue                        // ignores armor by definition
```

**Recommendation for this repo.** Model the **skeleton and the confirmed semantics** (pen subtracts
before mitigation; lifesteal is post-mitigation; crit multiplies; true damage bypasses defense; caps
per client). Keep the **mitigation coefficient behind the `DamageType` Strategy** and annotate it as
UNVERIFIED until a primary source is found. Do not assert `600` as canonical in code or comments.

## Consequences

**Easier.**

- Adding a new derived stat or damage type becomes a new class (Strategy / method on a Value Object),
  not an edit across call sites — OCP, consistent with ADR-0001.
- Immutable base-stat Value Objects are thread-safe and freely shareable across the catalog
  (Bloch Item 17); the catalog becomes a simple read-only Repository.
- Target-selectors stay decoupled from combat stats via narrow role interfaces (ISP), so `NearestEnemy`
  / `LowestHP` remain unit-testable in isolation exactly as today.
- ADR-0002 is untouched: HP stays a `float` mutable state machine; static values live elsewhere.

**Harder / costs.**

- More types (base-stat VOs + role interfaces + a calculator + strategies) — a navigation cost, and a
  real risk of premature generalization if introduced before it pays for itself (Clean Code Ch. 10).
  Mitigation: land the split incrementally, only as stats are actually consumed.
- The mitigation coefficient is unverified; any balance-accurate simulation is blocked until an
  authoritative value is sourced.
- Flat/bonus and flat/% pairs add small Value Objects that must be kept consistent with the display
  formula `displayed = flat × (1 + bonus%)`.

**Deliberately deferred.**

- Actual code (this is research only; a follow-up ADR + TDD will implement).
- Specification-based passives and Decorator buffs — until Yorn-style passives/buffs are in scope.
- Regen (HP/5s, Mana/5s) and energy heroes — noted as present in the client, not yet modeled.
- Settling the exact role-interface names and stat terms in CONTEXT.md.

## Open questions

1. **Mitigation coefficient** — can an authoritative Garena/Tencent source (patch notes, official
   forum, client data-mine with provenance) confirm the `DEF/(DEF+k)` curve and `k`? Until then it
   stays behind a Strategy, marked UNVERIFIED.
2. **Split granularity (R2)** — one `HeroStats` record now, or `Offense`/`Defense`/`Vitals` from the
   start? Depends on how soon consumers need narrow slices.
3. **Role-interface vocabulary (R5)** — names for the position-only and hp-only interfaces; must be
   added to CONTEXT.md with `_Avoid_` aliases before coding.
4. **Where do the "(+0%)" bonus channels come from** — items, Arcana, level? Modeling the *source* of
   bonuses (not just the total) may pull in the Decorator/aggregation design earlier than expected.
5. **Regen & energy** — do we model HP/5s, Mana/5s, and energy-type heroes now, or defer? They exist
   in the client (AoV Yorn infobox) but were not on the supplied screenshot.

## Sources

- Gamma, Helm, Johnson, Vlissides, *Design Patterns: Elements of Reusable Object-Oriented Software*
  (1994) — pattern intents (Strategy, Composite, Factory Method, Abstract Factory, Decorator, Memento).
  Catalog cross-checked at [en.wikipedia.org/wiki/Design_Patterns](https://en.wikipedia.org/wiki/Design_Patterns).
- Bloch, *Effective Java*, 3rd ed. — Item 2 (Builder), Item 17 (Minimize mutability), Item 20 (Prefer
  interfaces), Item 34 (Enums over int constants), Item 64 (Refer to objects by their interfaces).
- Martin, *Clean Code* (2008) — Ch. 10 (Classes, SRP, small classes); ISP/OCP from Martin's SOLID.
- Evans, *Domain-Driven Design* (2003) — Ch. 5 (Entity vs Value Object), Ch. 6 (Repository, Factory),
  Ch. 9 (Specification).
- [refactoring.guru — Strategy](https://refactoring.guru/design-patterns/strategy) — Strategy intent
  (repo-blessed reference, RESOURCES.md).
- Arena of Valor Wiki (in-game client data): [New Player FAQs](https://arenaofvalor.fandom.com/wiki/New_Player_FAQs)
  (attribute tooltips, caps), [Yorn](https://arenaofvalor.fandom.com/wiki/Yorn) (stat field set).
  Honor of Kings Wiki: [Equipment](https://honor-of-kings.fandom.com/wiki/Equipment) (stat caps),
  [Arcana](https://honor-of-kings.fandom.com/wiki/Arcana) (stat vocabulary). Fetched via the fandom
  MediaWiki `api.php` 2026-07-04. **Treated as secondary** (they mirror client text); used because the
  official Garena/Tencent sites were unreachable from this environment.
- Repo primary sources: `CONTEXT.md`, `docs/adr/0001-strategy-pattern-for-target-selection.md`,
  `docs/adr/0002-hero-combat-semantics.md`, `src/main/java/com/lqm/combat/*.java`,
  `src/main/java/com/lqm/strategy/*.java` (all verified present 2026-07-04).
