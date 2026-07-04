package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test cho Position — focus vào distanceTo semantics.
 * Tách khỏi NearestEnemy tests để verify geometry ở pure isolation.
 */
class PositionTest {

    /** Cycle 1 — zero distance khi cùng vị trí. */
    @Test
    void distanceToSelfIsZero() {
        Position p = new Position(3, 4);

        assertEquals(0.0, p.distanceTo(p));
    }

    /** Cycle 2 — Pythagorean 3-4-5 triangle. */
    @Test
    void distanceToUsesEuclideanFormula() {
        Position a = new Position(0, 0);
        Position b = new Position(3, 4);

        assertEquals(5.0, a.distanceTo(b));
    }

    /** Cycle 3 — symmetry. */
    @Test
    void distanceToIsSymmetric() {
        Position a = new Position(1, 2);
        Position b = new Position(7, 9);

        assertEquals(a.distanceTo(b), b.distanceTo(a));
    }

    /** Negative coords — verify handles sign correctly. */
    @Test
    void distanceToHandlesNegativeCoordinates() {
        Position a = new Position(-3, -4);
        Position b = new Position(0, 0);

        assertEquals(5.0, a.distanceTo(b));
    }
}