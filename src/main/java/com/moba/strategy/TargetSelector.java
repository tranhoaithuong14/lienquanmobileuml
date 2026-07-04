package com.moba.strategy;

import com.moba.combat.Enemy;

import java.util.List;

public interface TargetSelector {
    Enemy select(Enemy attacker, List<Enemy> enemies);
}
