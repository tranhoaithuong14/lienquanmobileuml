package com.lqm.combat;

/**
 * Cap policy cho mỗi game client (AoV, HoK, ...) — Value Object chứa giới hạn trên/giới hạn dưới của
 * các stat có cap trong client đó.
 *
 * ADR-0003 R7 tìm thấy rằng caps khác nhau giữa các client:
 * - AoV (Liên Quân Mobile VN): critDamage base 200%, không có cap rõ ràng; cooldownReduction cap 40%.
 * - HoK (Honor of Kings): critDamage cap 250%, cooldownReduction cap 40%, attack speed cap 200%, ...
 *
 * Vì lý do đó caps được lift ra khỏi {@link BaseStats} (data theo balance patch) thay vì là constant
 * private trong record. Mỗi client variant là một instance {@code GameClientCaps} bất biến; record
 * BaseStats nhận caps qua constructor và validate theo policy đó.
 *
 * Implementation gọn: dùng Java record + static factories cho AoV/HoK. Extend bằng cách thêm
 * factory method mới (Tournament, PBE, ...) — không edit {@code BaseStats}.
 */
public record GameClientCaps(
        float critDamageMin,
        float critDamageMax,
        float cooldownReductionMin,
        float cooldownReductionMax
) {

    /** AoV / Liên Quân Mobile VN — caps verified từ ADR-0003 R7 (in-game tooltips). */
    public static final GameClientCaps AOV = new GameClientCaps(
            /* critDamageMin          */ 1.00f,
            /* critDamageMax          */ Float.POSITIVE_INFINITY,  // AoV không cap rõ ràng
            /* cooldownReductionMin   */ 0.00f,
            /* cooldownReductionMax   */ 0.40f                       // 40% (AoV + HoK đều có cap này)
    );

    /** Honor of Kings — caps verified từ Honor of Kings Wiki Equipment page (ADR-0003 R7). */
    public static final GameClientCaps HOK = new GameClientCaps(
            /* critDamageMin          */ 1.00f,
            /* critDamageMax          */ 2.50f,                      // 250%
            /* cooldownReductionMin   */ 0.00f,
            /* cooldownReductionMax   */ 0.40f
    );

    public GameClientCaps {
        if (critDamageMin < 1.0f) {
            throw new IllegalArgumentException("critDamageMin must be >= 1.0 (a crit multiplies by at least 1.0x)");
        }
        if (critDamageMax < critDamageMin) {
            throw new IllegalArgumentException("critDamageMax must be >= critDamageMin");
        }
        if (cooldownReductionMin < 0f || cooldownReductionMax < cooldownReductionMin) {
            throw new IllegalArgumentException("cooldownReduction range must be valid [min, max] with min >= 0");
        }
    }
}
