package com.lqm.hero;

import com.lqm.combat.AttackRange;
import com.lqm.combat.BaseStats;
import com.lqm.combat.Position;
import com.lqm.combat.Hero;
import com.lqm.strategy.LowestHP;
import com.lqm.strategy.TargetSelector;

/**
 * Fixture factory cho hero Yorn — dữ liệu lấy từ màn hình chi tiết tướng Liên Quân Mobile (VN client).
 *
 * <p>Đây là <b>factory dữ liệu tĩnh</b> (data fixture, không phải logic runtime). Tách ra để:
 * <ul>
 *   <li>Số liệu literal từ game (HP 3582, AD 174, ...) cố định một chỗ, dễ patch khi balance update.</li>
 *   <li>Catalog (Repository pattern, ADR-0003 R6) import factory này — test/benchmark so sánh với ground-truth game.</li>
 *   <li>Không tạo subclass per-hero (ADR-0001 đã cấm); thay vào đó {@code Hero(name="Yorn", stats=Yorn.stats(), ...)}.</li>
 * </ul>
 *
 * <p><b>Caveat:</b> số liệu giữa Liên Quân VN và Arena of Valor global client khác nhau do patch/version
 * (xem ADR-0003 R1 cross-check). File này là bản VN theo screenshot người dùng cung cấp.
 *
 * <p><b>Cách map UI → field — chia 2 nhóm theo màn hình "Chi tiết":</b>
 * <pre>
 *   ╔════════════════════════════════════════════════════════════════════╗
 *   ║  THUỘC TÍNH CƠ BẢN (BasicStats — 8 stat)                          ║
 *   ╠════════════════════════════════════════════════════════════════════╣
 *   ║   "Máu 3582"                  ───────►  maxHp = 3582f              ║
 *   ║   "Năng lượng tối đa 440"     ───────►  maxMana = 440f             ║
 *   ║   "Công vật lý 174"           ───────►  attackDamage = 174f        ║
 *   ║   "Công phép 0"               ───────►  abilityPower = 0f          ║
 *   ║   "Giáp 140 | 0%"             ───────►  armor = 140f               ║
 *   ║   "Giáp phép 80 | 0%"         ───────►  magicDefense = 80f         ║
 *   ║   "Tốc chạy 360.0"            ───────►  movementSpeed = 360f       ║
 *   ║   "Tốc đánh + 0%"             ───────►  attackSpeedBonus = 0f      ║
 *   ╠════════════════════════════════════════════════════════════════════╣
 *   ║  THUỘC TÍNH TẤN CÔNG (OffensiveStats — 9 stat)                    ║
 *   ╠════════════════════════════════════════════════════════════════════╣
 *   ║   "Xuyên giáp 0 | 0%"         ───────►  armorPenFlat=0f,          ║
 *   ║                                       armorPenPct=0f               ║
 *   ║   "Xuyên giáp phép 0"         ───────►  magicPenFlat=0f,          ║
 *   ║   "Xuyên giáp phép 0%"                   magicPenPct=0f             ║
 *   ║   "Tỷ lệ chí mạng 0%"         ───────►  critChance = 0f            ║
 *   ║   "Sát thương chí mạng 200%"  ───────►  critDamage = 2.00f         ║
 *   ║                                       (200% theo AoV base —         ║
 *   ║                                        cap 250% áp cho HoK)         ║
 *   ║   "Hút máu 0%"                ───────►  lifeSteal = 0f            ║
 *   ║   "Hút máu phép 0%"           ───────►  spellVamp = 0f            ║
 *   ║   "Giảm hồi chiêu 0%"         ───────►  cooldownReduction = 0f    ║
 *   ╠════════════════════════════════════════════════════════════════════╣
 *   ║  NGOÀI NHÓM (BaseStats flat)                                       ║
 *   ╠════════════════════════════════════════════════════════════════════╣
 *   ║   "Tầm đánh Đánh xa"          ───────►  attackRange =              ║
 *   ║                                       AttackRange.RANGED           ║
 *   ╚════════════════════════════════════════════════════════════════════╝
 * </pre>
 *
 * <p>Bonus% (cột thứ hai trong UI, vd "140 | 0%") <b>không</b> thuộc BaseStats — chúng đến từ item/arcana
 * và sẽ được tổng hợp qua Decorator/aggregation ở lớp trên (chưa implement). BaseStats chỉ giữ phần gốc
 * mà hero có khi chưa trang bị gì.
 */
public final class Yorn {

    /** Tên hiển thị trong game và dùng làm key trong HeroCatalog. */
    public static final String NAME = "Yorn";

    /**
     * Cache instance duy nhất của BaseStats của Yorn.
     *
     * <p>Đây là 18 chỉ số <b>gốc (base) của Yorn khi chưa trang bị gì</b>, lấy từ màn hình "Chi tiết"
     * Liên Quân Mobile (bản VN). Mỗi dòng {@code /* xxx *&#47;} chính là nhãn trên UI để tra cứu nhanh.
     *
     * <p>Tại sao là {@code record} và cache một lần (theo Bloch Item 17, Effective Java 3rd ed.):
     * <ul>
     *   <li>Bất biến (immutable) → chia sẻ giữa nhiều thread, không cần đồng bộ.</li>
     *   <li>Một instance duy nhất → {@code HeroCatalog} tra cứu theo identity (==) nhanh hơn equals.</li>
     *   <li>Tránh cấp phát record mới trên mỗi call — quan trọng cho benchmark/test lặp lại.</li>
     * </ul>
     */
    private static final BaseStats STATS = new BaseStats(
            /* maxHp           */ 3582f,             // UI: "Máu 3582"
            /* maxMana         */ 440f,              // UI: "Năng lượng tối đa 440"
            /* attackDamage    */ 174f,              // UI: "Công vật lý 174" (sát thương đánh tay)
            /* abilityPower    */ 0f,                // UI: "Công phép 0" (Yorn là AD carry, không dùng phép)
            /* armor           */ 140f,              // UI: "Giáp 140 | 0%" — chỉ phần base 140; cột 0% là bonus từ item
            /* magicDefense    */ 80f,               // UI: "Giáp phép 80 | 0%"
            /* movementSpeed   */ 360f,              // UI: "Tốc chạy 360.0"
            /* attackSpeedBonus*/ 0f,                // UI: "Tốc đánh + 0%" — phần base; buff từ item/passive cộng ở lớp trên
            /* armorPenFlat    */ 0f,                // UI: "Xuyên giáp 0" (flat — bớt X giáp cố định)
            /* armorPenPct     */ 0f,                // UI: "Xuyên giáp 0%" (phần trăm — xuyên % giáp đối phương)
            /* magicPenFlat    */ 0f,                // UI: "Xuyên giáp phép 0" (cùng khái niệm, áp cho phép)
            /* magicPenPct     */ 0f,                // UI: "Xuyên giáp phép 0%"
            /* critChance      */ 0f,                // UI: "Tỷ lệ chí mạng 0%" — Yorn chưa có crit từ base
            /* critDamage      */ 2.00f,             // UI: "Sát thương chí mạng 200%" — AoV base 2.0×, HoK cap 250%
            /* lifeSteal       */ 0f,                // UI: "Hút máu 0%" (chỉ từ đòn đánh thường/physical)
            /* spellVamp       */ 0f,                // UI: "Hút máu phép 0%" (chỉ từ skill phép)
            /* cooldownReduction*/ 0f,               // UI: "Giảm hồi chiêu 0%" — phần base; trang bị/arcana cộng dồn sau
            /* attackRange     */ AttackRange.RANGED // UI: "Tầm đánh Đánh xa" — RANGED (không phải MELEE cận chiến)
    );

    private Yorn() {}

    /**
     * Trả về BaseStats của Yorn từ screenshot Liên Quân VN.
     *
     * <p>Cấu trúc record đầy đủ 18 stat; giá trị nào screenshot cho {@code 0} thì giữ {@code 0f} —
     * biểu thị Yorn chưa có stat đó từ base. Phần bonus% (vd "140 | 0%") giữ ở dạng {@code 0.0f}
     * vì cộng dồn từ items/arcana sẽ là <b>Decorator/aggregation</b> ở lớp trên (chưa implement).
     *
     * <p>Luôn trả về <b>cùng một instance</b> (cache {@link #STATS}) — để {@code HeroCatalog} lookup
     * trùng identity và tránh cấp phát record mới mỗi call.
     *
     * @return BaseStats bất biến, share an toàn giữa các thread.
     */
    public static BaseStats stats() {
        return STATS;
    }

    /**
     * Tạo {@link Hero} Yorn mặc định ở vị trí gốc tọa độ {@code (0, 0)} với chiến thuật
     * {@link LowestHP} (ưu tiên đánh mục tiêu thấp máu nhất — phù hợp passive ám sát của Yorn).
     */
    public static Hero create() {
        return create(new Position(0, 0), new LowestHP());
    }

    /**
     * Tạo {@link Hero} Yorn với vị trí và chiến thuật chọn mục tiêu tùy chỉnh.
     *
     * @param position vị trí spawn trên bản đồ (tọa độ grid)
     * @param selector bộ chọn mục tiêu (vd {@link LowestHP}, first/last trong team địch)
     * @return Hero Yorn sẵn sàng đưa vào mô phỏng combat
     */
    public static Hero create(Position position, TargetSelector selector) {
        return new Hero(NAME, position, stats(), selector);
    }
}
