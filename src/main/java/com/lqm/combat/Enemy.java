package com.lqm.combat;

/**
 * Bất kỳ thực thể nào có thể bị chọn làm mục tiêu: tướng địch, lính, quái rừng.
 * Strategy chỉ cần 2 thông tin: vị trí (cho NearestEnemy) và HP hiện tại (cho LowestHP).
 *
 * HP là float để khớp với Hero's internal HP semantics (currentHp: float).
 * Trước đây trả int + Math.round, gây type leak — LowestHP tie-break có thể sai
 * khi 2 giá trị float là khác nhau nhưng round về cùng int.
 */
public interface Enemy {
    Position getPosition();
    float getCurrentHp();
    String getName();
}