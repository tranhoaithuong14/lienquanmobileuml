# AoV — Targeting & Combat Context

Domain context for redesigning the in-combat targeting system and the Hero HP lifecycle in Arena of Valor / Liên Quân Mobile, in Java with GoF patterns. This is a single bounded context — all work in this repo belongs to the same context.

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
The in-combat decision that turns a player action into a concrete enemy unit. In AoV, this is not owned by `Hero`; it depends on player targeting priority, target kind filters, avatar lock, range, and action type.
_Avoid_: Hero-owned auto-attack rule

**TargetingSystem**:
Context module for AoV target selection. It filters candidates by alive/range/kind, applies avatar lock when valid, applies directional tap kind priority, resolves action-specific priority, then delegates scoring to a `TargetSelector`.
_Avoid_: Hero target selector

**TargetingRequest**:
Value object carrying the action context: attacker, `TargetingAction`, player `TargetingPriority`, range, out-of-range tolerance, allowed `TargetKind`s, and optional locked target.
_Avoid_: passing attacker + enemies only

**TargetingPriority**:
Player/control setting for automatic selection: `NEAREST`, `LOWEST_HP_AMOUNT`, or `LOWEST_HP_PERCENT`. AoV applies this setting to normal attacks and tap-to-cast abilities; finisher abilities can override it.
_Avoid_: hero strategy

**TargetingAction**:
Kind of action being resolved: normal attack, tap-to-cast ability, directional tap ability, or finisher ability. Action matters because AoV has different lock-on rules for these cases.
_Avoid_: treating every skill as an auto-attack

**TargetSelector**:
Strategy interface inside the targeting module. It scores an already-filtered candidate list for one priority rule.
_Avoid_: storing this on Hero

**NearestTarget**:
ConcreteStrategy that selects the candidate with the smallest Euclidean distance from the attacker.
_Avoid_: NearestEnemy

**LowestHpAmount**:
ConcreteStrategy that selects the candidate with the lowest current HP amount.
_Avoid_: LowestHP

**LowestHpPercent**:
ConcreteStrategy that selects the candidate with the lowest `currentHp / maxHp`.
_Avoid_: conflating percent with amount

**TargetKind**:
The target category used by targeting filters: `HERO`, `MINION`, `MONSTER`, `TOWER`.
_Avoid_: assuming every enemy is a hero

### Combat Lifecycle

**Hero**:
A character in AoV. Both an attacker and a target (implements `Enemy` so the targeting module can pick it). Owns HP lifecycle (currentHp, hp, alive state), but does not own targeting priority.
_Avoid_: Champion, Character, Avatar

**Enemy**:
Any enemy entity that can be picked as a target: Hero, minion, jungle monster, tower. Exposes position, current HP, max HP, target kind, name, alive state, and HP percent for targeting.
_Avoid_: Target, Opponent

**HeroRole**:
A classification bucket that every Hero belongs to, exactly one of `TANK` / `WARRIOR` / `ASSASSIN` / `MAGE` / `MARKSMAN` / `SUPPORT`. Used for UI filter, lane assignment, item recommendations, team-composition analysis — not for combat calculation. Single-value per Hero: the official Liên Quân Mobile data contract models role as one integer (`job` 1–6), and Arena of Valor matches. (Genre note: League of Legends uses a 1–2 tag array — different convention; see `.scratch/research/hero-role-classification.md`.)
_Avoid_: Class, Archetype

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
Static helper in package `targeting`. Method `minBy(List<T>, ToDoubleFunction<T>)` returns the element with the smallest score. Used by `NearestTarget`, `LowestHpAmount`, and `LowestHpPercent`. Tie-break: strict less-than — first element wins.
_Avoid_: minByScorer, findMin
