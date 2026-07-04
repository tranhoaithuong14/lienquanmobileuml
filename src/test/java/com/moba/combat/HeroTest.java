package com.moba.combat;

import com.moba.strategy.NearestEnemy;
import com.moba.strategy.TargetSelector;
import com.moba.test.EnemyStub;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class HeroTest {

    private static BaseStats minimalStats(float maxHp) {
        return new BaseStats(
                maxHp, 0f, 0f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                AttackRange.MELEE);
    }

    @Test
    void selectTargetReturnsNullWhenEnemiesListIsEmpty() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalStats(100f), new NearestEnemy());

        Enemy result = hero.selectTarget(List.of());

        assertNull(result);
    }

    @Test
    void deadHeroReturnsNullFromSelectTargetEvenWithEnemies() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalStats(100f), new NearestEnemy());
        Enemy a = EnemyStub.at(new Position(5, 0), 100, "A");

        hero.takeDamage(100f);

        assertNull(hero.selectTarget(List.of(a)));
    }

    @Test
    void getCurrentHpDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalStats(100f), new NearestEnemy());

        hero.takeDamage(25f);

        assertEquals(75f, hero.getCurrentHp());
    }

    @Test
    void takeDamageDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalStats(100f), new NearestEnemy());

        hero.takeDamage(40f);

        assertEquals(60f, hero.getCurrentHp());
    }

    @Test
    void aliveHeroDelegatesTargetSelectionToStrategy() {
        TargetSelector alwaysPickFirst = (attacker, list) -> list.isEmpty() ? null : list.get(0);
        Hero hero = new Hero("Yena", new Position(0, 0), minimalStats(100f), alwaysPickFirst);
        Enemy a = EnemyStub.at(new Position(5, 0), 100, "A");

        Enemy result = hero.selectTarget(List.of(a));

        assertEquals(a, result);
    }

    @Test
    void getBaseStatsPreservesReferenceIdentity() {
        BaseStats stats = minimalStats(100f);
        Hero hero = new Hero("Yena", new Position(0, 0), stats, new NearestEnemy());

        assertSame(stats, hero.getBaseStats());
    }

    @Test
    void currentHpInitialisedFromBaseStatsMaxHp() {
        Hero hero = new Hero("Yena", new Position(0, 0), minimalStats(250f), new NearestEnemy());

        assertEquals(250f, hero.getCurrentHp());
    }

    @Test
    void constructorRejectsNullBaseStats() {
        try {
            new Hero("Yena", new Position(0, 0), null, new NearestEnemy());
            org.junit.jupiter.api.Assertions.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }
}
