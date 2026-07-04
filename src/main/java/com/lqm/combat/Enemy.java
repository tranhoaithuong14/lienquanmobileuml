package com.lqm.combat;

/**
 * Bất kỳ thực thể nào có thể bị chọn làm mục tiêu: tướng địch, lính, quái rừng.
 * Strategy chỉ cần 2 thông tin: vị trí (cho NearestEnemy) và HP hiện tại (cho LowestHP).
 */
public interface Enemy {
    Position getPosition();
    int getCurrentHp();
    String getName();
}