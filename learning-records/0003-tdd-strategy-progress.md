# 0003 — TDD progression for NearestEnemy and LowestHP

## Status

active

## Evidence

Drove 6 pure TDD cycles for the 2 strategies in this workspace. Each cycle = one new test → minimal implementation to pass. The whole progression used the seam agreed upon earlier (only test `select()` on the 2 strategies; no Hero Context tests).

## Implications

### Pattern learned (TDD loop)

- **RED**: write a new test — it must fail. The failure can be a compile error (cycle 1 when NearestEnemy does not yet exist) or an assertion failure (cycles 2+ when the current implementation does not satisfy the new test).
- **GREEN**: write the minimal implementation that just makes the current test pass. Don't pre-write for future tests.
- **REFACTOR**: NOT inside the loop. Do it later, in the review stage.

### Test structure by vertical slice

Each strategy is built through 3 identical slices:
1. Edge case: empty list → null (slice 1)
2. Trivial case: 1 enemy → that enemy (slice 2)
3. Real case: many enemies → compare by the strategy's criterion (slice 3)

Same interface, same test skeleton, different comparison criterion. This demonstrates Strategy: changing the algorithm does not change the interface.

### Tie-break policy pinned (implicitly via the implementation)

Both NearestEnemy and LowestHP use **strict less-than** (`d < minDistance`, `hp < minHp`). Consequence: the first enemy in the list wins ties. This behavior is tested implicitly — no dedicated tie-break test yet, but one may be added to document it explicitly.

### Refactor opportunities (deferred, not in the loop)

- The test helper `enemyAt` is duplicated between NearestEnemyTest and LowestHPTest. Should be extracted into a test helper class `EnemyStub` (file `/test/java/com/moba/test/EnemyStub.java`).
- The "select min by comparator" logic could be extracted into a generic helper (Template Method or Strategy-of-Strategy). Over-engineering for 2 strategies — skip.

### Lessons on test design

Tests should verify **behavior at the public interface**, not implementation details:
- ✅ `assertSame(near, result)` — observe the output, don't care whether NearestEnemy has a `minDistance` field.
- ❌ (Avoid) checking `selector.getMinDistance()` or similar — implementation-detail coupling.
- Expected values in tests are literal/known-good (`(10, 0)`, `100`) — not tautologies (don't recompute expected using the same code as the implementation).

### Does lesson 0001 need fixing later?

Lesson 0001 currently has code snippets for NearestEnemy and LowestHP. The real code in `src/main/java/com/moba/strategy/` matches the snippets (same structure, same algorithm). The lesson could link to the real source files instead of inlining the snippet — would improve the lesson. Not a priority.