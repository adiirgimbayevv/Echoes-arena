
package com.patternforge.echoesarena.weapon;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.ability.ElementType;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.StatusEffectType;

public class MeleeWeapon extends Weapon {

    private final int hitCount;
    private final StatusEffectType onHitEffect;
    private final float elementChargePerHit;

    public MeleeWeapon(String id, String displayName, ElementType affinityElement,
                       float baseDamage, float attackSpeed, float range,
                       int hitCount, StatusEffectType onHitEffect, float elementChargePerHit) {
        super(id, displayName, WeaponType.MELEE, affinityElement, baseDamage, attackSpeed, range);
        this.hitCount = hitCount;
        this.onHitEffect = onHitEffect;
        this.elementChargePerHit = elementChargePerHit;
    }

    @Override
    public void performAttack(WeaponAttackContext context) {
        float effectiveDamage = getBaseDamage() * (1f + context.getCompatibilityBonus());
        Vector2 attackerPos = context.getAttacker().getPosition();
        Vector2 dir = context.getAttackDirection();
        int hits = 0;

        for (CombatSystem.CombatTarget target : context.getTargets()) {
            if (!target.isAlive() || hits >= hitCount) {
                break;
            }
            Vector2 toTarget = new Vector2(target.getPosition()).sub(attackerPos);
            float dist = toTarget.len();
            if (dist > getRange()) {
                continue;
            }
            if (dir.len2() > 0 && dist > 0) {
                float dot = toTarget.nor().dot(dir.nor());
                if (dot < 0.3f) {
                    continue;
                }
            }
            context.getCombatSystem().meleeHit(context.getAttacker(), target, effectiveDamage, onHitEffect);
            context.getElementProgress().addCharge(getAffinityElement(), elementChargePerHit);
            hits++;
        }
    }

    public int getHitCount() {
        return hitCount;
    }

    public StatusEffectType getOnHitEffect() {
        return onHitEffect;
    }

    public float getElementChargePerHit() {
        return elementChargePerHit;
    }
}
