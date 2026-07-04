package com.lqm.strategy;

import com.lqm.combat.Enemy;
import com.lqm.test.TestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test cho MinSelector — pure helper, không có dependency game-domain.
 * Test qua Enemy interface (LowestHP use case) + raw Object (generic case).
 */
class MinSelectorTest {

    /**
     * Case 1 — dùng qua LowestHP-style scoring (currentHp).
     * Verify: trả về enemy có HP thấp nhất.
     */
    @Test
    void picksLowestHpEnemy() {
        Enemy healthy = TestEnemy.at(new com.lqm.combat.Position(0, 0), 100, "Healthy");
        Enemy wounded = TestEnemy.at(new com.lqm.combat.Position(0, 0), 30, "Wounded");
        Enemy dying = TestEnemy.at(new com.lqm.combat.Position(0, 0), 5, "Dying");

        Enemy result = MinSelector.minBy(List.of(healthy, wounded, dying), Enemy::getCurrentHp);

        assertSame(dying, result);
    }

    /**
     * Case 2 — tie-break: 2 phần tử cùng score, phần tử đầu tiên thắng.
     * Verify: behavior "stable, không random" — quan trọng cho strategies phía trên.
     */
    @Test
    void tieBreakReturnsFirstItemInList() {
        Enemy a = TestEnemy.at(new com.lqm.combat.Position(0, 0), 50, "A");
        Enemy b = TestEnemy.at(new com.lqm.combat.Position(0, 0), 50, "B");

        Enemy result = MinSelector.minBy(List.of(a, b), Enemy::getCurrentHp);

        assertSame(a, result, "Same score → first item wins");
    }

    /**
     * Case 3 — single item list, trả về item đó.
     */
    @Test
    void returnsTheOnlyItemWhenListHasOneElement() {
        Enemy lone = TestEnemy.at(new com.lqm.combat.Position(0, 0), 42, "Lone");

        Enemy result = MinSelector.minBy(List.of(lone), Enemy::getCurrentHp);

        assertSame(lone, result);
    }
}