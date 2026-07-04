package com.moba.hero;

import com.moba.combat.AttackRange;
import com.moba.combat.BaseStats;
import com.moba.combat.BasicStats;
import com.moba.combat.Hero;
import com.moba.combat.OffensiveStats;
import com.moba.combat.Position;
import com.moba.strategy.LowestHP;
import com.moba.strategy.TargetSelector;

public final class Yorn {

    public static final String NAME = "Yorn";

    private static final BaseStats STATS = new BaseStats(
            new BasicStats(
                    3582f, 174f, 0f, 140f, 80f, 440f
            ),
            new OffensiveStats(
                    360f, 0f, 0f, 0f, 0f, 2.00f, 0f, 0f, 0f, 0f, 0f
            ),
            AttackRange.RANGED
    );

    private Yorn() {}

    public static BaseStats stats() {
        return STATS;
    }

    public static Hero create() {
        return create(new Position(0, 0), new LowestHP());
    }

    public static Hero create(Position position, TargetSelector targetSelector) {
        return new Hero(NAME, position, stats(), targetSelector);
    }
}