package com.moba.hero;

import com.moba.combat.CombatResource;
import com.moba.combat.Hero;
import com.moba.combat.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YornTest {

    @Test
    void yornResourceTypeIsMana() {
        assertEquals(CombatResource.MANA, Yorn.attribute().basic().resourceType());
    }

    @Test
    void yornCreateBuildsExpectedHero() {
        Hero yorn = Yorn.create();

        assertEquals(Yorn.NAME, yorn.getName());
        assertEquals(Yorn.attribute(), yorn.getAttribute());
        assertEquals(3582f, yorn.getCurrentHp());
        assertEquals(new Position(0, 0), yorn.getPosition());
    }

    @Test
    void yornCreateAllowsOverridingPosition() {
        Hero yorn = Yorn.create(new Position(10, 20));

        assertEquals(new Position(10, 20), yorn.getPosition());
    }
}
