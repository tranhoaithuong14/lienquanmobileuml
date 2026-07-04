package com.moba.combat;

public record BaseStats(
        CoreStats core,
        OffensiveStats offensive,
        AttackRange attackRange
) {

    public BaseStats {
        if (attackRange == null) {
            throw new IllegalArgumentException("attackRange must not be null");
        }
    }

    public BaseStats(
            float maxHp,
            float attackDamage,
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
                new CoreStats(maxHp, attackDamage, abilityPower, armor, magicDefense, maxMana),
                new OffensiveStats(movementSpeed, armorPen, magicPen,
                        attackSpeed, critChance, critDamage,
                        lifeSteal, spellVamp, cooldownReduction,
                        armorPenPercent, magicPenPercent),
                attackRange);
    }

    /* shortcut — delegate down to core / offensive */

    public float maxHp() { return core.maxHp(); }
    public float attackDamage() { return core.attackDamage(); }
    public float abilityPower() { return core.abilityPower(); }
    public float armor() { return core.armor(); }
    public float magicDefense() { return core.magicDefense(); }
    public float maxMana() { return core.maxMana(); }

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
