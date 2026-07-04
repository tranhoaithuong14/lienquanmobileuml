package com.lqm.combat;

import com.lqm.strategy.BoChonMucTieu;
import com.lqm.strategy.DichGanNhat;
import com.lqm.test.KeDichGia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class TuongTest {

    private static ThuocTinhGoc thuocTinhToiThieu(float mauToiDa) {
        return new ThuocTinhGoc(
                mauToiDa, 0f, 0f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f,
                TamDanh.CANCHIEN);
    }

    @Test
    void chonMucTieuRongTraVeNull() {
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), thuocTinhToiThieu(100f), new DichGanNhat());

        KeDich ketQua = tuong.chonMucTieu(List.of());

        assertNull(ketQua);
    }

    @Test
    void tuongChetKhongChonMucTieu() {
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), thuocTinhToiThieu(100f), new DichGanNhat());
        KeDich a = KeDichGia.o(new ViTri(5, 0), 100, "A");

        tuong.gaySatThuong(100f);

        assertNull(tuong.chonMucTieu(List.of(a)));
    }

    @Test
    void layMauHienTaiUyQuyenSangThuocTinhChienDau() {
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), thuocTinhToiThieu(100f), new DichGanNhat());

        tuong.gaySatThuong(25f);

        assertEquals(75f, tuong.layMauHienTai());
    }

    @Test
    void gaySatThuongUyQuyenSangThuocTinhChienDau() {
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), thuocTinhToiThieu(100f), new DichGanNhat());

        tuong.gaySatThuong(40f);

        assertEquals(60f, tuong.layMauHienTai());
    }

    @Test
    void tuongSongChonMucTieuTheoStrategy() {
        BoChonMucTieu luonChonDauTien = (tanCong, ds) -> ds.isEmpty() ? null : ds.get(0);
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), thuocTinhToiThieu(100f), luonChonDauTien);
        KeDich a = KeDichGia.o(new ViTri(5, 0), 100, "A");

        KeDich ketQua = tuong.chonMucTieu(List.of(a));

        assertEquals(a, ketQua);
    }

    @Test
    void layThuocTinhGocGiuNguyenThamChieu() {
        ThuocTinhGoc stats = thuocTinhToiThieu(100f);
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), stats, new DichGanNhat());

        assertSame(stats, tuong.layThuocTinhGoc());
    }

    @Test
    void mauHienTaiKhoiTaoTuThuocTinhGocMauToiDa() {
        Tuong tuong = new Tuong("Yena", new ViTri(0, 0), thuocTinhToiThieu(250f), new DichGanNhat());

        assertEquals(250f, tuong.layMauHienTai());
    }

    @Test
    void khoiTaoTuChoiThuocTinhGocNull() {
        try {
            new Tuong("Yena", new ViTri(0, 0), null, new DichGanNhat());
            org.junit.jupiter.api.Assertions.fail("mong doi IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            assertNotNull(expected.getMessage());
        }
    }
}
