# AoV Targeting Glossary

Canonical vocabulary for the AoV targeting teaching workspace. Strategy remains part of the implementation, but the domain source of truth is the targeting pipeline, not a hero-owned selector.

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
The in-combat decision that turns a player action into a concrete enemy unit. In AoV this depends on player priority, action type, range, target kind filters, and avatar lock.
_Avoid_: Hero-owned auto-attack logic

**TargetingSystem**:
Context module that filters target candidates, applies AoV-specific action rules, then delegates scoring to a priority Strategy.
_Avoid_: Hero target selector

**TargetingRequest**:
Value object containing attacker, action, player priority, range, out-of-range tolerance, allowed target kinds, and optional locked target.
_Avoid_: passing only attacker + candidates

**TargetingPriority**:
Player/control setting: `NEAREST`, `LOWEST_HP_AMOUNT`, or `LOWEST_HP_PERCENT`.
_Avoid_: hero strategy

**TargetingAction**:
The action being resolved: normal attack, tap-to-cast ability, directional tap ability, or finisher ability.
_Avoid_: treating every action as normal attack

**TargetSelector**:
Strategy interface inside `com.moba.targeting`. It chooses one target from an already-filtered candidate list.
_Avoid_: storing TargetSelector on Hero

**NearestTarget**:
ConcreteStrategy that selects the candidate with the smallest Euclidean distance from the attacker.
_Avoid_: NearestEnemy

**LowestHpAmount**:
ConcreteStrategy that selects the candidate with the lowest current HP amount.
_Avoid_: LowestHP

**LowestHpPercent**:
ConcreteStrategy that selects the candidate with the lowest current HP percentage.
_Avoid_: conflating HP amount with HP percent

**TargetKind**:
Target category: `HERO`, `MINION`, `MONSTER`, `TOWER`.
_Avoid_: assuming every Enemy is a Hero

## Combat lifecycle (added 2026-07-04)

**CombatStats**:
HP state machine for an entity that can take damage and die. Split out from Hero in review #3 (Hero → Hero + CombatStats composition). Owns hp, currentHp, alive + the actions (takeDamage, heal, respawn, isAlive). Reusable for towers, creeps, summons.
_Avoid_: Health, HpState

**currentHp**:
Current HP of the Hero, `float`. Mutable via `takeDamage` and `heal`. Floor 0, ceiling hp.
_Avoid_: hp, HP

**hp**:
Upper bound of currentHp, `float`. Final, set at constructor. currentHp starts equal to hp. Matches the "HP" label on the in-game hero detail screen.
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
Hero method that increases currentHp. Accepts amount ≥ 0. Ceilings at hp. No-op if dead (gameplay: dead heroes cannot be healed).
_Avoid_: restore, recover

**respawn**:
Hero method that brings it back to life. Sets `alive=true` and `currentHp=hp`. Triggered by an external respawn timer, not from heal.
_Avoid_: revive, spawnAgain

## Geometry (added 2026-07-04)

**distanceTo**:
Method on `Position` returning the Euclidean distance to another `Position`. Symmetric, returns 0 for the same position. Geometry belongs to `Position`, not to any strategy.
_Avoid_: getDistance, euclideanDistance

## Strategy helpers (added 2026-07-04)

**MinSelector**:
Static helper in package `targeting`. Method `minBy(List<T>, ToDoubleFunction<T>)` returns the element with the smallest score. Used by `NearestTarget`, `LowestHpAmount`, and `LowestHpPercent`. Tie-break: strict less-than — first element wins.
_Avoid_: minByScorer, findMin
