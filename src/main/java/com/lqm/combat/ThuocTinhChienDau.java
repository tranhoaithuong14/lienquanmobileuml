package com.lqm.combat;

public class ThuocTinhChienDau {

    private final float mauToiDa;
    private float mauHienTai;
    private boolean conSong;

    public ThuocTinhChienDau(float mauToiDa) {
        if (mauToiDa <= 0) {
            throw new IllegalArgumentException("mauToiDa phai > 0, nhan " + mauToiDa);
        }
        this.mauToiDa = mauToiDa;
        this.mauHienTai = mauToiDa;
        this.conSong = true;
    }

    public void gaySatThuong(float luong) {
        if (luong < 0) {
            throw new IllegalArgumentException("gaySatThuong phai >= 0, nhan " + luong);
        }
        mauHienTai = Math.max(0f, mauHienTai - luong);
        if (mauHienTai <= 0f) {
            conSong = false;
        }
    }

    public void heal(float luong) {
        if (luong < 0) {
            throw new IllegalArgumentException("heal phai >= 0, nhan " + luong);
        }
        if (!conSong) {
            return;
        }
        mauHienTai = Math.min(mauToiDa, mauHienTai + luong);
    }

    public void hoiSinh() {
        conSong = true;
        mauHienTai = mauToiDa;
    }

    public boolean conSongKhong() {
        return conSong;
    }

    public float layMauHienTai() {
        return mauHienTai;
    }

    public float layMauToiDa() {
        return mauToiDa;
    }
}
