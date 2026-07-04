package com.moba.hero;

import com.moba.combat.CombatResource;
import com.moba.combat.Hero;
import com.moba.combat.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToroTest {

    @Test
    void toroResourceTypeIsFightingSpirit() {
        assertEquals(CombatResource.FIGHTING_SPIRIT, Toro.attribute().basic().resourceType());
    }

    @Test
    void toroCreateBuildsExpectedHero() {
        Hero toro = Toro.create();

        assertEquals(Toro.NAME, toro.getName());
        assertEquals(Toro.attribute(), toro.getAttribute());
        assertEquals(15000f, toro.getCurrentHp());
        assertEquals(new Position(0, 0), toro.getPosition());
    }

    @Test
    void toroCreateAllowsOverridingPosition() {
        Hero toro = Toro.create(new Position(10, 20));

        assertEquals(new Position(10, 20), toro.getPosition());
    }
}
