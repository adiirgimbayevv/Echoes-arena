package com.patternforge.echoesarena.player;

import com.patternforge.echoesarena.config.BalanceConfig;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.PhysicalStats;

public class PlayerBuild {

    private final CombatStats combatStats;
    private final PhysicalStats physicalStats;
    private final MagicalStats magicalStats;
    private final ManaProfile manaProfile;

    public PlayerBuild() {
        this.combatStats = new CombatStats(
            BalanceConfig.PLAYER_BASE_HP,
            0f,
            1f
        );
        this.physicalStats = new PhysicalStats(
            BalanceConfig.PLAYER_BASE_SPEED,
            BalanceConfig.PLAYER_DASH_DISTANCE,
            BalanceConfig.PLAYER_DASH_COOLDOWN,
            32f,
            1f
        );
        this.magicalStats = new MagicalStats(0f, 0f, 0f);
        this.manaProfile = new ManaProfile(
            BalanceConfig.PLAYER_BASE_MANA,
            BalanceConfig.PLAYER_MANA_REGEN,
            BalanceConfig.PLAYER_MANA_BURST_THRESHOLD
        );
    }

    public PlayerBuild(CombatStats combatStats, PhysicalStats physicalStats,
                       MagicalStats magicalStats, ManaProfile manaProfile) {
        this.combatStats = combatStats;
        this.physicalStats = physicalStats;
        this.magicalStats = magicalStats;
        this.manaProfile = manaProfile;
    }

    public CombatStats getCombatStats() {
        return combatStats;
    }

    public PhysicalStats getPhysicalStats() {
        return physicalStats;
    }

    public MagicalStats getMagicalStats() {
        return magicalStats;
    }

    public ManaProfile getManaProfile() {
        return manaProfile;
    }
}
