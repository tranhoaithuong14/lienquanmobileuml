package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AttributeTest {

    private static Attribute allZeroExceptHp(float hp) {
        return new Attribute(
                /* basic:       */ hp, 0f, 0f, 0f, 0f, 0f,
                /* offensive:   */ 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                /* attackRange: */ AttackRange.MELEE,
                /* role:        */ HeroRole.WARRIOR);
    }

    @Test
    void recordEqualityHolds() {
        Attribute a = allZeroExceptHp(100f);
        Attribute b = allZeroExceptHp(100f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void rejectsNonPositiveHp() {
        assertThrows(IllegalArgumentException.class, () -> allZeroExceptHp(0f));
        assertThrows(IllegalArgumentException.class, () -> allZeroExceptHp(-1f));
    }

    @Test
    void rejectsNonPositiveMovementSpeed() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f,
                /* movementSpeed */ 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR));
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f,
                /* movementSpeed */ -1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR));
    }

    @Test
    void rejectsCritChanceOutsideUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                /* critChance */ -0.01f, /* critDamage */ 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR));
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                /* critChance */ 1.01f, /* critDamage */ 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR));
    }

    @Test
    void rejectsCritDamageBelowOne() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* critDamage */ 0.99f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR));
    }

    @Test
    void allowsHighCritDamage() {
        Attribute s = new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* critDamage */ 5.0f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR);

        assertEquals(5.0f, s.critDamage());
    }

    @Test
    void rejectsCooldownReductionAboveCap() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                /* cooldownReduction */ 0.41f, 0f, 0f,
                AttackRange.MELEE, HeroRole.WARRIOR));
    }

    @Test
    void rejectsNullAttackRange() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                null, HeroRole.WARRIOR));
    }

    @Test
    void rejectsNullRole() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE, null));
    }
}