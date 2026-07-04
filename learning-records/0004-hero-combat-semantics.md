# 0004 — Hero deepening: combat semantics across 7 TDD cycles

## Status

active

## Evidence

Drove 7 pure TDD cycles after the grilling session, transforming Hero from a data holder into a combat module with a complete lifecycle. The full progression is captured in `src/test/java/com/moba/combat/HeroTest.java` (8 tests) and `src/main/java/com/moba/combat/Hero.java`.

## Decisions pinned (from grilling)

| Decision | Value |
|----------|-------|
| HP type | float (instead of int) |
| hp | final, set at constructor |
| takeDamage | `float amount` ≥ 0; clamp 0; set alive=false if HP ≤ 0 |
| heal | `float amount` ≥ 0; clamp hp; no-op if !alive |
| respawn | separate method; set alive=true, currentHp=hp |
| isAlive | returns alive |

Decisions are based on the genre's actual gameplay: heal does not work on dead heroes (game mechanic), revival happens via an external respawn timer (not auto-revive from heal).

## Implications

### Depth ratio changed

Before: Hero interface = 4 getters + 1 delegate. Shallow — interface complexity ≈ implementation complexity.
After: Hero interface = 5 methods (getPosition, getCurrentHp, getName, takeDamage, heal, respawn, isAlive, selectTarget) hiding meaningful combat logic. Deeper — interface relatively simple, semantics rich.

### Locality gained

HP state (currentHp, hp, alive) and HP-mutation logic (takeDamage, heal, respawn) all live in Hero. Future damage modifiers (armor, magic resistance), life steal, regen — all of those will be methods on Hero or composition through Hero, not scattered.

### Test coverage

HeroTest went from 1 test → 8 tests. Each method has at least 1 dedicated test. The test seam is the public interface of Hero. Tests use the `EnemyStub` factory (already in place from the earlier refactor).

### TDD progression structure

7 cycles by vertical slice:
1. Basic edge case: takeDamage decreases HP
2. Edge case: takeDamage clamps at 0
3. State transition: HP=0 → !alive → selectTarget=null
4. Positive case: heal increases HP
5. Edge case (characterization): heal clamps at hp
6. Dead-state: heal is a no-op when dead
7. State transition: respawn restores

### Open questions for the future

- Damage modifiers (armor, magic resistance): a field on Hero, or an external module applied to Hero? May need Chain of Responsibility.
- Life steal: a strategy on the attacker, or a method on Hero? Could be Decorator.
- Regen on tick: dedicated `regenerate()` method, or implicit in the combat loop's `tick()`?

### Architecture note

The workspace still lacks `CONTEXT.md` at this stage (only `GLOSSARY.md` exists). New domain terms (currentHp, hp, takeDamage, heal, respawn, isAlive, alive) were added to GLOSSARY.md. If the workspace later adopts CONTEXT.md, vocabulary consistency needs a recheck.