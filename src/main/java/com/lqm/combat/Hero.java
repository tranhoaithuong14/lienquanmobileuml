package com.lqm.combat;

import com.lqm.strategy.TargetSelector;

import java.util.List;

/**
 * Hero trong Liên Quân Mobile — vừa là attacker (giữ TargetSelector), vừa là target (implement Enemy).
 *
 * Composition: Hero HAS-A CombatStats (HP state machine) thay vì tự quản lý HP fields.
 * CombatStats có thể reuse cho tower/creep/summon trong tương lai.
 *
 * HP methods (takeDamage, heal, respawn, isAlive, getCurrentHp) delegate sang stats.
 */
public class Hero implements Enemy {

    private final String name;
    private final Position position;
    private final CombatStats stats;
    private final TargetSelector targetSelector;

    public Hero(String name, Position position, float maxHp, TargetSelector targetSelector) {
        this.name = name;
        this.position = position;
        this.stats = new CombatStats(maxHp);
        this.targetSelector = targetSelector;
    }

    // ---- HP actions — delegate sang CombatStats ----

    public void takeDamage(float amount) {
        stats.takeDamage(amount);
    }

    public void heal(float amount) {
        stats.heal(amount);
    }

    public void respawn() {
        stats.respawn();
    }

    public boolean isAlive() {
        return stats.isAlive();
    }

    // ---- Targeting — dead Hero không chọn target ----

    public Enemy selectTarget(List<Enemy> enemies) {
        if (!stats.isAlive()) {
            return null;
        }
        return targetSelector.select(this, enemies);
    }

    // ---- Enemy interface ----

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public float getCurrentHp() {
        return stats.getCurrentHp();
    }

    @Override
    public String getName() {
        return name;
    }
}