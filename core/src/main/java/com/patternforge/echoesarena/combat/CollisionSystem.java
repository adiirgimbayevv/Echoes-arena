package com.patternforge.echoesarena.combat;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.entity.Projectile;

import java.util.List;
import java.util.function.BiConsumer;

public class CollisionSystem {

    public interface Collidable {
        Rectangle getBoundingBox();
        boolean isAlive();
    }

    public void checkProjectileHits(List<Projectile> projectiles,
                                    List<? extends Collidable> targets,
                                    BiConsumer<Projectile, Collidable> onHit) {
        for (Projectile projectile : projectiles) {
            if (projectile.isExpired()) {
                continue;
            }
            for (Collidable target : targets) {
                if (!target.isAlive()) {
                    continue;
                }
                if (circleOverlapsRect(projectile.getBounds(), target.getBoundingBox())) {
                    onHit.accept(projectile, target);
                    projectile.expire();
                    break;
                }
            }
        }
    }

    public boolean checkMeleeHit(Vector2 attackerPos, float attackRange,
                                  Vector2 attackDir, Collidable target) {
        if (!target.isAlive()) {
            return false;
        }
        Rectangle box = target.getBoundingBox();
        Vector2 targetCenter = new Vector2(box.x + box.width / 2f, box.y + box.height / 2f);
        Vector2 toTarget = new Vector2(targetCenter).sub(attackerPos);
        float distance = toTarget.len();
        if (distance > attackRange) {
            return false;
        }
        if (attackDir.len2() > 0 && distance > 0) {
            float dot = toTarget.nor().dot(attackDir.nor());
            return dot > 0.3f;
        }
        return true;
    }

    public boolean checkAoeHit(Vector2 center, float radius, Collidable target) {
        if (!target.isAlive()) {
            return false;
        }
        Rectangle box = target.getBoundingBox();
        Circle aoe = new Circle(center.x, center.y, radius);
        return circleOverlapsRect(aoe, box);
    }

    private boolean circleOverlapsRect(Circle circle, Rectangle rect) {
        float nearestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        float nearestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));
        float dx = circle.x - nearestX;
        float dy = circle.y - nearestY;
        return (dx * dx + dy * dy) < (circle.radius * circle.radius);
    }
}
