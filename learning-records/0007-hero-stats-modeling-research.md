# 0007 — Hero full combat-stats modeling (primary-source research)

## Status

active — research only. No Java changed. Full deliverable: [`docs/adr/0003-hero-stats-modeling.md`](../docs/adr/0003-hero-stats-modeling.md).

## What

Investigated how to model a hero's full detail-screen stats (HP, armor, AD, magic defense, AP, mana, move/attack speed, pen, crit, life steal, spell vamp, CDR, attack range) in Java with OOP + GoF + SOLID + Clean Code, against primary sources. Answers R1–R7 in ADR-0003.

## Headline findings

1. **Values vs state must stay split.** Base stats are immutable Value Objects (Evans DDD Ch.5; Bloch *Effective Java* Item 17 — records = built-in VOs, like `Position`); currentHp/currentMana/`alive` stay in the existing mutable `CombatStats` state machine (ADR-0002 untouched). Derived stats (post-armor damage, effective HP, attacks/sec) are **never stored** — computed on demand.

2. **`Enemy` must not grow combat stats (ISP).** Verified `NearestEnemy` uses only `getPosition()`, `LowestHP` only `getCurrentHp()`. Adding AD/armor to `Enemy` would force target-selectors to depend on data they never read. Recommend narrow role interfaces (position-only, hp-only); damage math travels a separate surface.

3. **Damage coefficient: NO PRIMARY SOURCE FOUND.** The `DEF/(DEF+600)` curve could not be confirmed from any official source. Confirmed *qualitatively* from in-game tooltip text: armor pen subtracts before mitigation; **life steal is post-mitigation**; crit base 200%; CDR cap 40%. Keep the mitigation curve behind a `DamageType` **Strategy** (extends ADR-0001) with the coefficient marked UNVERIFIED — never hard-coded.

## Other recommendations (see ADR for citations)

- **`attackRange` = `enum { MELEE, RANGED }`**, not String (Bloch Item 34). The screen shows a label ("Ranged"), not a number; numeric reach is a separate concern.
- **Catalog = Repository/Registry** of immutable `BaseStats`. No per-hero subclasses (ADR-0001 already rejected inheritance). Passives → Strategy/Specification; buffs → Decorator.
- **GoF Composite rejected** for stats (not a part-whole tree) — it's Value Object composition. **Memento rejected** for level-up (a forward transform, not undo).
- **Decomposition**: prefer immutable `BaseStats` record, optionally split into `CoreStats`/`OffensiveStats` VOs; kill flat/bonus and flat/% data clumps. Land incrementally to avoid premature generalization (Clean Code Ch.10).

## Caveats

- Wiki sources used as **secondary** (they mirror client text); official publisher sites and Google were unreachable from this environment. Per-hero numbers differ across regions/patches — model the **field structure**, not the literals.

## Follow-ups

Open questions in ADR-0003: confirm the mitigation coefficient; settle split granularity; add role-interface names to CONTEXT.md; model bonus sources (items / runes / level) and regen / energy. Next step: a decision ADR + TDD implementation.