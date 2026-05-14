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
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.progression.MetaProgression;
import com.patternforge.echoesarena.progression.StatPointAllocator;

public class StageResultView implements Disposable {

    public interface StageResultListener {
        void onContinue();
    }

    private final Stage stage;
    private final Skin skin;

    private Label physPointsLabel;
    private Label magPointsLabel;

    public StageResultView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    public void show(int stageId, int enemiesKilled, boolean bossDefeated,
                     MetaProgression metaProgression, StatPointAllocator allocator,
                     PlayerBuild playerBuild, StageResultListener listener) {

        metaProgression.recordStageResult(stageId, enemiesKilled, bossDefeated);

        int physPoints = metaProgression.drainPhysicalPoints();
        int magPoints = metaProgression.drainMagicalPoints();
        allocator.addPhysicalPoints(physPoints);
        allocator.addMagicalPoints(magPoints);

        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.pad(20);

        Label titleLabel = new Label("Stage " + stageId + " Complete!", skin);
        titleLabel.setColor(Color.GOLD);
        root.add(titleLabel).colspan(2).padBottom(12).row();

        Label killsLabel = new Label("Enemies Defeated: " + enemiesKilled, skin);
        root.add(killsLabel).colspan(2).row();

        if (bossDefeated) {
            Label bossLabel = new Label("Boss Defeated!", skin);
            bossLabel.setColor(Color.RED);
            root.add(bossLabel).colspan(2).row();
        }

        root.add(new Label("Stat Points Earned:", skin)).colspan(2).padTop(10).row();

        physPointsLabel = new Label("Physical: " + physPoints, skin);
        physPointsLabel.setColor(Color.ORANGE);
        magPointsLabel = new Label("Magical:  " + magPoints, skin);
        magPointsLabel.setColor(Color.CYAN);

        root.add(physPointsLabel).left().row();
        root.add(magPointsLabel).left().padBottom(16).row();

        Table allocTable = buildAllocTable(allocator, playerBuild);
        root.add(allocTable).colspan(2).row();

        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (listener != null) {
                    listener.onContinue();
                }
            }
        });
        root.add(continueButton).colspan(2).width(180).padTop(16);

        stage.addActor(root);
    }

    private Table buildAllocTable(StatPointAllocator allocator, PlayerBuild build) {
        Table t = new Table();
        t.add(new Label("Spend Physical Points", skin)).colspan(2).padBottom(4).row();

        addAllocButton(t, "Speed +2", allocator, build,
                StatPointAllocator.PhysicalStatField.SPEED, true);
        addAllocButton(t, "HP +5", allocator, build,
                StatPointAllocator.PhysicalStatField.HP, true);
        addAllocButton(t, "Defense +1", allocator, build,
                StatPointAllocator.PhysicalStatField.DEFENSE, true);
        addAllocButton(t, "Damage +5%", allocator, build,
                StatPointAllocator.PhysicalStatField.DAMAGE, true);

        t.add(new Label("Spend Magical Points", skin)).colspan(2).padTop(8).padBottom(4).row();

        addAllocButton(t, "Spell Power +5", allocator, build,
                StatPointAllocator.MagicalStatField.SPELL_POWER, false);
        addAllocButton(t, "Max Mana +10", allocator, build,
                StatPointAllocator.MagicalStatField.MAX_MANA, false);
        addAllocButton(t, "Mana Regen +1", allocator, build,
                StatPointAllocator.MagicalStatField.MANA_REGEN, false);
        addAllocButton(t, "CDR +3%", allocator, build,
                StatPointAllocator.MagicalStatField.CDR, false);

        return t;
    }

    private void addAllocButton(Table table, String label, StatPointAllocator allocator,
                                PlayerBuild build, Object field, boolean isPhysical) {
        TextButton btn = new TextButton(label, skin);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isPhysical) {
                    allocator.spendPhysical((StatPointAllocator.PhysicalStatField) field, build);
                    physPointsLabel.setText("Physical: " + allocator.getAvailablePhysicalPoints());
                } else {
                    allocator.spendMagical((StatPointAllocator.MagicalStatField) field, build);
                    magPointsLabel.setText("Magical:  " + allocator.getAvailableMagicalPoints());
                }
            }
        });
        table.add(btn).pad(2).row();
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
