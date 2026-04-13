package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.patternforge.echoesarena.core.GameContext;

public class PauseScreen extends ScreenAdapter {

    private final GameContext context;
    private final Screen previousScreen;
    private Stage stage;
    private Skin skin;

    public PauseScreen(GameContext context, Screen previousScreen) {
        this.context = context;
        this.previousScreen = previousScreen;
    }

    @Override
    public void show() {
        skin = context.getAssetService().getSkin();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton menuButton = new TextButton("Main Menu", skin);

        table.add(resumeButton).pad(10).row();
        table.add(menuButton).pad(10);

        stage.addActor(table);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getScreenRouter().goTo(previousScreen);
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getScreenRouter().goTo(new MainMenuScreen(context));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0.6f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
