package com.lqm.combat;

import com.lqm.strategy.NearestEnemy;
import com.lqm.strategy.TargetSelector;
import com.lqm.test.TestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test cho Hero — focus vào delegation sang CombatStats và TargetSelector.
 * HP lifecycle tests đã chuyển sang CombatStatsTest.
 * BaseStats composition tests (cấu tạo stats từ record, equality) đã chuyển sang BaseStatsTest.
 */
class HeroTest {

    /** Helper: BaseStats stub tối thiểu — chỉ quan tâm maxHp trong các test dưới.
     *  movementSpeed mặc định 1f (giá trị hợp lệ nhỏ nhất, không liên quan tới test dưới). */
    private static BaseStats stubStats(float maxHp) {
        return new BaseStats(
                maxHp, 0f, 0f, 0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE);
    }

    /** Hero + empty enemy list → selectTarget trả null (không qua strategy). */
    @Test
    void selectTargetReturnsNullWhenEnemiesListIsEmpty() {
        Hero hero = new Hero("Yena", new Position(0, 0), stubStats(100f), new NearestEnemy());

        Enemy result = hero.selectTarget(List.of());

        assertNull(result);
    }

    /** Dead Hero (CombatStats.isAlive=false) → selectTarget trả null dù có enemy trong list. */
    @Test
    void deadHeroReturnsNullFromSelectTargetEvenWithEnemies() {
        Hero hero = new Hero("Yena", new Position(0, 0), stubStats(100f), new NearestEnemy());
        Enemy a = TestEnemy.at(new Position(5, 0), 100, "A");

        hero.takeDamage(100f);

        assertNull(hero.selectTarget(List.of(a)));
    }

    /** Hero.getCurrentHp() delegate sang CombatStats.getCurrentHp(). */
    @Test
    void getCurrentHpDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), stubStats(100f), new NearestEnemy());

        hero.takeDamage(25f);

        assertEquals(75f, hero.getCurrentHp());
    }

    /** Hero takeDamage → CombatStats takeDamage → currentHp giảm. */
    @Test
    void takeDamageDelegatesToCombatStats() {
        Hero hero = new Hero("Yena", new Position(0, 0), stubStats(100f), new NearestEnemy());

        hero.takeDamage(40f);

        assertEquals(60f, hero.getCurrentHp());
    }

    /** Hero alive + strategy trả về enemy → selectTarget trả enemy đó. */
    @Test
    void aliveHeroDelegatesTargetSelectionToStrategy() {
        // Stub strategy trả về enemy cố định, verify Hero chỉ delegate
        TargetSelector alwaysReturnsFirst = (attacker, enemies) -> enemies.isEmpty() ? null : enemies.get(0);
        Hero hero = new Hero("Yena", new Position(0, 0), stubStats(100f), alwaysReturnsFirst);
        Enemy a = TestEnemy.at(new Position(5, 0), 100, "A");

        Enemy result = hero.selectTarget(List.of(a));

        assertEquals(a, result);
    }

    /** Hero vật (composition): reference đến BaseStats được giữ nguyên — không copy/cow. */
    @Test
    void getBaseStatsReturnsSameReference() {
        BaseStats stats = stubStats(100f);
        Hero hero = new Hero("Yena", new Position(0, 0), stats, new NearestEnemy());

        assertSame(stats, hero.getBaseStats());
    }

    /** Hero vitals khởi tạo từ BaseStats.maxHp(). */
    @Test
    void currentHpInitialisedFromBaseStatsMaxHp() {
        Hero hero = new Hero("Yena", new Position(0, 0), stubStats(250f), new NearestEnemy());

        assertEquals(250f, hero.getCurrentHp());
    }

    /** Constructor chặn BaseStats null — fail-fast. */
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