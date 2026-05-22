package io.github.some_example_name.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// ИСПРАВЛЕНИЕ 1: Импортируем твой настоящий класс игры вместо старого Main
import com.patternforge.echoesarena.EchoesArenaGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        // ИСПРАВЛЕНИЕ 2: Передаем EchoesArenaGame() в качестве точки входа
        return new Lwjgl3Application(new EchoesArenaGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();

        // Можешь поменять название окна на правильное
        configuration.setTitle("Echoes Arena");

        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        // Задаем базовый размер окна (можешь поменять под себя, например 1920х1080)
        configuration.setWindowedMode(1280, 720);

        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}
