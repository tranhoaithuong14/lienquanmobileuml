package com.moba.targeting;

public enum TargetingAction {
    NORMAL_ATTACK,
    TAP_TO_CAST_ABILITY,
    DIRECTIONAL_TAP_ABILITY,
    FINISHER_ABILITY;

    public TargetingPriority priorityFrom(TargetingPriority playerPriority) {
        if (this == FINISHER_ABILITY) {
            return TargetingPriority.LOWEST_HP_AMOUNT;
        }
        return playerPriority;
    }

    public boolean usesDirectionalKindPriority() {
        return this == DIRECTIONAL_TAP_ABILITY;
    }
}
