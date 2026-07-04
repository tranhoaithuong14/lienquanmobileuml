package com.moba.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class HeroTest {

    private static Attribute minimalAttribute(float hp) {
        return Attribute.builder()
                .basic(Basic.of(hp, 0f, 0f, 0f, 0f, CombatResource.MANA, 0f))
                .offensive(Offensive.of(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f))
                .attackRange(AttackRange.MELEE)
                .role(HeroRole.WARRIOR)
                .build();
    }

    @Test
    void getCurrentHpDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalAttribute(100f));

        hero.takeDamage(25f);

        assertEquals(75f, hero.getCurrentHp());
    }

    @Test
    void takeDamageDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalAttribute(100f));

        hero.takeDamage(40f);

        assertEquals(60f, hero.getCurrentHp());
    }

    @Test
    void heroIsATargetableHeroUnit() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalAttribute(100f));

        assertEquals(100f, hero.getMaxHp());
        assertEquals(TargetKind.HERO, hero.getTargetKind());
    }

    @Test
    void getAttributePreservesReferenceIdentity() {
        Attribute attribute = minimalAttribute(100f);
        Hero hero = new Hero("Yena", new Position(0, 0), attribute);

        assertSame(attribute, hero.getAttribute());
    }

    @Test
    void currentHpInitialisedFromAttributeHp() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalAttribute(250f));

        assertEquals(250f, hero.getCurrentHp());
    }

    @Test
    void constructorRejectsNullAttribute() {
        try {
            new Hero("Yena", new Position(0, 0), null);
            org.junit.jupiter.api.Assertions.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }
}
