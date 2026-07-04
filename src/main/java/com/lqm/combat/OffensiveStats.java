package com.lqm.combat;

/**
 * Nhóm "Thuộc tính tấn công" trên màn hình chi tiết tướng Liên Quân Mobile
 * (vn: "Thuộc tính tấn công"; en: "Offensive stats").
 *
 * <p>Value Object bất biến (record), gom 11 chỉ số <b>modifier</b> + di chuyển đi kèm đòn đánh / skill:
 * tốc chạy, tốc đánh, xuyên giáp (vật lý + phép, flat + %), chí mạng, hút máu, giảm hồi chiêu.
 *
 * <p><b>Thứ tự field theo UI — trái → phải, trên → dưới (toàn bộ nằm dưới header "Thuộc tính tấn công"):</b>
 * <pre>
 *   Hàng 1:  "Tốc chạy"             → movementSpeed    (UI: 360.0)
 *            "Xuyên giáp 0"         → armorPenFlat     (UI: 0)
 *            "Xuyên giáp phép 0"    → magicPenFlat     (UI: 0)
 *   Hàng 2:  "Tốc đánh +"           → attackSpeedBonus (UI: 0%)
 *            "Tỷ lệ chí mạng"      → critChance       (UI: 0%)
 *            "Sát thương chí mạng" → critDamage       (UI: 200% → 2.00)
 *   Hàng 3:  "Hút máu"              → lifeSteal        (UI: 0%)
 *            "Hút máu phép"         → spellVamp        (UI: 0%)
 *            "Giảm hồi chiêu"       → cooldownReduction (UI: 0%)
 * </pre>
 *
 * <p>Tách khỏi {@link BasicStats} theo Evans DDD Ch.5: hai nhóm có cap policy khác nhau
 * (critDamage / cooldownReduction phụ thuộc game client — AoV vs HoK) nên
 * OffensiveStats nhận {@link GameClientCaps} làm tham số để validate đúng theo patch.
 *
 * <p>Lưu ý quan trọng: <b>"Tốc chạy"</b> và <b>"Tốc đánh +%"</b> UI đặt dưới header "Thuộc tính tấn công",
 * không phải "Thuộc tính cơ bản" — nên hai field này ở đây, không ở {@link BasicStats}.
 */
public record OffensiveStats(
        float movementSpeed,
        float armorPenFlat,
        float magicPenFlat,
        float attackSpeedBonus,
        float critChance,
        float critDamage,
        float lifeSteal,
        float spellVamp,
        float cooldownReduction,
        float armorPenPct,
        float magicPenPct,
        GameClientCaps caps
) {

    /**
     * Factory tiện — tham số theo thứ tự UI, tự pick caps AoV.
     * Dùng cho code mới; nếu cần HoK caps gọi constructor 12-arg kèm caps.
     */
    public static OffensiveStats of(
            float movementSpeed,
            float armorPenFlat, float magicPenFlat,
            float attackSpeedBonus,
            float critChance, float critDamage,
            float lifeSteal, float spellVamp,
            float cooldownReduction,
            float armorPenPct, float magicPenPct) {
        return new OffensiveStats(movementSpeed, armorPenFlat, magicPenFlat,
                attackSpeedBonus, critChance, critDamage,
                lifeSteal, spellVamp, cooldownReduction,
                armorPenPct, magicPenPct, GameClientCaps.AOV);
    }

    public OffensiveStats {
        if (caps == null) {
            throw new IllegalArgumentException("caps must not be null (use GameClientCaps.AOV / .HOK)");
        }
        if (movementSpeed <= 0f) {
            throw new IllegalArgumentException("movementSpeed must be > 0, got " + movementSpeed);
        }
        if (attackSpeedBonus < 0f) {
            throw new IllegalArgumentException("attackSpeedBonus must be >= 0, got " + attackSpeedBonus);
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
