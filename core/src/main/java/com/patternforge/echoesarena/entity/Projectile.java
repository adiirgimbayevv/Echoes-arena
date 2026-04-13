package com.patternforge.echoesarena.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.HitData;

public class Projectile {

    private final Vector2 position;
    private final Vector2 velocity;
    private final Circle bounds;
    private final HitData hitData;
    private final float maxRange;
    private final float radius;

    private float distanceTravelled;
    private boolean expired;

    public Projectile(float x, float y, Vector2 direction, float speed,
                      float radius, float maxRange, HitData hitData) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(direction).nor().scl(speed);
        this.radius = radius;
        this.bounds = new Circle(x, y, radius);
        this.hitData = hitData;
        this.maxRange = maxRange;
        this.distanceTravelled = 0f;
        this.expired = false;
    }

    public void update(float delta) {
        if (expired) {
            return;
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
}
