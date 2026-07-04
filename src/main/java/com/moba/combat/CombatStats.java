package com.moba.combat;

public class CombatStats {

    private final float maxHp;
    private float currentHp;
    private boolean alive;

    public CombatStats(float maxHp) {
        if (maxHp <= 0) {
            throw new IllegalArgumentException("maxHp must be > 0, got " + maxHp);
        }
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.alive = true;
    }

    public void takeDamage(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("takeDamage must be >= 0, got " + amount);
        }
        currentHp = Math.max(0f, currentHp - amount);
        if (currentHp <= 0f) {
            alive = false;
        }
    }

    public void heal(float amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("heal must be >= 0, got " + amount);
        }
        if (!alive) {
            return;
        }
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void respawn() {
        alive = true;
        currentHp = maxHp;
    }

    public boolean isAlive() {
        return alive;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public float getMaxHp() {
        return maxHp;
    }
}
