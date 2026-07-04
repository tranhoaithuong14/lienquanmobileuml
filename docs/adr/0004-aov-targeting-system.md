# 0004 — AoV targeting system replaces hero-owned selector

## Status

accepted

## Context

This repo models Arena of Valor / Liên Quân Mobile, not a generic simplified MOBA. AoV targeting depends on player control settings and action context:

- `Targeting Priority` applies to normal attacks and tap-to-cast abilities.
- Finisher abilities such as Tulen/Butterfly style ultimates can override the player priority and target lowest HP.
- Directional tap abilities use target kind priority (`HERO > MINION > TOWER` in the official note; this model treats `MONSTER` as a non-hero unit between minion and tower).
- Avatar lock/manual lock can override automatic scoring when the locked target is eligible.
- Lowest HP amount and lowest HP percent are distinct settings.

References:

- Official AoV November 2019 patch note, "Match Controls Tweaks": https://sok.proximabeta.com/webplat/info/news_version3/26190/33375/33376/33731/m19427/201911/838444.shtml
- AoV settings guide distinguishing lowest HP amount vs lowest HP percent: https://samurai-gamers.com/arena-of-valor/recommended-settings/

## Decision

Replace the old `Hero`-owned `TargetSelector` model with a `com.moba.targeting.TargetingSystem`.

`Hero` is now only a combat unit and targetable entity. It owns identity, position, attributes, and HP lifecycle. It does not own player targeting priority.

`TargetingSystem` is the Context. Its interface is:

```java
Enemy select(TargetingRequest request, List<Enemy> candidates)
```

`TargetingRequest` carries the full action context: attacker, action type, player priority, range, out-of-range tolerance, allowed target kinds, and optional locked target.

`TargetSelector` remains a Strategy, but only inside the targeting module. Concrete strategies are:

- `NearestTarget`
- `LowestHpAmount`
- `LowestHpPercent`

## Consequences

- The pattern is still Strategy, but the seam is now in the targeting system, not on `Hero`.
- AoV settings can vary per player/action without rebuilding heroes.
- `Enemy` exposes `maxHp` and `TargetKind` so HP percent and target filters are modelled explicitly.
- The model can grow toward attack buttons, skill metadata, and ability-specific overrides without bloating `Hero`.
