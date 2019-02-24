package com.redsponge.energy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class Textures implements AssetLoader {

    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Textures", "Loading Textures!");
    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Textures", "Retrieving Textures");
    }
}
