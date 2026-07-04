package com.lqm.combat;

import com.lqm.strategy.BoChonMucTieu;

import java.util.List;

public class Tuong implements KeDich {

    private final String ten;
    private final ViTri viTri;
    private final ThuocTinhGoc thuocTinhGoc;
    private final ThuocTinhChienDau sinhLuc;
    private final BoChonMucTieu boChonMucTieu;

    public Tuong(String ten, ViTri viTri, ThuocTinhGoc thuocTinhGoc, BoChonMucTieu boChonMucTieu) {
        if (thuocTinhGoc == null) {
            throw new IllegalArgumentException("thuocTinhGoc khac null");
        }
        this.ten = ten;
        this.viTri = viTri;
        this.thuocTinhGoc = thuocTinhGoc;
        this.sinhLuc = new ThuocTinhChienDau(thuocTinhGoc.mauToiDa());
        this.boChonMucTieu = boChonMucTieu;
    }

    public ThuocTinhGoc layThuocTinhGoc() {
        return thuocTinhGoc;
    }

    public void gaySatThuong(float luong) {
        sinhLuc.gaySatThuong(luong);
    }

    public void heal(float luong) {
        sinhLuc.heal(luong);
    }

    public void hoiSinh() {
        sinhLuc.hoiSinh();
    }

    public boolean conSongKhong() {
        return sinhLuc.conSongKhong();
    }

    public KeDich chonMucTieu(List<KeDich> keDichList) {
        if (!sinhLuc.conSongKhong()) {
            return null;
        }
        return boChonMucTieu.chon(this, keDichList);
    }

    @Override
    public ViTri layViTri() {
        return viTri;
    }

    @Override
    public float layMauHienTai() {
        return sinhLuc.layMauHienTai();
    }

    @Override
    public String layTen() {
        return ten;
    }
}
