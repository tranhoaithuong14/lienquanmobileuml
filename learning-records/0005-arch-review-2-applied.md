# 0005 — Architecture review #2: applied all 3 candidates

## Status

active

## Evidence

Sau Hero deepening (LR-0004), đã chạy architecture review #2 và apply cả 3 candidates trong report:

| # | Candidate | Status |
|---|-----------|--------|
| 2 | Enemy.getCurrentHp int → float | ✅ Done |
| 1 | Position.distanceTo | ✅ Done |
| 3 | MinSelector helper | ✅ Done (dù là Speculative) |

## Decisions đã chốt (từ review + apply)

### Candidate 2: Enemy.getCurrentHp int → float

- **Trigger:** Hero deepening chọn float HP, nhưng Enemy interface vẫn int → type leak.
- **Change:** `Enemy.getCurrentHp()` returns `float`. Hero trả float trực tiếp (xóa `Math.round`). Xóa `getCurrentHpExact()`.
- **Blast radius:** Enemy, Hero, TestEnemy, HeroTest, LowestHPTest, NearestEnemyTest (literal updates).
- **Test mới:** `LowestHPTest.fractionalHpComparisonPicksTheTrueLower` — verify 99.4f vs 99.6f chọn đúng 99.4f.

### Candidate 1: Position.distanceTo

- **Trigger:** NearestEnemy giữ private `distance(Position, Position)` static method. Math leak qua seam.
- **Change:** Position có instance method `distanceTo(Position other)`. NearestEnemy gọi `attacker.getPosition().distanceTo(...)`.
- **Test mới:** `PositionTest` (4 tests) — zero distance, Pythagorean 3-4-5, symmetry, negative coords.

### Candidate 3: MinSelector helper

- **Trigger:** NearestEnemy và LowestHP duplicate vòng lặp "iterate + if < minScore".
- **Change:** Thêm `MinSelector.minBy(List<T>, ToDoubleFunction<T>)`. NearestEnemy và LowestHP trở thành 1-liner.
- **Test mới:** `MinSelectorTest` (3 tests) — LowestHP-style scoring, tie-break, single item.

## Implications

### Code shrinkage

```
NearestEnemy: ~35 dòng → 21 dòng  (-40%)
LowestHP:     ~30 dòng → 21 dòng  (-30%)
Hero:         ~80 dòng → 95 dòng  (+15%, do thêm combat semantics + sửa int→float; không có rò rỉ logic)
Position:     ~5 dòng → 20 dòng   (+300%, nhưng đó là deepening — gain locality)
```

Mặc dù tổng dòng code tăng (do thêm test files và MinSelector), **interface complexity giảm** — NearestEnemy và LowestHP giờ chỉ còn 1 method thân hàm 1-dòng. Đó là deepening thật.

### Test coverage

- Trước: 14 tests (3 NearestEnemy + 3 LowestHP + 8 Hero).
- Sau: 22 tests (+4 Position + 3 MinSelector + 1 LowestHP fractional + 1 đã có ở Hero).
- Mỗi module có seam test riêng. Position có pure-isolation tests (không qua strategy).

### Locality gains

- HP type decision: 1 chỗ (Enemy interface). Không còn bridge method ở Hero.
- Distance math: 1 chỗ (Position). NearestEnemy pure algorithm.
- Min selection: 1 chỗ (MinSelector). Strategies chỉ cần scorer.

### Friction còn lại (chưa address)

1. **Workspace không có CONTEXT.md** (chỉ có GLOSSARY.md). Engineering skills kỳ vọng CONTEXT.md + docs/adr/. GLOSSARY đã được dùng thay — flag này persist qua 2 reviews.
2. **Hero exception paths chưa test:** `takeDamage(-x)` throw IllegalArgumentException, `heal(-x)` throw, constructor `maxHp <= 0` throw. None of these have tests.
3. **Strategy 4+ chưa có:** nếu thêm FarthestEnemy, MostHP, v.v., MinSelector sẽ chứng minh giá trị. Hiện tại chỉ tiết kiệm được ~10 dòng duplicated.

### Bài học về deepening

Khi một module được deepen (Hero thêm combat semantics), các module liên quan (Enemy interface) có thể bị "trượt" — interface không còn match với internal types. Đây là friction mới phát sinh SAU khi deepening. Cần re-review sau mỗi deepening quan trọng.