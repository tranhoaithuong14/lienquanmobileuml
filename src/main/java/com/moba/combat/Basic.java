package com.moba.combat;

public record Basic(
        float hp,
        float normalAttack,
        float abilityPower,
        float armor,
        float magicDefense,
        CombatResource resourceType,
        float maxResource
) {

    public static Basic of(
            float hp,
            float normalAttack,
            float abilityPower,
            float armor,
            float magicDefense,
            CombatResource resourceType,
            float maxResource) {
        return new Basic(hp, normalAttack, abilityPower, armor, magicDefense, resourceType, maxResource);
    }

    public Basic {
        if (hp <= 0f) {
            throw new IllegalArgumentException("hp must be > 0, got " + hp);
        }
        if (resourceType == null) {
            throw new IllegalArgumentException("resourceType must not be null");
        }
        if (maxResource < 0f) {
            throw new IllegalArgumentException("maxResource must be >= 0, got " + maxResource);
        }
        if (armor < 0f || magicDefense < 0f) {
            throw new IllegalArgumentException("armor/magicDefense must be >= 0, got armor=" + armor + " magicDefense=" + magicDefense);
        }
    }
}