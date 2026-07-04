package com.lqm.combat;

/**
 * Phân loại tầm đánh của Hero — categorical label hiển thị trên màn hình chi tiết tướng
 * (Liên Quân Mobile: "Cận chiến" / "Đánh xa"; Arena of Valor: "Melee" / "Ranged" / "Long Range").
 *
 * Dùng enum thay vì String (Bloch, Effective Java, 3rd ed., Item 34: enums over int/String constants —
 * type-safe, iterable, switchable). Reach số (đơn vị bản đồ) là khái niệm khác — không nhồi vào enum
 * để tránh premature generalization (ADR-0003, R3); khi cần hit-detection sẽ thêm float reach riêng.
 */
public enum AttackRange {
    MELEE,
    RANGED
}
