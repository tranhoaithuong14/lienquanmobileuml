package com.lqm.combat;

/**
 * HP state machine cho bất kỳ entity nào có thể chịu damage và chết (Hero, sau này: tower, creep, summon).
 *
 * Tách ra từ Hero sau khi Hero 95 dòng bắt đầu conflate hai concerns (identity/targeting + combat state).
 * Hero giờ compose CombatStats thay vì tự quản lý HP fields.
 *
 * HP semantics (xem ADR-0002):
 * - float currentHp, floor 0, ceiling maxHp
 * - boolean active: false khi currentHp ≤ 0, true khi respawn
 * - takeDamage(amount): amount ≥ 0, giảm currentHp, clamp tại 0, set active=false nếu HP ≤ 0
 * - heal(amount): amount ≥ 0, tăng currentHp, clamp tại maxHp, no-op nếu !active
 * - respawn(): set active=true, currentHp=maxHp
 */
public class CombatStats {

    private final float maxHp;
    private float currentHp;
    private boolean active;

    public CombatStats(float maxHp) {
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be > 0, got " + maxHp);
        }
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.active = true;
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

    public boolean isAlive() {
        return active;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public float getMaxHp() {
        return maxHp;
    }
}