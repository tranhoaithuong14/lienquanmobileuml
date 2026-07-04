package com.moba.combat;

public record Offensive(
        float movementSpeed,
        float armorPen,
        float magicPen,
        float attackSpeed,
        float critChance,
        float critDamage,
        float lifeSteal,
        float spellVamp,
        float cooldownReduction,
        float armorPenPercent,
        float magicPenPercent
) {

    public static final float CRIT_DAMAGE_MIN = 1.0f;
    public static final float COOLDOWN_REDUCTION_MIN = 0f;
    public static final float COOLDOWN_REDUCTION_MAX = 0.40f;

    public static Offensive of(
            float movementSpeed,
            float armorPen, float magicPen,
            float attackSpeed,
            float critChance, float critDamage,
            float lifeSteal, float spellVamp,
            float cooldownReduction,
            float armorPenPercent, float magicPenPercent) {
        return new Offensive(movementSpeed, armorPen, magicPen,
                attackSpeed, critChance, critDamage,
                lifeSteal, spellVamp, cooldownReduction,
                armorPenPercent, magicPenPercent);
    }

    public Offensive {
        if (movementSpeed <= 0f) {
            throw new IllegalArgumentException("movementSpeed must be > 0, got " + movementSpeed);
        }
        if (attackSpeed < 0f) {
            throw new IllegalArgumentException("attackSpeed must be >= 0, got " + attackSpeed);
        }
        if (armorPen < 0f || armorPenPercent < 0f
                || magicPen < 0f || magicPenPercent < 0f) {
            throw new IllegalArgumentException("armor pen / magic pen must be >= 0");
        }
        if (lifeSteal < 0f || spellVamp < 0f) {
            throw new IllegalArgumentException("lifeSteal/spellVamp must be >= 0");
        }
        if (critChance < 0f || critChance > 1f) {
            throw new IllegalArgumentException("critChance must be in [0, 1], got " + critChance);
        }
        if (critDamage < CRIT_DAMAGE_MIN) {
            throw new IllegalArgumentException(
                    "critDamage must be >= " + CRIT_DAMAGE_MIN + ", got " + critDamage);
        }
        if (cooldownReduction < COOLDOWN_REDUCTION_MIN || cooldownReduction > COOLDOWN_REDUCTION_MAX) {
            throw new IllegalArgumentException(
                    "cooldownReduction must be in [" + COOLDOWN_REDUCTION_MIN + ", " + COOLDOWN_REDUCTION_MAX + "], got " + cooldownReduction);
        }
    }
}