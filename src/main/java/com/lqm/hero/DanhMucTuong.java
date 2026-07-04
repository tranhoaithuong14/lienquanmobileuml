package com.lqm.hero;

import com.lqm.combat.ThuocTinhGoc;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class DanhMucTuong {

    private static final Map<String, ThuocTinhGoc> MAU = Map.of(
            Yorn.TEN, Yorn.thuocTinh()
    );

    private DanhMucTuong() {}

    public static Optional<ThuocTinhGoc> tim(String ten) {
        Objects.requireNonNull(ten, "ten khac null");
        return Optional.ofNullable(MAU.get(ten));
    }

    public static ThuocTinhGoc lay(String ten) {
        return tim(ten).orElseThrow(() ->
                new IllegalArgumentException("khong biet tuong: " + ten));
    }

    public static Set<String> danhSachTen() {
        return MAU.keySet();
    }
}
