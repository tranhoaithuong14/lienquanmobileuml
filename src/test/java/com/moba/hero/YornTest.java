package com.moba.hero;

import com.moba.combat.AttackRange;
import com.moba.combat.BaseStats;
import com.moba.combat.Hero;
import com.moba.combat.Position;
import com.moba.strategy.LowestHP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YornTest {

    @Test
    void yornMaxHpMatchesSheet() {
        assertEquals(3582f, Yorn.stats().hp());
    }

    @Test
    void yornMaxManaMatchesSheet() {
        assertEquals(440f, Yorn.stats().maxMana());
    }

    @Test
    void yornAttackDamageMatchesSheet() {
        assertEquals(174f, Yorn.stats().normalAttack());
    }

    @Test
    void yornAbilityPowerMatchesSheet() {
        assertEquals(0f, Yorn.stats().abilityPower());
    }

    @Test
    void yornArmorMatchesSheet() {
        assertEquals(140f, Yorn.stats().armor());
    }

    @Test
    void yornMagicDefenseMatchesSheet() {
        assertEquals(80f, Yorn.stats().magicDefense());
    }

    @Test
    void yornMovementSpeedMatchesSheet() {
        assertEquals(360f, Yorn.stats().movementSpeed());
    }

    @Test
    void yornAttackSpeedMatchesSheet() {
        assertEquals(0f, Yorn.stats().attackSpeed());
    }

    @Test
    void yornArmorPenMatchesSheet() {
        BaseStats s = Yorn.stats();
        assertEquals(0f, s.armorPen());
        assertEquals(0f, s.armorPenPercent());
    }

    @Test
    void yornMagicPenMatchesSheet() {
        BaseStats s = Yorn.stats();
        assertEquals(0f, s.magicPen());
        assertEquals(0f, s.magicPenPercent());
    }

    @Test
    void yornCritChanceMatchesSheet() {
        assertEquals(0f, Yorn.stats().critChance());
    }

    @Test
    void yornCritDamageMatchesSheet() {
        assertEquals(2.00f, Yorn.stats().critDamage());
    }

    @Test
    void yornLifeStealMatchesSheet() {
        assertEquals(0f, Yorn.stats().lifeSteal());
    }

    @Test
    void yornSpellVampMatchesSheet() {
        assertEquals(0f, Yorn.stats().spellVamp());
    }

    @Test
    void yornCooldownReductionMatchesSheet() {
        assertEquals(0f, Yorn.stats().cooldownReduction());
    }

    @Test
    void yornAttackRangeIsRanged() {
        assertEquals(AttackRange.RANGED, Yorn.stats().attackRange());
    }

    @Test
    void yornCreateBuildsExpectedComponents() {
        Hero yorn = Yorn.create();

        assertEquals(Yorn.NAME, yorn.getName());
        assertEquals(Yorn.stats(), yorn.getBaseStats());
        assertEquals(3582f, yorn.getCurrentHp());
        assertEquals(new Position(0, 0), yorn.getPosition());
    }

    @Test
    void yornCreateAllowsOverridingPositionAndSelector() {
        Hero yorn = Yorn.create(new Position(10, 20), new LowestHP());

        assertEquals(new Position(10, 20), yorn.getPosition());
    }
}
