package com.moba.targeting;

import com.moba.combat.Enemy;

import java.util.List;

public interface TargetSelector {
    Enemy select(TargetingRequest request, List<Enemy> candidates);
}
