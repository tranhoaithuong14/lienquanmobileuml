package com.moba.hero;

import com.moba.combat.AttackRange;
import com.moba.combat.Attribute;
import com.moba.combat.Basic;
import com.moba.combat.CombatResource;
import com.moba.combat.Hero;
import com.moba.combat.HeroRole;
import com.moba.combat.Offensive;
import com.moba.combat.Position;
import com.moba.strategy.LowestHP;
import com.moba.strategy.TargetSelector;

public final class Yorn {

    public static final String NAME = "Yorn";

    private static final Attribute ATTRIBUTE = new Attribute(
            new Basic(
                    3582f, 174f, 0f, 140f, 80f,
                    CombatResource.MANA, 440f
            ),
            new Offensive(
                    360f, 0f, 0f, 0f, 0f, 2.00f, 0f, 0f, 0f, 0f, 0f
            ),
            AttackRange.RANGED,
            HeroRole.MARKSMAN
    );

    private Yorn() {}

    public static Attribute attribute() {
        return ATTRIBUTE;
    }

    public static Hero create() {
        return create(new Position(0, 0), new LowestHP());
    }

    public static Hero create(Position position, TargetSelector targetSelector) {
        return new Hero(NAME, position, attribute(), targetSelector);
    }
}