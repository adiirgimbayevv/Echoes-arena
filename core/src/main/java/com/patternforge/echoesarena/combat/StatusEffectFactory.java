package com.patternforge.echoesarena.combat;

import com.patternforge.echoesarena.config.BalanceConfig;

public class StatusEffectFactory {

    private StatusEffectFactory() {}

    public static StatusEffect create(StatusEffectType type) {
        switch (type) {
            case BURN:
                return new StatusEffect(StatusEffectType.BURN,
                        BalanceConfig.STATUS_BURN_DPS, 0f,
                        BalanceConfig.STATUS_BURN_DURATION);
            case SLOW:
                return new StatusEffect(StatusEffectType.SLOW,
                        0f, BalanceConfig.STATUS_SLOW_FACTOR,
                        BalanceConfig.STATUS_SLOW_DURATION);
            case STUN:
                return new StatusEffect(StatusEffectType.STUN,
                        0f, 1f,
                        BalanceConfig.STATUS_STUN_DURATION);
            case POISON:
                return new StatusEffect(StatusEffectType.POISON,
                        BalanceConfig.STATUS_POISON_DPS, 0f,
                        BalanceConfig.STATUS_POISON_DURATION);
            case BLEED:
                return new StatusEffect(StatusEffectType.BLEED,
                        BalanceConfig.STATUS_BLEED_DPS, 0f,
                        BalanceConfig.STATUS_BLEED_DURATION);
            case FREEZE:
                return new StatusEffect(StatusEffectType.FREEZE,
                        0f, 1f,
                        BalanceConfig.STATUS_FREEZE_DURATION);
            default:
                return new StatusEffect(StatusEffectType.NONE, 0f, 0f, 0f);
        }
    }
}
