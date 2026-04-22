package com.patternforge.echoesarena.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class PlayerController {

    private final Vector2 moveDirection;
    private boolean dashPressed;
    private boolean attackPressed;
    private boolean ability1Pressed;
    private boolean ability2Pressed;
    private boolean ultimatePressed;

    public PlayerController() {
        this.moveDirection = new Vector2();
    }

    public void update() {
        moveDirection.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveDirection.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDirection.y -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveDirection.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveDirection.x += 1;
        }

        if (moveDirection.len2() > 0) {
            moveDirection.nor();
        }

        dashPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        attackPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        ability1Pressed = Gdx.input.isKeyJustPressed(Input.Keys.Q);
        ability2Pressed = Gdx.input.isKeyJustPressed(Input.Keys.E);
        ultimatePressed = Gdx.input.isKeyJustPressed(Input.Keys.R);
    }

    public Vector2 getMoveDirection() {
        return moveDirection;
    }

    public boolean isDashPressed() {
        return dashPressed;
    }

    public boolean isAttackPressed() {
        return attackPressed;
    }

    public boolean isAbility1Pressed() {
        return ability1Pressed;
    }

    public boolean isAbility2Pressed() {
        return ability2Pressed;
    }

    public boolean isUltimatePressed() {
        return ultimatePressed;
    }

    public boolean isMoving() {
        return moveDirection.len2() > 0;
    }
}
