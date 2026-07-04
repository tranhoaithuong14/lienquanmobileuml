package com.lqm.hero;

import com.lqm.combat.ThuocTinhGoc;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DanhMucTuongTest {

    @Test
    void timYornTheoTen() {
        Optional<ThuocTinhGoc> stats = DanhMucTuong.tim(Yorn.TEN);

        assertTrue(stats.isPresent());
        assertSame(Yorn.thuocTinh(), stats.get());
    }

    @Test
    void timRongChoTenKhongTonTai() {
        Optional<ThuocTinhGoc> stats = DanhMucTuong.tim("KhongPhaiTuong");

        assertFalse(stats.isPresent());
    }

    @Test
    void timPhanBietHoaThuong() {
        assertFalse(DanhMucTuong.tim("yorn").isPresent());
        assertFalse(DanhMucTuong.tim("YORN").isPresent());
        assertTrue(DanhMucTuong.tim(Yorn.TEN).isPresent());
    }

    @Test
    void layChoTuongDaBiet() {
        ThuocTinhGoc stats = DanhMucTuong.lay(Yorn.TEN);

        assertSame(Yorn.thuocTinh(), stats);
    }

    @Test
    void layNemNgoaiLeChoTuongKhongBiet() {
        assertThrows(IllegalArgumentException.class, () -> DanhMucTuong.lay("KhongPhaiTuong"));
    }

    @Test
    void layTuChoiTenNull() {
        assertThrows(NullPointerException.class, () -> DanhMucTuong.lay(null));
    }

    @Test
    void timTuChoiTenNull() {
        assertThrows(NullPointerException.class, () -> DanhMucTuong.tim(null));
    }

    @Test
    void danhSachTenChuaYorn() {
        assertTrue(DanhMucTuong.danhSachTen().contains(Yorn.TEN));
    }

    @Test
    void danhSachTenKhongTheSua() {
        assertThrows(UnsupportedOperationException.class,
                () -> DanhMucTuong.danhSachTen().add("Foo"));
    }
}
