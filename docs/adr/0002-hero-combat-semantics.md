# 0002 — Hero combat semantics (float HP, final maxHp, heal no-op when dead, separate respawn)

## Status

accepted

## Context

When deepening the `Hero` module to represent its core concept (a combat hero with mutable HP that can die), we need to pin down 4 load-bearing decisions about HP semantics.

## Decision

1. **`currentHp` is `float`** — allows fractional damage (DoT, life steal) and smooth regen. The `Enemy.getCurrentHp()` interface also returns `float` to match (see consequences).
2. **`maxHp` final, set at constructor** — simple, sufficient for current scope. Level-up / buffs that change `maxHp` come later (will need a dedicated `setMaxHp` method).
3. **`heal` is a no-op when `alive=false`** — matches gameplay: dead heroes cannot be healed. Revival happens through the dedicated `respawn()` method.
4. **`respawn()` is a separate method** — matches gameplay: triggered by an external respawn timer, not auto-revived from `heal`. Sets `alive=true`, `currentHp=maxHp`.

## Considered alternatives

- **HP is `int`**: loses precision with fractional damage. Classic GoF syntax uses int, but the genre has DoT / life steal, so `float` matches the domain better.
- **`maxHp` mutable with `setMaxHp` setter**: needs a test for "currentHp clamps to the new maxHp when maxHp shrinks". Too anticipatory for current scope.
- **Heal auto-revives**: does not match gameplay. Healing a dead hero is meaningless.
- **Only `isAlive()`, no separate `alive` flag**: `isAlive()` still works but lacks an explicit state field — hard to debug, and cannot distinguish "dead" from "untargetable for another reason".

## Consequences

- `Enemy.getCurrentHp()` must return `float` (not `int`). Strategies read `float` directly, no `Math.round` — no rounding artifact in tie-breaks.
- `Hero.getCurrentHpExact()` method was removed (existed only to bypass `Math.round` — no longer needed).
- `Hero` constructor throws `IllegalArgumentException` when `maxHp <= 0`. Tests for the exception path were not initially written — coverage gap closed in subsequent test hygiene work.
- Future extensions: damage modifiers (armor / magic resistance) will be either composition or a method on Hero (see learning-records/0004).