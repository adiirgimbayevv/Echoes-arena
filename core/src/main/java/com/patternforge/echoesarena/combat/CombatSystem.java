package com.patternforge.echoesarena.combat;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.entity.Projectile;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;

import java.util.ArrayList;
import java.util.List;

public class CombatSystem {

    public interface CombatTarget {
        CombatStats getCombatStats();
        StatusEffectSystem getStatusEffectSystem();
        Vector2 getPosition();
        boolean isAlive();
    }

    private final DamageCalculator damageCalculator;
    private final CollisionSystem collisionSystem;
    private final List<Projectile> projectiles;

    public CombatSystem() {
        this.damageCalculator = new DamageCalculator();
        this.collisionSystem = new CollisionSystem();
        this.projectiles = new ArrayList<>();
    }

    public void update(float delta) {
        for (Projectile p : projectiles) {
            p.update(delta);
        }
        projectiles.removeIf(Projectile::isExpired);
    }

    public void meleeHit(CombatTarget attacker, CombatTarget defender,
                         float basePhysical, StatusEffectType effectType) {
        if (!defender.isAlive()) {
            return;
        }
        CombatStats acs = attacker.getCombatStats();
        MagicalStats ams = getMagicalStats(attacker);
        HitData hit = new HitData(HitType.MELEE, basePhysical, 0f, acs, ams, effectType);
        applyHit(hit, defender);
    }

    public void rangedHit(Projectile projectile, CombatTarget defender) {
        if (!defender.isAlive()) {
            return;
        }
        applyHit(projectile.getHitData(), defender);
    }

    public void abilityHit(CombatTarget attacker, CombatTarget defender,
                           float basePhysical, float baseMagical,
                           StatusEffectType effectType) {
        if (!defender.isAlive()) {
            return;
        }
        CombatStats acs = attacker.getCombatStats();
        MagicalStats ams = getMagicalStats(attacker);
        HitData hit = new HitData(HitType.ABILITY, basePhysical, baseMagical, acs, ams, effectType);
        applyHit(hit, defender);
    }

    public void abilityHitAoe(CombatTarget attacker, List<? extends CombatTarget> defenders,
                               Vector2 center, float radius,
                               float basePhysical, float baseMagical,
                               StatusEffectType effectType) {
        for (CombatTarget defender : defenders) {
            if (!defender.isAlive()) {
                continue;
            }
            Vector2 defPos = defender.getPosition();
            float dx = defPos.x - center.x;
            float dy = defPos.y - center.y;
            if ((dx * dx + dy * dy) <= radius * radius) {
                abilityHit(attacker, defender, basePhysical, baseMagical, effectType);
            }
        }
    }

    public Projectile spawnProjectile(float x, float y, Vector2 direction,
                                      float speed, float radius, float maxRange,
                                      HitData hitData) {
        Projectile projectile = new Projectile(x, y, direction, speed, radius, maxRange, hitData);
        projectiles.add(projectile);
        return projectile;
    }

    public void checkProjectileCollisions(List<? extends CombatTarget> targets) {
        for (Projectile projectile : projectiles) {
            if (projectile.isExpired()) {
                continue;
            }
            for (CombatTarget target : targets) {
                if (!target.isAlive()) {
                    continue;
                }
                Vector2 pos = target.getPosition();
                float dx = projectile.getPosition().x - pos.x;
                float dy = projectile.getPosition().y - pos.y;
                float distSq = dx * dx + dy * dy;
                float combinedRadius = projectile.getRadius() + 8f;
                if (distSq <= combinedRadius * combinedRadius) {
                    rangedHit(projectile, target);
                    projectile.expire();
                    break;
                }
            }
        }
    }

    private void applyHit(HitData hit, CombatTarget defender) {
        float damage = damageCalculator.calculate(hit, defender.getCombatStats());
        defender.getCombatStats().applyDamage(damage);
        if (hit.getAppliedEffect() != null && hit.getAppliedEffect() != StatusEffectType.NONE) {
            StatusEffect effect = StatusEffectFactory.create(hit.getAppliedEffect());
            defender.getStatusEffectSystem().apply(effect);
        }
    }

    private MagicalStats getMagicalStats(CombatTarget target) {
        if (target instanceof MagicalStatsProvider) {
            return ((MagicalStatsProvider) target).getMagicalStats();
        }
        return new MagicalStats(0f, 0f, 0f);
    }

    public interface MagicalStatsProvider {
        MagicalStats getMagicalStats();
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public DamageCalculator getDamageCalculator() {
        return damageCalculator;
    }

    public CollisionSystem getCollisionSystem() {
        return collisionSystem;
    }
}
