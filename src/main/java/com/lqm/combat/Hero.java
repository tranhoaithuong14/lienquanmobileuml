package com.lqm.combat;

import com.lqm.strategy.TargetSelector;

import java.util.List;

/**
 * Context trong Strategy pattern + combat module với HP lifecycle.
 *
 * HP semantics:
 * - float currentHp, floor 0, ceiling maxHp
 * - boolean active: false khi currentHp ≤ 0 (chết), true khi respawn
 * - takeDamage(amount): amount ≥ 0, giảm currentHp, clamp tại 0, set active=false nếu HP ≤ 0
 * - heal(amount): amount ≥ 0, tăng currentHp, clamp tại maxHp, no-op nếu !active
 * - respawn(): set active=true, currentHp=maxHp
 *
 * Cũng là Enemy — implement Enemy interface để truyền `this` làm attacker.
 *
 * getCurrentHp() trả float trực tiếp — không qua Math.round (đã xóa type leak).
 */
public class Hero implements Enemy {

    private final String name;
    private final Position position;
    private final float maxHp;
    private float currentHp;
    private boolean active;
    private final TargetSelector targetSelector;

    public Hero(String name, Position position, float maxHp, TargetSelector targetSelector) {
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be > 0, got " + maxHp);
        }
        this.name = name;
        this.position = position;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.active = true;
        this.targetSelector = targetSelector;
    }

    /** Giảm currentHp theo amount (phải ≥ 0). Clamp tại 0. Set active=false nếu currentHp ≤ 0. */
    public void takeDamage(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("takeDamage amount must be >= 0, got " + amount);
        }
        currentHp = Math.max(0f, currentHp - amount);
        if (currentHp <= 0f) {
            active = false;
        }
    }

    /** Tăng currentHp theo amount (phải ≥ 0). Clamp tại maxHp. No-op nếu !active. */
    public void heal(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("heal amount must be >= 0, got " + amount);
        }
        if (!active) {
            return;
        }
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    /** Hồi sinh. Set active=true, currentHp=maxHp. */
    public void respawn() {
        active = true;
        currentHp = maxHp;
    }

    /** Ủy quyền cho TargetSelector. Trả null nếu !active hoặc selector trả null. */
    public Enemy selectTarget(List<Enemy> enemies) {
        if (!active) {
            return null;
        }
        return targetSelector.select(this, enemies);
    }

    public boolean isAlive() {
        return active;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public float getCurrentHp() {
        return currentHp;
    }

    @Override
    public String getName() {
        return name;
    }
}