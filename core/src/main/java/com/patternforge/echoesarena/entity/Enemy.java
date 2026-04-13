package com.patternforge.echoesarena.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.HitData;
import com.patternforge.echoesarena.combat.HitType;
import com.patternforge.echoesarena.combat.StatusEffectSystem;
import com.patternforge.echoesarena.combat.StatusEffectType;
import com.patternforge.echoesarena.enemy.EnemyState;
import com.patternforge.echoesarena.enemy.EnemyStateMachine;
import com.patternforge.echoesarena.enemy.EnemyType;
import com.patternforge.echoesarena.enemy.ai.EnemyStrategy;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;

import java.util.List;

public class Enemy implements CombatSystem.CombatTarget, CombatSystem.MagicalStatsProvider {

    private final EnemyType type;
    private final EnemyStateMachine stateMachine;
    private final CombatStats combatStats;
    private final MagicalStats magicalStats;
    private final StatusEffectSystem statusEffectSystem;
    private final EnemyStrategy strategy;

    private final Vector2 position;
    private final Vector2 velocity;
    private final Rectangle boundingBox;

    private final float detectionRange;
    private final float baseDamage;
    private final float baseSpeed;
    private final float hitboxSize;

    private CombatSystem combatSystem;

    public Enemy(EnemyType type, float x, float y, float hitboxSize,
                 CombatStats combatStats, MagicalStats magicalStats,
                 EnemyStrategy strategy, CombatSystem combatSystem) {
        this.type = type;
        this.combatStats = combatStats;
        this.magicalStats = magicalStats;
        this.strategy = strategy;
        this.combatSystem = combatSystem;
        this.stateMachine = new EnemyStateMachine();
        this.statusEffectSystem = new StatusEffectSystem();
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.hitboxSize = hitboxSize;
        this.boundingBox = new Rectangle(x - hitboxSize / 2f, y - hitboxSize / 2f, hitboxSize, hitboxSize);
        this.detectionRange = type.getDetectionRange();
        this.baseDamage = type.getBaseDamage();
        this.baseSpeed = type.getBaseSpeed();
    }

    public void update(float delta, Player target, List<Enemy> nearbyEnemies) {
        if (stateMachine.is(EnemyState.DEAD)) {
            return;
        }

        statusEffectSystem.update(delta, combatStats);

        if (combatStats.isDead()) {
            stateMachine.forceState(EnemyState.DEAD);
            onDeath();
            return;
        }

        if (statusEffectSystem.isStunned()) {
            stateMachine.transition(EnemyState.STUNNED);
            setVelocity(0, 0);
            return;
        }

        float distToTarget = position.dst(target.getPosition());
        if (distToTarget <= detectionRange) {
            strategy.execute(this, target, nearbyEnemies, delta);
        } else {
            stateMachine.transition(EnemyState.IDLE);
            setVelocity(0, 0);
        }

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        boundingBox.setPosition(position.x - hitboxSize / 2f, position.y - hitboxSize / 2f);
    }

    protected void onDeath() {
    }

    public void performAttack(Player target) {
        HitData hit = new HitData(
                HitType.MELEE,
                baseDamage,
                0f,
                combatStats,
                magicalStats,
                StatusEffectType.NONE
        );
        float damage = combatSystem.getDamageCalculator().calculate(hit, target.getCombatStats());
        target.takeDamage(damage);
    }

    public void performRangedAttack(Player target) {
        Vector2 dir = new Vector2(target.getPosition()).sub(position).nor();
        HitData hit = new HitData(
                HitType.RANGED,
                baseDamage,
                0f,
                combatStats,
                magicalStats,
                StatusEffectType.NONE
        );
        combatSystem.spawnProjectile(position.x, position.y, dir, 120f, 4f, detectionRange, hit);
    }

    public void heal(float amount) {
        combatStats.heal(amount);
    }

    public void takeDamage(float amount) {
        if (!isAlive()) {
            return;
        }
        combatStats.applyDamage(amount);
    }

    public void setVelocity(float vx, float vy) {
        velocity.set(vx, vy);
    }

    public float getEffectiveSpeed() {
        return baseSpeed * statusEffectSystem.getSpeedMultiplier();
    }

    @Override
    public boolean isAlive() {
        return !stateMachine.is(EnemyState.DEAD) && !combatStats.isDead();
    }

    @Override
    public CombatStats getCombatStats() {
        return combatStats;
    }

    @Override
    public StatusEffectSystem getStatusEffectSystem() {
        return statusEffectSystem;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public MagicalStats getMagicalStats() {
        return magicalStats;
    }

    public EnemyType getType() {
        return type;
    }

    public EnemyStateMachine getStateMachine() {
        return stateMachine;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getDetectionRange() {
        return detectionRange;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public float getHitboxSize() {
        return hitboxSize;
    }
}
