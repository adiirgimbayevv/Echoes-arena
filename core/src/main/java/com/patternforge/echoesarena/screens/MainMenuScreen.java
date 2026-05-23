package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import com.patternforge.echoesarena.player.PlayerClass;
import com.patternforge.echoesarena.player.PlayerSelection;

public class MainMenuScreen extends ScreenAdapter {

    private static final int PREVIEW_FRAMES = 4;
    private static final float PREVIEW_FRAME_TIME = 0.22f;

    private final GameContext context;

    private Stage stage;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private GlyphLayout glyphLayout;
    private Texture menuBackgroundTexture;

    private Texture bruiserPreviewTexture;
    private Texture runnerPreviewTexture;
    private Texture magePreviewTexture;

    private TextButton bruiserSelectButton;
    private TextButton runnerSelectButton;
    private TextButton mageSelectButton;
    private Label selectedClassLabel;
    private Label selectedClassDescriptionLabel;

    private float stateTime;

    public MainMenuScreen(GameContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        menuBackgroundTexture = loadTexture(resolveMenuBackgroundPath(), Texture.TextureFilter.Linear);
        bruiserPreviewTexture = loadTexture("external/anokolisa/Entities/Npc's/Knight/Idle/Idle-Sheet.png", Texture.TextureFilter.Nearest);
        runnerPreviewTexture = loadTexture("external/anokolisa/Entities/Npc's/Rogue/Idle/Idle-Sheet.png", Texture.TextureFilter.Nearest);
        magePreviewTexture = loadTexture("external/anokolisa/Entities/Npc's/Wizzard/Idle/Idle-Sheet.png", Texture.TextureFilter.Nearest);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.15f);
        titleFont.setColor(0.88f, 0.95f, 1f, 1f);
        glyphLayout = new GlyphLayout();

        Skin skin = context.getAssetService().getSkin();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        buildMenu(skin);
        context.getAudioService().playMusic(AudioService.MUSIC_MENU, true);
        stateTime = 0f;
    }

    private Texture loadTexture(String path, Texture.TextureFilter filter) {
        Texture texture = new Texture(Gdx.files.internal(path));
        texture.setFilter(filter, filter);
        return texture;
    }

    private void buildMenu(Skin skin) {
        stage.clear();

        Table root = new Table();
        root.setFillParent(true);
        root.left().center();
        root.padLeft(92f);

        TextButton playButton = new TextButton("PLAY", skin);
        TextButton settingsButton = new TextButton("SETTINGS", skin);
        TextButton changeCharacterButton = new TextButton("CHANGE CLASS", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        root.add(playButton).width(330f).height(62f).pad(8f).row();
        root.add(settingsButton).width(330f).height(56f).pad(8f).row();
        root.add(changeCharacterButton).width(330f).height(56f).pad(8f).row();
        root.add(exitButton).width(330f).height(56f).pad(8f).row();
        stage.addActor(root);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                context.getScreenRouter().goToGameplay();
            }
        });

        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                buildSettingsMenu(skin);
            }
        });

        changeCharacterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                buildCharacterMenu(skin);
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                Gdx.app.exit();
            }
        });
    }

    private void buildCharacterMenu(final Skin skin) {
        stage.clear();

        Table root = new Table();
        root.setFillParent(true);
        root.left().center();
        root.padLeft(92f);

        Label title = new Label("CHOOSE HERO CLASS", skin);
        bruiserSelectButton = new TextButton(PlayerClass.BRUISER.getDisplayName(), skin);
        runnerSelectButton = new TextButton(PlayerClass.RUNNER.getDisplayName(), skin);
        mageSelectButton = new TextButton(PlayerClass.MAGE.getDisplayName(), skin);
        selectedClassLabel = new Label("", skin);
        selectedClassDescriptionLabel = new Label("", skin);
        selectedClassDescriptionLabel.setWrap(true);
        TextButton backButton = new TextButton("BACK", skin);

        root.add(title).width(420f).pad(10f).left().row();
        root.add(bruiserSelectButton).width(370f).height(56f).pad(8f).left().row();
        root.add(runnerSelectButton).width(370f).height(56f).pad(8f).left().row();
        root.add(mageSelectButton).width(370f).height(56f).pad(8f).left().row();
        root.add(selectedClassLabel).width(420f).padTop(16f).left().row();
        root.add(selectedClassDescriptionLabel).width(420f).padTop(6f).left().row();
        root.add(backButton).width(330f).height(56f).padTop(18f).left().row();
        stage.addActor(root);

        bruiserSelectButton.addListener(characterSelectListener(PlayerClass.BRUISER));
        runnerSelectButton.addListener(characterSelectListener(PlayerClass.RUNNER));
        mageSelectButton.addListener(characterSelectListener(PlayerClass.MAGE));

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                buildMenu(skin);
            }
        });

        refreshClassSelectionUi();
    }

    private ChangeListener characterSelectListener(final PlayerClass playerClass) {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                context.getAudioService().playSound(AudioService.SFX_BUTTON);
                PlayerSelection.setSelectedClass(playerClass);
                refreshClassSelectionUi();
            }
        };
    }

    private void refreshClassSelectionUi() {
        if (bruiserSelectButton == null || runnerSelectButton == null || mageSelectButton == null) {
            return;
        }

        PlayerClass selected = PlayerSelection.getSelectedClass();
        styleClassButton(bruiserSelectButton, selected == PlayerClass.BRUISER, PlayerClass.BRUISER.getColor());
        styleClassButton(runnerSelectButton, selected == PlayerClass.RUNNER, PlayerClass.RUNNER.getColor());
        styleClassButton(mageSelectButton, selected == PlayerClass.MAGE, PlayerClass.MAGE.getColor());

        selectedClassLabel.setText("Selected: " + selected.getDisplayName());
        selectedClassLabel.setColor(selected.getColor());
        selectedClassDescriptionLabel.setText(selected.getDescription());
        selectedClassDescriptionLabel.setColor(0.88f, 0.94f, 1f, 1f);
    }

    private void styleClassButton(TextButton button, boolean selected, Color accent) {
        button.getLabel().setColor(selected ? accent : Color.WHITE);
        button.setTransform(true);
        button.setScale(selected ? 1.05f : 1f);
    }

    private void buildSettingsMenu(final Skin skin) {
        stage.clear();

        final Label musicLabel = new Label("", skin);
        final Label sfxLabel = new Label("", skin);
        final Slider musicSlider = new Slider(0f, 1f, 0.05f, false, skin);
        final Slider sfxSlider = new Slider(0f, 1f, 0.05f, false, skin);
        musicSlider.setValue(context.getAudioService().getMusicVolume());
        sfxSlider.setValue(context.getAudioService().getSfxVolume());
        updateVolumeLabels(musicLabel, sfxLabel);

        Table root = new Table();
        root.setFillParent(true);
        root.left().center();
        root.padLeft(92f);

        Label title = new Label("SETTINGS", skin);
        Label controlsLabel = new Label(
            "Move: WASD / Arrow Keys\n"
                + "Dash: Space\n"
                + "Ranged Homing: Left Mouse Button\n"
                + "Melee Strike: Right Mouse Button\n"
                + "Ultimate Fireball: Q\n"
                + "Ultimate Frost Rift: E\n"
                + "Pause: Esc",
            skin
        );
        TextButton backButton = new TextButton("BACK", skin);

        root.add(title).width(360f).pad(10f).left().row();
        root.add(musicLabel).width(360f).padTop(12f).left().row();
        root.add(musicSlider).width(330f).height(36f).pad(8f).left().row();
        root.add(sfxLabel).width(360f).padTop(12f).left().row();
        root.add(sfxSlider).width(330f).height(36f).pad(8f).left().row();
        root.add(controlsLabel).width(360f).padTop(14f).left().row();
        root.add(backButton).width(330f).height(56f).padTop(18f).left().row();
        stage.addActor(root);

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
                buildMenu(skin);
            }
        });
    }

    private void updateVolumeLabels(Label musicLabel, Label sfxLabel) {
        musicLabel.setText("Music Volume: " + Math.round(context.getAudioService().getMusicVolume() * 100f) + "%");
        sfxLabel.setText("SFX Volume: " + Math.round(context.getAudioService().getSfxVolume() * 100f) + "%");
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        Gdx.gl.glClearColor(0.01f, 0.015f, 0.03f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        drawMenuBackground();
        drawTitleAndSelection();
        drawCharacterPreviewCard();

        stage.act(delta);
        stage.draw();
    }

    private void drawMenuBackground() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        drawCover(menuBackgroundTexture, 0f, 0f, width, height);
        batch.setColor(0f, 0f, 0f, 0.26f);
        drawCover(menuBackgroundTexture, 0f, 0f, width, height);
        batch.setColor(Color.WHITE);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.48f);
        shapeRenderer.rect(0f, 0f, width * 0.44f, height);
        shapeRenderer.setColor(0.02f, 0.06f, 0.10f, 0.20f);
        shapeRenderer.rect(width * 0.44f, 0f, width * 0.15f, height);
        shapeRenderer.end();
    }

    private void drawCover(Texture texture, float x, float y, float width, float height) {
        float scale = Math.max(width / texture.getWidth(), height / texture.getHeight());
        float drawWidth = texture.getWidth() * scale;
        float drawHeight = texture.getHeight() * scale;
        float drawX = x + (width - drawWidth) * 0.5f;
        float drawY = y + (height - drawHeight) * 0.5f;
        batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }

    private void drawTitleAndSelection() {
        PlayerClass selected = PlayerSelection.getSelectedClass();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        titleFont.setColor(0.90f, 0.97f, 1f, 1f);
        drawCenteredText("ECHOES ARENA", Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() - 76f);
        titleFont.getData().setScale(1.16f);
        titleFont.setColor(selected.getColor());
        drawCenteredText(selected.getDisplayName(), Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() - 122f);
        titleFont.setColor(0.85f, 0.90f, 0.97f, 1f);
        drawCenteredText("Class stats are applied in gameplay", Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() - 152f);
        titleFont.getData().setScale(2.15f);
        batch.end();
    }

    private void drawCharacterPreviewCard() {
        Texture sheet = getSelectedPreviewTexture();
        if (sheet == null) {
            return;
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float cardWidth = Math.min(330f, screenWidth * 0.26f);
        float cardHeight = Math.min(420f, screenHeight * 0.62f);
        float cardX = screenWidth - cardWidth - 88f;
        float cardY = screenHeight * 0.16f;
        Color accent = PlayerSelection.getSelectedClass().getColor();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, 0.52f);
        shapeRenderer.rect(cardX, cardY, cardWidth, cardHeight);
        shapeRenderer.setColor(0.03f, 0.05f, 0.09f, 0.92f);
        shapeRenderer.rect(cardX + 6f, cardY + 6f, cardWidth - 12f, cardHeight - 12f);
        shapeRenderer.setColor(accent.r, accent.g, accent.b, 0.35f + 0.18f * (0.5f + 0.5f * MathUtils.sin(stateTime * 2.2f)));
        shapeRenderer.rect(cardX + 12f, cardY + 12f, cardWidth - 24f, cardHeight - 24f);
        shapeRenderer.end();

        TextureRegion frame = getAnimatedSheetFrame(sheet, PREVIEW_FRAMES, PREVIEW_FRAME_TIME);
        float frameAspect = frame.getRegionWidth() / (float) frame.getRegionHeight();
        float targetHeight = cardHeight * 0.48f;
        float targetWidth = targetHeight * frameAspect;
        targetWidth = Math.min(targetWidth, cardWidth * 0.72f);
        targetHeight = targetWidth / frameAspect;
        float drawX = cardX + (cardWidth - targetWidth) * 0.5f;
        float drawY = cardY + cardHeight * 0.35f - targetHeight * 0.5f;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(frame, drawX, drawY, targetWidth, targetHeight);

        titleFont.getData().setScale(1.05f);
        titleFont.setColor(accent);
        drawCenteredText(PlayerSelection.getSelectedClass().getDisplayName(), cardX + cardWidth * 0.5f, cardY + 58f);
        titleFont.getData().setScale(2.15f);
        batch.end();
    }

    private TextureRegion getAnimatedSheetFrame(Texture sheet, int frames, float frameTime) {
        int frameCount = Math.max(1, frames);
        int frameWidth = Math.max(1, sheet.getWidth() / frameCount);
        int frameHeight = sheet.getHeight();
        int frameIndex = ((int) (stateTime / frameTime)) % frameCount;
        return new TextureRegion(sheet, frameIndex * frameWidth, 0, frameWidth, frameHeight);
    }

    private Texture getSelectedPreviewTexture() {
        PlayerClass selected = PlayerSelection.getSelectedClass();
        if (selected == PlayerClass.RUNNER) {
            return runnerPreviewTexture;
        }
        if (selected == PlayerClass.MAGE) {
            return magePreviewTexture;
        }
        return bruiserPreviewTexture;
    }

    private String resolveMenuBackgroundPath() {
        String[] candidates = {
            "external/menu-background/lost_kingdom_2560x1440.png",
            "external/menu-background/lost_kingdom_3840x2160.png",
            "external/menu-background/lost_kingdom_1280x720.png",
            "art/maps/fantasy_outdoor_night.png"
        };

        for (String path : candidates) {
            if (Gdx.files.internal(path).exists()) {
                return path;
            }
        }
        return "art/maps/fantasy_outdoor_night.png";
    }

    private void drawCenteredText(String text, float centerX, float y) {
        glyphLayout.setText(titleFont, text);
        titleFont.draw(batch, text, centerX - glyphLayout.width / 2f, y);
    }

    @Override
    public void resize(int width, int height) {
        if (camera != null) {
            camera.setToOrtho(false, width, height);
        }
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (titleFont != null) {
            titleFont.dispose();
        }

        disposeTexture(menuBackgroundTexture);
        disposeTexture(bruiserPreviewTexture);
        disposeTexture(runnerPreviewTexture);
        disposeTexture(magePreviewTexture);
    }

    private void disposeTexture(Texture texture) {
        if (texture != null) {
            texture.dispose();
        }
    }
}
