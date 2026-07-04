package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicTest {

    private static Basic validBasic() {
        return Basic.of(100f, 10f, 0f, 50f, 30f, CombatResource.MANA, 200f);
    }

    @Test
    void recordEqualityHolds() {
        Basic a = validBasic();
        Basic b = Basic.of(100f, 10f, 0f, 50f, 30f, CombatResource.MANA, 200f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void rejectsNonPositiveHp() {
        assertThrows(IllegalArgumentException.class,
                () -> Basic.of(0f, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Basic.of(-1f, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f));
    }

    @Test
    void rejectsNullResourceType() {
        assertThrows(IllegalArgumentException.class,
                () -> Basic.of(100f, 0f, 0f, 0f, 0f, null, 0f));
    }

    @Test
    void rejectsNegativeMaxResource() {
        assertThrows(IllegalArgumentException.class,
                () -> Basic.of(100f, 0f, 0f, 0f, 0f, CombatResource.MANA, -1f));
    }

    @Test
    void rejectsNegativeArmorOrMagicDefense() {
        assertThrows(IllegalArgumentException.class,
                () -> Basic.of(100f, 0f, 0f, -1f, 0f, CombatResource.MANA, 0f));
        assertThrows(IllegalArgumentException.class,
                () -> Basic.of(100f, 0f, 0f, 0f, -1f, CombatResource.MANA, 0f));
    }
}
