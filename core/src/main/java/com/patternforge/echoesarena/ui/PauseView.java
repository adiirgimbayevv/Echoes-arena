package com.patternforge.echoesarena.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseView implements Disposable {

    public interface PauseListener {
        void onResume();
        void onSave();
        void onMainMenu();
    }

    private final Stage stage;
    private final Skin skin;

    public PauseView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    public void show(PauseListener listener) {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.center();

        Label titleLabel = new Label("Paused", skin);
        titleLabel.setColor(Color.WHITE);
        root.add(titleLabel).padBottom(20).row();

        TextButton resumeBtn = new TextButton("Resume", skin);
        TextButton saveBtn = new TextButton("Save Game", skin);
        TextButton menuBtn = new TextButton("Main Menu", skin);

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) listener.onResume();
            }
        });

        saveBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) listener.onSave();
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) listener.onMainMenu();
            }
        });

        root.add(resumeBtn).width(200).pad(6).row();
        root.add(saveBtn).width(200).pad(6).row();
        root.add(menuBtn).width(200).pad(6).row();

        stage.addActor(root);
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
