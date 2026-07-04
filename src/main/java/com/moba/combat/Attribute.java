package com.moba.combat;

public record Attribute(
        Basic basic,
        Offensive offensive,
        AttackRange attackRange,
        HeroRole role
) {

    public Attribute {
        if (attackRange == null) {
            throw new IllegalArgumentException("attackRange must not be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }
    }

    public Attribute(
            float hp,
            float normalAttack,
            float abilityPower,
            float armor,
            float magicDefense,
            CombatResource resourceType,
            float maxResource,
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
            float magicPenPercent,
            AttackRange attackRange,
            HeroRole role
    ) {
        this(
                new Basic(hp, normalAttack, abilityPower, armor, magicDefense, resourceType, maxResource),
                new Offensive(movementSpeed, armorPen, magicPen,
                        attackSpeed, critChance, critDamage,
                        lifeSteal, spellVamp, cooldownReduction,
                        armorPenPercent, magicPenPercent),
                attackRange,
                role);
    }

    /* shortcut — delegate down to basic / offensive */

    public float hp() { return basic.hp(); }
    public float normalAttack() { return basic.normalAttack(); }
    public float abilityPower() { return basic.abilityPower(); }
    public float armor() { return basic.armor(); }
    public float magicDefense() { return basic.magicDefense(); }
    public CombatResource resourceType() { return basic.resourceType(); }
    public float maxResource() { return basic.maxResource(); }

    public float movementSpeed() { return offensive.movementSpeed(); }
    public float armorPen() { return offensive.armorPen(); }
    public float magicPen() { return offensive.magicPen(); }
    public float attackSpeed() { return offensive.attackSpeed(); }
    public float critChance() { return offensive.critChance(); }
    public float critDamage() { return offensive.critDamage(); }
    public float lifeSteal() { return offensive.lifeSteal(); }
    public float spellVamp() { return offensive.spellVamp(); }
    public float cooldownReduction() { return offensive.cooldownReduction(); }
    public float armorPenPercent() { return offensive.armorPenPercent(); }
    public float magicPenPercent() { return offensive.magicPenPercent(); }
}