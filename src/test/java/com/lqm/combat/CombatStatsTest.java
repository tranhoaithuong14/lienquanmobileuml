package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test cho CombatStats — pure HP state machine, không phụ thuộc Hero.
 * Tách ra từ HeroTest sau khi split Hero → Hero + CombatStats.
 *
 * 8 tests cover toàn bộ lifecycle + 1 test cho constructor validation
 * (gap đã được close ở review #3 follow-up).
 */
class CombatStatsTest {

    /** Cycle 1 — GREEN. */
    @Test
    void takeDamageReducesCurrentHp() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(30f);

        assertEquals(70f, stats.getCurrentHp());
        assertTrue(stats.isAlive());
    }

    /** Cycle 2 — GREEN. */
    @Test
    void takeDamageClampsAtZero() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(150f);

        assertEquals(0f, stats.getCurrentHp());
        assertFalse(stats.isAlive());
    }

    /** Cycle 3 — GREEN. */
    @Test
    void takeDamageToZeroKills() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(100f);

        assertFalse(stats.isAlive());
        assertEquals(0f, stats.getCurrentHp());
    }

    /** Cycle 4 — GREEN. */
    @Test
    void healIncreasesCurrentHp() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(30f);
        stats.heal(20f);

        assertEquals(90f, stats.getCurrentHp());
        assertTrue(stats.isAlive());
    }

    /** Cycle 5 — characterization. */
    @Test
    void healClampsAtMaxHp() {
        CombatStats stats = new CombatStats(100f);

        stats.heal(50f);

        assertEquals(100f, stats.getCurrentHp());
    }

    /** Cycle 6 — GREEN. */
    @Test
    void healOnDeadIsNoOp() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(100f);
        stats.heal(50f);

        assertEquals(0f, stats.getCurrentHp());
        assertFalse(stats.isAlive());
    }

    /** Cycle 7 — GREEN. */
    @Test
    void respawnRestoresFullHpAndAlive() {
        CombatStats stats = new CombatStats(100f);

        stats.takeDamage(100f);
        stats.respawn();

        assertEquals(100f, stats.getCurrentHp());
        assertTrue(stats.isAlive());
    }

    /** Coverage gap đã close — exception path. */
    @Test
    void constructorThrowsOnNonPositiveMaxHp() {
        assertThrows(IllegalArgumentException.class, () -> new CombatStats(0f));
        assertThrows(IllegalArgumentException.class, () -> new CombatStats(-1f));
    }

    /** Exception path cho takeDamage — coverage gap đã close. */
    @Test
    void takeDamageThrowsOnNegativeAmount() {
        CombatStats stats = new CombatStats(100f);

        assertThrows(IllegalArgumentException.class, () -> stats.takeDamage(-10f));
    }

    /** Exception path cho heal — coverage gap đã close. */
    @Test
    void healThrowsOnNegativeAmount() {
        CombatStats stats = new CombatStats(100f);

        assertThrows(IllegalArgumentException.class, () -> stats.heal(-10f));
    }
}