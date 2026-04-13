package com.patternforge.echoesarena.player;

public class PlayerStateMachine {

    private PlayerState current;

    public PlayerStateMachine() {
        this.current = PlayerState.IDLE;
    }

    public void transition(PlayerState next) {
        if (!canTransition(current, next)) {
            return;
        }
        current = next;
    }

    private boolean canTransition(PlayerState from, PlayerState to) {
        if (from == PlayerState.DEAD) {
            return false;
        }
        if (from == PlayerState.DASHING && to != PlayerState.HURT && to != PlayerState.DEAD) {
            return false;
        }
        return true;
    }

    public boolean is(PlayerState state) {
        return current == state;
    }

    public PlayerState getCurrent() {
        return current;
    }

    public void forceState(PlayerState state) {
        current = state;
    }
}
