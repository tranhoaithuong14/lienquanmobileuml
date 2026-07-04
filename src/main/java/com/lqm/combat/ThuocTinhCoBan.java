package com.lqm.combat;

public record ThuocTinhCoBan(
        float mauToiDa,
        float congVatLy,
        float congPhep,
        float giap,
        float giapPhep,
        float nangLuongToiDa
) {

    public static ThuocTinhCoBan cua(
            float mauToiDa,
            float congVatLy,
            float congPhep,
            float giap,
            float giapPhep,
            float nangLuongToiDa) {
        return new ThuocTinhCoBan(mauToiDa, congVatLy, congPhep, giap, giapPhep, nangLuongToiDa);
    }

    public ThuocTinhCoBan {
        if (mauToiDa <= 0f) {
            throw new IllegalArgumentException("mauToiDa phai > 0, nhan " + mauToiDa);
        }
        if (nangLuongToiDa < 0f) {
            throw new IllegalArgumentException("nangLuongToiDa phai >= 0, nhan " + nangLuongToiDa);
        }
        if (giap < 0f || giapPhep < 0f) {
            throw new IllegalArgumentException("giap/giapPhep phai >= 0, nhan giap=" + giap + " giapPhep=" + giapPhep);
        }
    }
}
