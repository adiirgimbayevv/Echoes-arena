package com.patternforge.echoesarena.enemy;

public enum EnemyType {

    GRUNT(40f, 0f, 1.25f, 50f, 16f, 48f),
    SWARM(15f, 0f, 0.85f, 75f, 9f, 24f),
    TANK(160f, 8f, 1.85f, 30f, 30f, 80f),
    SNIPER(25f, 0f, 1.15f, 40f, 28f, 200f),
    SUPPORT(30f, 2f, 0.8f, 45f, 11f, 96f);

    private final float baseHp;
    private final float baseDefense;
    private final float damageMultiplier;
    private final float baseSpeed;
    private final float baseDamage;
    private final float detectionRange;

    EnemyType(float baseHp, float baseDefense, float damageMultiplier,
              float baseSpeed, float baseDamage, float detectionRange) {
        this.baseHp = baseHp;
        this.baseDefense = baseDefense;
        this.damageMultiplier = damageMultiplier;
        this.baseSpeed = baseSpeed;
        this.baseDamage = baseDamage;
        this.detectionRange = detectionRange;
    }

    public float getBaseHp() {
        return baseHp;
    }

    public float getBaseDefense() {
        return baseDefense;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public float getDetectionRange() {
        return detectionRange;
    }
}
