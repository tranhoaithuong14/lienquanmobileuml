package com.lqm.strategy;

import com.lqm.combat.Enemy;

import java.util.List;

/**
 * ConcreteStrategy: chọn địch có HP hiện tại thấp nhất.
 *
 * Cycle 6 GREEN: với nhiều enemy, duyệt list tìm min HP, trả về enemy tương ứng.
 * Điều kiện `hp < minHp` (strict less-than) đảm bảo tie-break ổn định:
 * enemy đầu tiên trong list thắng khi cùng HP.
 */
public class LowestHP implements TargetSelector {

    @Override
    public Enemy select(Enemy attacker, List<Enemy> enemies) {
        if (enemies.isEmpty()) {
            return null;
        }

        Enemy weakest = enemies.get(0);
        int minHp = weakest.getCurrentHp();

        for (int i = 1; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            int hp = e.getCurrentHp();
            if (hp < minHp) {
                minHp = hp;
                weakest = e;
            }
        }
        return weakest;
    }
}