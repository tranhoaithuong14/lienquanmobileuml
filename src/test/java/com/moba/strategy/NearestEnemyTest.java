package com.moba.strategy;

import com.moba.combat.Enemy;
import com.moba.combat.Position;
import com.moba.test.EnemyStub;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class NearestEnemyTest {

    @Test
    void returnsNullWhenEnemiesListIsEmpty() {
        NearestEnemy selector = new NearestEnemy();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");

        Enemy result = selector.select(attacker, List.of());

        assertNull(result);
    }

    @Test
    void returnsTheOnlyEnemyWhenListHasOneElement() {
        NearestEnemy selector = new NearestEnemy();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");
        Enemy lone = EnemyStub.at(new Position(5, 5), 100, "Lone");

        Enemy result = selector.select(attacker, List.of(lone));

        assertSame(lone, result);
    }

    @Test
    void returnsNearestEnemyWhenMultipleEnemies() {
        NearestEnemy selector = new NearestEnemy();
        Enemy attacker = EnemyStub.at(new Position(0, 0), 100, "Attacker");
        Enemy far = EnemyStub.at(new Position(10, 0), 100, "Far");
        Enemy near = EnemyStub.at(new Position(3, 4), 100, "Near");

        Enemy result = selector.select(attacker, List.of(far, near));

        assertSame(near, result);
    }
}
