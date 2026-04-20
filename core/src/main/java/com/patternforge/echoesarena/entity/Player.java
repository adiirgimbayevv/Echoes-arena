package com.patternforge.echoesarena.entity;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.StatusEffectSystem;
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.player.PlayerController;
import com.patternforge.echoesarena.player.PlayerState;
import com.patternforge.echoesarena.player.PlayerStateMachine;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.PhysicalStats;

public class Player implements CombatSystem.CombatTarget, CombatSystem.MagicalStatsProvider {

    private final Vector2 position;
    private final Vector2 velocity;
    private final Vector2 facing;

    private final PlayerBuild build;
    private final PlayerStateMachine stateMachine;
    private final PlayerController controller;
    private final StatusEffectSystem statusEffectSystem;

    private float dashCooldownTimer;
    private float dashDurationTimer;

    private float guardianShieldCooldownTimer;
    private float guardianShieldDurationTimer;
    private boolean guardianShieldTriggered;

    private static final float DASH_DURATION = 0.15f;

    private static final float GUARDIAN_SHIELD_BASE_COOLDOWN = 60f;
    private static final float GUARDIAN_SHIELD_DURATION = 3.5f;

    public Player(float x, float y, PlayerBuild build) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.facing = new Vector2(1, 0);
        this.build = build;
        this.stateMachine = new PlayerStateMachine();
        this.controller = new PlayerController();
        this.statusEffectSystem = new StatusEffectSystem();
        this.dashCooldownTimer = 0f;
        this.dashDurationTimer = 0f;

        this.guardianShieldCooldownTimer = 0f;
        this.guardianShieldDurationTimer = 0f;
        this.guardianShieldTriggered = false;
    }

    public void update(float delta) {
        if (stateMachine.is(PlayerState.DEAD)) {
            return;
        }

        controller.update();
        build.getManaProfile().update(delta);

        guardianShieldTriggered = false;

        if (guardianShieldCooldownTimer > 0f) {
            guardianShieldCooldownTimer -= delta;
        }

        if (guardianShieldDurationTimer > 0f) {
            guardianShieldDurationTimer -= delta;
        }

        if (dashCooldownTimer > 0) {
            dashCooldownTimer -= delta;
        }

        if (stateMachine.is(PlayerState.DASHING)) {
            dashDurationTimer -= delta;

            if (dashDurationTimer <= 0) {
                velocity.setZero();

                if (controller.isMoving()) {
                    stateMachine.transition(PlayerState.MOVING);
                } else {
                    stateMachine.transition(PlayerState.IDLE);
                }
            }
        } else {
            handleMovement(delta);
            handleDash();
        }

        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
    }

    private void handleMovement(float delta) {
        PhysicalStats phys = build.getPhysicalStats();
        Vector2 dir = controller.getMoveDirection();

        if (controller.isMoving()) {
            velocity.set(dir).scl(phys.getSpeed());
            facing.set(dir);
            stateMachine.transition(PlayerState.MOVING);
        } else {
            velocity.setZero();
            if (stateMachine.is(PlayerState.MOVING)) {
                stateMachine.transition(PlayerState.IDLE);
            }
        }
    }

    private void handleDash() {
        if (!controller.isDashPressed()) {
            return;
        }

        if (dashCooldownTimer > 0) {
            return;
        }

        PhysicalStats phys = build.getPhysicalStats();
        Vector2 dashDir = controller.isMoving() ? controller.getMoveDirection() : facing;

        velocity.set(dashDir).scl(phys.getDashDistance() / DASH_DURATION);
        dashCooldownTimer = phys.getDashCooldown();
        dashDurationTimer = DASH_DURATION;

        stateMachine.transition(PlayerState.DASHING);
    }

    public void takeDamage(float amount) {
        if (stateMachine.is(PlayerState.DEAD)) {
            return;
        }

        if (isGuardianShieldActive()) {
            return;
        }

        CombatStats combat = build.getCombatStats();
        float hpBeforeDamage = combat.getCurrentHp();

        combat.applyDamage(amount);

        if (combat.isDead()) {
            if (canTriggerGuardianShield()) {
                combat.setCurrentHp(1f);
                activateGuardianShield();
                stateMachine.transition(PlayerState.HURT);
                return;
            }

            stateMachine.forceState(PlayerState.DEAD);
            return;
        }

        if (combat.getCurrentHp() < hpBeforeDamage) {
            stateMachine.transition(PlayerState.HURT);
        }
    }

    private boolean canTriggerGuardianShield() {
        return guardianShieldCooldownTimer <= 0f;
    }

    private void activateGuardianShield() {
        guardianShieldTriggered = true;
        guardianShieldDurationTimer = GUARDIAN_SHIELD_DURATION;
        guardianShieldCooldownTimer = getGuardianShieldCooldown();
    }

    public void heal(float amount) {
        build.getCombatStats().heal(amount);
    }

    public boolean spendMana(float amount) {
        return build.getManaProfile().spend(amount);
    }

    public boolean isDead() {
        return stateMachine.is(PlayerState.DEAD);
    }

    @Override
    public boolean isAlive() {
        return !isDead();
    }

    public boolean isDashing() {
        return stateMachine.is(PlayerState.DASHING);
    }

    public boolean isDashReady() {
        return dashCooldownTimer <= 0;
    }

    public float getDashCooldownRatio() {
        if (build.getPhysicalStats().getDashCooldown() <= 0) {
            return 1f;
        }

        return 1f - (dashCooldownTimer / build.getPhysicalStats().getDashCooldown());
    }

    public boolean isGuardianShieldActive() {
        return guardianShieldDurationTimer > 0f;
    }

    public boolean wasGuardianShieldTriggered() {
        return guardianShieldTriggered;
    }

    public float getGuardianShieldCooldown() {
        float cooldownReduction = build.getMagicalStats().getAbilityCooldownReduction();
        return GUARDIAN_SHIELD_BASE_COOLDOWN * (1f - cooldownReduction);
    }

    public float getGuardianShieldCooldownRemaining() {
        return Math.max(0f, guardianShieldCooldownTimer);
    }

    public float getGuardianShieldCooldownRatio() {
        float cooldown = getGuardianShieldCooldown();

        if (cooldown <= 0f) {
            return 1f;
        }

        return 1f - Math.max(0f, guardianShieldCooldownTimer) / cooldown;
    }

    public float getGuardianShieldDurationRemaining() {
        return Math.max(0f, guardianShieldDurationTimer);
    }

    public float getGuardianShieldDuration() {
        return GUARDIAN_SHIELD_DURATION;
    }

    public boolean isGuardianShieldReady() {
        return guardianShieldCooldownTimer <= 0f && !isGuardianShieldActive();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getFacing() {
        return facing;
    }

    public PlayerBuild getBuild() {
        return build;
    }

    public PlayerStateMachine getStateMachine() {
        return stateMachine;
    }

    public PlayerController getController() {
        return controller;
    }

    public CombatStats getCombatStats() {
        return build.getCombatStats();
    }

    @Override
    public StatusEffectSystem getStatusEffectSystem() {
        return statusEffectSystem;
    }

    @Override
    public MagicalStats getMagicalStats() {
        return build.getMagicalStats();
    }

    public ManaProfile getManaProfile() {
        return build.getManaProfile();
    }
}
