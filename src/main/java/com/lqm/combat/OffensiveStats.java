package com.lqm.combat;

/**
 * Nhóm "Thuộc tính tấn công" trên màn hình chi tiết tướng Liên Quân Mobile
 * (vn: "Thuộc tính tấn công"; en: "Offensive stats").
 *
 * <p>Value Object bất biến (record), gom 9 chỉ số <b>modifier</b> đi kèm đòn đánh / skill:
 * xuyên giáp (vật lý + phép, flat + %), chí mạng, hút máu, giảm hồi chiêu.
 *
 * <p>Tách khỏi {@link BasicStats} theo Evans DDD Ch.5: hai nhóm có cap policy khác nhau
 * (critDamage / cooldownReduction phụ thuộc game client — AoV vs HoK) nên
 * OffensiveStats nhận {@link GameClientCaps} làm tham số để validate đúng theo patch.
 *
 * <p>Map UI → field:
 * <pre>
 *   "Xuyên giáp 0"          → armorPenFlat     (UI: 0)
 *   "Xuyên giáp 0%"         → armorPenPct      (UI: 0% — phần % xuyên giáp địch)
 *   "Xuyên giáp phép 0"     → magicPenFlat     (UI: 0)
 *   "Xuyên giáp phép 0%"    → magicPenPct      (UI: 0% — cùng khái niệm, áp cho phép)
 *   "Tỷ lệ chí mạng"        → critChance       (UI: 0% — Yorn chưa có crit base)
 *   "Sát thương chí mạng"   → critDamage       (UI: 200% → 2.00; AoV base, HoK cap 250%)
 *   "Hút máu"              → lifeSteal        (UI: 0% — chỉ đánh thường/physical)
 *   "Hút máu phép"         → spellVamp        (UI: 0% — chỉ skill phép)
 *   "Giảm hồi chiêu"       → cooldownReduction (UI: 0% — base; trang bị/arcana cộng dồn)
 * </pre>
 */
public record OffensiveStats(
        float armorPenFlat,
        float armorPenPct,
        float magicPenFlat,
        float magicPenPct,
        float critChance,
        float critDamage,
        float lifeSteal,
        float spellVamp,
        float cooldownReduction,
        GameClientCaps caps
) {

    /** Factory tiện — tự pick caps theo default. Dùng cho code mới. */
    public static OffensiveStats of(
            float armorPenFlat, float armorPenPct,
            float magicPenFlat, float magicPenPct,
            float critChance, float critDamage,
            float lifeSteal, float spellVamp,
            float cooldownReduction) {
        return new OffensiveStats(armorPenFlat, armorPenPct, magicPenFlat, magicPenPct,
                critChance, critDamage, lifeSteal, spellVamp,
                cooldownReduction, GameClientCaps.AOV);
    }

    public OffensiveStats {
        if (caps == null) {
            throw new IllegalArgumentException("caps must not be null (use GameClientCaps.AOV / .HOK)");
        }
        if (armorPenFlat < 0f || armorPenPct < 0f
                || magicPenFlat < 0f || magicPenPct < 0f) {
            throw new IllegalArgumentException("pen stats must be >= 0");
        }
        if (lifeSteal < 0f || spellVamp < 0f) {
            throw new IllegalArgumentException("lifeSteal/spellVamp must be >= 0");
        }
        if (critChance < 0f || critChance > 1f) {
            throw new IllegalArgumentException("critChance must be in [0, 1], got " + critChance);
        }
        if (critDamage < caps.critDamageMin() || critDamage > caps.critDamageMax()) {
            throw new IllegalArgumentException(
                    "critDamage must be in [" + caps.critDamageMin() + ", " + caps.critDamageMax() + "] per caps policy, got " + critDamage);
        }
        if (cooldownReduction < caps.cooldownReductionMin() || cooldownReduction > caps.cooldownReductionMax()) {
            throw new IllegalArgumentException(
                    "cooldownReduction must be in [" + caps.cooldownReductionMin() + ", " + caps.cooldownReductionMax() + "] per caps policy, got " + cooldownReduction);
        }
    }
}
