package com.lqm.test;

import com.lqm.combat.KeDich;
import com.lqm.combat.ViTri;

public final class KeDichGia {

    private KeDichGia() {}

    public static KeDich o(ViTri viTri, float mau, String ten) {
        return new KeDich() {
            @Override public ViTri layViTri() { return viTri; }
            @Override public float layMauHienTai() { return mau; }
            @Override public String layTen() { return ten; }
        };
    }
}
