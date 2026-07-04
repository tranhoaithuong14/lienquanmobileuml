package com.moba.strategy;

import com.moba.combat.Enemy;

import java.util.List;

public class NearestEnemy implements TargetSelector {

    @Override
    public Enemy select(Enemy attacker, List<Enemy> enemies) {
        if (enemies.isEmpty()) {
            return null;
        }
        return MinSelector.minBy(enemies, e -> attacker.getPosition().distanceTo(e.getPosition()));
    }
}
