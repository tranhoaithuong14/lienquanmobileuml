package com.moba.combat;

public record BasicStats(
        float hp,
        float normalAttack,
        float abilityPower,
        float armor,
        float magicDefense,
        float maxMana
) {

    public static BasicStats of(
            float hp,
            float normalAttack,
            float abilityPower,
            float armor,
            float magicDefense,
            float maxMana) {
        return new BasicStats(hp, normalAttack, abilityPower, armor, magicDefense, maxMana);
    }

    public BasicStats {
        if (hp <= 0f) {
            throw new IllegalArgumentException("hp must be > 0, got " + hp);
        }
        if (maxMana < 0f) {
            throw new IllegalArgumentException("maxMana must be >= 0, got " + maxMana);
        }
        if (armor < 0f || magicDefense < 0f) {
            throw new IllegalArgumentException("armor/magicDefense must be >= 0, got armor=" + armor + " magicDefense=" + magicDefense);
        }
    }
}