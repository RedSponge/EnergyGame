package com.redsponge.energy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class Sounds implements AssetLoader {

    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Sounds", "Loading Sounds!");
    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Sounds", "Retrieving Sounds!");
    }
}
