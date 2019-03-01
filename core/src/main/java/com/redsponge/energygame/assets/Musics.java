package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class Musics implements AssetLoader {

    public MusicHolder background;

    @Override
    public void load(AssetManager am) {
        background = new MusicHolder(Gdx.files.internal("music/music.ogg"));
    }

    @Override
    public void getResources(AssetManager am) {

    }

    public void disposeAll() {
        background.dispose();
    }
}
