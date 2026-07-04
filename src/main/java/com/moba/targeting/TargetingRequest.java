package com.moba.targeting;

import com.moba.combat.Enemy;
import com.moba.combat.TargetKind;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public record TargetingRequest(
        Enemy attacker,
        TargetingAction action,
        TargetingPriority priority,
        double range,
        double outOfRangeTolerance,
        Set<TargetKind> allowedKinds,
        Enemy lockedTarget
) {

    public TargetingRequest {
        attacker = Objects.requireNonNull(attacker, "attacker must not be null");
        action = Objects.requireNonNull(action, "action must not be null");
        priority = Objects.requireNonNull(priority, "priority must not be null");
        allowedKinds = Objects.requireNonNull(allowedKinds, "allowedKinds must not be null");
        if (range < 0d) {
            throw new IllegalArgumentException("range must not be negative");
        }
        if (outOfRangeTolerance < 0d) {
            throw new IllegalArgumentException("outOfRangeTolerance must not be negative");
        }
        if (allowedKinds.isEmpty()) {
            throw new IllegalArgumentException("allowedKinds must not be empty");
        }
        allowedKinds = Collections.unmodifiableSet(EnumSet.copyOf(allowedKinds));
    }

    public static TargetingRequest normalAttack(Enemy attacker, TargetingPriority priority, double range) {
        return normalAttack(attacker, priority, range, EnumSet.allOf(TargetKind.class));
    }

    public static TargetingRequest normalAttack(
            Enemy attacker,
            TargetingPriority priority,
            double range,
            Set<TargetKind> allowedKinds) {
        return new TargetingRequest(attacker, TargetingAction.NORMAL_ATTACK, priority, range, 0d, allowedKinds, null);
    }

    public static TargetingRequest tapToCastAbility(Enemy attacker, TargetingPriority priority, double range) {
        return new TargetingRequest(
                attacker,
                TargetingAction.TAP_TO_CAST_ABILITY,
                priority,
                range,
                0d,
                EnumSet.allOf(TargetKind.class),
                null);
    }

    public static TargetingRequest directionalTapAbility(
            Enemy attacker,
            TargetingPriority priority,
            double range,
            double outOfRangeTolerance) {
        return new TargetingRequest(
                attacker,
                TargetingAction.DIRECTIONAL_TAP_ABILITY,
                priority,
                range,
                outOfRangeTolerance,
                EnumSet.allOf(TargetKind.class),
                null);
    }

    public static TargetingRequest finisherAbility(Enemy attacker, TargetingPriority priority, double range) {
        return new TargetingRequest(
                attacker,
                TargetingAction.FINISHER_ABILITY,
                priority,
                range,
                0d,
                EnumSet.of(TargetKind.HERO),
                null);
    }

    public TargetingRequest withLockedTarget(Enemy lockedTarget) {
        return new TargetingRequest(
                attacker,
                action,
                priority,
                range,
                outOfRangeTolerance,
                allowedKinds,
                Objects.requireNonNull(lockedTarget, "lockedTarget must not be null"));
    }
}
