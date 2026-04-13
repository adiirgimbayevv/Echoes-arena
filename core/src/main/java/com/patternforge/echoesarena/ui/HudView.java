
package com.patternforge.echoesarena.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.patternforge.echoesarena.entity.Player;
import com.patternforge.echoesarena.progression.LevelUpService;

public class HudView implements Disposable {

    private final Stage stage;
    private final Skin skin;

    private ProgressBar hpBar;
    private ProgressBar manaBar;
    private ProgressBar xpBar;
    private Label hpLabel;
    private Label manaLabel;
    private Label levelLabel;
    private Label stageLabel;
    private Label waveLabel;

    public HudView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        buildLayout();
    }

    private void buildLayout() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().left().pad(8);

        hpBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        manaBar = new ProgressBar(0f, 1f, 0.01f, false, skin);
        xpBar = new ProgressBar(0f, 1f, 0.01f, false, skin);

        hpLabel = new Label("HP", skin);
        hpLabel.setColor(Color.RED);
        manaLabel = new Label("MP", skin);
        manaLabel.setColor(Color.CYAN);
        levelLabel = new Label("Lv 1", skin);
        stageLabel = new Label("Stage -", skin);
        waveLabel = new Label("Wave -", skin);

        Table statsLeft = new Table();
        statsLeft.add(hpLabel).left().row();
        statsLeft.add(hpBar).width(120).height(8).row();
        statsLeft.add(manaLabel).left().row();
        statsLeft.add(manaBar).width(120).height(8).row();
        statsLeft.add(xpBar).width(120).height(5).row();
        statsLeft.add(levelLabel).left();

        Table statsRight = new Table();
        statsRight.add(stageLabel).right().row();
        statsRight.add(waveLabel).right();

        root.add(statsLeft).expandX().left();
        root.add(statsRight).right().top();

        stage.addActor(root);
    }

    public void update(Player player, LevelUpService levelUpService, int stageId, int waveIndex, int totalWaves) {
        float hpRatio = player.getCombatStats().getHpRatio();
        float manaRatio = player.getManaProfile().getManaRatio();
        float xpRatio = levelUpService.getXpProgress();

        hpBar.setValue(hpRatio);
        manaBar.setValue(manaRatio);
        xpBar.setValue(xpRatio);

        hpLabel.setText(String.format("HP  %.0f / %.0f",
                player.getCombatStats().getCurrentHp(),
                player.getCombatStats().getMaxHp()));

        manaLabel.setText(String.format("MP  %.0f / %.0f",
                player.getManaProfile().getCurrentMana(),
                player.getManaProfile().getMaxMana()));

        levelLabel.setText("Lv " + levelUpService.getCurrentLevel());
        stageLabel.setText("Stage " + stageId);
        waveLabel.setText("Wave " + (waveIndex + 1) + " / " + totalWaves);
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
