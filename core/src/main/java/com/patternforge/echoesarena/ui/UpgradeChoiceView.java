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
import com.patternforge.echoesarena.progression.UpgradeOption;

import java.util.List;

public class UpgradeChoiceView implements Disposable {

    public interface UpgradeChoiceListener {
        void onChoiceMade(int choiceIndex);
    }

    private final Stage stage;
    private final Skin skin;
    private UpgradeChoiceListener listener;

    public UpgradeChoiceView(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
    }

    public void show(List<UpgradeOption> choices, UpgradeChoiceListener choiceListener) {
        this.listener = choiceListener;
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.center();

        Label title = new Label("LEVEL UP — Choose an Upgrade", skin);
        title.setColor(Color.GOLD);
        root.add(title).colspan(choices.size()).padBottom(20).row();

        for (int i = 0; i < choices.size(); i++) {
            final int index = i;
            UpgradeOption option = choices.get(i);

            Table card = new Table();
            card.setBackground(skin.getDrawable("list"));
            card.pad(12);

            Label nameLabel = new Label(option.getDisplayName(), skin);
            nameLabel.setColor(Color.WHITE);
            Label descLabel = new Label(option.getDescription(), skin);
            descLabel.setColor(Color.LIGHT_GRAY);
            Label catLabel = new Label("[" + option.getCategory().name() + "]", skin);
            catLabel.setColor(Color.GRAY);

            TextButton selectButton = new TextButton("Select", skin);
            selectButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (listener != null) {
                        listener.onChoiceMade(index);
                    }
                }
            });

            card.add(nameLabel).left().row();
            card.add(descLabel).left().row();
            card.add(catLabel).left().padBottom(8).row();
            card.add(selectButton).expandX().fillX();

            root.add(card).width(180).padLeft(8).padRight(8).top();
        }

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
