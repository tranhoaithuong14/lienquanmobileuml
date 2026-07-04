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
 *   ╔════════════════════════════════════════════════════════════════════════╗
 *   ║  THUỘC TÍNH CƠ BẢN (BasicStats — 6 stat, 2 hàng × 3 ô)               ║
 *   ╠════════════════════════════════════════════════════════════════════════╣
 *   ║   Hàng 1:                                                              ║
 *   ║     "Máu 3582"               ───────►  maxHp = 3582f                  ║
 *   ║     "Công vật lý 174"        ───────►  attackDamage = 174f             ║
 *   ║     "Công phép 0"            ───────►  abilityPower = 0f               ║
 *   ║   Hàng 2:                                                              ║
 *   ║     "Giáp 140 | 0%"          ───────►  armor = 140f                    ║
 *   ║     "Giáp phép 80 | 0%"      ───────►  magicDefense = 80f              ║
 *   ║     "Năng lượng tối đa 440"  ───────►  maxMana = 440f                  ║
 *   ╠════════════════════════════════════════════════════════════════════════╣
 *   ║  THUỘC TÍNH TẤN CÔNG (OffensiveStats — 11 stat, 3 hàng × 3 ô)         ║
 *   ╠════════════════════════════════════════════════════════════════════════╣
 *   ║   Hàng 1:                                                              ║
 *   ║     "Tốc chạy 360.0"         ───────►  movementSpeed = 360f            ║
 *   ║     "Xuyên giáp 0 | 0%"      ───────►  armorPenFlat=0f,               ║
 *   ║                                      armorPenPct=0f                   ║
 *   ║     "Xuyên giáp phép 0 | 0%" ───────►  magicPenFlat=0f,               ║
 *   ║                                      magicPenPct=0f                   ║
 *   ║   Hàng 2:                                                              ║
 *   ║     "Tốc đánh + 0%"          ───────►  attackSpeedBonus = 0f          ║
 *   ║     "Tỷ lệ chí mạng 0%"      ───────►  critChance = 0f                ║
 *   ║     "Sát thương chí mạng"    ───────►  critDamage = 2.00f              ║
 *   ║     "200%"                                (200% theo AoV base —         ║
 *   ║                                           cap 250% áp cho HoK)        ║
 *   ║   Hàng 3:                                                              ║
 *   ║     "Hút máu 0%"             ───────►  lifeSteal = 0f                 ║
 *   ║     "Hút máu phép 0%"        ───────►  spellVamp = 0f                 ║
 *   ║     "Giảm hồi chiêu 0%"      ───────►  cooldownReduction = 0f         ║
 *   ╠════════════════════════════════════════════════════════════════════════╣
 *   ║  NGOÀI NHÓM (BaseStats flat)                                          ║
 *   ╠════════════════════════════════════════════════════════════════════════╣
 *   ║     "Tầm đánh Đánh xa"       ───────►  attackRange =                  ║
 *   ║                                      AttackRange.RANGED                ║
 *   ╚════════════════════════════════════════════════════════════════════════╝
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
     * <p>Đây là 17 chỉ số <b>gốc (base) của Yorn khi chưa trang bị gì</b>, lấy từ màn hình "Chi tiết"
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
            /* maxHp           */ 3582f,             // UI hàng 1 trái:   "Máu 3582"
            /* attackDamage    */ 174f,              // UI hàng 1 giữa:  "Công vật lý 174" (sát thương đánh tay)
            /* abilityPower    */ 0f,                // UI hàng 1 phải:  "Công phép 0" (Yorn AD carry, không dùng phép)
            /* armor           */ 140f,              // UI hàng 2 trái:  "Giáp 140 | 0%" — chỉ phần base 140
            /* magicDefense    */ 80f,               // UI hàng 2 giữa:  "Giáp phép 80 | 0%"
            /* maxMana         */ 440f,              // UI hàng 2 phải:  "Năng lượng tối đa 440"
            /* movementSpeed   */ 360f,              // UI tấn công h1 trái: "Tốc chạy 360.0"
            /* armorPenFlat    */ 0f,                // UI tấn công h1 giữa: "Xuyên giáp 0" (flat)
            /* magicPenFlat    */ 0f,                // UI tấn công h1 phải: "Xuyên giáp phép 0" (flat)
            /* attackSpeedBonus*/ 0f,                // UI tấn công h2 trái: "Tốc đánh + 0%" — base
            /* critChance      */ 0f,                // UI tấn công h2 giữa: "Tỷ lệ chí mạng 0%" — Yorn chưa có crit base
            /* critDamage      */ 2.00f,             // UI tấn công h2 phải: "Sát thương chí mạng 200%" — AoV 2.0×, HoK cap 250%
            /* lifeSteal       */ 0f,                // UI tấn công h3 trái: "Hút máu 0%" (đòn đánh thường)
            /* spellVamp       */ 0f,                // UI tấn công h3 giữa: "Hút máu phép 0%" (skill phép)
            /* cooldownReduction*/ 0f,               // UI tấn công h3 phải: "Giảm hồi chiêu 0%" — base
            /* armorPenPct     */ 0f,                // (Pen % ở h1 giữa "Xuyên giáp 0%" — cặp với armorPenFlat)
            /* magicPenPct     */ 0f,                // (Pen phép % ở h1 phải "Xuyên giáp phép 0%")
            /* attackRange     */ AttackRange.RANGED // UI dưới cùng: "Tầm đánh Đánh xa" — RANGED
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
