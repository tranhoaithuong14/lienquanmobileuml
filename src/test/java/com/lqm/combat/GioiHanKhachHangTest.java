package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GioiHanKhachHangTest {

    @Test
    void lqmnSatThuongChiMangMaxLaVoCung() {
        assertEquals(Float.POSITIVE_INFINITY, GioiHanKhachHang.LQMN.satThuongChiMangMax());
    }

    @Test
    void hvkSatThuongChiMangMaxLa250() {
        assertEquals(2.50f, GioiHanKhachHang.HVK.satThuongChiMangMax());
    }

    @Test
    void giamHoiChieuCapGiongNhauTrenCaHaiKhachHang() {
        assertEquals(0.40f, GioiHanKhachHang.LQMN.giamHoiChieuMax());
        assertEquals(0.40f, GioiHanKhachHang.HVK.giamHoiChieuMax());
    }

    @Test
    void tuChoiSatThuongChiMangMinDuoiMot() {
        assertThrows(IllegalArgumentException.class,
                () -> new GioiHanKhachHang(0.99f, 2.5f, 0f, 0.4f));
    }

    @Test
    void tuChoiSatThuongChiMangMaxNhoHonMin() {
        assertThrows(IllegalArgumentException.class,
                () -> new GioiHanKhachHang(2.0f, 1.5f, 0f, 0.4f));
    }

    @Test
    void tuChoiGiamHoiChieuMinAm() {
        assertThrows(IllegalArgumentException.class,
                () -> new GioiHanKhachHang(1f, 2.5f, -0.01f, 0.4f));
    }

    @Test
    void tuChoiKhoangGiamHoiChieuNguoc() {
        assertThrows(IllegalArgumentException.class,
                () -> new GioiHanKhachHang(1f, 2.5f, 0.5f, 0.4f));
    }
}
