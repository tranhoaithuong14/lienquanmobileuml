package com.lqm.combat;

/**
 * Base stats bất biến của Hero — Value Object cho mọi thông số tĩnh hiển thị trên màn hình chi tiết tướng
 * (Liên Quân Mobile: "Máu", "Giáp", "Công vật lý"...).
 *
 * Tách ra từ Hero theo ADR-0003:
 * - Value Object (Evans, DDD Ch.5): không có identity, defined thuần bởi attributes, immutable.
 * - Minimize mutability (Bloch, Effective Java 3rd ed., Item 17): thread-safe, freely shareable.
 * - Tách rời với mutable vitals state machine (currentHp/currentMana/active) — cái đó vẫn nằm trong
 *   CombatStats theo ADR-0002.
 * - Derived stats (effective armor sau pen, effective HP, attacks/sec) KHÔNG lưu — tính on-demand
 *   ở lớp consumer (DamageCalculator — chưa tồn tại, deferred).
 *
 * Validation ở compact constructor: phạm vi cho phép khớp với AoV client (cap values verified trong
 * ADR-0003 R7); critDamage có base 200% (AoV) / cap 250% (HoK), cooldownReduction cap 40%. Các giá trị
 * bonus%/pen% được lưu dưới dạng phân số (0.0–1.0) thay vì 0–100 để tránh primitive obsession.
 */
public record BaseStats(
        float maxHp,
        float maxMana,
        float attackDamage,
        float abilityPower,
        float armor,
        float magicDefense,
        float movementSpeed,
        float attackSpeedBonus,
        float armorPenFlat,
        float armorPenPct,
        float magicPenFlat,
        float magicPenPct,
        float critChance,
        float critDamage,
        float lifeSteal,
        float spellVamp,
        float cooldownReduction,
        AttackRange attackRange
) {

    /** Cap values verified từ AoV/HoK client tooltips (ADR-0003 R7). */
    private static final float CRIT_DAMAGE_CAP = 2.50f;
    private static final float COOLDOWN_REDUCTION_CAP = 0.40f;

    public BaseStats {
        if (maxHp <= 0f) {
            throw new IllegalArgumentException("maxHp must be > 0, got " + maxHp);
        }
        if (maxMana < 0f) {
            throw new IllegalArgumentException("maxMana must be >= 0, got " + maxMana);
        }
        if (armor < 0f || magicDefense < 0f) {
            throw new IllegalArgumentException("armor/magicDefense must be >= 0");
        }
        if (movementSpeed <= 0f) {
            throw new IllegalArgumentException("movementSpeed must be > 0, got " + movementSpeed);
        }
        if (attackSpeedBonus < 0f) {
            throw new IllegalArgumentException("attackSpeedBonus must be >= 0, got " + attackSpeedBonus);
        }
        if (critChance < 0f || critChance > 1f) {
            throw new IllegalArgumentException("critChance must be in [0, 1], got " + critChance);
        }
        if (critDamage < 1f || critDamage > CRIT_DAMAGE_CAP) {
            throw new IllegalArgumentException(
                    "critDamage must be in [1.0, " + CRIT_DAMAGE_CAP + "] (AoV/HoK cap), got " + critDamage);
        }
        if (cooldownReduction < 0f || cooldownReduction > COOLDOWN_REDUCTION_CAP) {
            throw new IllegalArgumentException(
                    "cooldownReduction must be in [0, " + COOLDOWN_REDUCTION_CAP + "] (AoV/HoK cap), got " + cooldownReduction);
        }
        if (attackRange == null) {
            throw new IllegalArgumentException("attackRange must not be null");
        }
    }
}
