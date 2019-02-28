package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Textures implements AssetLoader {

    public Animation<AtlasRegion> lowRun;
    public Animation<AtlasRegion> medRun;
    public Animation<AtlasRegion> highRun;

    public AtlasRegion lowIdle;
    public AtlasRegion medIdle;
    public AtlasRegion highIdle;

    public AtlasRegion lowDashPrep;
    public AtlasRegion medDashPrep;
    public AtlasRegion highDashPrep;

    public Animation<AtlasRegion> lowDash;
    public Animation<AtlasRegion> medDash;
    public Animation<AtlasRegion> highDash;


    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Textures", "Loading Textures!");
        am.load("textures/player/game_textures.atlas", TextureAtlas.class);
    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Textures", "Retrieving Textures");
        TextureAtlas textures = am.get("textures/player/game_textures.atlas");

        lowRun = load(textures, 12, "low/run");
        medRun = load(textures, 12, "med/run");
        highRun = load(textures, 12, "high/run");

        lowIdle = textures.findRegion("low/idle");
        medIdle = textures.findRegion("med/idle");
        highIdle = textures.findRegion("high/idle");

        lowDashPrep = textures.findRegion("low/dash_prep");
        medDashPrep = textures.findRegion("med/dash_prep");
        highDashPrep = textures.findRegion("high/dash_prep");

        lowDash = load(textures, 4, "low/dash");
        medDash = load(textures, 4, "med/dash");
        highDash = load(textures, 4, "high/dash");
    }

    public Animation<AtlasRegion> load(TextureAtlas atlas, int numFrames, String name) {
        Array<AtlasRegion> frames = new Array<AtlasRegion>();
        for(int i = 1; i <= numFrames; i++) {
            frames.add(atlas.findRegion(name, i));
        }
        return new Animation<AtlasRegion>(0.75f/12f, frames, PlayMode.LOOP);
    }


}
