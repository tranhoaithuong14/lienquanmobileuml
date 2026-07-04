package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTest {

    @Test
    void distanceToSelfIsZero() {
        Position p = new Position(3, 4);

        assertEquals(0.0, p.distanceTo(p));
    }

    @Test
    void distanceToFollowsEuclideanFormula() {
        Position a = new Position(0, 0);
        Position b = new Position(3, 4);

        assertEquals(5.0, a.distanceTo(b));
    }

    @Test
    void distanceToIsSymmetric() {
        Position a = new Position(1, 2);
        Position b = new Position(7, 9);

        assertEquals(a.distanceTo(b), b.distanceTo(a));
    }

    @Test
    void distanceToHandlesNegativeCoordinates() {
        Position a = new Position(-3, -4);
        Position b = new Position(0, 0);

        assertEquals(5.0, a.distanceTo(b));
    }
}
