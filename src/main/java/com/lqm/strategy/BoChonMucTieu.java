package com.lqm.strategy;

import com.lqm.combat.KeDich;
import java.util.List;

public interface BoChonMucTieu {
    KeDich chon(KeDich tanCong, List<KeDich> keDichList);
}
