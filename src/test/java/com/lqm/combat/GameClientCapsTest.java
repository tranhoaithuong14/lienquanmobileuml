package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test cho {@link GameClientCaps} — pin AoV vs HoK cap policy từ ADR-0003 R7.
 * Caps là DATA (không phải constants), vì vậy mỗi client là một instance riêng.
 */
class GameClientCapsTest {

    @Test
    void aovCritDamageMaxIsInfinity() {
        // AoV (Liên Quân Mobile VN) — không có cap critDamage rõ ràng trong in-game tooltip.
        assertEquals(Float.POSITIVE_INFINITY, GameClientCaps.AOV.critDamageMax());
    }

    @Test
    void hokCritDamageMaxIs250() {
        // Honor of Kings — cap 250% theo Honor of Kings Wiki Equipment page.
        assertEquals(2.50f, GameClientCaps.HOK.critDamageMax());
    }

    @Test
    void cooldownReductionCapIdenticalAcrossClients() {
        // AoV và HoK đều cap cooldownReduction ở 40%.
        assertEquals(0.40f, GameClientCaps.AOV.cooldownReductionMax());
        assertEquals(0.40f, GameClientCaps.HOK.cooldownReductionMax());
    }

    @Test
    void rejectsCritDamageMinBelowOne() {
        // critDamage < 1.0 không có ý nghĩa vật lý (một crit phải ≥ 1.0x damage).
        assertThrows(IllegalArgumentException.class,
                () -> new GameClientCaps(0.99f, 2.5f, 0f, 0.4f));
    }

    @Test
    void rejectsCritDamageMaxBelowMin() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameClientCaps(2.0f, 1.5f, 0f, 0.4f));
    }

    @Test
    void rejectsNegativeCooldownReductionMin() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameClientCaps(1f, 2.5f, -0.01f, 0.4f));
    }

    @Test
    void rejectsInvertedCooldownReductionRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new GameClientCaps(1f, 2.5f, 0.5f, 0.4f));
    }
}
