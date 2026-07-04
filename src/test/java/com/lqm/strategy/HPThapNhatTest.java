package com.lqm.strategy;

import com.lqm.combat.KeDich;
import com.lqm.combat.ViTri;
import com.lqm.test.KeDichGia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class HPThapNhatTest {

    @Test
    void traNullKhiDanhSachRong() {
        HPThapNhat selector = new HPThapNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");

        KeDich ketQua = selector.chon(tanCong, List.of());

        assertNull(ketQua);
    }

    @Test
    void traEnemyDuyNhatKhiListMotPhanTu() {
        HPThapNhat selector = new HPThapNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");
        KeDich donLe = KeDichGia.o(new ViTri(0, 0), 50, "DonLe");

        KeDich ketQua = selector.chon(tanCong, List.of(donLe));

        assertSame(donLe, ketQua);
    }

    @Test
    void traEnemyHPThapNhatKhiNhieuEnemy() {
        HPThapNhat selector = new HPThapNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");
        KeDich khoe = KeDichGia.o(new ViTri(5, 0), 100, "Khoe");
        KeDich thuong = KeDichGia.o(new ViTri(5, 0), 30, "Thuong");

        KeDich ketQua = selector.chon(tanCong, List.of(khoe, thuong));

        assertSame(thuong, ketQua);
    }

    @Test
    void soSanhHPThapPhanSoChonDungCaiThapHon() {
        HPThapNhat selector = new HPThapNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");
        KeDich a = KeDichGia.o(new ViTri(5, 0), 99.6f, "A");
        KeDich b = KeDichGia.o(new ViTri(5, 0), 99.4f, "B");

        KeDich ketQua = selector.chon(tanCong, List.of(a, b));

        assertSame(b, ketQua, "99.4f < 99.6f — phai chon B bat ke thu tu list");
    }
}
