package com.redsponge.energygame.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

    private AssetManager am;
    private Sounds sounds;
    private Particles particles;
    private Textures textures;
    private Fonts fonts;
    private Skins skins;
    private Musics musics;

    public Assets() {
        am = new AssetManager();
        particles = new Particles();
        sounds = new Sounds();
        textures = new Textures();
        fonts = new Fonts();
        skins = new Skins();
        musics = new Musics();

        sounds.load(am);
        particles.load(am);
        textures.load(am);
        fonts.load(am);
        skins.load(am);
        musics.load(am);
    }

    public void getResources() {
        sounds.getResources(am);
        particles.getResources(am);
        textures.getResources(am);
        fonts.getResources(am);
        skins.getResources(am);
        musics.getResources(am);
    }

    public Sounds getSounds() {
        return sounds;
    }

    public Particles getParticles() {
        return particles;
    }

    public Textures getTextures() {
        return textures;
    }

    public Fonts getFonts() {
        return fonts;
    }

    public Skins getSkins() {
        return skins;
    }

    @Override
    public void dispose() {
        am.dispose();
        fonts.dispose();
        musics.disposeAll();
    }

    public void finishLoading() {
        am.finishLoading();
    }

    public boolean update() {
        return am.update();
    }

    public Musics getMusics() {
        return musics;
    }
}
