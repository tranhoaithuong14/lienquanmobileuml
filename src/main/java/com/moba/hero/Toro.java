package com.moba.hero;

import com.moba.combat.AttackRange;
import com.moba.combat.Attribute;
import com.moba.combat.Basic;
import com.moba.combat.CombatResource;
import com.moba.combat.Hero;
import com.moba.combat.HeroRole;
import com.moba.combat.Offensive;
import com.moba.combat.Position;

public final class Toro {

    public static final String NAME = "Toro";

    private static final Attribute ATTRIBUTE = new Attribute(
            new Basic(
                    15000f, 163f, 0f, 132f, 50f,
                    CombatResource.FIGHTING_SPIRIT, 200f
            ),
            new Offensive(
                    370f, 0f, 0f, 0f, 0f, 2.00f, 0f, 0f, 0f, 0f, 0f
            ),
            AttackRange.MELEE,
            HeroRole.TANK
    );

    private Toro() {}

    public static Attribute attribute() {
        return ATTRIBUTE;
    }

    public static Hero create() {
        return create(new Position(0, 0));
    }

    public static Hero create(Position position) {
        return new Hero(NAME, position, attribute());
    }
}
