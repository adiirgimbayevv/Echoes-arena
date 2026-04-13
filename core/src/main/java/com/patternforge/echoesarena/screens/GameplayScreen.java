package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.patternforge.echoesarena.config.GameConfig;
import com.patternforge.echoesarena.core.GameContext;
import com.patternforge.echoesarena.entity.Player;
import com.patternforge.echoesarena.player.PlayerBuild;

public class GameplayScreen extends ScreenAdapter {

    private final GameContext context;
    private Player player;

    public GameplayScreen(GameContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        float cx = GameConfig.VIEWPORT_WIDTH / 2f;
        float cy = GameConfig.VIEWPORT_HEIGHT / 2f;
        player = new Player(cx, cy, new PlayerBuild());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update(delta);

        if (player.isDead()) {
            context.getScreenRouter().goTo(new GameOverScreen(context));
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            context.getScreenRouter().goTo(new PauseScreen(context, this));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
    }

    public Player getPlayer() {
        return player;
    }
}
