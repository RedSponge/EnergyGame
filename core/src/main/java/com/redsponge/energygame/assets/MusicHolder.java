package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

public class MusicHolder implements Disposable {

    private FileHandle file;
    private Music instance;

    public MusicHolder(FileHandle file) {
        this.file = file;
    }

    public void load() {
        instance = Gdx.audio.newMusic(file);
    }

    public Music getInstance() {
        return instance;
    }

    @Override
    public void dispose() {
        if(instance != null) instance.dispose();
    }
}
