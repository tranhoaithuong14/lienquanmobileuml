# 0006 — Architecture review #3 applied: CONTEXT.md + ADRs + Hero split

## Status

active

## Evidence

Sau Hero deepening + reviews #1, #2, đã apply cả 2 candidates từ review #3:

| # | Candidate | Status |
|---|-----------|--------|
| 1 | CONTEXT.md + docs/adr/0001, 0002 | ✅ Done |
| 2 | Hero + CombatStats composition | ✅ Done |

Persistent flag (CONTEXT.md missing) đã resolve sau 3 reviews.

## Changes applied

### Candidate 1: CONTEXT.md + ADRs

**CONTEXT.md** (new, repo root):
- Single-context "Liên Quân Mobile — Target Selection & Combat Context"
- 4 language subheadings: Pattern terms, Target Selection, Combat Lifecycle, Geometry, Strategy Helpers
- 18 terms với definitions + _Avoid_ aliases
- Match CONTEXT-FORMAT.md (single-context, language section, terms grouped)

**docs/adr/0001-strategy-pattern-for-target-selection.md** (new):
- Status: accepted
- Decision: GoF Strategy với 2 concrete strategies (Nearest, LowestHP); per-ability logic out of scope
- Considered: if/else, inheritance, switch-enum
- Consequences: public seam, O/C friendly

**docs/adr/0002-hero-combat-semantics.md** (new):
- Status: accepted
- Decision: float HP, maxHp final at constructor, heal no-op khi dead, respawn riêng
- Considered: int HP, mutable maxHp, heal auto-revive, isAlive only (no active flag)
- Consequences: Enemy.getCurrentHp float, xóa getCurrentHpExact, constructor validation

### Candidate 2: Hero + CombatStats composition

**CombatStats.java** (new):
- HP state machine thuần: maxHp, currentHp, active + takeDamage/heal/respawn/isAlive/getCurrentHp/getMaxHp
- 0 dependencies lên Hero/Position/TargetSelector — pure reusable
- Constructor throws khi maxHp ≤ 0

**Hero.java** (refactored):
- Fields: name, position, stats (CombatStats), targetSelector
- takeDamage/heal/respawn/isAlive/getCurrentHp → delegate sang stats
- selectTarget → check stats.isAlive() trước khi delegate
- Implements Enemy (getPosition/getCurrentHp/getName)

**CombatStatsTest.java** (new, 11 tests):
- 7 HP lifecycle tests (mirror HeroTest cycles 1-7, giờ ở CombatStats)
- 3 exception path tests (constructor non-positive maxHp, takeDamage(-x), heal(-x)) — **đóng coverage gap flagged ở LR-0005**
- 1 hero kill test (overlap với test cũ, đã viết lại cho CombatStats)

**HeroTest.java** (refactored, 5 tests):
- selectTargetReturnsNullWhenEnemiesListIsEmpty
- deadHeroReturnsNullFromSelectTargetEvenWithEnemies
- getCurrentHpDelegatesToCombatStats
- takeDamageDelegatesToCombatStats
- aliveHeroDelegatesTargetSelectionToStrategy (dùng stub TargetSelector)

## Implications

### Code shape

```
Before:                              After:
combat/                              combat/
├── Enemy.java                       ├── Enemy.java
├── Position.java                    ├── Position.java
├── Hero.java (95 lines, 2 concerns) ├── Hero.java (60 lines, identity + targeting)
└── ...                              ├── CombatStats.java (75 lines, HP state machine)
                                     └── ...
```

Hero giảm 95 → 60 dòng (-37%). CombatStats 75 dòng nhưng zero coupling lên Hero/Position. Mỗi module giờ single-purpose.

### Test count

22 → 30 tests (+8):
- HeroTest: 8 → 5 (-3, lifecycle tests moved)
- CombatStatsTest: 0 → 11 (+11)
- Other tests: unchanged

3 exception path tests đã đóng coverage gap từ LR-0005.

### Locality

- HP state + HP actions: 100% trong CombatStats. Hero không còn field HP.
- Identity + targeting: 100% trong Hero. CombatStats không biết Hero/Position/selector.
- Future combat mechanics (damage modifiers, status effects, regen): thêm vào CombatStats, không bloat Hero.

### Leverage

- CombatStats reusable: tower, creep, summon có thể compose cùng module. Chỉ cần implement Enemy interface (getCurrentHp + getPosition).
- Tests tách rời: CombatStats test pure, không cần set up Hero+position+selector.

### Workspace debt resolved

CONTEXT.md + docs/adr/ giờ align với convention engineering skills. Future architecture reviews sẽ:
- Anchor domain vocabulary ở CONTEXT.md (single source of truth).
- Reference ADRs trước khi re-litigate (ADR-0001 cho Strategy choice, ADR-0002 cho HP semantics).
- Stop re-flagging persistent note (resolved).

### Friction còn lại

- GLOSSARY.md và CONTEXT.md hiện chứa overlap lớn. Có thể deprecate GLOSSARY sau vài session, hoặc giữ GLOSSARY cho teaching-context và CONTEXT cho engineering-context. Chưa quyết.
- Module khác (NearestEnemy, LowestHP, MinSelector, Position) có đang shallow không? Các review trước đã address; không thấy friction mới ngoài Hero split đã làm.

## Lessons

### Deepening đôi khi "trượt" modules liên quan

Khi Hero deepened (thêm float HP), Enemy interface cần theo (int → float). Lesson: sau khi deepen một module, kiểm tra interface mà nó implement/depend-on — chúng có thể đã trở nên inconsistent.

### Workspace-level flags persist nếu không được address

CONTEXT.md missing đã được flag 3 lần. Mỗi review lại mất effort để note. Lesson: nếu một flag persistent, nó xứng đáng được address sớm — apply it hoặc ghi ADR để future reviews không re-flag.

### Composition > inheritance ngay cả với module chưa "cần" split

Hero 95 dòng vẫn workable. Nhưng future combat mechanics (armor, status effects) chắc chắn sẽ thêm. Composition done preemptively = cheap; composition done after = expensive rewrite.