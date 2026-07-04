package com.moba.targeting;

import com.moba.combat.Enemy;
import com.moba.combat.TargetKind;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class TargetingSystem {

    private static final List<TargetKind> DIRECTIONAL_KIND_PRIORITY = List.of(
            TargetKind.HERO,
            TargetKind.MINION,
            TargetKind.MONSTER,
            TargetKind.TOWER
    );

    private final Map<TargetingPriority, TargetSelector> selectors;

    public TargetingSystem() {
        this(Map.of(
                TargetingPriority.NEAREST, new NearestTarget(),
                TargetingPriority.LOWEST_HP_AMOUNT, new LowestHpAmount(),
                TargetingPriority.LOWEST_HP_PERCENT, new LowestHpPercent()
        ));
    }

    public TargetingSystem(Map<TargetingPriority, TargetSelector> selectors) {
        Objects.requireNonNull(selectors, "selectors must not be null");
        this.selectors = new EnumMap<>(TargetingPriority.class);
        for (TargetingPriority priority : TargetingPriority.values()) {
            TargetSelector selector = selectors.get(priority);
            if (selector == null) {
                throw new IllegalArgumentException("missing selector for " + priority);
            }
            this.selectors.put(priority, selector);
        }
    }

    public Enemy select(TargetingRequest request, List<Enemy> candidates) {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(candidates, "candidates must not be null");
        if (!request.attacker().isAlive()) {
            return null;
        }

        double directRange = request.range();
        if (isEligible(request, request.lockedTarget(), directRange)) {
            return request.lockedTarget();
        }

        List<Enemy> eligible = eligibleCandidates(request, candidates, directRange);
        if (request.action().usesDirectionalKindPriority()) {
            eligible = firstDirectionalKindGroup(eligible);
            if (eligible.isEmpty() && request.outOfRangeTolerance() > 0d) {
                eligible = firstDirectionalKindGroup(eligibleCandidates(
                        request,
                        candidates,
                        request.range() + request.outOfRangeTolerance()));
            }
        }

        if (eligible.isEmpty()) {
            return null;
        }

        TargetingPriority resolvedPriority = request.action().priorityFrom(request.priority());
        return selectors.get(resolvedPriority).select(request, eligible);
    }

    private List<Enemy> eligibleCandidates(TargetingRequest request, List<Enemy> candidates, double range) {
        return candidates.stream()
                .filter(candidate -> isEligible(request, candidate, range))
                .toList();
    }

    private boolean isEligible(TargetingRequest request, Enemy candidate, double range) {
        return candidate != null
                && candidate != request.attacker()
                && candidate.isAlive()
                && request.allowedKinds().contains(candidate.getTargetKind())
                && request.attacker().getPosition().distanceTo(candidate.getPosition()) <= range;
    }

    private List<Enemy> firstDirectionalKindGroup(List<Enemy> candidates) {
        for (TargetKind targetKind : DIRECTIONAL_KIND_PRIORITY) {
            List<Enemy> group = candidates.stream()
                    .filter(candidate -> candidate.getTargetKind() == targetKind)
                    .toList();
            if (!group.isEmpty()) {
                return group;
            }
        }
        return List.of();
    }
}
