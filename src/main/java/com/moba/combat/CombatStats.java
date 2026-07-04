package com.moba.combat;

public class CombatStats {

    private final float hp;
    private float currentHp;
    private boolean alive;

    public CombatStats(float hp) {
        if (hp <= 0) {
            throw new IllegalArgumentException("hp must be > 0, got " + hp);
        }
        this.hp = hp;
        this.currentHp = hp;
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
        currentHp = Math.min(hp, currentHp + amount);
    }

    public void respawn() {
        alive = true;
        currentHp = hp;
    }

    public boolean isAlive() {
        return alive;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public float getHp() {
        return hp;
    }
}