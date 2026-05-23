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
import com.patternforge.echoesarena.progression.LevelUpService;
import com.patternforge.echoesarena.progression.UpgradeBranch;
import com.patternforge.echoesarena.progression.UpgradeOption;

public class UpgradeTreeView implements Disposable {

    public interface UpgradeTreeListener {
        void onBranchSelected(UpgradeBranch branch);
        void onClosed();
    }

    private final Stage stage;
    private final Skin skin;
    private UpgradeTreeListener listener;

    public UpgradeTreeView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    public void show(LevelUpService levelUpService, UpgradeTreeListener listener) {
        this.listener = listener;
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.center();

        Label title = new Label("UPGRADE TREE", skin);
        title.setColor(Color.GOLD);
        Label points = new Label("Skill Points: " + levelUpService.getPendingSkillPoints(), skin);
        points.setColor(Color.CYAN);

        int branchCount = UpgradeBranch.values().length;

        root.add(title).colspan(branchCount).padBottom(8f).row();
        root.add(points).colspan(branchCount).padBottom(18f).row();

        for (UpgradeBranch branch : UpgradeBranch.values()) {
            root.add(buildBranchCard(branch, levelUpService)).width(210f).height(220f).pad(8f).top();
        }
        root.row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (UpgradeTreeView.this.listener != null) {
                    UpgradeTreeView.this.listener.onClosed();
                }
            }
        });

        root.add(closeButton).colspan(branchCount).width(260f).height(52f).padTop(18f);
        stage.addActor(root);
    }

    private Table buildBranchCard(final UpgradeBranch branch, LevelUpService levelUpService) {
        Table card = new Table();
        card.setBackground(skin.getDrawable("list"));
        card.pad(10f);

        UpgradeOption next = levelUpService.previewBranchUpgrade(branch);
        int level = levelUpService.getBranchLevel(branch);

        Label name = new Label(branch.getDisplayName(), skin);
        name.setColor(Color.WHITE);
        Label desc = new Label(branch.getDescription(), skin);
        desc.setColor(Color.LIGHT_GRAY);
        desc.setWrap(true);
        Label current = new Label("Branch level: " + level, skin);
        current.setColor(Color.GRAY);
        Label nextLabel = new Label(next == null ? "Completed" : "Next: " + next.getDisplayName() + "\n" + next.getDescription(), skin);
        nextLabel.setColor(Color.CYAN);
        nextLabel.setWrap(true);

        TextButton upgradeButton = new TextButton(level == 0 ? "Open Branch" : "Upgrade", skin);
        upgradeButton.setDisabled(levelUpService.getPendingSkillPoints() <= 0 || next == null);
        upgradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (UpgradeTreeView.this.listener != null) {
                    UpgradeTreeView.this.listener.onBranchSelected(branch);
                }
            }
        });

        card.add(name).left().padBottom(6f).row();
        card.add(desc).width(180f).left().padBottom(8f).row();
        card.add(current).left().padBottom(8f).row();
        card.add(nextLabel).width(180f).left().expandY().top().row();
        card.add(upgradeButton).expandX().fillX().height(44f);
        return card;
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
