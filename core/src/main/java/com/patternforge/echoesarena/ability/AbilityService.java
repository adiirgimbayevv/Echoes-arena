package com.patternforge.echoesarena.ability;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.enemy.EnemyType;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AbilityService {

    public static final class AbilityProjectile {
        private final ActiveAbility sourceAbility;
        private final Vector2 position;
        private final Vector2 velocity;
        private float traveledDistance;
        private boolean alive;

        private AbilityProjectile(ActiveAbility sourceAbility, float x, float y, Vector2 direction, float speed) {
            this.sourceAbility = sourceAbility;
            this.position = new Vector2(x, y);
            this.velocity = new Vector2(direction).nor().scl(speed);
            this.traveledDistance = 0f;
            this.alive = true;
        }

        public Vector2 getPosition() {
            return position;
        }
    }

    private static final float ULTIMATE_MAX_CHARGE = 100f;
    private static final float ULTIMATE_PASSIVE_CHARGE_PER_SECOND = 1.8f;
    private static final float ULTIMATE_CHARGE_PER_KILL = 8f;
    private static final float FIREBALL_SPEED = 520f;
    private static final float FIREBALL_RADIUS = 13f;
    private static final float FIREBALL_MAX_DISTANCE = 760f;
    private static final float FIREBALL_EXPLOSION_RADIUS = 130f;
    private static final float MAGIC_VISUAL_TIME = 0.18f;
    private static final float FREEZE_VISUAL_TIME = 0.35f;

    private final CombatSystem combatSystem;
    private final ElementProgress elementProgress;
    private final ActiveAbility fireball;
    private final ActiveAbility frostNova;
    private final UltimateAbility glacialRift;
    private final List<AbilityProjectile> fireballs;
    private final List<FrozenTarget> frozenTargets;

    private float ultimateCharge;
    private float magicVisualTimer;
    private float freezeVisualTimer;

    public AbilityService(CombatSystem combatSystem) {
        this.combatSystem = combatSystem;
        this.elementProgress = new ElementProgress();
        this.fireball = AbilityFactory.fireball();
        this.frostNova = AbilityFactory.frostNova();
        this.glacialRift = AbilityFactory.glacialRift();
        this.fireballs = new ArrayList<>();
        this.frozenTargets = new ArrayList<>();
    }

    public void update(float delta, Player player, List<Enemy> enemies) {
        fireball.update(delta);
        frostNova.update(delta);
        ultimateCharge = Math.min(ULTIMATE_MAX_CHARGE, ultimateCharge + ULTIMATE_PASSIVE_CHARGE_PER_SECOND * delta);
        magicVisualTimer = Math.max(0f, magicVisualTimer - delta);
        freezeVisualTimer = Math.max(0f, freezeVisualTimer - delta);

        updateFrozenTargets(delta);
        updateFireballs(delta, player, enemies);
    }

    public boolean castFrostNova(Player player, List<Enemy> enemies) {
        if (!player.spendMana(frostNova.getManaCost())) {
            return false;
        }

        boolean cast = frostNova.cast(player, enemies, player.getPosition(), combatSystem, elementProgress);
        if (!cast) {
            player.getManaProfile().add(frostNova.getManaCost());
            return false;
        }

        magicVisualTimer = MAGIC_VISUAL_TIME;
        return true;
    }

    public boolean castFireball(Player player) {
        if (ultimateCharge < ULTIMATE_MAX_CHARGE) {
            return false;
        }

        Vector2 direction = new Vector2(player.getFacing());
        if (direction.len2() <= 0.001f) {
            direction.set(1f, 0f);
        }

        fireballs.add(new AbilityProjectile(
            fireball,
            player.getPosition().x,
            player.getPosition().y,
            direction,
            FIREBALL_SPEED
        ));
        ultimateCharge = 0f;
        return true;
    }

    public boolean castGlacialRift(Player player, List<Enemy> enemies) {
        if (ultimateCharge < ULTIMATE_MAX_CHARGE) {
            return false;
        }

        combatSystem.abilityHitAoe(
            player,
            enemies,
            player.getPosition(),
            glacialRift.getAoeRadius(),
            glacialRift.getBasePhysicalDamage(),
            glacialRift.getBaseMagicalDamage(),
            glacialRift.getAppliedEffect()
        );
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.getPosition().dst(player.getPosition()) <= glacialRift.getAoeRadius()) {
                addOrRefreshFrozenTarget(enemy, 3.2f);
                enemy.setVelocity(0f, 0f);
            }
        }

        freezeVisualTimer = FREEZE_VISUAL_TIME;
        ultimateCharge = 0f;
        return true;
    }

    public void addUltimateChargeFromKill() {
        ultimateCharge = Math.min(ULTIMATE_MAX_CHARGE, ultimateCharge + ULTIMATE_CHARGE_PER_KILL);
    }

    public void resetStageState() {
        fireballs.clear();
        frozenTargets.clear();
        magicVisualTimer = 0f;
        freezeVisualTimer = 0f;
    }

    public boolean isEnemyFrozen(Enemy enemy) {
        for (FrozenTarget frozen : frozenTargets) {
            if (frozen.enemy == enemy && frozen.timer > 0f) {
                return true;
            }
        }
        return false;
    }

    public List<AbilityProjectile> getFireballs() {
        return fireballs;
    }

    public float getFireballRadius() {
        return FIREBALL_RADIUS;
    }

    public float getMagicVisualTimer() {
        return magicVisualTimer;
    }

    public float getMagicVisualTime() {
        return MAGIC_VISUAL_TIME;
    }

    public float getMagicVisualRadius() {
        return frostNova.getAoeRadius();
    }

    public float getFreezeVisualTimer() {
        return freezeVisualTimer;
    }

    public float getFreezeVisualTime() {
        return FREEZE_VISUAL_TIME;
    }

    public float getFreezeVisualRadius() {
        return glacialRift.getAoeRadius();
    }

    public float getUltimateCharge() {
        return ultimateCharge;
    }

    public float getUltimateRatio() {
        return Math.max(0f, Math.min(1f, ultimateCharge / ULTIMATE_MAX_CHARGE));
    }

    public boolean isUltimateReady() {
        return ultimateCharge >= ULTIMATE_MAX_CHARGE;
    }

    public float getFrostNovaCooldownRatio() {
        return frostNova.getCooldownRatio();
    }

    private void updateFrozenTargets(float delta) {
        for (int i = frozenTargets.size() - 1; i >= 0; i--) {
            FrozenTarget frozen = frozenTargets.get(i);
            if (!frozen.enemy.isAlive()) {
                frozenTargets.remove(i);
                continue;
            }

            frozen.timer -= delta;
            frozen.enemy.setVelocity(0f, 0f);

            if (frozen.timer <= 0f) {
                frozenTargets.remove(i);
            }
        }
    }

    private void updateFireballs(float delta, Player player, List<Enemy> enemies) {
        for (int i = fireballs.size() - 1; i >= 0; i--) {
            AbilityProjectile projectile = fireballs.get(i);
            if (!projectile.alive) {
                fireballs.remove(i);
                continue;
            }

            float moveX = projectile.velocity.x * delta;
            float moveY = projectile.velocity.y * delta;
            projectile.position.add(moveX, moveY);
            projectile.traveledDistance += (float) Math.sqrt(moveX * moveX + moveY * moveY);

            if (shouldFireballExplode(projectile, enemies)) {
                explodeFireball(projectile, player, enemies);
                projectile.alive = false;
                fireballs.remove(i);
            }
        }
    }

    private boolean shouldFireballExplode(AbilityProjectile projectile, List<Enemy> enemies) {
        if (projectile.traveledDistance >= FIREBALL_MAX_DISTANCE) {
            return true;
        }

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()
                && enemy.getPosition().dst(projectile.position) <= getFireballImpactRadius(enemy)) {
                return true;
            }
        }

        return false;
    }

    private void explodeFireball(AbilityProjectile projectile, Player player, List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.getPosition().dst(projectile.position) <= getFireballExplosionRadius(enemy)) {
                combatSystem.abilityHit(
                    player,
                    enemy,
                    projectile.sourceAbility.getBasePhysicalDamage(),
                    projectile.sourceAbility.getBaseMagicalDamage(),
                    projectile.sourceAbility.getAppliedEffect()
                );
            }
        }
    }

    private float getFireballImpactRadius(Enemy enemy) {
        float radius = Math.max(18f, enemy.getHitboxSize() + FIREBALL_RADIUS);
        if (isFinalBoss(enemy)) {
            radius = Math.max(radius, 110f);
        }
        return radius;
    }

    private float getFireballExplosionRadius(Enemy enemy) {
        float radius = FIREBALL_EXPLOSION_RADIUS;
        if (isFinalBoss(enemy)) {
            radius += 90f;
        }
        return radius;
    }

    private boolean isFinalBoss(Enemy enemy) {
        return enemy.getType() == EnemyType.TANK && enemy.getCombatStats().getMaxHp() >= 300f;
    }

    private void addOrRefreshFrozenTarget(Enemy enemy, float duration) {
        for (FrozenTarget frozen : frozenTargets) {
            if (frozen.enemy == enemy) {
                frozen.timer = duration;
                return;
            }
        }
        frozenTargets.add(new FrozenTarget(enemy, duration));
    }

    private static final class FrozenTarget {
        private final Enemy enemy;
        private float timer;

        private FrozenTarget(Enemy enemy, float timer) {
            this.enemy = enemy;
            this.timer = timer;
        }
    }
}
