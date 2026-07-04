package com.lqm.hero;

import com.lqm.combat.GioiHanKhachHang;
import com.lqm.combat.TamDanh;
import com.lqm.combat.ThuocTinhCoBan;
import com.lqm.combat.ThuocTinhGoc;
import com.lqm.combat.ThuocTinhTanCong;
import com.lqm.combat.Tuong;
import com.lqm.combat.ViTri;
import com.lqm.strategy.BoChonMucTieu;
import com.lqm.strategy.HPThapNhat;

public final class Yorn {

    public static final String TEN = "Yorn";

    private static final ThuocTinhGoc THUOC_TINH = new ThuocTinhGoc(
            ThuocTinhCoBan.cua(
                    3582f, 174f, 0f, 140f, 80f, 440f
            ),
            ThuocTinhTanCong.cua(
                    360f, 0f, 0f, 0f, 0f, 2.00f, 0f, 0f, 0f, 0f, 0f
            ),
            TamDanh.DANHXHA,
            GioiHanKhachHang.LQMN
    );

    private Yorn() {}

    public static ThuocTinhGoc thuocTinh() {
        return THUOC_TINH;
    }

    public static Tuong tao() {
        return tao(new ViTri(0, 0), new HPThapNhat());
    }

    public static Tuong tao(ViTri viTri, BoChonMucTieu boChonMucTieu) {
        return new Tuong(TEN, viTri, thuocTinh(), boChonMucTieu);
    }
}
