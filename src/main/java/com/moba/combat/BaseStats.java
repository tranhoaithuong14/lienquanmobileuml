package com.moba.combat;

public record BaseStats(
        BasicStats basic,
        OffensiveStats offensive,
        AttackRange attackRange
) {

    public BaseStats {
        if (attackRange == null) {
            throw new IllegalArgumentException("attackRange must not be null");
        }
    }

    public BaseStats(
            float hp,
            float normalAttack,
            float abilityPower,
            float armor,
            float magicDefense,
            float maxMana,
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
            AttackRange attackRange
    ) {
        this(
                new BasicStats(hp, normalAttack, abilityPower, armor, magicDefense, maxMana),
                new OffensiveStats(movementSpeed, armorPen, magicPen,
                        attackSpeed, critChance, critDamage,
                        lifeSteal, spellVamp, cooldownReduction,
                        armorPenPercent, magicPenPercent),
                attackRange);
    }

    /* shortcut — delegate down to basic / offensive */

    public float hp() { return basic.hp(); }
    public float normalAttack() { return basic.normalAttack(); }
    public float abilityPower() { return basic.abilityPower(); }
    public float armor() { return basic.armor(); }
    public float magicDefense() { return basic.magicDefense(); }
    public float maxMana() { return basic.maxMana(); }

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