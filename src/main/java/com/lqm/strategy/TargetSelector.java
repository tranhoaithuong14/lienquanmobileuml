package com.lqm.strategy;

import com.lqm.combat.Enemy;
import java.util.List;

/**
 * Strategy interface — quy tắc chọn một mục tiêu từ danh sách địch.
 * Trả về null nếu không có mục tiêu hợp lệ.
 *
 * ConcreteStrategy: NearestEnemy, LowestHP (xem lessons/0001-strategy-target-selection.html).
 */
public interface TargetSelector {
    Enemy select(Enemy attacker, List<Enemy> enemies);
}