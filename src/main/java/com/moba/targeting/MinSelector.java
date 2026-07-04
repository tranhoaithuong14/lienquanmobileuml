package com.moba.targeting;

import java.util.List;
import java.util.function.ToDoubleFunction;

public final class MinSelector {

    private MinSelector() {}

    public static <T> T minBy(List<T> items, ToDoubleFunction<T> scorer) {
        T best = items.get(0);
        double bestScore = scorer.applyAsDouble(best);
        for (int i = 1; i < items.size(); i++) {
            T candidate = items.get(i);
            double score = scorer.applyAsDouble(candidate);
            if (score < bestScore) {
                best = candidate;
                bestScore = score;
            }
        }
        return best;
    }
}
