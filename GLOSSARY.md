# Strategy Pattern Glossary

Canonical vocabulary for the Strategy pattern teaching workspace. Every lesson, code sample, and reference must use these terms. If a new term shows up in a lesson, it is only added here after the user has used it correctly in a quiz or exercise.

## Terms

**Strategy**:
An interface (or abstract class) that defines a *behavior* with multiple interchangeable algorithms.
_Avoid_: Algorithm interface, behavior contract

**ConcreteStrategy**:
A class that implements `Strategy` and provides one concrete way to carry out the behavior.
_Avoid_: Strategy implementation, behavior variant

**Context**:
A class that holds a reference to a `Strategy` and delegates work to it. The Context does not know which ConcreteStrategy is in use.
_Avoid_: Client, Caller, Holder

**Client**:
External code that chooses which ConcreteStrategy to wire into the Context (at construction time or via a setter).
_Avoid_: User code, Main

**Target Selection**:
In MOBA games, the rule that decides *which enemy* is hit when a hero auto-attacks or uses a skill.
_Avoid_: Targeting, Auto-attack logic

**NearestEnemy**:
A ConcreteStrategy that selects the target with the smallest Euclidean distance from the attacker.
_Avoid_: Closest target strategy

**LowestHP**:
A ConcreteStrategy that selects the target with the lowest current HP.
_Avoid_: Weakest target, Lowest health strategy

**MarkedTarget**:
_(Out of scope.)_ Per-ability logic (marked, target lock, skillshot direction) — does not belong to `TargetSelector`. Don't force it into the interface.
_Avoid_: TargetSelector implementation for marks

**HeroPriority**:
_(Not implemented.)_ Filters the candidate list to only enemy heroes, ignoring minions and jungle monsters. Currently unused in the model because the genre only has 2 global strategies.

## Combat lifecycle (added 2026-07-04)

**CombatStats**:
HP state machine for an entity that can take damage and die. Split out from Hero in review #3 (Hero → Hero + CombatStats composition). Owns maxHp, currentHp, alive + the actions (takeDamage, heal, respawn, isAlive). Reusable for towers, creeps, summons.
_Avoid_: Health, HpState

**currentHp**:
Current HP of the Hero, `float`. Mutable via `takeDamage` and `heal`. Floor 0, ceiling maxHp.
_Avoid_: hp, HP

**maxHp**:
Upper bound of currentHp, `float`. Final, set at constructor. currentHp starts equal to maxHp.
_Avoid_: maxHealth, hpCap

**alive**:
`boolean` flag on `CombatStats`. `true` = can act; `false` = dead. Set `false` when `takeDamage` brings currentHp ≤ 0. Set `true` by `respawn()`. Unchanged by `heal` — heal only works when alive.
_Avoid_: active, isDead

**isAlive**:
Returns `true` if the Hero is still alive. In the current model: `currentHp > 0`. Equivalent to the legacy `alive` flag inside `CombatStats`.
_Avoid_: isDead (the opposite)

**takeDamage**:
Hero method that decreases currentHp. Accepts amount ≥ 0. Floors at 0. Marks the Hero dead if currentHp reaches 0.
_Avoid_: damage, applyDamage

**heal**:
Hero method that increases currentHp. Accepts amount ≥ 0. Ceilings at maxHp. No-op if dead (gameplay: dead heroes cannot be healed).
_Avoid_: restore, recover

**respawn**:
Hero method that brings it back to life. Sets `alive=true` and `currentHp=maxHp`. Triggered by an external respawn timer, not from heal.
_Avoid_: revive, spawnAgain

## Geometry (added 2026-07-04)

**distanceTo**:
Method on `Position` returning the Euclidean distance to another `Position`. Symmetric, returns 0 for the same position. Geometry belongs to `Position`, not to any strategy.
_Avoid_: getDistance, euclideanDistance

## Strategy helpers (added 2026-07-04)

**MinSelector**:
Static helper in package `strategy`. Method `minBy(List<T>, ToDoubleFunction<T>)` returns the element with the smallest score. Used by NearestEnemy (scorer = distanceTo) and LowestHP (scorer = getCurrentHp). Tie-break: strict less-than — first element wins.
_Avoid_: minByScorer, findMin
