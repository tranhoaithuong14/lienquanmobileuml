package com.lqm.combat;

public record ViTri(double x, double y) {

    public double khoangCachDen(ViTri khac) {
        double dx = this.x - khac.x;
        double dy = this.y - khac.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
