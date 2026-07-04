# 0006 — Architecture review #3 applied: CONTEXT.md + ADRs + Hero split

## Status

active

## Evidence

After Hero deepening + reviews #1, #2, applied both candidates from review #3:

| # | Candidate | Status |
|---|-----------|--------|
| 1 | CONTEXT.md + docs/adr/0001, 0002 | ✅ Done |
| 2 | Hero + CombatStats composition | ✅ Done |

The persistent flag (CONTEXT.md missing) was resolved after 3 reviews.

## Changes applied

### Candidate 1: CONTEXT.md + ADRs

**CONTEXT.md** (new, repo root):
- Single-context "MOBA — Target Selection & Combat Context"
- Language subheadings: Pattern terms, Target Selection, Combat Lifecycle, Geometry, Strategy Helpers
- 18 terms with definitions + `_Avoid_` aliases
- Matches the single-context layout from CONTEXT-FORMAT.md

**docs/adr/0001-strategy-pattern-for-target-selection.md** (new):
- Status: accepted
- Decision: GoF Strategy with 2 concrete strategies (Nearest, LowestHP); per-ability logic out of scope
- Considered: if/else, inheritance, switch-enum
- Consequences: public seam, O/C friendly

**docs/adr/0002-hero-combat-semantics.md** (new):
- Status: accepted
- Decision: float HP, maxHp final at constructor, heal no-op when dead, respawn separate
- Considered: int HP, mutable maxHp, heal auto-revive, isAlive only (no alive flag)
- Consequences: `Enemy.getCurrentHp` float, removed `getCurrentHpExact`, constructor validation

### Candidate 2: Hero + CombatStats composition

**CombatStats.java** (new):
- Pure HP state machine: maxHp, currentHp, alive + takeDamage/heal/respawn/isAlive/getCurrentHp/getMaxHp
- 0 dependencies on Hero/Position/TargetSelector — pure, reusable
- Constructor throws when maxHp ≤ 0

**Hero.java** (refactored):
- Fields: name, position, stats (CombatStats), targetSelector
- takeDamage/heal/respawn/isAlive/getCurrentHp → delegate to stats
- selectTarget → check stats.isAlive() before delegating
- Implements Enemy (getPosition/getCurrentHp/getName)

**CombatStatsTest.java** (new, 11 tests):
- 7 HP lifecycle tests (mirror HeroTest cycles 1–7, now on CombatStats)
- 3 exception path tests (constructor non-positive maxHp, takeDamage(-x), heal(-x)) — **closes the coverage gap flagged in LR-0005**
- 1 hero kill test (overlap with the old test, rewritten for CombatStats)

**HeroTest.java** (refactored, 5 tests):
- selectTargetReturnsNullWhenEnemiesListIsEmpty
- deadHeroReturnsNullFromSelectTargetEvenWithEnemies
- getCurrentHpDelegatesToCombatStats
- takeDamageDelegatesToCombatStats
- aliveHeroDelegatesTargetSelectionToStrategy (uses a stub TargetSelector)

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

Hero shrunk 95 → 60 lines (-37%). CombatStats is 75 lines but has zero coupling back to Hero/Position. Each module is now single-purpose.

### Test count

22 → 30 tests (+8):
- HeroTest: 8 → 5 (-3, lifecycle tests moved)
- CombatStatsTest: 0 → 11 (+11)
- Other tests: unchanged

The 3 exception path tests closed the coverage gap from LR-0005.

### Locality

- HP state + HP actions: 100% in CombatStats. Hero no longer has an HP field.
- Identity + targeting: 100% in Hero. CombatStats doesn't know about Hero/Position/selector.
- Future combat mechanics (damage modifiers, status effects, regen): add to CombatStats, no bloat on Hero.

### Leverage

- CombatStats is reusable: tower, creep, summon can compose the same module. Just implement Enemy (getCurrentHp + getPosition).
- Tests are decoupled: CombatStats tests are pure, no need to set up Hero + position + selector.

### Workspace debt resolved

CONTEXT.md + docs/adr/ now align with engineering-skills conventions. Future architecture reviews will:
- Anchor domain vocabulary in CONTEXT.md (single source of truth).
- Reference ADRs before re-litigating (ADR-0001 for Strategy choice, ADR-0002 for HP semantics).
- Stop re-flagging the persistent note.

### Friction remaining

- GLOSSARY.md and CONTEXT.md currently overlap a lot. Could deprecate GLOSSARY in a few sessions, or keep GLOSSARY for teaching context and CONTEXT for engineering context. Undecided.
- Are other modules (NearestEnemy, LowestHP, MinSelector, Position) shallow? Past reviews addressed this; no new friction beyond the Hero split already done.

## Lessons

### Deepening sometimes "drags" dependent modules

When Hero was deepened (float HP added), the Enemy interface had to follow (int → float). Lesson: after deepening a module, check the interfaces it implements / depends on — they may have become inconsistent.

### Workspace-level flags persist if not addressed

CONTEXT.md missing was flagged 3 times. Every review paid the cost to re-note it. Lesson: a persistent flag deserves early action — apply it or write an ADR so future reviews stop re-flagging.

### Composition beats inheritance even for modules that don't "need" splitting yet

Hero at 95 lines was still workable. But future combat mechanics (armor, status effects) will certainly be added. Composition done preemptively is cheap; composition done later is an expensive rewrite.