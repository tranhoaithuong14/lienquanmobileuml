package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseStatsTest {

    private static BaseStats allZeroExceptMaxHp(float maxHp) {
        return new BaseStats(
                /* core:        */ maxHp, 0f, 0f, 0f, 0f, 0f,
                /* offensive:   */ 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                /* attackRange: */ AttackRange.MELEE);
    }

    @Test
    void recordEqualityHolds() {
        BaseStats a = allZeroExceptMaxHp(100f);
        BaseStats b = allZeroExceptMaxHp(100f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void rejectsNonPositiveMaxHp() {
        assertThrows(IllegalArgumentException.class, () -> allZeroExceptMaxHp(0f));
        assertThrows(IllegalArgumentException.class, () -> allZeroExceptMaxHp(-1f));
    }

    @Test
    void rejectsNonPositiveMovementSpeed() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f,
                /* movementSpeed */ 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f,
                /* movementSpeed */ -1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE));
    }

    @Test
    void rejectsCritChanceOutsideUnit() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                /* critChance */ -0.01f, /* critDamage */ 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                /* critChance */ 1.01f, /* critDamage */ 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE));
    }

    @Test
    void rejectsCritDamageBelowOne() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* critDamage */ 0.99f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE));
    }

    @Test
    void allowsHighCritDamage() {
        BaseStats s = new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* critDamage */ 5.0f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE);

        assertEquals(5.0f, s.critDamage());
    }

    @Test
    void rejectsCooldownReductionAboveCap() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                /* cooldownReduction */ 0.41f, 0f, 0f,
                AttackRange.MELEE));
    }

    @Test
    void rejectsNullAttackRange() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                null));
    }
}
