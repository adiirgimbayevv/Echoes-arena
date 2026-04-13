package com.patternforge.echoesarena.stats;

public class CombatStats {

    private float maxHp;
    private float currentHp;
    private float defense;
    private float damageMultiplier;

    public CombatStats(float maxHp, float defense, float damageMultiplier) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.defense = defense;
        this.damageMultiplier = damageMultiplier;
    }

    public void applyDamage(float rawDamage) {
        float reduced = Math.max(0, rawDamage - defense);
        currentHp = Math.max(0, currentHp - reduced);
    }

    public void heal(float amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public boolean isDead() {
        return currentHp <= 0;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(float currentHp) {
        this.currentHp = Math.max(0, Math.min(maxHp, currentHp));
    }

    public float getDefense() {
        return defense;
    }

    public void setDefense(float defense) {
        this.defense = defense;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public float getHpRatio() {
        return currentHp / maxHp;
    }
}
