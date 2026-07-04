package com.lqm.combat;

import com.lqm.strategy.NearestEnemy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test cho Hero (Context trong Strategy pattern).
 * Verify Hero delegate đúng sang TargetSelector chứ không tự quyết định.
 */
class HeroTest {

    /**
     * Slice 1 — RED (Hero chưa tồn tại).
     *
     * Quy tắc: Hero với NearestEnemy, khi enemies rỗng, selectTarget trả null.
     * Đây là verify Hero delegate đúng — không crash, không return dummy.
     */
    @Test
    void selectTargetReturnsNullWhenEnemiesListIsEmpty() {
        Hero hero = new Hero("Yena", new Position(0, 0), 100, new NearestEnemy());

        Enemy result = hero.selectTarget(List.of());

        assertNull(result);
    }
}