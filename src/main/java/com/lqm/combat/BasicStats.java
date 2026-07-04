package com.lqm.combat;

/**
 * Nhóm "Thuộc tính cơ bản" trên màn hình chi tiết tướng Liên Quân Mobile
 * (vn: "Thuộc tính cơ bản"; en: "Basic stats").
 *
 * <p>Value Object bất biến (record), gom 6 chỉ số <b>gốc</b> quyết định sinh tồn + sát thương cơ sở
 * của hero: máu, công vật lý, công phép, giáp, giáp phép, năng lượng.
 *
 * <p><b>Thứ tự field theo UI — trái → phải, trên → dưới:</b>
 * <pre>
 *   Hàng 1:  "Máu"                  → maxHp            (UI: 3582)
 *            "Công vật lý"          → attackDamage     (UI: 174)
 *            "Công phép"            → abilityPower     (UI: 0)
 *   Hàng 2:  "Giáp"                 → armor            (UI: 140 | 0%)
 *            "Giáp phép"            → magicDefense     (UI: 80 | 0%)
 *            "Năng lượng tối đa"    → maxMana          (UI: 440)
 * </pre>
 *
 * <p>Lưu ý: <b>"Tốc chạy"</b> và <b>"Tốc đánh +%"</b> nằm dưới header "Thuộc tính tấn công"
 * trong UI → thuộc {@link OffensiveStats}, không có ở đây.
 *
 * <p>Tách khỏi {@link OffensiveStats} (nhóm tấn công) theo Evans DDD Ch.5 (Value Object cohesion):
 * hai nhóm có invariant khác nhau nên đặt ở hai nơi giúp:
 * <ul>
 *   <li>Validation đặt cạnh field liên quan — đọc dễ hơn 6+9 điều kiện trộn lẫn.</li>
 *   <li>Consumer cần chỉ "vitals" thì dùng {@code BaseStats#basic()} thay vì phải truyền nguyên
 *       {@code BaseStats} (vd {@code DamageCalculator} tương lai).</li>
 * </ul>
 *
 * <p>Bonus% từ UI (vd "Giáp 140 | 0%") KHÔNG thuộc BasicStats — phần {@code 0%} đến từ
 * item/arcana và sẽ cộng dồn ở lớp Decorator phía trên.
 */
public record BasicStats(
        float maxHp,
        float attackDamage,
        float abilityPower,
        float armor,
        float magicDefense,
        float maxMana
) {

    /** Factory tiện — tham số theo thứ tự UI. Dùng cho code mới. */
    public static BasicStats of(
            float maxHp,
            float attackDamage,
            float abilityPower,
            float armor,
            float magicDefense,
            float maxMana) {
        return new BasicStats(maxHp, attackDamage, abilityPower, armor, magicDefense, maxMana);
    }

    public BasicStats {
        if (maxHp <= 0f) {
            throw new IllegalArgumentException("maxHp must be > 0, got " + maxHp);
        }
        if (maxMana < 0f) {
            throw new IllegalArgumentException("maxMana must be >= 0, got " + maxMana);
        }
        if (armor < 0f || magicDefense < 0f) {
            throw new IllegalArgumentException("armor/magicDefense must be >= 0, got armor=" + armor + " magicDefense=" + magicDefense);
        }
    }
}
