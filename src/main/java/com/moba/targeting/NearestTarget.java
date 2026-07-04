package com.moba.targeting;

import com.moba.combat.Enemy;

import java.util.List;

public class NearestTarget implements TargetSelector {

    @Override
    public Enemy select(TargetingRequest request, List<Enemy> candidates) {
        if (candidates.isEmpty()) {
            return null;
        }
        return MinSelector.minBy(candidates,
                candidate -> request.attacker().getPosition().distanceTo(candidate.getPosition()));
    }
}
