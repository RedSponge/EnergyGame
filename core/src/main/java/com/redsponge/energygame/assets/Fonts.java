package com.redsponge.energygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public class Fonts implements AssetLoader, Disposable {

    public BitmapFont titleFont;
    public BitmapFont pixelMix;

    public Fonts() {
    }

    @Override
    public void load(AssetManager am) {
        am.load("fonts/title.fnt", BitmapFont.class);
        am.load("fonts/pixelmix.fnt", BitmapFont.class);
    }

    @Override
    public void getResources(AssetManager am) {
        titleFont = am.get("fonts/title.fnt", BitmapFont.class);
        pixelMix = am.get("fonts/pixelmix.fnt");
    }

    @Override
    public void dispose() {

    }
}
