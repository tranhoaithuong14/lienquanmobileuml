package com.lqm.combat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThuocTinhChienDauTest {

    @Test
    void gaySatThuongGiamMauHienTai() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.gaySatThuong(30f);

        assertEquals(70f, stats.layMauHienTai());
        assertTrue(stats.conSongKhong());
    }

    @Test
    void gaySatThuongVuotMauKhongBiAm() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.gaySatThuong(150f);

        assertEquals(0f, stats.layMauHienTai());
        assertFalse(stats.conSongKhong());
    }

    @Test
    void gaySatThuongBangKhongThiChet() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.gaySatThuong(100f);

        assertFalse(stats.conSongKhong());
        assertEquals(0f, stats.layMauHienTai());
    }

    @Test
    void healTangMauHienTai() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.gaySatThuong(30f);
        stats.heal(20f);

        assertEquals(90f, stats.layMauHienTai());
        assertTrue(stats.conSongKhong());
    }

    @Test
    void healKhongVuotQuaMauToiDa() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.heal(50f);

        assertEquals(100f, stats.layMauHienTai());
    }

    @Test
    void healKhiChetLaKhongTacDung() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.gaySatThuong(100f);
        stats.heal(50f);

        assertEquals(0f, stats.layMauHienTai());
        assertFalse(stats.conSongKhong());
    }

    @Test
    void hoiSinhDayMauVaSongLai() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        stats.gaySatThuong(100f);
        stats.hoiSinh();

        assertEquals(100f, stats.layMauHienTai());
        assertTrue(stats.conSongKhong());
    }

    @Test
    void khoiTaoTuChoiMauKhongDuong() {
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhChienDau(0f));
        assertThrows(IllegalArgumentException.class, () -> new ThuocTinhChienDau(-1f));
    }

    @Test
    void gaySatThuongTuChoiLuongAm() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        assertThrows(IllegalArgumentException.class, () -> stats.gaySatThuong(-10f));
    }

    @Test
    void healTuChoiLuongAm() {
        ThuocTinhChienDau stats = new ThuocTinhChienDau(100f);

        assertThrows(IllegalArgumentException.class, () -> stats.heal(-10f));
    }
}
