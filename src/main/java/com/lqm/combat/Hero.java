package com.lqm.combat;

import com.lqm.strategy.TargetSelector;

import java.util.List;

/**
 * Hero trong Liên Quân Mobile — vừa là attacker (giữ TargetSelector), vừa là target (implement Enemy).
 *
 * Composition (sau ADR-0003):
 * - BaseStats: sheet tĩnh bất biến (AD, armor, mana, attackRange...) — Value Object, immutable record.
 *   Hero KHÔNG copy từng field ra field riêng; chỉ giữ một reference đến record duy nhất.
 * - CombatStats: state machine cho vitals mutable (currentHp, active) — giữ nguyên ADR-0002.
 * - Position: tọa độ Euclidean.
 * - TargetSelector: Strategy chọn mục tiêu (ADR-0001).
 *
 * Tại sao tách BaseStats thay vì nhồi field vào Hero:
 * - SRP: Hero là Entity có identity & lifecycle (Evans DDD Ch.5); BaseStats là Value Object — hai lý do
 *   đổi khác nhau (balance patch đổi stats vs gameplay đổi identity/role).
 * - Bloch Item 17: immutable BaseStats shareable, thread-safe, equals/hashCode/toString miễn phí từ
 *   record; catalog nhiều Hero có thể cache cùng một BaseStats template.
 * - Hero phụ thuộc combat stats ở mức "có sheet", không truy cập từng stat — chừa chỗ cho
 *   DamageCalculator (tương lai) đọc trực tiếp BaseStats mà không qua Hero.
 */
public class Hero implements Enemy {

    private final String name;
    private final Position position;
    private final BaseStats baseStats;
    private final CombatStats vitals;
    private final TargetSelector targetSelector;

    public Hero(String name, Position position, BaseStats baseStats, TargetSelector targetSelector) {
        if (baseStats == null) {
            throw new IllegalArgumentException("baseStats must not be null");
        }
        this.name = name;
        this.position = position;
        this.baseStats = baseStats;
        this.vitals = new CombatStats(baseStats.maxHp());
        this.targetSelector = targetSelector;
    }

    public BaseStats getBaseStats() {
        return baseStats;
    }

    // ---- HP actions — delegate sang CombatStats (vitals state machine giữ nguyên ADR-0002) ----

    public void takeDamage(float amount) {
        vitals.takeDamage(amount);
    }

    public void heal(float amount) {
        vitals.heal(amount);
    }

    public void respawn() {
        vitals.respawn();
    }

    public boolean isAlive() {
        return vitals.isAlive();
    }

    // ---- Targeting — dead Hero không chọn target ----

    public Enemy selectTarget(List<Enemy> enemies) {
        if (!vitals.isAlive()) {
            return null;
        }
        return targetSelector.select(this, enemies);
    }

    // ---- Enemy interface — minimum surface cho TargetSelectors (ISP, ADR-0003 R5) ----

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public float getCurrentHp() {
        return vitals.getCurrentHp();
    }

    @Override
    public String getName() {
        return name;
    }
}
