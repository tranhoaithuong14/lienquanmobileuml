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
                    3582f,   /* mauToiDa             */  // UI hang 1 trai:  "Mau 3582"
                    174f,    /* congVatLy            */  // UI hang 1 giua:  "Cong vat ly 174"
                    0f,      /* congPhep             */  // UI hang 1 phai:  "Cong phep 0"
                    140f,    /* giap                 */  // UI hang 2 trai:  "Giap 140 | 0%"
                    80f,     /* giapPhep             */  // UI hang 2 giua:  "Giap phep 80 | 0%"
                    440f     /* nangLuongToiDa       */  // UI hang 2 phai:  "Nang luong toi da 440"
            ),
            ThuocTinhTanCong.cua(
                    360f,    /* tocChay              */  // UI tan cong h1 trai:  "Toc chay 360.0"
                    0f,      /* xuyenGiap            */  // UI tan cong h1 giua:  "Xuyen giap 0"
                    0f,      /* xuyenGiapPhep        */  // UI tan cong h1 phai:  "Xuyen giap phep 0"
                    0f,      /* tocDanhCong          */  // UI tan cong h2 trai:  "Toc danh + 0%"
                    0f,      /* tyLeChiMang          */  // UI tan cong h2 giua:  "Ty le chi mang 0%"
                    2.00f,   /* satThuongChiMang     */  // UI tan cong h2 phai:  "Sat thuong chi mang 200% (AoV 2.0x)"
                    0f,      /* hutMau               */  // UI tan cong h3 trai:  "Hut mau 0%"
                    0f,      /* hutMauPhep           */  // UI tan cong h3 giua:  "Hut mau phep 0%"
                    0f,      /* giamHoiChieu         */  // UI tan cong h3 phai:  "Giam hoi chieu 0%"
                    0f,      /* xuyenGiapPhanTram    */
                    0f       /* xuyenGiapPhepPhanTram*/
            ),
            TamDanh.DANHXHA,                                  // UI duoi: "Tam danh Danh xa"
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
