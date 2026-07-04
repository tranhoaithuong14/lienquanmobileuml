package com.moba.combat;

import com.moba.strategy.TargetSelector;

import java.util.List;

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

    public Enemy selectTarget(List<Enemy> enemies) {
        if (!vitals.isAlive()) {
            return null;
        }
        return targetSelector.select(this, enemies);
    }

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
