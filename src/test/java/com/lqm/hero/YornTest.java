package com.lqm.hero;

import com.lqm.combat.TamDanh;
import com.lqm.combat.ThuocTinhGoc;
import com.lqm.combat.Tuong;
import com.lqm.combat.ViTri;
import com.lqm.strategy.HPThapNhat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YornTest {

    @Test
    void yornMauToiDaKhopSheet() {
        assertEquals(3582f, Yorn.thuocTinh().mauToiDa());
    }

    @Test
    void yornNangLuongToiDaKhopSheet() {
        assertEquals(440f, Yorn.thuocTinh().nangLuongToiDa());
    }

    @Test
    void yornCongVatLyKhopSheet() {
        assertEquals(174f, Yorn.thuocTinh().congVatLy());
    }

    @Test
    void yornCongPhepKhopSheet() {
        assertEquals(0f, Yorn.thuocTinh().congPhep());
    }

    @Test
    void yornGiapKhopSheet() {
        assertEquals(140f, Yorn.thuocTinh().giap());
    }

    @Test
    void yornGiapPhepKhopSheet() {
        assertEquals(80f, Yorn.thuocTinh().giapPhep());
    }

    @Test
    void yornTocChayKhopSheet() {
        assertEquals(360f, Yorn.thuocTinh().tocChay());
    }

    @Test
    void yornTocDanhCongKhopSheet() {
        assertEquals(0f, Yorn.thuocTinh().tocDanhCong());
    }

    @Test
    void yornXuyenGiapKhopSheet() {
        ThuocTinhGoc s = Yorn.thuocTinh();
        assertEquals(0f, s.xuyenGiap());
        assertEquals(0f, s.xuyenGiapPhanTram());
    }

    @Test
    void yornXuyenGiapPhepKhopSheet() {
        ThuocTinhGoc s = Yorn.thuocTinh();
        assertEquals(0f, s.xuyenGiapPhep());
        assertEquals(0f, s.xuyenGiapPhepPhanTram());
    }

    @Test
    void yornTyLeChiMangKhopSheet() {
        assertEquals(0f, Yorn.thuocTinh().tyLeChiMang());
    }

    @Test
    void yornSatThuongChiMangKhopSheet() {
        assertEquals(2.00f, Yorn.thuocTinh().satThuongChiMang());
    }

    @Test
    void yornHutMauKhopSheet() {
        assertEquals(0f, Yorn.thuocTinh().hutMau());
    }

    @Test
    void yornHutMauPhepKhopSheet() {
        assertEquals(0f, Yorn.thuocTinh().hutMauPhep());
    }

    @Test
    void yornGiamHoiChieuKhopSheet() {
        assertEquals(0f, Yorn.thuocTinh().giamHoiChieu());
    }

    @Test
    void yornTamDanhLaDanhXa() {
        assertEquals(TamDanh.DANHXHA, Yorn.thuocTinh().tamDanh());
    }

    @Test
    void yornTaoHopThanhPhanDung() {
        Tuong yorn = Yorn.tao();

        assertEquals(Yorn.TEN, yorn.layTen());
        assertEquals(Yorn.thuocTinh(), yorn.layThuocTinhGoc());
        assertEquals(3582f, yorn.layMauHienTai());
        assertEquals(new ViTri(0, 0), yorn.layViTri());
    }

    @Test
    void yornTaoChoPhepGhiDeViTriVaBoChon() {
        Tuong yorn = Yorn.tao(new ViTri(10, 20), new HPThapNhat());

        assertEquals(new ViTri(10, 20), yorn.layViTri());
    }
}
