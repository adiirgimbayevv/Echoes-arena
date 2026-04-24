package com.patternforge.echoesarena.enemy;

public class EnemyStateMachine {

    private EnemyState current;

    public EnemyStateMachine() {
        this.current = EnemyState.IDLE;
    }

    public void transition(EnemyState next) {
        if (!canTransition(current, next)) {
            return;
        }
        current = next;
    }

    private boolean canTransition(EnemyState from, EnemyState to) {
        if (from == EnemyState.DEAD) {
            return false;
        }
        if (from == EnemyState.STUNNED && to != EnemyState.IDLE && to != EnemyState.DEAD) {
            return false;
        }
        return true;
    }

    public void forceState(EnemyState state) {
        current = state;
    }

    public boolean is(EnemyState state) {
        return current == state;
    }

    public EnemyState getCurrent() {
        return current;
    }
}
