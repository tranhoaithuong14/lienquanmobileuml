package com.lqm.combat;

/**
 * Vị trí trong bản đồ game. Dùng Java 17 record để có equality, hashCode, toString miễn phí.
 *
 * Khoảng cách Euclidean là một property của cặp Position — đặt ở đây để mọi consumer
 * (NearestEnemy, movement, AoE, line-of-sight) dùng chung mà không duplicate math.
 */
public record Position(double x, double y) {

    /**
     * Khoảng cách Euclidean từ `this` đến `other`.
     * Symmetric: a.distanceTo(b) == b.distanceTo(a).
     * Zero khi cùng vị trí.
     */
    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}