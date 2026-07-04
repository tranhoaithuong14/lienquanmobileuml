# 0001 — Strategy pattern for Target Selection

## Status

superseded by [ADR-0004](0004-aov-targeting-system.md)

## Context

A MOBA hero auto-attacks during combat. The genre defines exactly 2 global auto-target rules: Nearest (the classic "attack whoever is closest") and LowestHP (the "focus fire the weakest" rule common to assassins and finishers). We need to model this in Java.

## Decision

Use the **GoF Strategy pattern**: a `TargetSelector` interface with a method `select(Enemy attacker, List<Enemy> enemies)`. Concrete strategies: `NearestEnemy`, `LowestHP`. `Hero` (the Context) holds a reference to a `TargetSelector` and delegates the work to it.

Only **2 strategies** are implemented. Per-ability logic (marked target, target lock, skillshot direction) is confirmed **out of scope** for the `TargetSelector` interface — those are per-skill concerns with their own input/output (cast direction, mark state, AoE radius) and forcing them into `TargetSelector` would break the pattern's cleanliness.

## Considered alternatives

- **`if/else` chain inside `Hero`**: violates Open/Closed. Adding a new hero → editing `Hero`. Tests become bulk.
- **Inheritance** (`NearestEnemyHero extends Hero`): combinatorial explosion once there are N variation axes (role × lane × strategy).
- **`switch` on enum**: still violates O/C. Adding an enum = editing the switch.

## Consequences

- `TargetSelector` is a public seam — strategies can be tested in pure isolation (`NearestEnemyTest`, `LowestHPTest`).
- Adding a new strategy (`FarthestEnemy`, `MostHP`, …) only needs a new class that implements the interface. Zero edits to `Hero`.
- `Hero` only holds the reference and delegates. Strategy logic does not leak into `Hero`.
- When `LowestHP` needs to read the float HP from an enemy, the `Enemy` interface must return `float` (see ADR-0002).
