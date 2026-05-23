package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.patternforge.echoesarena.core.AudioService;
import com.patternforge.echoesarena.core.GameContext;

public class PauseScreen extends ScreenAdapter {

    private final GameContext context;
    private final Screen previousScreen;

    private Stage stage;
    private Skin skin;
    private Table rootTable;

    private boolean settingsOpen;

    public PauseScreen(GameContext context, Screen previousScreen) {
        this.context = context;
        this.previousScreen = previousScreen;
    }

    @Override
    public void show() {
        skin = context.getAssetService().getSkin();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        settingsOpen = false;

        buildMainPauseMenu();
    }

    private void buildMainPauseMenu() {
        stage.clear();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();

        Label titleLabel = new Label("PAUSED", skin);

        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton settingsButton = new TextButton("Settings / Controls", skin);
        TextButton menuButton = new TextButton("Main Menu", skin);

        rootTable.add(titleLabel).pad(12f).row();
        rootTable.add(resumeButton).width(260f).height(55f).pad(8f).row();
        rootTable.add(settingsButton).width(260f).height(55f).pad(8f).row();
        rootTable.add(menuButton).width(260f).height(55f).pad(8f).row();

        stage.addActor(rootTable);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                resumeGame();
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                settingsOpen = true;
                buildSettingsMenu();
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                Gdx.input.setInputProcessor(null);
                context.getScreenRouter().goToMainMenu();
            }
        });
    }

    private void buildSettingsMenu() {
        stage.clear();

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();

        Label titleLabel = new Label("SETTINGS / CONTROLS", skin);
        final Label musicLabel = new Label("", skin);
        final Label sfxLabel = new Label("", skin);
        final Slider musicSlider = new Slider(0f, 1f, 0.05f, false, skin);
        final Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, skin);

        musicSlider.setValue(context.getAudioService().getMusicVolume());
        sfxSlider.setValue(context.getAudioService().getSfxVolume());
        updateVolumeLabels(musicLabel, sfxLabel);

        Label controlsLabel = new Label(
            "Move: WASD / Arrow Keys\n"
                + "Dash: Space\n"
                + "Ranged Homing: Left Mouse Button\n"
                + "Melee Strike: Right Mouse Button\n"
                + "Ultimate Fireball: Q\n"
                + "Ultimate Frost Rift: E\n"
                + "Pause: Esc\n\n"
                + "Goal: survive all waves and clear 10 levels.",
            skin
        );

        TextButton backButton = new TextButton("Back", skin);
        TextButton resumeButton = new TextButton("Resume", skin);

        rootTable.add(titleLabel).pad(12f).row();
        rootTable.add(musicLabel).width(520f).padTop(8f).row();
        rootTable.add(musicSlider).width(420f).height(36f).pad(6f).row();
        rootTable.add(sfxLabel).width(520f).padTop(8f).row();
        rootTable.add(sfxSlider).width(420f).height(36f).pad(6f).row();
        rootTable.add(controlsLabel).width(520f).pad(12f).row();
        rootTable.add(backButton).width(260f).height(55f).pad(8f).row();
        rootTable.add(resumeButton).width(260f).height(55f).pad(8f).row();

        stage.addActor(rootTable);

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().setMusicVolume(musicSlider.getValue());
                updateVolumeLabels(musicLabel, sfxLabel);
            }
        });

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().setSfxVolume(sfxSlider.getValue());
                updateVolumeLabels(musicLabel, sfxLabel);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                settingsOpen = false;
                buildMainPauseMenu();
            }
        });

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                resumeGame();
            }
        });
    }

    private void updateVolumeLabels(Label musicLabel, Label sfxLabel) {
        musicLabel.setText("Music Volume: " + Math.round(context.getAudioService().getMusicVolume() * 100f) + "%");
        sfxLabel.setText("SFX Volume: " + Math.round(context.getAudioService().getSfxVolume() * 100f) + "%");
    }

    private void resumeGame() {
        Gdx.input.setInputProcessor(null);
        context.getScreenRouter().returnTo(previousScreen);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.04f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            if (settingsOpen) {
                settingsOpen = false;
                buildMainPauseMenu();
            } else {
                resumeGame();
            }
            return;
        }

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
        if (stage != null) {
            stage.dispose();
        }
    }
}
