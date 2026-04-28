package com.patternforge.echoesarena.combat;

import com.patternforge.echoesarena.stats.CombatStats;

public class StatusEffect {

    private final StatusEffectType type;
    private final float damagePerSecond;
    private final float slowFactor;
    private final float duration;
    private float remainingTime;

    public StatusEffect(StatusEffectType type, float damagePerSecond, float slowFactor, float duration) {
        this.type = type;
        this.damagePerSecond = damagePerSecond;
        this.slowFactor = slowFactor;
        this.duration = duration;
        this.remainingTime = duration;
    }

    public void update(float delta, CombatStats target) {
        if (remainingTime <= 0) {
            return;
        }
        if (damagePerSecond > 0) {
            target.applyDamage(damagePerSecond * delta);
        }
        remainingTime -= delta;
    }

    public void refresh() {
        remainingTime = duration;
    }

    public boolean isExpired() {
        return remainingTime <= 0;
    }

    public StatusEffectType getType() {
        return type;
    }

    public float getSlowFactor() {
        return slowFactor;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public float getDuration() {
        return duration;
    }
}
