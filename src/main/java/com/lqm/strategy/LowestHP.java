package com.lqm.strategy;

import com.lqm.combat.Enemy;

import java.util.List;

/**
 * ConcreteStrategy: chọn địch có HP hiện tại thấp nhất.
 *
 * Sau Candidate 3: thân hàm trở thành 1-liner. Thuật toán min-loop delegate sang
 * MinSelector (shared helper, test riêng).
 */
public class LowestHP implements TargetSelector {

    @Override
    public Enemy select(Enemy attacker, List<Enemy> enemies) {
        if (enemies.isEmpty()) {
            return null;
        }
        return MinSelector.minBy(enemies, Enemy::getCurrentHp);
    }
}