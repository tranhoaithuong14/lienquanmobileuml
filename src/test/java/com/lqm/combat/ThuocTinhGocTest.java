package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThuocTinhGocTest {

    private static ThuocTinhGoc lqmnKhong(float mauToiDa) {
        return new ThuocTinhGoc(
                /* coBan:        */ mauToiDa, 0f, 0f, 0f, 0f, 0f,
                /* tanCong:      */ 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                /* tamDanh + gioiHan: */ TamDanh.CANCHIEN, GioiHanKhachHang.LQMN);
    }

    private static ThuocTinhGoc hvkKhong(float mauToiDa) {
        return new ThuocTinhGoc(
                /* coBan:        */ mauToiDa, 0f, 0f, 0f, 0f, 0f,
                /* tanCong:      */ 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                /* tamDanh + gioiHan: */ TamDanh.CANCHIEN, GioiHanKhachHang.HVK);
    }

    @Test
    void recordBangCacField() {
        ThuocTinhGoc a = lqmnKhong(100f);
        ThuocTinhGoc b = lqmnKhong(100f);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void tuChoiMauToiDaKhongDuong() {
        assertThrows(IllegalArgumentException.class, () -> lqmnKhong(0f));
        assertThrows(IllegalArgumentException.class, () -> lqmnKhong(-1f));
    }

    @Test
    void tuChoiTocChayKhongDuong() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f,
                /* tocChay */ 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.LQMN));
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f,
                /* tocChay */ -1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.LQMN));
    }

    @Test
    void tuChoiTyLeChiMangNgoaiDonVi() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                /* tyLeChiMang */ -0.01f, /* satThuongChiMang */ 1f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.LQMN));
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f,
                /* tyLeChiMang */ 1.01f, /* satThuongChiMang */ 1f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.LQMN));
    }

    @Test
    void tuChoiSatThuongChiMangDuoiMot() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* satThuongChiMang */ 0.99f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.HVK));
    }

    @Test
    void hvkTuChoiSatThuongChiMangTren250() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* satThuongChiMang */ 2.51f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.HVK));
    }

    @Test
    void lqmnChoPhepSatThuongChiMangCao() {
        ThuocTinhGoc s = new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
                /* satThuongChiMang */ 5.0f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.LQMN);

        assertEquals(5.0f, s.satThuongChiMang());
    }

    @Test
    void tuChoiGiamHoiChieuTrenCap() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                /* giamHoiChieu */ 0.41f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.LQMN));
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                /* giamHoiChieu */ 0.41f, 0f, 0f,
                TamDanh.CANCHIEN, GioiHanKhachHang.HVK));
    }

    @Test
    void tuChoiTamDanhNull() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhGoc(
                100f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                null, GioiHanKhachHang.LQMN));
    }
}
