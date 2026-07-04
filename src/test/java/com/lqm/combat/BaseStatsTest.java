package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test cho BaseStats — tập trung vào: equality/hashCode (của record), validation ở compact constructor,
 * và cap-policy injection qua {@link GameClientCaps}.
 *
 * Lưu ý về "Yorn screenshot numbers" vs "AoV client numbers":
 * Số liệu Yorn ở screenshot VN (HP 3582, armor 140) khác với AoV client (HP 3401, armor 88) do
 * region/patch. Test này chỉ lock FIELD STRUCTURE — số literal của một Hero cụ thể được pin trong
 * YornTest, không phải ở đây.
 */
class BaseStatsTest {

    /**
     * BaseStats với cap policy AoV (default), tất cả stat = 0 ngoại trừ maxHp + movementSpeed + critDamage.
     * Tham số theo thứ tự UI mới: BasicStats(6) → OffensiveStats(11) → AttackRange → Caps.
     */
    private static BaseStats aovZeros(float maxHp) {
        return new BaseStats(
                /* basic:        */ maxHp, 0f, 0f, 0f, 0f, 0f,
                /* offensive:    */ 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                /* attackRange + caps: */ AttackRange.MELEE, GameClientCaps.AOV);
    }

    /** BaseStats với cap policy HoK. */
    private static BaseStats hokZeros(float maxHp) {
        return new BaseStats(
                /* basic:        */ maxHp, 0f, 0f, 0f, 0f, 0f,
                /* offensive:    */ 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                /* attackRange + caps: */ AttackRange.MELEE, GameClientCaps.HOK);
    }

    /** Record equality: hai BaseStats cùng field → equals. */
    @Test
    void recordEqualityByAllFields() {
        BaseStats a = aovZeros(100f);
        BaseStats b = aovZeros(100f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    /** Validation: maxHp <= 0 bị reject. */
    @Test
    void rejectsNonPositiveMaxHp() {
        assertThrows(IllegalArgumentException.class, () -> aovZeros(0f));
        assertThrows(IllegalArgumentException.class, () -> aovZeros(-1f));
    }

    /** Validation: movementSpeed phải > 0. */
    @Test
    void rejectsNonPositiveMovementSpeed() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.AOV));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, -1f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.AOV));
    }

    /** Validation: critChance ngoài [0,1] bị reject (global — không phụ thuộc caps). */
    @Test
    void rejectsCritChanceOutsideUnitInterval() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, -0.01f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.AOV));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 1.01f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.AOV));
    }

    /** Validation: caps reject critDamage ngoài policy. HoK cap = 2.50; AoV không cap (Infinity). */
    @Test
    void rejectsCritDamageBelowMinimum() {
        // dưới 1.0 — fail ở cả AoV và HoK
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 0.99f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.HOK));
    }

    @Test
    void hokCapsRejectsCritDamageAboveCap() {
        // AoV: cap = +Infinity → 2.51 OK. HoK: cap = 2.50 → 2.51 reject.
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 2.51f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.HOK));
    }

    @Test
    void aovCapsAllowsHighCritDamage() {
        // AoV không cap critDamage — 5.00 phải pass validation (chỉ là sanity check).
        BaseStats s = new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 0f, 5.0f, 0f, 0f, 0f,
                AttackRange.MELEE, GameClientCaps.AOV);

        assertEquals(5.0f, s.critDamage());
    }

    /** Validation: cooldownReduction > 0.40 reject ở cả AoV + HoK (cap policy giống nhau). */
    @Test
    void rejectsCooldownReductionAboveCap() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0.41f,
                AttackRange.MELEE, GameClientCaps.AOV));
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0.41f,
                AttackRange.MELEE, GameClientCaps.HOK));
    }

    /** Validation: attackRange null bị reject (không dùng String, không default enum). */
    @Test
    void rejectsNullAttackRange() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                null, GameClientCaps.AOV));
    }

    /** Validation: caps null bị reject. */
    @Test
    void rejectsNullCaps() {
        assertThrows(IllegalArgumentException.class, () -> new BaseStats(
                100f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                AttackRange.MELEE, null));
    }
}
