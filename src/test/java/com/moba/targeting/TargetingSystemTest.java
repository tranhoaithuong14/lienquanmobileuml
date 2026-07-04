package com.moba.targeting;

import com.moba.combat.Enemy;
import com.moba.combat.Position;
import com.moba.combat.TargetKind;
import com.moba.test.EnemyStub;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TargetingSystemTest {

    private final TargetingSystem targetingSystem = new TargetingSystem();
    private final Enemy attacker = EnemyStub.at(new Position(0, 0), 1000, 1000, TargetKind.HERO, "Attacker");

    @Test
    void normalAttackUsesLowestHpAmountWhenThatIsThePlayerPriority() {
        Enemy lowAmount = EnemyStub.at(new Position(5, 0), 300, 300, TargetKind.HERO, "Low amount");
        Enemy lowPercent = EnemyStub.at(new Position(3, 0), 500, 10000, TargetKind.HERO, "Low percent");
        TargetingRequest request = TargetingRequest.normalAttack(attacker, TargetingPriority.LOWEST_HP_AMOUNT, 6);

        Enemy result = targetingSystem.select(request, List.of(lowPercent, lowAmount));

        assertSame(lowAmount, result);
    }

    @Test
    void normalAttackUsesLowestHpPercentWhenThatIsThePlayerPriority() {
        Enemy lowAmount = EnemyStub.at(new Position(5, 0), 300, 300, TargetKind.HERO, "Low amount");
        Enemy lowPercent = EnemyStub.at(new Position(3, 0), 500, 10000, TargetKind.HERO, "Low percent");
        TargetingRequest request = TargetingRequest.normalAttack(attacker, TargetingPriority.LOWEST_HP_PERCENT, 6);

        Enemy result = targetingSystem.select(request, List.of(lowAmount, lowPercent));

        assertSame(lowPercent, result);
    }

    @Test
    void tapToCastAbilityUsesTheSamePlayerPriorityAsNormalAttack() {
        Enemy near = EnemyStub.at(new Position(2, 0), 900, 1000, TargetKind.HERO, "Near");
        Enemy far = EnemyStub.at(new Position(4, 0), 100, 1000, TargetKind.HERO, "Far");
        TargetingRequest request = TargetingRequest.tapToCastAbility(attacker, TargetingPriority.LOWEST_HP_AMOUNT, 6);

        Enemy result = targetingSystem.select(request, List.of(near, far));

        assertSame(far, result);
    }

    @Test
    void finisherAbilityIgnoresPlayerPriorityAndPicksLowestHpAmount() {
        Enemy near = EnemyStub.at(new Position(1, 0), 900, 1000, TargetKind.HERO, "Near");
        Enemy killable = EnemyStub.at(new Position(4, 0), 50, 1000, TargetKind.HERO, "Killable");
        TargetingRequest request = TargetingRequest.finisherAbility(attacker, TargetingPriority.NEAREST, 6);

        Enemy result = targetingSystem.select(request, List.of(near, killable));

        assertSame(killable, result);
    }

    @Test
    void directionalTapChoosesHeroGroupBeforeMinionsMonstersAndTowers() {
        Enemy minion = EnemyStub.at(new Position(1, 0), 1, 100, TargetKind.MINION, "Minion");
        Enemy monster = EnemyStub.at(new Position(1, 0), 1, 100, TargetKind.MONSTER, "Monster");
        Enemy tower = EnemyStub.at(new Position(1, 0), 1, 100, TargetKind.TOWER, "Tower");
        Enemy hero = EnemyStub.at(new Position(5, 0), 900, 1000, TargetKind.HERO, "Hero");
        TargetingRequest request = TargetingRequest.directionalTapAbility(attacker, TargetingPriority.NEAREST, 6, 0);

        Enemy result = targetingSystem.select(request, List.of(minion, monster, tower, hero));

        assertSame(hero, result);
    }

    @Test
    void directionalTapCanUseSlightlyOutOfRangeTargetsWhenNoTargetIsInRange() {
        Enemy justOutside = EnemyStub.at(new Position(5.5, 0), 900, 1000, TargetKind.HERO, "Just outside");
        Enemy tooFar = EnemyStub.at(new Position(8, 0), 10, 1000, TargetKind.HERO, "Too far");
        TargetingRequest request = TargetingRequest.directionalTapAbility(attacker, TargetingPriority.LOWEST_HP_AMOUNT, 5, 1);

        Enemy result = targetingSystem.select(request, List.of(tooFar, justOutside));

        assertSame(justOutside, result);
    }

    @Test
    void avatarLockOverridesPriorityWhenTheLockedTargetIsEligible() {
        Enemy lowHp = EnemyStub.at(new Position(3, 0), 1, 1000, TargetKind.HERO, "Low HP");
        Enemy locked = EnemyStub.at(new Position(4, 0), 900, 1000, TargetKind.HERO, "Locked");
        TargetingRequest request = TargetingRequest
                .normalAttack(attacker, TargetingPriority.LOWEST_HP_AMOUNT, 6)
                .withLockedTarget(locked);

        Enemy result = targetingSystem.select(request, List.of(lowHp, locked));

        assertSame(locked, result);
    }

    @Test
    void avatarLockFallsBackToPriorityWhenTheLockedTargetIsOutOfRange() {
        Enemy lowHp = EnemyStub.at(new Position(3, 0), 1, 1000, TargetKind.HERO, "Low HP");
        Enemy lockedOutOfRange = EnemyStub.at(new Position(8, 0), 900, 1000, TargetKind.HERO, "Locked");
        TargetingRequest request = TargetingRequest
                .normalAttack(attacker, TargetingPriority.LOWEST_HP_AMOUNT, 6)
                .withLockedTarget(lockedOutOfRange);

        Enemy result = targetingSystem.select(request, List.of(lowHp, lockedOutOfRange));

        assertSame(lowHp, result);
    }

    @Test
    void ignoresDeadCandidatesAndReturnsNullWhenNoEligibleTargetRemains() {
        Enemy dead = EnemyStub.at(new Position(3, 0), 0, 1000, TargetKind.HERO, "Dead");
        TargetingRequest request = TargetingRequest.normalAttack(attacker, TargetingPriority.NEAREST, 6);

        Enemy result = targetingSystem.select(request, List.of(dead));

        assertNull(result);
    }

    @Test
    void allowedKindsModelAttackButtonsSuchAsTowerOnlyTargeting() {
        Enemy hero = EnemyStub.at(new Position(1, 0), 1, 1000, TargetKind.HERO, "Hero");
        Enemy tower = EnemyStub.at(new Position(5, 0), 5000, 5000, TargetKind.TOWER, "Tower");
        TargetingRequest request = TargetingRequest.normalAttack(
                attacker,
                TargetingPriority.LOWEST_HP_AMOUNT,
                6,
                EnumSet.of(TargetKind.TOWER));

        Enemy result = targetingSystem.select(request, List.of(hero, tower));

        assertSame(tower, result);
    }
}
