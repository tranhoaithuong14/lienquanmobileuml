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

    @Test
    void returnsNullWhenEnemiesListIsEmpty() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");

        Enemy result = selector.select(attacker, List.of());

        assertNull(result);
    }

    @Test
    void returnsTheOnlyEnemyWhenListHasOneElement() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy lone = TestEnemy.at(new Position(0, 0), 50, "Lone");

        Enemy result = selector.select(attacker, List.of(lone));

        assertSame(lone, result);
    }

    @Test
    void returnsLowestHpEnemyWhenMultipleAreInRange() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy healthy = TestEnemy.at(new Position(5, 0), 100, "Healthy");
        Enemy wounded = TestEnemy.at(new Position(5, 0), 30, "Wounded");

        Enemy result = selector.select(attacker, List.of(healthy, wounded));

        assertSame(wounded, result);
    }

    /**
     * Cycle mới (Candidate 2 fix): verify float HP comparison chính xác.
     *
     * Hai enemy có HP float gần nhau nhưng khác nhau — chọn đúng cái thấp hơn.
     * Với int HP (cũ): cả 2 round về 99 → tie-break sai.
     * Với float HP (mới): 99.4f < 99.6f → chọn đúng.
     */
    @Test
    void fractionalHpComparisonPicksTheTrueLower() {
        LowestHP selector = new LowestHP();
        Enemy attacker = TestEnemy.at(new Position(0, 0), 100, "Attacker");
        Enemy a = TestEnemy.at(new Position(5, 0), 99.6f, "A");
        Enemy b = TestEnemy.at(new Position(5, 0), 99.4f, "B");

        Enemy result = selector.select(attacker, List.of(a, b));

        assertSame(b, result, "99.4f < 99.6f — must pick B regardless of list order");
    }
}