package com.lqm.strategy;

import com.lqm.combat.KeDich;
import com.lqm.combat.ViTri;
import com.lqm.test.KeDichGia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DichGanNhatTest {

    @Test
    void traNullKhiDanhSachRong() {
        DichGanNhat selector = new DichGanNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");

        KeDich ketQua = selector.chon(tanCong, List.of());

        assertNull(ketQua);
    }

    @Test
    void traEnemyDuyNhatKhiListMotPhanTu() {
        DichGanNhat selector = new DichGanNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");
        KeDich donLe = KeDichGia.o(new ViTri(5, 5), 100, "DonLe");

        KeDich ketQua = selector.chon(tanCong, List.of(donLe));

        assertSame(donLe, ketQua);
    }

    @Test
    void traEnemyGanNhatKhiNhieuEnemy() {
        DichGanNhat selector = new DichGanNhat();
        KeDich tanCong = KeDichGia.o(new ViTri(0, 0), 100, "TanCong");
        KeDich xa = KeDichGia.o(new ViTri(10, 0), 100, "Xa");
        KeDich gan = KeDichGia.o(new ViTri(3, 4), 100, "Gan");

        KeDich ketQua = selector.chon(tanCong, List.of(xa, gan));

        assertSame(gan, ketQua);
    }
}
