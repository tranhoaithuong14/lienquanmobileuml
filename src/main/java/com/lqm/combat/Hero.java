package com.lqm.combat;

import com.lqm.strategy.TargetSelector;

import java.util.List;

/**
 * Context trong Strategy pattern.
 * Giữ một TargetSelector và ủy quyền việc chọn mục tiêu cho nó.
 *
 * Hero cũng là Enemy (tướng có thể bị tướng khác chọn làm mục tiêu),
 * nên implement Enemy interface để truyền `this` làm attacker khi gọi strategy.
 */
public class Hero implements Enemy {

    private final String name;
    private final Position position;
    private final int currentHp;
    private final TargetSelector targetSelector;

    public Hero(String name, Position position, int currentHp, TargetSelector targetSelector) {
        this.name = name;
        this.position = position;
        this.currentHp = currentHp;
        this.targetSelector = targetSelector;
    }

    /**
     * Ủy quyền cho TargetSelector — Hero KHÔNG tự quyết định mục tiêu.
     * Trả về null nếu selector trả null (vd: danh sách địch rỗng).
     */
    public Enemy selectTarget(List<Enemy> enemies) {
        return targetSelector.select(this, enemies);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public String getName() {
        return name;
    }
}