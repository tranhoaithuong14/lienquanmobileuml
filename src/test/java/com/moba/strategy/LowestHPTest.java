package com.moba.strategy;

import com.moba.combat.Enemy;
import com.moba.combat.Position;
import com.moba.test.EnemyStub;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class LowestHPTest {

    @Test
    void returnsNullWhenEnemiesListIsEmpty() {
        LowestHP selector = new LowestHP();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");

        Enemy result = selector.select(attacker, List.of());

        assertNull(result);
    }

    @Test
    void returnsTheOnlyEnemyWhenListHasOneElement() {
        LowestHP selector = new LowestHP();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");
        Enemy lone = EnemyStub.at(new Position(0, 0), 50, "Lone");

        Enemy result = selector.select(attacker, List.of(lone));

        assertSame(lone, result);
    }

    @Test
    void returnsEnemyWithLowestHpWhenMultipleEnemies() {
        LowestHP selector = new LowestHP();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");
        Enemy healthy = EnemyStub.at(new Position(5, 0), 100, "Healthy");
        Enemy wounded = EnemyStub.at(new Position(5, 0), 30, "Wounded");

        Enemy result = selector.select(attacker, List.of(healthy, wounded));

        assertSame(wounded, result);
    }

    @Test
    void fractionalHpComparisonPicksTheTrueLower() {
        LowestHP selector = new LowestHP();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");
        Enemy a = EnemyStub.at(new Position(5, 0), 99.6f, "A");
        Enemy b = EnemyStub.at(new Position(5, 0), 99.4f, "B");

        Enemy result = selector.select(attacker, List.of(a, b));

        assertSame(b, result, "99.4f < 99.6f — must pick B regardless of list order");
    }
}
