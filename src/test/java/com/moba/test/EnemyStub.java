package com.moba.test;

import com.moba.combat.Enemy;
import com.moba.combat.Position;

public final class EnemyStub {

    private EnemyStub() {}

    public static Enemy at(Position position, float hp, String name) {
        return new Enemy() {
            @Override public Position getPosition() { return position; }
            @Override public float getCurrentHp() { return hp; }
            @Override public String getName() { return name; }
        };
    }
}
