package com.moba.combat;

public record CoreStats(
        float maxHp,
        float attackDamage,
        float abilityPower,
        float armor,
        float magicDefense,
        float maxMana
) {

    public static CoreStats of(
            float maxHp,
            float attackDamage,
            float abilityPower,
            float armor,
            float magicDefense,
            float maxMana) {
        return new CoreStats(maxHp, attackDamage, abilityPower, armor, magicDefense, maxMana);
    }

    public CoreStats {
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
