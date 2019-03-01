package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Skins implements AssetLoader {

    public Skin menu;

    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Skins", "Loading Skins!");
        am.load("skins/menu/menu_skin.json", Skin.class);
    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Skins", "Retrieving Skins!");
        menu = am.get("skins/menu/menu_skin.json", Skin.class);
        menu.getPatch("button_reg").getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }
}
