# 0005 — Architecture review #2: applied all 3 candidates

## Status

active

## Evidence

After Hero deepening (LR-0004), ran architecture review #2 and applied all 3 candidates in the report:

| # | Candidate | Status |
|---|-----------|--------|
| 2 | `Enemy.getCurrentHp` int → float | ✅ Done |
| 1 | `Position.distanceTo` | ✅ Done |
| 3 | `MinSelector` helper | ✅ Done (Speculative) |

## Decisions pinned (from review + apply)

### Candidate 2: `Enemy.getCurrentHp` int → float

- **Trigger:** Hero deepening picked float HP, but `Enemy` interface still used int → type leak.
- **Change:** `Enemy.getCurrentHp()` returns `float`. Hero returns float directly (removed `Math.round`). Deleted `getCurrentHpExact()`.
- **Blast radius:** Enemy, Hero, EnemyStub, HeroTest, LowestHPTest, NearestEnemyTest (literal updates).
- **New test:** `LowestHPTest.fractionalHpComparisonPicksTheTrueLower` — verify 99.4f vs 99.6f picks the right one.

### Candidate 1: `Position.distanceTo`

- **Trigger:** NearestEnemy held a private `distance(Position, Position)` static method. Math leaking across the seam.
- **Change:** Position has an instance method `distanceTo(Position other)`. NearestEnemy calls `attacker.getPosition().distanceTo(...)`.
- **New tests:** `PositionTest` (4 tests) — zero distance, Pythagorean 3-4-5, symmetry, negative coordinates.

### Candidate 3: `MinSelector` helper

- **Trigger:** NearestEnemy and LowestHP duplicated the "iterate + if < minScore" loop.
- **Change:** Added `MinSelector.minBy(List<T>, ToDoubleFunction<T>)`. NearestEnemy and LowestHP became 1-liners.
- **New tests:** `MinSelectorTest` (3 tests) — LowestHP-style scoring, tie-break, single item.

## Implications

### Code shrinkage

```
NearestEnemy: ~35 lines → 21 lines  (-40%)
LowestHP:     ~30 lines → 21 lines  (-30%)
Hero:         ~80 lines → 95 lines  (+15%, from added combat semantics + int→float fix; no logic leak)
Position:     ~5 lines  → 20 lines  (+300%, but that is deepening — locality gain)
```

Even though total lines went up (added test files + MinSelector), **interface complexity decreased** — NearestEnemy and LowestHP are now 1-line method bodies. That is real deepening.

### Test coverage

- Before: 14 tests (3 NearestEnemy + 3 LowestHP + 8 Hero).
- After: 22 tests (+4 Position + 3 MinSelector + 1 LowestHP fractional + 1 already in Hero).
- Each module has its own seam test. Position has pure-isolation tests (not via a strategy).

### Locality gains

- HP type decision: 1 place (Enemy interface). No more bridge method in Hero.
- Distance math: 1 place (Position). NearestEnemy is pure algorithm.
- Min selection: 1 place (MinSelector). Strategies only need a scorer.

### Friction remaining (not addressed)

1. **Workspace has no CONTEXT.md** (only GLOSSARY.md). Engineering skills expect CONTEXT.md + docs/adr/. GLOSSARY is being used as a substitute — flag persists across 2 reviews.
2. **Hero exception paths untested:** `takeDamage(-x)` throws IllegalArgumentException, `heal(-x)` throws, constructor `maxHp <= 0` throws. None have tests.
3. **No 4th strategy yet:** if we add FarthestEnemy, MostHP, etc., MinSelector will pay for itself. Currently only ~10 duplicate lines saved.

### Lesson on deepening

When a module is deepened (Hero gains combat semantics), dependent modules (Enemy interface) may "drift" — the interface no longer matches internal types. This is friction that emerges AFTER deepening. Re-review after every significant deepening.