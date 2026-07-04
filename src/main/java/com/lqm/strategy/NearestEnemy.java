package com.lqm.strategy;

import com.lqm.combat.Enemy;
import com.lqm.combat.Position;

import java.util.List;

/**
 * ConcreteStrategy: chọn địch có khoảng cách Euclidean nhỏ nhất từ attacker.
 *
 * Cycle 3 GREEN: với nhiều enemy, duyệt list tìm khoảng cách min, trả về enemy tương ứng.
 * Điều kiện `d < minDistance` (strict less-than) đảm bảo tie-break ổn định:
 * enemy đầu tiên trong list thắng khi cùng khoảng cách.
 */
public class NearestEnemy implements TargetSelector {

    @Override
    public Enemy select(Enemy attacker, List<Enemy> enemies) {
        if (enemies.isEmpty()) {
            return null;
        }

        Enemy nearest = enemies.get(0);
        double minDistance = distance(attacker.getPosition(), nearest.getPosition());

        for (int i = 1; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            double d = distance(attacker.getPosition(), e.getPosition());
            if (d < minDistance) {
                minDistance = d;
                nearest = e;
            }
        }
        return nearest;
    }

    private static double distance(Position a, Position b) {
        double dx = a.x() - b.x();
        double dy = a.y() - b.y();
        return Math.sqrt(dx * dx + dy * dy);
    }
}