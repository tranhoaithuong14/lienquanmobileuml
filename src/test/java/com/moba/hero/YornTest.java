package com.moba.hero;

import com.moba.combat.AttackRange;
import com.moba.combat.Attribute;
import com.moba.combat.Hero;
import com.moba.combat.HeroRole;
import com.moba.combat.Position;
import com.moba.strategy.LowestHP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YornTest {

    @Test
    void yornHpMatchesSheet() {
        assertEquals(3582f, Yorn.attribute().hp());
    }

    @Test
    void yornMaxManaMatchesSheet() {
        assertEquals(440f, Yorn.attribute().maxMana());
    }

    @Test
    void yornNormalAttackMatchesSheet() {
        assertEquals(174f, Yorn.attribute().normalAttack());
    }

    @Test
    void yornAbilityPowerMatchesSheet() {
        assertEquals(0f, Yorn.attribute().abilityPower());
    }

    @Test
    void yornArmorMatchesSheet() {
        assertEquals(140f, Yorn.attribute().armor());
    }

    @Test
    void yornMagicDefenseMatchesSheet() {
        assertEquals(80f, Yorn.attribute().magicDefense());
    }

    @Test
    void yornMovementSpeedMatchesSheet() {
        assertEquals(360f, Yorn.attribute().movementSpeed());
    }

    @Test
    void yornAttackSpeedMatchesSheet() {
        assertEquals(0f, Yorn.attribute().attackSpeed());
    }

    @Test
    void yornArmorPenMatchesSheet() {
        Attribute s = Yorn.attribute();
        assertEquals(0f, s.armorPen());
        assertEquals(0f, s.armorPenPercent());
    }

    @Test
    void yornMagicPenMatchesSheet() {
        Attribute s = Yorn.attribute();
        assertEquals(0f, s.magicPen());
        assertEquals(0f, s.magicPenPercent());
    }

    @Test
    void yornCritChanceMatchesSheet() {
        assertEquals(0f, Yorn.attribute().critChance());
    }

    @Test
    void yornCritDamageMatchesSheet() {
        assertEquals(2.00f, Yorn.attribute().critDamage());
    }

    @Test
    void yornLifeStealMatchesSheet() {
        assertEquals(0f, Yorn.attribute().lifeSteal());
    }

    @Test
    void yornSpellVampMatchesSheet() {
        assertEquals(0f, Yorn.attribute().spellVamp());
    }

    @Test
    void yornCooldownReductionMatchesSheet() {
        assertEquals(0f, Yorn.attribute().cooldownReduction());
    }

    @Test
    void yornAttackRangeIsRanged() {
        assertEquals(AttackRange.RANGED, Yorn.attribute().attackRange());
    }

    @Test
    void yornRoleIsMarksman() {
        assertEquals(HeroRole.MARKSMAN, Yorn.attribute().role());
    }

    @Test
    void yornCreateBuildsExpectedComponents() {
        Hero yorn = Yorn.create();

        assertEquals(Yorn.NAME, yorn.getName());
        assertEquals(Yorn.attribute(), yorn.getAttribute());
        assertEquals(3582f, yorn.getCurrentHp());
        assertEquals(new Position(0, 0), yorn.getPosition());
    }

    @Test
    void yornCreateAllowsOverridingPositionAndSelector() {
        Hero yorn = Yorn.create(new Position(10, 20), new LowestHP());

        assertEquals(new Position(10, 20), yorn.getPosition());
    }
}