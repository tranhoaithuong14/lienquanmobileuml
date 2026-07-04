package com.lqm.combat;

/**
 * Nhóm "Thuộc tính cơ bản" trên màn hình chi tiết tướng Liên Quân Mobile
 * (vn: "Thuộc tính cơ bản"; en: "Basic stats").
 *
 * <p>Value Object bất biến (record), gom 8 chỉ số <b>gốc</b> quyết định sinh tồn + sát thương cơ sở
 * của hero: máu, mana, công vật lý / phép, giáp, tốc chạy, tốc đánh.
 *
 * <p>Tách khỏi {@link OffensiveStats} (nhóm tấn công) theo Evans DDD Ch.5 (Value Object cohesion):
 * hai nhóm có invariant khác nhau nên đặt ở hai nơi giúp:
 * <ul>
 *   <li>Validation đặt cạnh field liên quan — đọc dễ hơn 18 điều kiện trộn lẫn.</li>
 *   <li>Consumer cần chỉ "vitals" thì dùng {@code BaseStats#basic()} thay vì phải truyền nguyên
 *       {@code BaseStats} (vd {@code DamageCalculator} tương lai).</li>
 * </ul>
 *
 * <p>Bonus% từ UI (vd "Giáp 140 | 0%") KHÔNG thuộc BasicStats — phần {@code 0%} đến từ
 * item/arcana và sẽ cộng dồn ở lớp Decorator phía trên.
 *
 * <p>Map UI → field:
 * <pre>
 *   "Máu"                  → maxHp            (UI: 3582 với Yorn)
 *   "Năng lượng tối đa"     → maxMana          (UI: 440)
 *   "Công vật lý"          → attackDamage     (UI: 174)
 *   "Công phép"            → abilityPower     (UI: 0 — Yorn không dùng phép)
 *   "Giáp"                 → armor            (UI: 140, chỉ phần base)
 *   "Giáp phép"            → magicDefense     (UI: 80)
 *   "Tốc chạy"             → movementSpeed    (UI: 360.0)
 *   "Tốc đánh +"           → attackSpeedBonus (UI: 0% — base)
 * </pre>
 */
public record BasicStats(
        float maxHp,
        float maxMana,
        float attackDamage,
        float abilityPower,
        float armor,
        float magicDefense,
        float movementSpeed,
        float attackSpeedBonus
) {

    /** Factory tiện — đọc ngang màn hình UI thay vì positional. Dùng cho code mới. */
    public static BasicStats of(
            float maxHp, float maxMana,
            float attackDamage, float abilityPower,
            float armor, float magicDefense,
            float movementSpeed, float attackSpeedBonus) {
        return new BasicStats(maxHp, maxMana, attackDamage, abilityPower,
                armor, magicDefense, movementSpeed, attackSpeedBonus);
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
        if (movementSpeed <= 0f) {
            throw new IllegalArgumentException("movementSpeed must be > 0, got " + movementSpeed);
        }
        if (attackSpeedBonus < 0f) {
            throw new IllegalArgumentException("attackSpeedBonus must be >= 0, got " + attackSpeedBonus);
        }
    }
}
