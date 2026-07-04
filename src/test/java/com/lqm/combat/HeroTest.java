package com.lqm.combat;

import com.lqm.strategy.NearestEnemy;
import com.lqm.test.TestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cho Hero (Context trong Strategy pattern + combat lifecycle).
 * Mỗi cycle = 1 test method, theo TDD progression.
 */
class HeroTest {

    @Test
    void selectTargetReturnsNullWhenEnemiesListIsEmpty() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        Enemy result = hero.selectTarget(List.of());

        assertNull(result);
    }

    /** Cycle 1 — GREEN. */
    @Test
    void takeDamageReducesCurrentHp() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(30.0f);

        assertEquals(70.0f, hero.getCurrentHpExact());
    }

    /** Cycle 2 — GREEN. */
    @Test
    void takeDamageClampsAtZero() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(150.0f);

        assertEquals(0.0f, hero.getCurrentHpExact());
    }

    /** Cycle 3 — GREEN. */
    @Test
    void takeDamageToZeroKillsHeroAndNullsSelectTarget() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());
        Enemy a = TestEnemy.at(new Position(5, 0), 100, "A");
        Enemy b = TestEnemy.at(new Position(3, 4), 100, "B");

        hero.takeDamage(100.0f);

        assertFalse(hero.isAlive(), "Hero with 0 HP must not be alive");
        assertNull(hero.selectTarget(List.of(a, b)),
                "Dead Hero must not return any target, even with valid enemies");
    }

    /** Cycle 4 — GREEN. */
    @Test
    void healIncreasesCurrentHp() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(30.0f);
        hero.heal(20.0f);

        assertEquals(90.0f, hero.getCurrentHpExact());
        assertTrue(hero.isAlive(), "Healed hero must still be alive");
    }

    /**
     * Cycle 5 — characterization test (impl đã có clamp ở cycle 4).
     *
     * Quy tắc: heal không bao giờ tăng currentHp vượt maxHp.
     * Hero đang full HP (100) + heal(50) → vẫn 100 (không 150).
     */
    @Test
    void healClampsAtMaxHp() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.heal(50.0f);

        assertEquals(100.0f, hero.getCurrentHpExact());
    }

    /**
     * Cycle 6 — RED→GREEN.
     *
     * Quy tắc: heal trên dead hero là no-op (match game thật).
     * takeDamage(100) → dead, currentHp=0. heal(50) → vẫn 0, vẫn dead.
     */
    @Test
    void healOnDeadHeroIsNoOp() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(100.0f);
        hero.heal(50.0f);

        assertEquals(0.0f, hero.getCurrentHpExact());
        assertFalse(hero.isAlive(), "Heal on dead hero must not revive");
    }

    /**
     * Cycle 7 — RED→GREEN.
     *
     * Quy tắc: respawn() đưa dead hero về trạng thái alive với full HP.
     * takeDamage(100) → dead. respawn() → currentHp=maxHp, isAlive=true.
     */
    @Test
    void respawnRestoresHeroToFullHpAndAlive() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(100.0f);
        hero.respawn();

        assertEquals(100.0f, hero.getCurrentHpExact());
        assertTrue(hero.isAlive(), "Respawned hero must be alive");
    }
}