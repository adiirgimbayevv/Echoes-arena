package com.patternforge.echoesarena.stats;

public class PhysicalStats {

    private float speed;
    private float dashDistance;
    private float dashCooldown;
    private float attackRange;
    private float attackSpeed;

    public PhysicalStats(float speed, float dashDistance, float dashCooldown,
                         float attackRange, float attackSpeed) {
        this.speed = speed;
        this.dashDistance = dashDistance;
        this.dashCooldown = dashCooldown;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDashDistance() {
        return dashDistance;
    }

    public void setDashDistance(float dashDistance) {
        this.dashDistance = dashDistance;
    }

    public float getDashCooldown() {
        return dashCooldown;
    }

    public void setDashCooldown(float dashCooldown) {
        this.dashCooldown = dashCooldown;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
}
