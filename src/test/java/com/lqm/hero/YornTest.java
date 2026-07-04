package com.lqm.hero;

import com.lqm.combat.AttackRange;
import com.lqm.combat.BaseStats;
import com.lqm.combat.Hero;
import com.lqm.combat.Position;
import com.lqm.strategy.LowestHP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pin field structure của Yorn từ screenshot Liên Quân Mobile (xem ADR-0003).
 *
 * Mỗi test tương ứng MỘT dòng trên màn hình chi tiết tướng: nếu Garena thay đổi 1 stat,
 * đúng 1 test fail — dễ truy nguyên khi balance patch khác số liệu.
 *
 * Cảnh báo: số liệu giữa các region/patch khác nhau (xem ADR-0003 R1 cross-check). Khi nguồn
 * chính thức thay đổi, cập nhật Yorn.stats() — không sửa test.
 */
class YornTest {

    @Test
    void yornMaxHpMatchesSheet() {
        assertEquals(3582f, Yorn.stats().maxHp());
    }

    @Test
    void yornMaxManaMatchesSheet() {
        assertEquals(440f, Yorn.stats().maxMana());
    }

    @Test
    void yornAttackDamageMatchesSheet() {
        assertEquals(174f, Yorn.stats().attackDamage());
    }

    @Test
    void yornAbilityPowerMatchesSheet() {
        assertEquals(0f, Yorn.stats().abilityPower());
    }

    @Test
    void yornArmorMatchesSheet() {
        assertEquals(140f, Yorn.stats().armor());
    }

    @Test
    void yornMagicDefenseMatchesSheet() {
        assertEquals(80f, Yorn.stats().magicDefense());
    }

    @Test
    void yornMovementSpeedMatchesSheet() {
        assertEquals(360f, Yorn.stats().movementSpeed());
    }

    @Test
    void yornAttackSpeedBonusMatchesSheet() {
        assertEquals(0f, Yorn.stats().attackSpeedBonus());
    }

    @Test
    void yornArmorPenMatchesSheet() {
        BaseStats s = Yorn.stats();
        assertEquals(0f, s.armorPenFlat());
        assertEquals(0f, s.armorPenPct());
    }

    @Test
    void yornMagicPenMatchesSheet() {
        BaseStats s = Yorn.stats();
        assertEquals(0f, s.magicPenFlat());
        assertEquals(0f, s.magicPenPct());
    }

    @Test
    void yornCritChanceMatchesSheet() {
        assertEquals(0f, Yorn.stats().critChance());
    }

    @Test
    void yornCritDamageMatchesSheet() {
        assertEquals(2.00f, Yorn.stats().critDamage());
    }

    @Test
    void yornLifeStealMatchesSheet() {
        assertEquals(0f, Yorn.stats().lifeSteal());
    }

    @Test
    void yornSpellVampMatchesSheet() {
        assertEquals(0f, Yorn.stats().spellVamp());
    }

    @Test
    void yornCooldownReductionMatchesSheet() {
        assertEquals(0f, Yorn.stats().cooldownReduction());
    }

    @Test
    void yornAttackRangeIsRanged() {
        // Sheet shows "Đánh xa" → RANGED enum.
        assertEquals(AttackRange.RANGED, Yorn.stats().attackRange());
    }

    /** Hero Yorn default: composition gắn đúng stats + selector + name. */
    @Test
    void yornCreateComposesCorrectly() {
        Hero yorn = Yorn.create();

        assertEquals(Yorn.NAME, yorn.getName());
        assertEquals(Yorn.stats(), yorn.getBaseStats());
        assertEquals(3582f, yorn.getCurrentHp());
        assertEquals(new Position(0, 0), yorn.getPosition());
    }

    /** Yorn.create(position, selector) cho phép override vị trí và TargetSelector. */
    @Test
    void yornCreateAcceptsPositionAndSelector() {
        Hero yorn = Yorn.create(new Position(10, 20), new LowestHP());

        assertEquals(new Position(10, 20), yorn.getPosition());
    }
}
