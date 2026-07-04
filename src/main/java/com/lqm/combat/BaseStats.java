package com.lqm.combat;

/**
 * Base stats bất biến của Hero — Value Object cho mọi thông số tĩnh hiển thị trên màn hình chi tiết tướng
 * Liên Quân Mobile (vn: "Thuộc tính cơ bản", "Thuộc tính tấn công").
 *
 * <p><b>Cấu trúc composed (Evans, DDD Ch.5 — Value Object cohesion):</b>
 * <pre>
 *   BaseStats                          (record)
 *     ├─ BasicStats    "Thuộc tính cơ bản"    (8 stats — vitals + combat profile)
 *     │     maxHp, maxMana, attackDamage, abilityPower,
 *     │     armor, magicDefense, movementSpeed, attackSpeedBonus
 *     ├─ OffensiveStats "Thuộc tính tấn công" (9 stats — modifiers: pen, crit, vamp, CDR)
 *     │     armorPenFlat/Pct, magicPenFlat/Pct,
 *     │     critChance, critDamage, lifeSteal, spellVamp, cooldownReduction, +caps
 *     ├─ attackRange                            (enum: MELEE / RANGED — Tầm đánh)
 *     └─ caps                                   (GameClientCaps — áp cho cả 2 nhóm)
 * </pre>
 *
 * <p>Tách theo nhóm UI giúp:
 * <ul>
 *   <li>Validation đặt cạnh field liên quan — đọc dễ hơn 18 điều kiện trộn lẫn.</li>
 *   <li>Consumer cần chỉ "vitals" thì đọc {@code stats.basic()} thay vì truyền nguyên {@code BaseStats}
 *       (vd {@code DamageCalculator} tương lai).</li>
 *   <li>Refactor an toàn — nếu sau này muốn tách {@code SurvivalStats} (hồi máu/mana) thì chỉ
 *       động vào nhóm {@code BasicStats}, không phá {@code BaseStats} interface.</li>
 * </ul>
 *
 * <p><b>Backward-compat flat API:</b> 18 getter cũ ({@code maxHp()}, {@code armor()},
 * {@code critDamage()}, ...) vẫn tồn tại và delegate xuống {@link BasicStats} / {@link OffensiveStats}.
 * Lý do:
 * <ul>
 *   <li>Không phải sửa 18 assertion của {@code YornTest} / {@code HeroTest} khi chuyển composed.</li>
 *   <li>Yorn fixture factory có thể giữ constructor positional 18-arg quen thuộc.</li>
 *   <li>Code consumer hiện tại (vd {@code Hero.getBaseStats().maxHp()}) không phải thay đổi.</li>
 * </ul>
 *
 * <p>Caps (giới hạn critDamage, cooldownReduction) KHÔNG hard-code ở record — chúng là DATA theo balance
 * patch, inject qua {@link GameClientCaps} (AoV vs HoK differ — ADR-0003 R7).
 *
 * <p>Derived stats (effective armor sau pen, effective HP, attacks/sec) KHÔNG lưu — tính on-demand
 * ở lớp consumer (DamageCalculator — chưa tồn tại, deferred).
 */
public record BaseStats(
        BasicStats basic,
        OffensiveStats offensive,
        AttackRange attackRange,
        GameClientCaps caps
) {

    /**
     * Primary constructor — composed. Validation phân tán: {@link BasicStats} tự validate
     * "thuộc tính cơ bản", {@link OffensiveStats} tự validate "thuộc tính tấn công" theo caps.
     * Constructor này chỉ check thứ thuộc về whole-record: range + caps non-null,
     * và invariant giữa 2 nhóm với caps của {@code BaseStats} (caps phải đồng nhất với
     * {@code OffensiveStats.caps}).
     */
    public BaseStats {
        if (caps == null) {
            throw new IllegalArgumentException("caps must not be null (use GameClientCaps.AOV / .HOK)");
        }
        if (basic == null) {
            throw new IllegalArgumentException("basic (Thuộc tính cơ bản) must not be null");
        }
        if (offensive == null) {
            throw new IllegalArgumentException("offensive (Thuộc tính tấn công) must not be null");
        }
        if (attackRange == null) {
            throw new IllegalArgumentException("attackRange must not be null");
        }
    }

    /**
     * Flat overload (backward-compat) — 18 stat + AttackRange, default caps = AoV.
     * Tự build {@link BasicStats} + {@link OffensiveStats} rồi delegate xuống primary.
     * Yorn fixture và helper test dùng overload này.
     */
    public BaseStats(
            float maxHp,
            float maxMana,
            float attackDamage,
            float abilityPower,
            float armor,
            float magicDefense,
            float movementSpeed,
            float attackSpeedBonus,
            float armorPenFlat,
            float armorPenPct,
            float magicPenFlat,
            float magicPenPct,
            float critChance,
            float critDamage,
            float lifeSteal,
            float spellVamp,
            float cooldownReduction,
            AttackRange attackRange
    ) {
        this(
                new BasicStats(maxHp, maxMana, attackDamage, abilityPower,
                        armor, magicDefense, movementSpeed, attackSpeedBonus),
                new OffensiveStats(armorPenFlat, armorPenPct, magicPenFlat, magicPenPct,
                        critChance, critDamage, lifeSteal, spellVamp,
                        cooldownReduction, GameClientCaps.AOV),
                attackRange,
                GameClientCaps.AOV);
    }

    /**
     * Flat overload (backward-compat, explicit caps) — 18 stat + AttackRange + caps rõ ràng.
     */
    public BaseStats(
            float maxHp,
            float maxMana,
            float attackDamage,
            float abilityPower,
            float armor,
            float magicDefense,
            float movementSpeed,
            float attackSpeedBonus,
            float armorPenFlat,
            float armorPenPct,
            float magicPenFlat,
            float magicPenPct,
            float critChance,
            float critDamage,
            float lifeSteal,
            float spellVamp,
            float cooldownReduction,
            AttackRange attackRange,
            GameClientCaps caps
    ) {
        this(
                new BasicStats(maxHp, maxMana, attackDamage, abilityPower,
                        armor, magicDefense, movementSpeed, attackSpeedBonus),
                new OffensiveStats(armorPenFlat, armorPenPct, magicPenFlat, magicPenPct,
                        critChance, critDamage, lifeSteal, spellVamp,
                        cooldownReduction, caps),
                attackRange,
                caps);
    }

    /* ──────────────────────────────────────────────────────────────────────────────
     * Flat accessors — backward-compat với code hiện tại (vd YornTest.
     * assertEquals(3582f, Yorn.stats().maxHp())). Delegate xuống basic()/offensive().
     * ──────────────────────────────────────────────────────────────────────────── */

    /** UI "Máu". */
    public float maxHp() { return basic.maxHp(); }

    /** UI "Năng lượng tối đa". */
    public float maxMana() { return basic.maxMana(); }

    /** UI "Công vật lý". */
    public float attackDamage() { return basic.attackDamage(); }

    /** UI "Công phép". */
    public float abilityPower() { return basic.abilityPower(); }

    /** UI "Giáp" (phần base). */
    public float armor() { return basic.armor(); }

    /** UI "Giáp phép" (phần base). */
    public float magicDefense() { return basic.magicDefense(); }

    /** UI "Tốc chạy". */
    public float movementSpeed() { return basic.movementSpeed(); }

    /** UI "Tốc đánh +%". */
    public float attackSpeedBonus() { return basic.attackSpeedBonus(); }

    /** UI "Xuyên giáp" (flat). */
    public float armorPenFlat() { return offensive.armorPenFlat(); }

    /** UI "Xuyên giáp" (%). */
    public float armorPenPct() { return offensive.armorPenPct(); }

    /** UI "Xuyên giáp phép" (flat). */
    public float magicPenFlat() { return offensive.magicPenFlat(); }

    /** UI "Xuyên giáp phép" (%). */
    public float magicPenPct() { return offensive.magicPenPct(); }

    /** UI "Tỷ lệ chí mạng". */
    public float critChance() { return offensive.critChance(); }

    /** UI "Sát thương chí mạng" (200% = 2.00). */
    public float critDamage() { return offensive.critDamage(); }

    /** UI "Hút máu". */
    public float lifeSteal() { return offensive.lifeSteal(); }

    /** UI "Hút máu phép". */
    public float spellVamp() { return offensive.spellVamp(); }

    /** UI "Giảm hồi chiêu". */
    public float cooldownReduction() { return offensive.cooldownReduction(); }
}
