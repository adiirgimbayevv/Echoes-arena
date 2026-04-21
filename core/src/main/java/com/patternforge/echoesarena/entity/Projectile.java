package com.patternforge.echoesarena.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.HitData;

public class Projectile {

    private final Vector2 position;
    private final Vector2 velocity;
    private final Circle bounds;
    private final HitData hitData;
    private final float maxRange;
    private final float radius;
    private final boolean fromPlayer;
    private final float baseSpeed;

    private CombatSystem.CombatTarget homingTarget;
    private float homingTurnRate;

    private float distanceTravelled;
    private boolean expired;

    public Projectile(float x, float y, Vector2 direction, float speed,
                      float radius, float maxRange, HitData hitData, boolean fromPlayer) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(direction).nor().scl(speed);
        this.radius = radius;
        this.bounds = new Circle(x, y, radius);
        this.hitData = hitData;
        this.maxRange = maxRange;
        this.fromPlayer = fromPlayer;
        this.baseSpeed = speed;
        this.distanceTravelled = 0f;
        this.expired = false;
        this.homingTarget = null;
        this.homingTurnRate = 0f;
    }

    public void update(float delta) {
        if (expired) {
            return;
        }

        if (homingTarget != null && homingTarget.isAlive()) {
            Vector2 toTarget = new Vector2(homingTarget.getPosition()).sub(position);
            if (toTarget.len2() > 0.001f) {
                Vector2 desiredVelocity = toTarget.nor().scl(baseSpeed);
                float alpha = Math.max(0f, Math.min(1f, homingTurnRate * delta));
                velocity.lerp(desiredVelocity, alpha);
            }
        }

        float dx = velocity.x * delta;
        float dy = velocity.y * delta;
        position.x += dx;
        position.y += dy;
        bounds.setPosition(position.x, position.y);
        distanceTravelled += Math.sqrt(dx * dx + dy * dy);
        if (distanceTravelled >= maxRange) {
            expired = true;
        }
    }

    public void expire() {
        expired = true;
    }

    public boolean isExpired() {
        return expired;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getBounds() {
        return bounds;
    }

    public HitData getHitData() {
        return hitData;
    }

    public float getRadius() {
        return radius;
    }

    public float getDistanceTravelled() {
        return distanceTravelled;
    }

    public float getMaxRange() {
        return maxRange;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean isFromPlayer() {
        return fromPlayer;
    }

    public void setHomingTarget(CombatSystem.CombatTarget homingTarget, float homingTurnRate) {
        this.homingTarget = homingTarget;
        this.homingTurnRate = Math.max(0f, homingTurnRate);
    }
}
