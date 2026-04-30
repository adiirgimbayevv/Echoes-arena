package com.patternforge.echoesarena.magic;

import com.patternforge.echoesarena.stats.ManaProfile;

public class ManaSystem {
    private static final float MIN_COST_RATIO = 0.20f; // Efficiency can't drop cost below 20%
    private static final float ACTIVE_REGEN_MULTIPLIER = 4.0f; // 4x regen while active regen
    private static final float ACTIVE_REGEN_HEALTH_COST = 5.0f; // Saps 5 HP per second during active regen (as per GDD risk/reward)

    public float calculateSpellCost(float baseCost, float energyEfficiency) {
        // Efficiency reduces cost, but it can never go below 20% of base cost
        float reduction = Math.min(energyEfficiency, 1.0f - MIN_COST_RATIO);
        return baseCost * (1.0f - reduction);
    }

    public float calculateBurstPower(float physicalPower, float magicalPower) {
        // As per GDD, Burst Power scales with both Physical and Magical
        return (physicalPower * 0.4f) + (magicalPower * 0.6f);
    }

    public void updateRegen(ManaProfile profile, boolean isActiveRegenRequested, float delta) {
        if (isActiveRegenRequested) {
            profile.add(profile.getManaRegenPerSecond() * ACTIVE_REGEN_MULTIPLIER * delta);
        } else {
            profile.add(profile.getManaRegenPerSecond() * delta);
        }
    }
}
