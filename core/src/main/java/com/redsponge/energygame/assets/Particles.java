package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Particles implements AssetLoader {

    public ParticleManager sparkle;
    public ParticleManager electric;

    public Particles() {
        sparkle = new ParticleManager("sparkle.p");
        electric = new ParticleManager("electric.p");
    }

    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Particles", "Loading Particles");
        am.load("particles/textures.atlas", TextureAtlas.class);
    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Particles", "Retrieving Particles");
        TextureAtlas atlas = am.get("particles/textures.atlas", TextureAtlas.class);
        sparkle.getResources(atlas);
        electric.getResources(atlas);
    }

    public void render(float delta, SpriteBatch batch) {
        sparkle.render(delta, batch);
        electric.render(delta, batch);
    }

    public void cleanUp() {
        sparkle.cleanUp();
        electric.cleanUp();
    }
}
