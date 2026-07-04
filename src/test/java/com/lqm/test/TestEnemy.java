package com.lqm.test;

import com.lqm.combat.Enemy;
import com.lqm.combat.Position;

/**
 * Test helper: tạo Enemy implementation giả cho unit test.
 * Dùng anonymous inner class — không cần Mockito cho interfaces đơn giản.
 *
 * HP là float để khớp với Enemy interface.
 */
public final class TestEnemy {

    private TestEnemy() {}

    public static Enemy at(Position position, float hp, String name) {
        return new Enemy() {
            @Override public Position getPosition() { return position; }
            @Override public float getCurrentHp() { return hp; }
            @Override public String getName() { return name; }
        };
    }
}