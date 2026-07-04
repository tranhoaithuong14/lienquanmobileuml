package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OffensiveTest {

    private static Offensive validOffensive() {
        return Offensive.of(360f, 0f, 0f, 0f, 0f, 2.0f, 0f, 0f, 0f, 0f, 0f);
    }

    @Test
    void recordEqualityHolds() {
        Offensive a = validOffensive();
        Offensive b = Offensive.of(360f, 0f, 0f, 0f, 0f, 2.0f, 0f, 0f, 0f, 0f, 0f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void rejectsNonPositiveMovementSpeed() {
        assertThrows(IllegalArgumentException.class,
                () -> Offensive.of(0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Offensive.of(-1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f));
    }

    @Test
    void rejectsCritChanceOutsideUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> Offensive.of(1f, 0f, 0f, 0f, -0.01f, 1f, 0f, 0f, 0f, 0f, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Offensive.of(1f, 0f, 0f, 0f, 1.01f, 1f, 0f, 0f, 0f, 0f, 0f));
    }

    @Test
    void rejectsCritDamageBelowOne() {
        assertThrows(IllegalArgumentException.class,
                () -> Offensive.of(1f, 0f, 0f, 0f, 0f, 0.99f, 0f, 0f, 0f, 0f, 0f));
    }

    @Test
    void allowsHighCritDamage() {
        Offensive s = Offensive.of(1f, 0f, 0f, 0f, 0f, 5.0f, 0f, 0f, 0f, 0f, 0f);

        assertEquals(5.0f, s.critDamage());
    }

    @Test
    void rejectsCooldownReductionAboveCap() {
        assertThrows(IllegalArgumentException.class,
                () -> Offensive.of(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0.41f, 0f, 0f));
    }
}
