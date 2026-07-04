package com.moba.combat;

public class Hero implements Enemy {

    private final String name;
    private final Position position;
    private final Attribute attribute;
    private final CombatStats vitals;

    public Hero(String name, Position position, Attribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("attribute must not be null");
        }
        this.name = name;
        this.position = position;
        this.attribute = attribute;
        this.vitals = new CombatStats(attribute.basic().hp());
    }

    public Attribute getAttribute() {
        return attribute;
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

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public float getCurrentHp() {
        return vitals.getCurrentHp();
    }

    @Override
    public float getMaxHp() {
        return attribute.basic().hp();
    }

    @Override
    public TargetKind getTargetKind() {
        return TargetKind.HERO;
    }

    @Override
    public String getName() {
        return name;
    }
}
