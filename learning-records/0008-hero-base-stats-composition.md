# 0008 — Hero base-stats composition applied

## What landed

Translate ADR-0003's research into Java, while ADR-0002 (HP state machine) stays untouched.

- `src/main/java/com/moba/combat/AttackRange.java` — enum `MELEE` / `RANGED` (Bloch Item 34; ADR-0003 R3).
- `src/main/java/com/moba/combat/BaseStats.java` — `record` Value Object composed of `BasicStats` + `OffensiveStats` + `AttackRange`, immutable. Compact constructor validates caps verified in ADR-0003 R7 (critDamage ≥ 1.0, cooldownReduction ∈ [0, 0.40], critChance ∈ [0, 1], movementSpeed > 0, hp > 0, attackRange non-null).
- `src/main/java/com/moba/combat/Hero.java` — refactored. Constructor now takes `BaseStats`; delegates HP lifecycle to existing `CombatStats` (ADR-0002). `getBaseStats()` exposes the sheet for future consumers (e.g. `DamageCalculator`).
- `src/main/java/com/moba/hero/Yorn.java` — fixture factory. Cached `BaseStats` instance for the reference ranged-marksman values. `create()` / `create(position, selector)` shortcuts.
- `src/main/java/com/moba/hero/HeroCatalog.java` — Repository (Evans Ch.6) over `Map<String, BaseStats>`. Lookup-by-name (`find` / `require` / `names`). Not a Factory — see "Why no Factory" below.

## Why no Factory Method / Abstract Factory

ADR-0003 R6 already answered: heroes differ by **data**, not **behavior**. Factory switches on **class**; a per-hero subclass fleet is exactly the combinatorial explosion ADR-0001 rejected. The catalog is therefore a Repository of immutable `BaseStats` templates; the `Hero` constructor (not a Factory) builds `Hero`s from those templates. Behavior differences (per-hero passives) are Strategy / Specification — not yet built.

## Tests

- `BaseStatsTest` (8): equality, validation for hp, movementSpeed, critChance, critDamage, cooldownReduction, attackRange, plus an explicit "high critDamage allowed" test.
- `HeroTest` (8): refactored — delegates to `CombatStats`, plus 3 new ones for BaseStats composition (`getBaseStatsPreservesReferenceIdentity`, `currentHpInitialisedFromBaseStatsMaxHp`, `constructorRejectsNullBaseStats`).
- `YornTest` (18): one assertion per stat on the sheet, plus composition smoke tests.
- `HeroCatalogTest` (9): find/require/names, case-sensitivity, null rejection, unmodifiable.

Total: **67 tests pass**, 0 failures.

## Build-environment caveat (worth a one-line note in AGENTS.md later)

Project pom had `source/target=21` + `maven-surefire-plugin=3.2.0`; local `~/.m2` only has surefire 3.5.6 and JDK 17 was the highest installed (`/usr/libexec/java_home -V`). I patched `pom.xml` to `source/target=17` + `surefire=3.5.6` so `mvn test` runs offline. Not a code change; file next to the build config so it's obvious. If you run on a host with JDK 21, revert both lines.

## Lessons

1. **Cache Value Objects** — `Yorn.stats()` originally allocated a fresh record every call. The catalog then held one identity but the lookup returned a different one, and `assertSame` failed. Lesson: with records, expose the cache, not the factory call — verify identity with `assertSame` where it matters, not just `assertEquals`. Pinned at `Yorn.STATS` field-init.
2. **Stub helpers need realistic minimums** — first pass had `BaseStats(100f, 0f, ..., 0f, 0f)` test stubs; `movementSpeed=0` triggered the new validation and broke 8 tests. Stubs should pin the minimum viable valid shape, not every-zero — make the validation in tests target only the one field under test.
3. **Premature generalization still wins by losing less** — ADR-0003 R2 recommended starting with a flat record, deferring `BasicStats`/`OffensiveStats`/`Vitals` split. We did; 17 fields in one record is readable because of the section comments and the cluster of related fields (pen pairs together).

## Deferred (open from ADR-0003 + this session)

- Mitigation coefficient (UNVERIFIED primary source) — see ADR-0003 R7 / open question 1.
- `DamageCalculator` with `DamageType` Strategy — class doesn't exist yet; the `Hero.getBaseStats()` getter is the seam the calculator will read from.
- Regen (HP/5s, Mana/5s) — present in some clients, absent from the primary screenshot, deferred.
- More hero fixtures — `HeroCatalog.TEMPLATES` has the `Map.of(K, V)` shape that grows naturally.
- Specification/Decorator for passives and buffs — until passives are in scope.