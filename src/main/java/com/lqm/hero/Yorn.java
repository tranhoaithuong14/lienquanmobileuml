package com.lqm.hero;

import com.lqm.combat.AttackRange;
import com.lqm.combat.BaseStats;
import com.lqm.combat.Position;
import com.lqm.combat.Hero;
import com.lqm.strategy.LowestHP;
import com.lqm.strategy.TargetSelector;

/**
 * Fixture factory cho hero Yorn — dữ liệu lấy từ màn hình chi tiết tướng Liên Quân Mobile (VN client).
 *
 * Đây là data, không phải logic. Tách ra để:
 * - Số liệu literal từ game (HP 3582, AD 174, ...) cố định một chỗ, dễ patch khi balance update.
 * - Catalog (Repository, ADR-0003 R6) import factory này — test/benchmark so sánh với ground-truth game.
 * - Không tạo subclass per-hero (ADR-0001 đã cấm); Hero Yorn = Hero(name="Yorn", stats=Yorn.stats(), ...).
 *
 * Caveat: số liệu giữa Liên Quân VN và Arena of Valor global client khác nhau do patch/version
 * (xem ADR-0003 R1 cross-check). Đây là bản VN theo screenshot người dùng cung cấp.
 */
public final class Yorn {

    public static final String NAME = "Yorn";

    /** Cache instance duy nhất — record immutable (Bloch Item 17), shareless/thread-safe. */
    private static final BaseStats STATS = new BaseStats(
            /* maxHp        */ 3582f,
            /* maxMana      */ 440f,
            /* attackDamage */ 174f,
            /* abilityPower */ 0f,
            /* armor        */ 140f,
            /* magicDefense */ 80f,
            /* movementSpeed*/ 360f,
            /* attackSpeedBonus*/ 0f,
            /* armorPenFlat */ 0f,
            /* armorPenPct  */ 0f,
            /* magicPenFlat */ 0f,
            /* magicPenPct  */ 0f,
            /* critChance   */ 0f,
            /* critDamage   */ 2.00f,   // 200% — AoV base (HoK cap 250%)
            /* lifeSteal    */ 0f,
            /* spellVamp    */ 0f,
            /* cooldownReduction */ 0f,
            /* attackRange  */ AttackRange.RANGED  // "Đánh xa"
    );

    private Yorn() {}

    /** BaseStats của Yorn từ screenshot Liên Quân VN. Field structure đầy đủ 18 stat, giá trị 0 chỗ
     *  nào screenshot cho 0. Bonus% giữ ở dạng 0.0 (cộng dồn từ items/arcana sẽ là Decorator/aggregation
     *  sau). Trả về cùng một record instance mỗi lần — để HeroCatalog lookup trùng identity, đồng thời
     *  tránh cấp phát record mới trên mỗi call. */
    public static BaseStats stats() {
        return STATS;
    }

    /** Hero Yorn mặc định: vị trí (0,0), TargetSelector LowestHP (passive ám sát). */
    public static Hero create() {
        return create(new Position(0, 0), new LowestHP());
    }

    public static Hero create(Position position, TargetSelector selector) {
        return new Hero(NAME, position, stats(), selector);
    }
}
