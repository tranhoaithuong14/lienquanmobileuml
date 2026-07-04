package com.moba.targeting;

import com.moba.combat.Enemy;
import com.moba.combat.Position;
import com.moba.test.EnemyStub;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class MinSelectorTest {

    @Test
    void picksEnemyWithLowestHp() {
        Enemy healthy = EnemyStub.at(new Position(0, 0), 100, "Healthy");
        Enemy wounded = EnemyStub.at(new Position(0, 0), 30, "Wounded");
        Enemy dying = EnemyStub.at(new Position(0, 0), 5, "Dying");

        Enemy result = MinSelector.minBy(List.of(healthy, wounded, dying), Enemy::getCurrentHp);

        assertSame(dying, result);
    }

    @Test
    void tiedScoresMakeTheFirstElementWin() {
        Enemy a = EnemyStub.at(new Position(0, 0), 50, "A");
        Enemy b = EnemyStub.at(new Position(0, 0), 50, "B");

        Enemy result = MinSelector.minBy(List.of(a, b), Enemy::getCurrentHp);

        assertSame(a, result, "Tied scores - first element should win");
    }

    @Test
    void singleElementListReturnsThatElement() {
        Enemy lone = EnemyStub.at(new Position(0, 0), 42, "Lone");

        Enemy result = MinSelector.minBy(List.of(lone), Enemy::getCurrentHp);

        assertSame(lone, result);
    }
}
