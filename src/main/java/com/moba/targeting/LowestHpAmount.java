package com.moba.targeting;

import com.moba.combat.Enemy;

import java.util.List;

public class LowestHpAmount implements TargetSelector {

    @Override
    public Enemy select(TargetingRequest request, List<Enemy> candidates) {
        if (candidates.isEmpty()) {
            return null;
        }
        return MinSelector.minBy(candidates, Enemy::getCurrentHp);
    }
}
