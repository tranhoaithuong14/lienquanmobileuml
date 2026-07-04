package com.lqm.strategy;

import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Shared helper: tìm phần tử có score nhỏ nhất trong list theo scorer function.
 *
 * Strategy pattern ở đây: "min selection" là một chiến lược mà NearestEnemy / LowestHP
 * dùng với scorer khác nhau. Trước đây mỗi strategy duplicate vòng lặp min; với helper
 * này, mỗi strategy trở thành 1-liner gọi scorer.
 *
 * Tie-break: strict less-than → phần tử đầu tiên trong list thắng khi cùng score.
 * Caller phải kiểm tra list không rỗng trước khi gọi (helper không handle empty).
 */
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