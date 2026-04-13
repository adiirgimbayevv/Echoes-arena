package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.patternforge.echoesarena.core.AssetService;
import com.patternforge.echoesarena.core.GameContext;

public class BootScreen extends ScreenAdapter {

    private final GameContext context;
    private final AssetService assetService;

    public BootScreen(GameContext context) {
        this.context = context;
        this.assetService = context.getAssetService();
    }

    @Override
    public void show() {
        assetService.loadUiSkin();
    }

    @Override
    public void render(float delta) {
        if (assetService.update()) {
            context.getScreenRouter().goTo(new MainMenuScreen(context));
        }
    }
}
