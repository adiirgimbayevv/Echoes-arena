package com.patternforge.echoesarena.combat;

import com.patternforge.echoesarena.stats.CombatStats;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StatusEffectSystem {

    private final Map<StatusEffectType, StatusEffect> activeEffects;

    public StatusEffectSystem() {
        this.activeEffects = new EnumMap<>(StatusEffectType.class);
    }

    public void apply(StatusEffect effect) {
        if (effect.getType() == StatusEffectType.NONE) {
            return;
        }
        StatusEffect existing = activeEffects.get(effect.getType());
        if (existing != null) {
            existing.refresh();
        } else {
            activeEffects.put(effect.getType(), effect);
        }
    }

    public void update(float delta, CombatStats target) {
        List<StatusEffectType> toRemove = new ArrayList<>();
        for (Map.Entry<StatusEffectType, StatusEffect> entry : activeEffects.entrySet()) {
            entry.getValue().update(delta, target);
            if (entry.getValue().isExpired()) {
                toRemove.add(entry.getKey());
            }
        }
        for (StatusEffectType type : toRemove) {
            activeEffects.remove(type);
        }
    }

    public boolean has(StatusEffectType type) {
        return activeEffects.containsKey(type);
    }

    public float getSpeedMultiplier() {
        float multiplier = 1f;
        for (StatusEffect effect : activeEffects.values()) {
            if (effect.getSlowFactor() > 0) {
                multiplier *= (1f - effect.getSlowFactor());
            }
        }
        return Math.max(0f, multiplier);
    }

    public boolean isStunned() {
        return has(StatusEffectType.STUN) || has(StatusEffectType.FREEZE);
    }

    public void clear() {
        activeEffects.clear();
    }

    public Map<StatusEffectType, StatusEffect> getActiveEffects() {
        return activeEffects;
    }
}
