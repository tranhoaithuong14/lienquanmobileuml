package com.lqm.test;

import com.lqm.combat.Enemy;
import com.lqm.combat.Position;

/**
 * Test helper: tạo Enemy implementation giả cho unit test.
 * Dùng anonymous inner class — không cần Mockito cho interfaces đơn giản.
 */
public final class TestEnemy {

    private TestEnemy() {}

    public static Enemy at(Position position, int hp, String name) {
        return new Enemy() {
            @Override public Position getPosition() { return position; }
            @Override public int getCurrentHp() { return hp; }
            @Override public String getName() { return name; }
        };
    }
}