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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.patternforge.echoesarena.core.AudioService;
import com.patternforge.echoesarena.core.GameContext;

public class VictoryRoomScreen extends ScreenAdapter {

    private final GameContext context;

    private Stage stage;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private Texture backgroundTexture;
    private Texture dungeonPropsTexture;
    private TextureRegion pedestalRegion;

    private float stateTime;

    public VictoryRoomScreen(GameContext context) {
        this.context = context;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.3f);
        glyphLayout = new GlyphLayout();

        backgroundTexture = new Texture(Gdx.files.internal(resolveBackgroundPath()));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        dungeonPropsTexture = new Texture(Gdx.files.internal("external/anokolisa/Environment/Props/Static/Dungeon_Props.png"));
        dungeonPropsTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        pedestalRegion = new TextureRegion(dungeonPropsTexture, 0, 48, 16, 16);

        Skin skin = context.getAssetService().getSkin();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        context.getAudioService().playMusic(AudioService.MUSIC_MENU, true);
        context.getAudioService().playSound(AudioService.SFX_VICTORY);

        Table root = new Table();
        root.setFillParent(true);
        root.bottom();
        root.padBottom(48f);

        TextButton restartButton = new TextButton("PLAY AGAIN", skin);
        TextButton menuButton = new TextButton("MAIN MENU", skin);
        root.add(restartButton).width(280f).height(58f).pad(10f);
        root.add(menuButton).width(280f).height(58f).pad(10f);
        stage.addActor(root);

        restartButton.addListener(new ChangeListener() {
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
        stateTime += delta;
        Gdx.gl.glClearColor(0.01f, 0.02f, 0.03f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        drawBackground();
        drawVictoryScene();

        stage.act(delta);
        stage.draw();
    }

    private void drawBackground() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(backgroundTexture, 0f, 0f, width, height);
        batch.setColor(0f, 0f, 0f, 0.60f);
        batch.draw(backgroundTexture, 0f, 0f, width, height);
        batch.setColor(Color.WHITE);
        batch.end();
    }

    private void drawVictoryScene() {
        float cx = Gdx.graphics.getWidth() * 0.5f;
        float cy = Gdx.graphics.getHeight() * 0.46f;
        float pulse = 0.5f + 0.5f * MathUtils.sin(stateTime * 2.2f);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.10f, 0.12f, 0.18f, 0.92f);
        shapeRenderer.circle(cx, cy, 88f);
        shapeRenderer.setColor(0.22f, 0.78f, 1f, 0.22f + 0.22f * pulse);
        shapeRenderer.circle(cx, cy, 122f + pulse * 20f);

        for (int i = 0; i < 44; i++) {
            float angle = i * 8.2f + stateTime * 24f;
            float dist = 66f + (i % 7) * 11f + MathUtils.sin(stateTime * 2f + i) * 6f;
            float px = cx + MathUtils.cosDeg(angle) * dist;
            float py = cy + MathUtils.sinDeg(angle) * dist;
            float radius = 1.8f + (i % 3) * 0.8f;
            shapeRenderer.setColor(1f, 0.85f, 0.42f, 0.22f + 0.45f * pulse);
            shapeRenderer.circle(px, py, radius);
        }

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 0.96f);
        batch.draw(pedestalRegion, cx - 48f, cy - 36f, 96f, 96f);

        font.setColor(1f, 0.93f, 0.52f, 1f);
        drawCenteredText("VICTORY", cx, Gdx.graphics.getHeight() - 116f);
        font.setColor(0.82f, 0.94f, 1f, 1f);
        drawCenteredText("The arena is conquered.", cx, Gdx.graphics.getHeight() - 156f);
        font.setColor(Color.WHITE);
        batch.end();
    }

    private String resolveBackgroundPath() {
        String[] candidates = {
            "external/menu-background/lost_kingdom_2560x1440.png",
            "external/menu-background/lost_kingdom_1280x720.png"
        };

        for (String candidate : candidates) {
            if (Gdx.files.internal(candidate).exists()) {
                return candidate;
            }
        }

        return "art/maps/fantasy_outdoor_night.png";
    }

    private void drawCenteredText(String text, float centerX, float y) {
        glyphLayout.setText(font, text);
        font.draw(batch, text, centerX - glyphLayout.width / 2f, y);
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
        if (font != null) {
            font.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        if (dungeonPropsTexture != null) {
            dungeonPropsTexture.dispose();
        }
    }
}
