package com.moba.combat;

public record Basic(
        float hp,
        float normalAttack,
        float abilityPower,
        float armor,
        float magicDefense,
        float maxMana
) {

    public static Basic of(
            float hp,
            float normalAttack,
            float abilityPower,
            float armor,
            float magicDefense,
            float maxMana) {
        return new Basic(hp, normalAttack, abilityPower, armor, magicDefense, maxMana);
    }

    public Basic {
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