package com.lqm.combat;

public record GioiHanKhachHang(
        float satThuongChiMangMin,
        float satThuongChiMangMax,
        float giamHoiChieuMin,
        float giamHoiChieuMax
) {

    public static final GioiHanKhachHang LQMN = new GioiHanKhachHang(
            1.00f,
            Float.POSITIVE_INFINITY,
            0.00f,
            0.40f
    );

    public static final GioiHanKhachHang HVK = new GioiHanKhachHang(
            1.00f,
            2.50f,
            0.00f,
            0.40f
    );

    public GioiHanKhachHang {
        if (satThuongChiMangMin < 1.0f) {
            throw new IllegalArgumentException("satThuongChiMangMin phai >= 1.0, nhan " + satThuongChiMangMin);
        }
        if (satThuongChiMangMax < satThuongChiMangMin) {
            throw new IllegalArgumentException("satThuongChiMangMax phai >= satThuongChiMangMin");
        }
        if (giamHoiChieuMin < 0f || giamHoiChieuMax < giamHoiChieuMin) {
            throw new IllegalArgumentException("giamHoiChieu phai trong [min, max] voi min >= 0");
        }
    }
}
