package com.patternforge.echoesarena.enemy.ai;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.enemy.EnemyState;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public class RangedStrategy implements EnemyStrategy {

    private final float preferredRange;
    private final float minRange;
    private final float attackCooldown;
    private float attackTimer;

    public RangedStrategy(float preferredRange, float minRange, float attackCooldown) {
        this.preferredRange = preferredRange;
        this.minRange = minRange;
        this.attackCooldown = attackCooldown;
        this.attackTimer = 0f;
    }

    @Override
    public void execute(Enemy self, Player target, List<Enemy> nearbyEnemies, float delta) {
        if (attackTimer > 0) {
            attackTimer -= delta;
        }

        Vector2 toTarget = new Vector2(target.getPosition()).sub(self.getPosition());
        float distance = toTarget.len();

        if (distance < minRange) {
            Vector2 retreat = toTarget.nor().scl(-1f);
            float speed = self.getEffectiveSpeed();
            self.setVelocity(retreat.x * speed, retreat.y * speed);
            self.getStateMachine().transition(EnemyState.CHASE);
            return;
        }

        if (distance > preferredRange) {
            Vector2 approach = toTarget.nor();
            float speed = self.getEffectiveSpeed();
            self.setVelocity(approach.x * speed, approach.y * speed);
            self.getStateMachine().transition(EnemyState.CHASE);
            return;
        }

        self.setVelocity(0, 0);
        self.getStateMachine().transition(EnemyState.ATTACK);
        if (attackTimer <= 0) {
            self.performRangedAttack(target);
            attackTimer = attackCooldown;
        }
    }
}
