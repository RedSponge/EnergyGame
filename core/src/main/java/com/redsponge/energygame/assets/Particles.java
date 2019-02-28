package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Particles implements AssetLoader {

    public ParticleManager sparkle;
    public ParticleManager lightning;

    public Particles() {
        sparkle = new ParticleManager("sparkle.p");
        lightning = new ParticleManager("lightning.p");
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
        lightning.getResources(atlas);
    }

    public void render(float delta, SpriteBatch batch) {
        sparkle.render(delta, batch);
        lightning.render(delta, batch);
    }

    public void cleanUp() {
        sparkle.cleanUp();
        lightning.cleanUp();
    }
}
