package com.moba.test;

import com.moba.combat.Enemy;
import com.moba.combat.Position;
import com.moba.combat.TargetKind;

public final class EnemyStub {

    private EnemyStub() {}

    public static Enemy at(Position position, float hp, String name) {
        return at(position, hp, hp, TargetKind.HERO, name);
    }

    public static Enemy at(Position position, float currentHp, float maxHp, TargetKind targetKind, String name) {
        return new Enemy() {
            @Override public Position getPosition() { return position; }
            @Override public float getCurrentHp() { return currentHp; }
            @Override public float getMaxHp() { return maxHp; }
            @Override public TargetKind getTargetKind() { return targetKind; }
            @Override public String getName() { return name; }
        };
    }
}
