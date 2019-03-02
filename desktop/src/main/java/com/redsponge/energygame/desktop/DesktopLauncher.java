package com.redsponge.energygame.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.redsponge.energygame.EnergyGame;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new EnergyGame(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Micro Mania!";
        configuration.width = 640*2;
        configuration.height = 360*2;
        for (int size : new int[] { 128, 64, 32 }) {
            configuration.addIcon("logo_" + size + "x" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}