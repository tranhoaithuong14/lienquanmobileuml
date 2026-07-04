package com.lqm.combat;

public record ThuocTinhTanCong(
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
        GioiHanKhachHang gioiHan
) {

    public static ThuocTinhTanCong cua(
            float tocChay,
            float xuyenGiap, float xuyenGiapPhep,
            float tocDanhCong,
            float tyLeChiMang, float satThuongChiMang,
            float hutMau, float hutMauPhep,
            float giamHoiChieu,
            float xuyenGiapPhanTram, float xuyenGiapPhepPhanTram) {
        return new ThuocTinhTanCong(tocChay, xuyenGiap, xuyenGiapPhep,
                tocDanhCong, tyLeChiMang, satThuongChiMang,
                hutMau, hutMauPhep, giamHoiChieu,
                xuyenGiapPhanTram, xuyenGiapPhepPhanTram, GioiHanKhachHang.LQMN);
    }

    public ThuocTinhTanCong {
        if (gioiHan == null) {
            throw new IllegalArgumentException("gioiHan khac null (dung GioiHanKhachHang.LQMN / .HVK)");
        }
        if (tocChay <= 0f) {
            throw new IllegalArgumentException("tocChay phai > 0, nhan " + tocChay);
        }
        if (tocDanhCong < 0f) {
            throw new IllegalArgumentException("tocDanhCong phai >= 0, nhan " + tocDanhCong);
        }
        if (xuyenGiap < 0f || xuyenGiapPhanTram < 0f
                || xuyenGiapPhep < 0f || xuyenGiapPhepPhanTram < 0f) {
            throw new IllegalArgumentException("cac chi so xuyen giap phai >= 0");
        }
        if (hutMau < 0f || hutMauPhep < 0f) {
            throw new IllegalArgumentException("hutMau/hutMauPhep phai >= 0");
        }
        if (tyLeChiMang < 0f || tyLeChiMang > 1f) {
            throw new IllegalArgumentException("tyLeChiMang phai trong [0, 1], nhan " + tyLeChiMang);
        }
        if (satThuongChiMang < gioiHan.satThuongChiMangMin() || satThuongChiMang > gioiHan.satThuongChiMangMax()) {
            throw new IllegalArgumentException(
                    "satThuongChiMang phai trong [" + gioiHan.satThuongChiMangMin() + ", " + gioiHan.satThuongChiMangMax() + "] theo gioiHan, nhan " + satThuongChiMang);
        }
        if (giamHoiChieu < gioiHan.giamHoiChieuMin() || giamHoiChieu > gioiHan.giamHoiChieuMax()) {
            throw new IllegalArgumentException(
                    "giamHoiChieu phai trong [" + gioiHan.giamHoiChieuMin() + ", " + gioiHan.giamHoiChieuMax() + "] theo gioiHan, nhan " + giamHoiChieu);
        }
    }
}
