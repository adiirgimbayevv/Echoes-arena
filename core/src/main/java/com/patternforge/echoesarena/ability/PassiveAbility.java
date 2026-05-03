package com.patternforge.echoesarena.ability;

public class PassiveAbility extends Ability {

    public enum PassiveEffect {
        BONUS_DAMAGE_ON_BURN,
        RESTORE_HP_ON_KILL,
        MANA_ON_HIT,
        CHARGE_BOOST,
        REDUCED_COOLDOWN_ON_KILL
    }

    private final PassiveEffect effect;
    private final float effectValue;

    public PassiveAbility(String id, String displayName, ElementType element,
                          PassiveEffect effect, float effectValue) {
        super(id, displayName, element, AbilitySlot.SECONDARY, 0f, 0f);
        this.effect = effect;
        this.effectValue = effectValue;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.PASSIVE;
    }

    public PassiveEffect getEffect() {
        return effect;
    }

    public float getEffectValue() {
        return effectValue;
    }
}
