package com.patternforge.echoesarena.entity;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.player.PlayerController;
import com.patternforge.echoesarena.player.PlayerState;
import com.patternforge.echoesarena.player.PlayerStateMachine;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.PhysicalStats;

public class Player {

    private final Vector2 position;
    private final Vector2 velocity;
    private final Vector2 facing;

    private final PlayerBuild build;
    private final PlayerStateMachine stateMachine;
    private final PlayerController controller;

    private float dashCooldownTimer;
    private float dashDurationTimer;
    private static final float DASH_DURATION = 0.15f;

    public Player(float x, float y, PlayerBuild build) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.facing = new Vector2(1, 0);
        this.build = build;
        this.stateMachine = new PlayerStateMachine();
        this.controller = new PlayerController();
        this.dashCooldownTimer = 0f;
        this.dashDurationTimer = 0f;
    }

    public void update(float delta) {
        if (stateMachine.is(PlayerState.DEAD)) {
            return;
        }

        controller.update();
        build.getManaProfile().update(delta);

        if (dashCooldownTimer > 0) {
            dashCooldownTimer -= delta;
        }

        if (stateMachine.is(PlayerState.DASHING)) {
            dashDurationTimer -= delta;
            if (dashDurationTimer <= 0) {
                stateMachine.transition(PlayerState.IDLE);
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
        CombatStats combat = build.getCombatStats();
        combat.applyDamage(amount);
        if (combat.isDead()) {
            stateMachine.forceState(PlayerState.DEAD);
        } else {
            stateMachine.transition(PlayerState.HURT);
        }
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

    public ManaProfile getManaProfile() {
        return build.getManaProfile();
    }
}
