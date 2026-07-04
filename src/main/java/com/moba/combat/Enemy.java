package com.moba.combat;

public interface Enemy {
    Position getPosition();
    float getCurrentHp();
    float getMaxHp();
    TargetKind getTargetKind();
    String getName();

    default boolean isAlive() {
        return getCurrentHp() > 0f;
    }

    default float getCurrentHpPercent() {
        if (getMaxHp() <= 0f) {
            return 0f;
        }
        return getCurrentHp() / getMaxHp();
    }
}
