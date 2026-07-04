package com.lqm.combat;

import com.lqm.strategy.NearestEnemy;
import com.lqm.strategy.TargetSelector;
import com.lqm.test.TestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cho Hero — focus vào delegation sang CombatStats và TargetSelector.
 * HP lifecycle tests đã chuyển sang CombatStatsTest.
 */
class HeroTest {

    /** Hero + empty enemy list → selectTarget trả null (không qua strategy). */
    @Test
    void selectTargetReturnsNullWhenEnemiesListIsEmpty() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        Enemy result = hero.selectTarget(List.of());

        assertNull(result);
    }

    /** Dead Hero (CombatStats.isAlive=false) → selectTarget trả null dù có enemy trong list. */
    @Test
    void deadHeroReturnsNullFromSelectTargetEvenWithEnemies() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());
        Enemy a = TestEnemy.at(new Position(5, 0), 100, "A");

        hero.takeDamage(100f);

        assertNull(hero.selectTarget(List.of(a)));
    }

    /** Hero.getCurrentHp() delegate sang CombatStats.getCurrentHp(). */
    @Test
    void getCurrentHpDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(25f);

        assertEquals(75f, hero.getCurrentHp());
    }

    /** Hero takeDamage → CombatStats takeDamage → currentHp giảm. */
    @Test
    void takeDamageDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, new NearestEnemy());

        hero.takeDamage(40f);

        assertEquals(60f, hero.getCurrentHp());
    }

    /** Hero alive + strategy trả về enemy → selectTarget trả enemy đó. */
    @Test
    void aliveHeroDelegatesTargetSelectionToStrategy() {
        // Stub strategy trả về enemy cố định, verify Hero chỉ delegate
        TargetSelector alwaysReturnsFirst = (attacker, enemies) -> enemies.isEmpty() ? null : enemies.get(0);
        Hero hero = new Hero("Yena", new Position(0, 0), 100.0f, alwaysReturnsFirst);
        Enemy a = TestEnemy.at(new Position(5, 0), 100, "A");

        Enemy result = hero.selectTarget(List.of(a));

        assertEquals(a, result);
    }
}