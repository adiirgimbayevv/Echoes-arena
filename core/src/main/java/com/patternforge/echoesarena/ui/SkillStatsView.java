package com.patternforge.echoesarena.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.patternforge.echoesarena.player.PlayerBuild;

public class SkillStatsView implements Disposable {

    private final Stage stage;
    private final Skin skin;
    private boolean visible;

    private Label speedLabel;
    private Label hpLabel;
    private Label defenseLabel;
    private Label damageLabel;
    private Label spellPowerLabel;
    private Label maxManaLabel;
    private Label manaRegenLabel;
    private Label cdrLabel;

    public SkillStatsView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        this.visible = false;
        buildLayout();
    }

    private void buildLayout() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().right().pad(8);

        Table panel = new Table();
        panel.setBackground(skin.getDrawable("list"));
        panel.pad(10);

        Label title = new Label("Player Stats", skin);
        title.setColor(Color.GOLD);
        panel.add(title).left().colspan(2).padBottom(6).row();

        speedLabel = addRow(panel, "Speed:", Color.WHITE);
        hpLabel = addRow(panel, "Max HP:", Color.RED);
        defenseLabel = addRow(panel, "Defense:", Color.ORANGE);
        damageLabel = addRow(panel, "Damage x:", Color.YELLOW);
        spellPowerLabel = addRow(panel, "Spell Power:", Color.CYAN);
        maxManaLabel = addRow(panel, "Max Mana:", Color.BLUE);
        manaRegenLabel = addRow(panel, "Mana Regen:", Color.SKY);
        cdrLabel = addRow(panel, "CDR:", Color.GREEN);

        root.add(panel).top().right();
        stage.addActor(root);
    }

    private Label addRow(Table table, String labelText, Color color) {
        Label keyLabel = new Label(labelText, skin);
        keyLabel.setColor(Color.GRAY);
        Label valueLabel = new Label("-", skin);
        valueLabel.setColor(color);
        table.add(keyLabel).left().padRight(8);
        table.add(valueLabel).right().row();
        return valueLabel;
    }

    public void update(PlayerBuild build) {
        speedLabel.setText(String.format("%.1f", build.getPhysicalStats().getSpeed()));
        hpLabel.setText(String.format("%.0f", build.getCombatStats().getMaxHp()));
        defenseLabel.setText(String.format("%.1f", build.getCombatStats().getDefense()));
        damageLabel.setText(String.format("%.2f", build.getCombatStats().getDamageMultiplier()));
        spellPowerLabel.setText(String.format("%.1f", build.getMagicalStats().getSpellPower()));
        maxManaLabel.setText(String.format("%.0f", build.getManaProfile().getMaxMana()));
        manaRegenLabel.setText(String.format("%.1f/s", build.getManaProfile().getManaRegenPerSecond()));
        cdrLabel.setText(String.format("%.0f%%", build.getMagicalStats().getAbilityCooldownReduction() * 100f));
    }

    public void toggle() {
        visible = !visible;
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            toggle();
        }
    }

    public void render() {
        if (!visible) {
            return;
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
