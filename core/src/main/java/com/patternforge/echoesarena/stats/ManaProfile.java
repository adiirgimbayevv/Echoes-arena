package com.patternforge.echoesarena.stats;

public class ManaProfile {

    private float maxMana;
    private float currentMana;
    private float manaRegenPerSecond;
    private float burstThreshold;

    public ManaProfile(float maxMana, float manaRegenPerSecond, float burstThreshold) {
        this.maxMana = maxMana;
        this.currentMana = maxMana;
        this.manaRegenPerSecond = manaRegenPerSecond;
        this.burstThreshold = burstThreshold;
    }

    public void update(float delta) {
        currentMana = Math.min(maxMana, currentMana + manaRegenPerSecond * delta);
    }

    public boolean spend(float amount) {
        if (currentMana < amount) {
            return false;
        }
        currentMana -= amount;
        return true;
    }

    public void add(float amount) {
        currentMana = Math.min(maxMana, currentMana + amount);
    }

    public boolean isBurstReady() {
        return currentMana >= burstThreshold;
    }

    public float getManaRatio() {
        return currentMana / maxMana;
    }

    public float getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(float maxMana) {
        this.maxMana = maxMana;
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public float getManaRegenPerSecond() {
        return manaRegenPerSecond;
    }

    public void setManaRegenPerSecond(float manaRegenPerSecond) {
        this.manaRegenPerSecond = manaRegenPerSecond;
    }

    public float getBurstThreshold() {
        return burstThreshold;
    }

    public void setBurstThreshold(float burstThreshold) {
        this.burstThreshold = burstThreshold;
    }
}
