package com.patternforge.echoesarena.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class AssetService implements Disposable {

    private final AssetManager manager;

    public AssetService() {
        this.manager = new AssetManager();
    }

    public void loadUiSkin() {
        manager.load("ui/uiskin.json", Skin.class);
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public boolean update() {
        return manager.update();
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public <T> T get(String path, Class<T> type) {
        return manager.get(path, type);
    }

    public Skin getSkin() {
        return manager.get("ui/uiskin.json", Skin.class);
    }

    public AssetManager getManager() {
        return manager;
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
