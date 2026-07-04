package com.lqm.strategy;

import java.util.List;
import java.util.function.ToDoubleFunction;

public final class BoChonNhoNhat {

    private BoChonNhoNhat() {}

    public static <T> T chonNhoNhat(List<T> items, ToDoubleFunction<T> scorer) {
        T totNhat = items.get(0);
        double diemTotNhat = scorer.applyAsDouble(totNhat);
        for (int i = 1; i < items.size(); i++) {
            T ungVien = items.get(i);
            double diem = scorer.applyAsDouble(ungVien);
            if (diem < diemTotNhat) {
                totNhat = ungVien;
                diemTotNhat = diem;
            }
        }
        return totNhat;
    }
}
