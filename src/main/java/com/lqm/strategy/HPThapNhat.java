package com.lqm.strategy;

import com.lqm.combat.KeDich;

import java.util.List;

public class HPThapNhat implements BoChonMucTieu {

    @Override
    public KeDich chon(KeDich tanCong, List<KeDich> keDichList) {
        if (keDichList.isEmpty()) {
            return null;
        }
        return BoChonNhoNhat.chonNhoNhat(keDichList, KeDich::layMauHienTai);
    }
}
