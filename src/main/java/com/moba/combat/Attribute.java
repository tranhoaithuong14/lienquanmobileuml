package com.moba.combat;

import java.util.Objects;

public record Attribute(
        Basic basic,
        Offensive offensive,
        AttackRange attackRange,
        HeroRole role
) {

    public Attribute {
        Objects.requireNonNull(basic, "basic must not be null");
        Objects.requireNonNull(offensive, "offensive must not be null");
        if (attackRange == null) {
            throw new IllegalArgumentException("attackRange must not be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Basic basic;
        private Offensive offensive;
        private AttackRange attackRange;
        private HeroRole role;

        private Builder() {}

        public Builder basic(Basic basic) {
            this.basic = basic;
            return this;
        }

        public Builder basic(
                float hp,
                float normalAttack,
                float abilityPower,
                float armor,
                float magicDefense,
                CombatResource resourceType,
                float maxResource) {
            this.basic = new Basic(hp, normalAttack, abilityPower, armor, magicDefense, resourceType, maxResource);
            return this;
        }

        public Builder offensive(Offensive offensive) {
            this.offensive = offensive;
            return this;
        }

        public Builder offensive(
                float movementSpeed,
                float armorPen, float magicPen,
                float attackSpeed,
                float critChance, float critDamage,
                float lifeSteal, float spellVamp,
                float cooldownReduction,
                float armorPenPercent, float magicPenPercent) {
            this.offensive = new Offensive(
                    movementSpeed, armorPen, magicPen,
                    attackSpeed, critChance, critDamage,
                    lifeSteal, spellVamp, cooldownReduction,
                    armorPenPercent, magicPenPercent);
            return this;
        }

        public Builder attackRange(AttackRange attackRange) {
            this.attackRange = attackRange;
            return this;
        }

        public Builder role(HeroRole role) {
            this.role = role;
            return this;
        }

        public Attribute build() {
            return new Attribute(basic, offensive, attackRange, role);
        }
    }
}
