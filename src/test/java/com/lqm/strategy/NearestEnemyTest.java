package com.lqm.strategy;

import com.lqm.combat.Enemy;
import com.lqm.combat.Position;
import com.lqm.test.TestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test cho NearestEnemy — chỉ test public interface select().
 * Mỗi test là một "vertical slice" của behavior: một test → một impl → lặp.
 */
class NearestEnemyTest {

    /**
     * Slice 1 — GREEN.
     * Quy tắc được verify: khi danh sách địch rỗng, strategy trả về null.
     */
    @Test
    void returnsNullWhenEnemiesListIsEmpty() {
        NearestEnemy selector = new NearestEnemy();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");

        Enemy result = selector.select(attacker, List.of());

        assertNull(result);
    }

    /**
     * Slice 2 — GREEN.
     * Quy tắc: chỉ có 1 enemy → enemy đó.
     */
    @Test
    void returnsTheOnlyEnemyWhenListHasOneElement() {
        NearestEnemy selector = new NearestEnemy();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy lone = TestEnemy.at(new Position(5, 5), 100, "Lone");

        Enemy result = selector.select(attacker, List.of(lone));

        assertSame(lone, result);
    }

    /**
     * Slice 3 — GREEN.
     * Quy tắc: trong list nhiều enemy, chọn enemy có khoảng cách Euclidean nhỏ nhất.
     *
     * Test xếp "Far" TRƯỚC "Near" trong list để impl sai (return first) chắc chắn fail.
     * Attacker ở (0,0); Far ở (10,0) — khoảng cách 10; Near ở (3,4) — khoảng cách 5.
     */
    @Test
    void returnsClosestEnemyWhenMultipleAreInRange() {
        NearestEnemy selector = new NearestEnemy();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy far = TestEnemy.at(new Position(10, 0), 100, "Far");
        Enemy near = TestEnemy.at(new Position(3, 4), 100, "Near"); // 3-4-5 triangle, dist = 5

        Enemy result = selector.select(attacker, List.of(far, near));

        assertSame(near, result);
    }
}