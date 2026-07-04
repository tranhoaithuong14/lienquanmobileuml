package com.lqm.combat;

public record ThuocTinhGoc(
        ThuocTinhCoBan coBan,
        ThuocTinhTanCong tanCong,
        TamDanh tamDanh,
        GioiHanKhachHang gioiHan
) {

    public ThuocTinhGoc {
        if (tamDanh == null) {
            throw new IllegalArgumentException("tamDanh khac null");
        }
    }

    public ThuocTinhGoc(
            float mauToiDa,
            float congVatLy,
            float congPhep,
            float giap,
            float giapPhep,
            float nangLuongToiDa,
            float tocChay,
            float xuyenGiap,
            float xuyenGiapPhep,
            float tocDanhCong,
            float tyLeChiMang,
            float satThuongChiMang,
            float hutMau,
            float hutMauPhep,
            float giamHoiChieu,
            float xuyenGiapPhanTram,
            float xuyenGiapPhepPhanTram,
            TamDanh tamDanh
    ) {
        this(
                new ThuocTinhCoBan(mauToiDa, congVatLy, congPhep, giap, giapPhep, nangLuongToiDa),
                new ThuocTinhTanCong(tocChay, xuyenGiap, xuyenGiapPhep,
                        tocDanhCong, tyLeChiMang, satThuongChiMang,
                        hutMau, hutMauPhep, giamHoiChieu,
                        xuyenGiapPhanTram, xuyenGiapPhepPhanTram, GioiHanKhachHang.LQMN),
                tamDanh,
                GioiHanKhachHang.LQMN);
    }

    public ThuocTinhGoc(
            float mauToiDa,
            float congVatLy,
            float congPhep,
            float giap,
            float giapPhep,
            float nangLuongToiDa,
            float tocChay,
            float xuyenGiap,
            float xuyenGiapPhep,
            float tocDanhCong,
            float tyLeChiMang,
            float satThuongChiMang,
            float hutMau,
            float hutMauPhep,
            float giamHoiChieu,
            float xuyenGiapPhanTram,
            float xuyenGiapPhepPhanTram,
            TamDanh tamDanh,
            GioiHanKhachHang gioiHan
    ) {
        this(
                new ThuocTinhCoBan(mauToiDa, congVatLy, congPhep, giap, giapPhep, nangLuongToiDa),
                new ThuocTinhTanCong(tocChay, xuyenGiap, xuyenGiapPhep,
                        tocDanhCong, tyLeChiMang, satThuongChiMang,
                        hutMau, hutMauPhep, giamHoiChieu,
                        xuyenGiapPhanTram, xuyenGiapPhepPhanTram, gioiHan),
                tamDanh,
                gioiHan);
    }

    /* shortcut — delegate xuong coBan/tanCong */

    public float mauToiDa() { return coBan.mauToiDa(); }
    public float congVatLy() { return coBan.congVatLy(); }
    public float congPhep() { return coBan.congPhep(); }
    public float giap() { return coBan.giap(); }
    public float giapPhep() { return coBan.giapPhep(); }
    public float nangLuongToiDa() { return coBan.nangLuongToiDa(); }

    public float tocChay() { return tanCong.tocChay(); }
    public float xuyenGiap() { return tanCong.xuyenGiap(); }
    public float xuyenGiapPhep() { return tanCong.xuyenGiapPhep(); }
    public float tocDanhCong() { return tanCong.tocDanhCong(); }
    public float tyLeChiMang() { return tanCong.tyLeChiMang(); }
    public float satThuongChiMang() { return tanCong.satThuongChiMang(); }
    public float hutMau() { return tanCong.hutMau(); }
    public float hutMauPhep() { return tanCong.hutMauPhep(); }
    public float giamHoiChieu() { return tanCong.giamHoiChieu(); }
    public float xuyenGiapPhanTram() { return tanCong.xuyenGiapPhanTram(); }
    public float xuyenGiapPhepPhanTram() { return tanCong.xuyenGiapPhepPhanTram(); }
}
