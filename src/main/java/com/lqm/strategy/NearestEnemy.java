package com.lqm.strategy;

import com.lqm.combat.Enemy;

import java.util.List;

/**
 * ConcreteStrategy: chọn địch có khoảng cách Euclidean nhỏ nhất từ attacker.
 *
 * Sau Candidate 3: thân hàm trở thành 1-liner. Khoảng cách tính qua Position.distanceTo;
 * thuật toán min-loop delegate sang MinSelector (shared helper, test riêng).
 */
public class NearestEnemy implements TargetSelector {

    @Override
    public Enemy select(Enemy attacker, List<Enemy> enemies) {
        if (enemies.isEmpty()) {
            return null;
        }
        return MinSelector.minBy(enemies, e -> attacker.getPosition().distanceTo(e.getPosition()));
    }
}