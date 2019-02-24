package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

public class Fonts implements AssetLoader, Disposable {

    public BitmapFont pixelMix;

    public Fonts() {
    }

    @Override
    public void load(AssetManager am) {
    }

    @Override
    public void getResources(AssetManager am) {
        pixelMix = new BitmapFont(Gdx.files.internal("fonts/pixelmix/pixelmix.fnt"));
    }

    @Override
    public void dispose() {
        pixelMix.dispose();
    }
}
