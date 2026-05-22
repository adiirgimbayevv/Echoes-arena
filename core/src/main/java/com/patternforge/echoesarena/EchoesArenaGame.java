package com.patternforge.echoesarena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.patternforge.echoesarena.core.AssetService;
import com.patternforge.echoesarena.core.AudioService;
import com.patternforge.echoesarena.core.EventBus;
import com.patternforge.echoesarena.core.GameContext;
import com.patternforge.echoesarena.core.SaveService;
import com.patternforge.echoesarena.core.ScreenRouter;

public class EchoesArenaGame extends Game {
    public SpriteBatch batch;
    public GameContext context;
    public ScreenRouter screenRouter;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 1. Создаем независимые сервисы
        AssetService assetService = new AssetService();

        // ИСПРАВЛЕНИЕ: Передаем assetService внутрь AudioService
        AudioService audioService = new AudioService(assetService);

        EventBus eventBus = new EventBus();
        SaveService saveService = new SaveService();

        // 2. Создаем ScreenRouter ПЕРЕД GameContext (ему нужна только игра)
        screenRouter = new ScreenRouter(this);

        // 3. Создаем GameContext В ТОЧНОМ ПОРЯДКЕ, как просит компилятор:
        context = new GameContext(assetService, audioService, eventBus, screenRouter, saveService);

        // 4. Запускаем Экран Загрузки (А НЕ Главное меню), чтобы загрузить текстуры!
        screenRouter.goToBoot();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (screen != null) screen.dispose();
    }
}
