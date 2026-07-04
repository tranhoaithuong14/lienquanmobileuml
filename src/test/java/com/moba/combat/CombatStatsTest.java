package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CombatStatsTest {

    @Test
    void takeDamageDecreasesCurrentHp() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(30f);

        assertEquals(70f, stats.getCurrentHp());
        assertTrue(stats.isAlive());
    }

    @Test
    void takeDamageBeyondMaxDoesNotGoNegative() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(150f);

        assertEquals(0f, stats.getCurrentHp());
        assertFalse(stats.isAlive());
    }

    @Test
    void takeDamageExactlyReachingZeroKills() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(100f);

        assertFalse(stats.isAlive());
        assertEquals(0f, stats.getCurrentHp());
    }

    @Test
    void healIncreasesCurrentHp() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(30f);
        stats.heal(20f);

        assertEquals(90f, stats.getCurrentHp());
        assertTrue(stats.isAlive());
    }

    @Test
    void healCannotExceedMaxHp() {
        CombatStats stats = new CombatStats(100f);

        stats.heal(50f);

        assertEquals(100f, stats.getCurrentHp());
    }

    @Test
    void healOnDeadStatsIsANoOp() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(100f);
        stats.heal(50f);

        assertEquals(0f, stats.getCurrentHp());
        assertFalse(stats.isAlive());
    }

    @Test
    void respawnRefillsHpAndRestoresAlive() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(100f);
        stats.respawn();

        assertEquals(100f, stats.getCurrentHp());
        assertTrue(stats.isAlive());
    }

    @Test
    void constructorRejectsNonPositiveMaxHp() {
        assertThrows(IllegalArgumentException.class, () -> new CombatStats(0f));
        assertThrows(IllegalArgumentException.class, () -> new CombatStats(-1f));
    }

    @Test
    void takeDamageRejectsNegativeAmount() {
        CombatStats stats = new CombatStats(100f);

        assertThrows(IllegalArgumentException.class, () -> stats.takeDamage(-10f));
    }

    @Test
    void healRejectsNegativeAmount() {
        CombatStats stats = new CombatStats(100f);

        assertThrows(IllegalArgumentException.class, () -> stats.heal(-10f));
    }
}
