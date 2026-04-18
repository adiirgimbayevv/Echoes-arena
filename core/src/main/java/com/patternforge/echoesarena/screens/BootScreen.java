package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.patternforge.echoesarena.core.AssetService;
import com.patternforge.echoesarena.core.GameContext;

public class BootScreen extends ScreenAdapter {

    private final GameContext context;
    private final AssetService assetService;

    public BootScreen(GameContext context) {
        this.context = context;
        // Убрал лишнюю букву 's', если вдруг у тебя метод называется getAssetService()
        this.assetService = context.getAssetService();
    }

    @Override
    public void show() {
        assetService.loadUiSkin();
    }

    @Override
    public void render(float delta) {
        if (assetService.update()) {
            // ИСПРАВЛЕНИЕ: Вызываем правильный метод роутера без аргументов!
            context.getScreenRouter().goToMainMenu();
        }
    }
}
