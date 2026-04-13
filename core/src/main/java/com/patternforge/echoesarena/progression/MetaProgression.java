package com.patternforge.echoesarena.progression;

import com.patternforge.echoesarena.config.BalanceConfig;

import java.util.HashMap;
import java.util.Map;

public class MetaProgression {

    private int totalStatPoints;
    private int physicalPointsBank;
    private int magicalPointsBank;
    private int highestStageCleared;
    private final Map<String, Integer> stagePointsEarned;

    public MetaProgression() {
        this.totalStatPoints = 0;
        this.physicalPointsBank = 0;
        this.magicalPointsBank = 0;
        this.highestStageCleared = 0;
        this.stagePointsEarned = new HashMap<>();
    }

    public void recordStageResult(int stageId, int enemiesKilled, boolean bossDefeated) {
        if (stagePointsEarned.containsKey(String.valueOf(stageId))) {
            return;
        }
        int points = computePoints(stageId, enemiesKilled, bossDefeated);
        stagePointsEarned.put(String.valueOf(stageId), points);
        totalStatPoints += points;
        distributePoints(points);
        if (stageId > highestStageCleared) {
            highestStageCleared = stageId;
        }
    }

    private int computePoints(int stageId, int enemiesKilled, boolean bossDefeated) {
        int base = BalanceConfig.META_POINTS_PER_STAGE * stageId;
        int killBonus = (enemiesKilled / BalanceConfig.META_KILLS_PER_BONUS_POINT);
        int bossBonus = bossDefeated ? BalanceConfig.META_BOSS_BONUS_POINTS : 0;
        return base + killBonus + bossBonus;
    }

    private void distributePoints(int points) {
        int physical = points / 2;
        int magical = points - physical;
        physicalPointsBank += physical;
        magicalPointsBank += magical;
    }

    public int drainPhysicalPoints() {
        int drained = physicalPointsBank;
        physicalPointsBank = 0;
        return drained;
    }

    public int drainMagicalPoints() {
        int drained = magicalPointsBank;
        magicalPointsBank = 0;
        return drained;
    }

    public boolean hasPointsToAllocate() {
        return physicalPointsBank > 0 || magicalPointsBank > 0;
    }

    public int getTotalStatPoints() {
        return totalStatPoints;
    }

    public int getPhysicalPointsBank() {
        return physicalPointsBank;
    }

    public int getMagicalPointsBank() {
        return magicalPointsBank;
    }

    public int getHighestStageCleared() {
        return highestStageCleared;
    }

    public Map<String, Integer> getStagePointsEarned() {
        return stagePointsEarned;
    }

    public void restoreState(int physBank, int magBank, int highestStage,
                             Map<String, Integer> earned) {
        this.physicalPointsBank = physBank;
        this.magicalPointsBank = magBank;
        this.highestStageCleared = highestStage;
        this.stagePointsEarned.clear();
        this.stagePointsEarned.putAll(earned);
    }
}
