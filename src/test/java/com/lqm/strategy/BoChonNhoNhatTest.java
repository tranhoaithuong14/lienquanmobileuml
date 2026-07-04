package com.lqm.strategy;

import com.lqm.combat.KeDich;
import com.lqm.combat.ViTri;
import com.lqm.test.KeDichGia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

class BoChonNhoNhatTest {

    @Test
    void chonEnemyHPThapNhat() {
        KeDich khoe = KeDichGia.o(new ViTri(0, 0), 100, "Khoe");
        KeDich thuong = KeDichGia.o(new ViTri(0, 0), 30, "Thuong");
        KeDich sapChet = KeDichGia.o(new ViTri(0, 0), 5, "SapChet");

        KeDich ketQua = BoChonNhoNhat.chonNhoNhat(List.of(khoe, thuong, sapChet), KeDich::layMauHienTai);

        assertSame(sapChet, ketQua);
    }

    @Test
    void cungDiemThiPhanTuDauTienThang() {
        KeDich a = KeDichGia.o(new ViTri(0, 0), 50, "A");
        KeDich b = KeDichGia.o(new ViTri(0, 0), 50, "B");

        KeDich ketQua = BoChonNhoNhat.chonNhoNhat(List.of(a, b), KeDich::layMauHienTai);

        assertSame(a, ketQua, "Cung diem → phan tu dau tien thang");
    }

    @Test
    void listMotPhanTuTraVePhanTuDo() {
        KeDich donLe = KeDichGia.o(new ViTri(0, 0), 42, "DonLe");

        KeDich ketQua = BoChonNhoNhat.chonNhoNhat(List.of(donLe), KeDich::layMauHienTai);

        assertSame(donLe, ketQua);
    }
}
