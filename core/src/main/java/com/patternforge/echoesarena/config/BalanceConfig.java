package com.patternforge.echoesarena.config;

public class BalanceConfig {

    public static final float PLAYER_BASE_SPEED = 80f;
    public static final float PLAYER_BASE_HP = 100f;
    public static final float PLAYER_BASE_DAMAGE = 10f;
    public static final float PLAYER_DASH_DISTANCE = 64f;
    public static final float PLAYER_DASH_COOLDOWN = 1.5f;
    public static final float PLAYER_BASE_MANA = 100f;
    public static final float PLAYER_MANA_REGEN = 5f;
    public static final float PLAYER_MANA_BURST_THRESHOLD = 80f;

    public static final float ENEMY_BASE_HP = 40f;
    public static final float ENEMY_BASE_SPEED = 50f;
    public static final float ENEMY_BASE_DAMAGE = 8f;

    public static final float PICKUP_HEAL_AMOUNT = 20f;
    public static final int XP_PER_KILL = 10;
    public static final int XP_PER_LEVEL = 100;
    public static final int XP_LEVEL_SCALING = 20;

    public static final float STATUS_BURN_DPS = 5f;
    public static final float STATUS_BURN_DURATION = 3f;
    public static final float STATUS_SLOW_FACTOR = 0.4f;
    public static final float STATUS_SLOW_DURATION = 2f;
    public static final float STATUS_STUN_DURATION = 1f;
    public static final float STATUS_POISON_DPS = 3f;
    public static final float STATUS_POISON_DURATION = 5f;
    public static final float STATUS_BLEED_DPS = 4f;
    public static final float STATUS_BLEED_DURATION = 4f;
    public static final float STATUS_FREEZE_DURATION = 1.5f;

    public static final int META_POINTS_PER_STAGE = 2;
    public static final int META_KILLS_PER_BONUS_POINT = 10;
    public static final int META_BOSS_BONUS_POINTS = 5;

    public static final float WEAPON_COMPAT_BONUS_MELEE_EARTH = 0.15f;
    public static final float WEAPON_COMPAT_BONUS_MELEE_FIRE = 0.10f;
    public static final float WEAPON_COMPAT_BONUS_RANGED_ICE = 0.15f;
    public static final float WEAPON_COMPAT_BONUS_RANGED_FIRE = 0.08f;
    public static final float WEAPON_COMPAT_BONUS_FOCUS_ICE = 0.12f;
    public static final float WEAPON_COMPAT_BONUS_FOCUS_EARTH = 0.10f;
    public static final float WEAPON_COMPAT_BONUS_MELEE_LAVA = 0.20f;
    public static final float WEAPON_COMPAT_BONUS_FOCUS_STEAM = 0.18f;

    private BalanceConfig() {}
}
