# MOBA — Target Selection & Combat Context

Domain context for redesigning the in-combat auto-targeting system and the Hero HP lifecycle in a generic MOBA, in Java with GoF patterns. This is a single bounded context — all work in this repo belongs to the same context.

## Language

### Pattern terms (GoF Strategy)

**Strategy**:
An interface describing an interchangeable behavior with multiple algorithms.
_Avoid_: Algorithm interface, behavior contract

**ConcreteStrategy**:
A class implementing `Strategy`, providing one concrete way to carry out that behavior.
_Avoid_: Strategy implementation, behavior variant

**Context**:
A class that holds a reference to a `Strategy` and delegates work to it. The Context does not know which ConcreteStrategy is in use.
_Avoid_: Client, Caller, Holder

**Client**:
External code that chooses which `ConcreteStrategy` to wire into the `Context` (at construction time or via a setter).
_Avoid_: User code, Main

### Target Selection

**Target Selection**:
The hero auto-target rule in MOBA combat — when a hero auto-attacks, who does it hit. There are exactly 2 global auto-target rules in the genre: Nearest and LowestHP. Per-ability logic (marked target, target lock, skillshot direction) is a different concern and does not belong in the `TargetSelector` interface.
_Avoid_: Targeting, Auto-attack logic

**NearestEnemy**:
A ConcreteStrategy that selects the target with the smallest Euclidean distance from the attacker. Inspired by classic ranged-hero kiting logic in any MOBA.
_Avoid_: Closest target strategy

**LowestHP**:
A ConcreteStrategy that selects the target with the lowest current HP. The "focus fire" or "execute" rule common to assassins and finishers.
_Avoid_: Weakest target, Lowest health strategy

### Combat Lifecycle

**Hero**:
A character in a MOBA. Both an attacker (owns a `TargetSelector` to pick a target) and a target (implements `Enemy` so others can pick it). Owns HP lifecycle (currentHp, hp, alive state).
_Avoid_: Champion, Character, Avatar

**Enemy**:
Any entity that can be picked as a target: Hero, minion, jungle monster, tower. Interface the strategies read from.
_Avoid_: Target, Opponent

**currentHp**:
Current HP of the Hero, `float`. Mutable via `takeDamage` and `heal`. Floor 0, ceiling hp.
_Avoid_: hp, HP

**hp**:
Upper bound of `currentHp`, `float`. Final, set at constructor. `currentHp` starts equal to `hp`. Matches the "HP" label on the in-game hero detail screen.
_Avoid_: maxHealth, hpCap

**active** _(historical)_:
A pre-split boolean flag exposed via `isAlive()`. Kept for compatibility with earlier ADRs; in current code, `alive` lives inside `CombatStats`.
_Avoid_: alive, canAct

**isAlive**:
Returns `true` if the Hero is still alive. In this model: `currentHp > 0`.
_Avoid_: isDead (the opposite)

**takeDamage**:
Hero method that decreases currentHp. Accepts amount ≥ 0. Floors at 0. Marks the Hero dead if currentHp reaches 0.
_Avoid_: damage, applyDamage

**heal**:
Hero method that increases currentHp. Accepts amount ≥ 0. Ceilings at hp. No-op if the Hero is dead (matches gameplay: dead heroes cannot be healed).
_Avoid_: restore, recover

**respawn**:
Hero method that brings it back to life. Sets `alive=true` and `currentHp=hp`. Matches gameplay: triggered by an external respawn timer, not from `heal`.
_Avoid_: revive, spawnAgain

### Geometry

**Position**:
A location on the map. Java record with `x` and `y` (double).
_Avoid_: Coordinate, Point

**distanceTo**:
A method on `Position` returning the Euclidean distance to another `Position`. Symmetric, returns 0 for the same position. Geometry belongs to `Position`, not to any strategy.
_Avoid_: getDistance, euclideanDistance

### Strategy Helpers

**MinSelector**:
Static helper in package `strategy`. Method `minBy(List<T>, ToDoubleFunction<T>)` returns the element with the smallest score. Used by NearestEnemy (scorer = `distanceTo`) and LowestHP (scorer = `getCurrentHp`). Tie-break: strict less-than — first element wins.
_Avoid_: minByScorer, findMin
