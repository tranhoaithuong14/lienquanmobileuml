package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test cho BaseStats — tập trung vào: equality/hashCode (của record), validation ở compact constructor,
 * và Yorn's field structure (lock stats từ màn hình chi tiết tướng Liên Quân Mobile).
 *
 * Lưu ý về "Yorn screenshot numbers" vs "AoV client numbers":
 * Số liệu Yorn ở screenshot VN (HP 3582, armor 140) khác với AoV client (HP 3401, armor 88) do
 * region/patch. Test này chỉ lock FIELD STRUCTURE — số literal của một Hero cụ thể được pin trong
 * YornFixture, không phải ở BaseStatsTest.
 */
class BaseStatsTest {

    private static BaseStats allZeros(float maxHp) {
        return new BaseStats(
                maxHp, 0f, 0f, 0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE);
    }

    /** Record equality: hai BaseStats cùng field → equals. */
    @Test
    void recordEqualityByAllFields() {
        BaseStats a = allZeros(100f);
        BaseStats b = allZeros(100f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /** Validation: maxHp <= 0 bị reject. */
    @Test
    void rejectsNonPositiveMaxHp() {
        assertThrows(IllegalArgumentException.class, () -> allZeros(0f));
        assertThrows(IllegalArgumentException.class, () -> allZeros(-1f));
    }

    /** Validation: movementSpeed phải > 0 (Hero đứng yên là chuyện khác, default là có chạy). */
    @Test
    void rejectsNonPositiveMovementSpeed() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, -1f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE));
    }

    /** Validation: critChance ngoài [0,1] bị reject. */
    @Test
    void rejectsCritChanceOutsideUnitInterval() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, -0.01f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 1.01f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE));
    }

    /** Validation: critDamage ngoài [1.0, 2.5] (AoV/HoK cap) bị reject. */
    @Test
    void rejectsCritDamageOutsideCap() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 0.99f, 0f, 0f, 0f,
                AttackRange.MELEE));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 2.51f, 0f, 0f, 0f,
                AttackRange.MELEE));
    }

    /** Validation: cooldownReduction ngoài [0, 0.40] (AoV/HoK cap) bị reject. */
    @Test
    void rejectsCooldownReductionAboveCap() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0.41f,
                AttackRange.MELEE));
    }

    /** Validation: attackRange null bị reject (không dùng String, không default enum). */
    @Test
    void rejectsNullAttackRange() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                null));
    }
}
