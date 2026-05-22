package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.patternforge.echoesarena.core.AudioService;
import com.patternforge.echoesarena.core.GameContext;

public class GameOverScreen extends ScreenAdapter {

    private final GameContext context;
    private final boolean victory;

    private Stage stage;

    public GameOverScreen(GameContext context, boolean victory) {
        this.context = context;
        this.victory = victory;
    }

    @Override
    public void show() {
        Skin skin = context.getAssetService().getSkin();
        context.getAudioService().playMusic(AudioService.MUSIC_MENU, true);
        context.getAudioService().playSound(AudioService.SFX_LOSE);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        String titleText = victory ? "VICTORY!" : "GAME OVER";
        String subtitleText = victory
            ? "You survived the arena and defeated all enemies."
            : "You were defeated. Try again.";

        Label titleLabel = new Label(titleText, skin);
        Label subtitleLabel = new Label(subtitleText, skin);

        TextButton retryButton = new TextButton("Play Again", skin);
        TextButton menuButton = new TextButton("Main Menu", skin);

        table.add(titleLabel).pad(10).row();
        table.add(subtitleLabel).pad(10).row();
        table.add(retryButton).width(220f).height(55f).pad(10).row();
        table.add(menuButton).width(220f).height(55f).pad(10);

        stage.addActor(table);

        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                context.getScreenRouter().goToGameplay();
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                context.getScreenRouter().goToMainMenu();
            }
        });
    }

    @Override
    public void render(float delta) {
        if (victory) {
            Gdx.gl.glClearColor(0.02f, 0.12f, 0.06f, 1f);
        } else {
            Gdx.gl.glClearColor(0.12f, 0.02f, 0.02f, 1f);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (stage != null)
            stage.dispose();
        }
    }
