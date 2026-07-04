package com.lqm.strategy;

import com.lqm.combat.Enemy;
import com.lqm.combat.Position;
import com.lqm.test.TestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test cho LowestHP — chỉ test public interface select().
 * Mỗi test là một "vertical slice" của behavior.
 */
class LowestHPTest {

    /**
     * Slice 1 — GREEN.
     * Quy tắc: danh sách rỗng → null.
     */
    @Test
    void returnsNullWhenEnemiesListIsEmpty() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");

        Enemy result = selector.select(attacker, List.of());

        assertNull(result);
    }

    /**
     * Slice 2 — GREEN.
     * Quy tắc: 1 enemy → enemy đó.
     */
    @Test
    void returnsTheOnlyEnemyWhenListHasOneElement() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy lone = TestEnemy.at(new Position(0, 0), 50, "Lone");

        Enemy result = selector.select(attacker, List.of(lone));

        assertSame(lone, result);
    }

    /**
     * Slice 3 — GREEN.
     * Quy tắc: trong list nhiều enemy, chọn enemy có HP hiện tại thấp nhất.
     *
     * Test xếp "Healthy" (HP 100) TRƯỚC "Wounded" (HP 30) để impl sai (return first) chắc chắn fail.
     * Position giống nhau để loại trừ yếu tố khoảng cách — verify LowestHP đúng là dựa trên HP.
     */
    @Test
    void returnsLowestHpEnemyWhenMultipleAreInRange() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy healthy = TestEnemy.at(new Position(5, 0), 100, "Healthy");
        Enemy wounded = TestEnemy.at(new Position(5, 0), 30, "Wounded");

        Enemy result = selector.select(attacker, List.of(healthy, wounded));

        assertSame(wounded, result);
    }
}