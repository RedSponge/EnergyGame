package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.redsponge.energygame.util.Constants;

public class Textures implements AssetLoader {

    public Animation<AtlasRegion> lowRun;
    public Animation<AtlasRegion> medRun;
    public Animation<AtlasRegion> highRun;
    public Animation<AtlasRegion> noneRun;

    public Animation<AtlasRegion> enemyIdle;

    public AtlasRegion lowIdle;
    public AtlasRegion medIdle;
    public AtlasRegion highIdle;

    public AtlasRegion lowDashPrep;
    public AtlasRegion medDashPrep;
    public AtlasRegion highDashPrep;

    public Animation<AtlasRegion> lowDash;
    public Animation<AtlasRegion> medDash;
    public Animation<AtlasRegion> highDash;

    public Texture sky;
    public Texture title;

    public Animation<AtlasRegion> lowAttack;
    public Animation<AtlasRegion> medAttack;
    public Animation<AtlasRegion> highAttack;

    public Animation<AtlasRegion> noneWallJump;
    public Animation<AtlasRegion> lowWallJump;
    public Animation<AtlasRegion> medWallJump;
    public Animation<AtlasRegion> highWallJump;

    public Animation<AtlasRegion> highElectricStart;

    public Animation<AtlasRegion> elecBallSpawn;
    public Animation<AtlasRegion> elecBallExist;
    public Animation<AtlasRegion> elecBallRemove;


    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Textures", "Loading Textures!");
        am.load("textures/player/game_textures.atlas", TextureAtlas.class);
        am.load("textures/enemy/enemy_textures.atlas", TextureAtlas.class);
        am.load("textures/sky.png", Texture.class);
        am.load("textures/title.png", Texture.class);
    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Textures", "Retrieving Textures");
        TextureAtlas textures = am.get("textures/player/game_textures.atlas");

        lowRun = load(textures, 12, "low/run");
        medRun = load(textures, 12, "med/run");
        highRun = load(textures, 12, "high/run");
        noneRun = load(textures, 7, "none/run");

        lowIdle = textures.findRegion("low/idle");
        medIdle = textures.findRegion("med/idle");
        highIdle = textures.findRegion("high/idle");

        lowDashPrep = textures.findRegion("low/dash_prep");
        medDashPrep = textures.findRegion("med/dash_prep");
        highDashPrep = textures.findRegion("high/dash_prep");

        lowDash = load(textures, 4, "low/dash");
        medDash = load(textures, 4, "med/dash");
        highDash = load(textures, 4, "high/dash");

        lowAttack = load(textures, 7, "low/hot_punch", Constants.HEAT_ATTACK_LENGTH / 7);
        medAttack = load(textures, 7, "med/hot_punch", Constants.HEAT_ATTACK_LENGTH / 7);
        highAttack = load(textures, 7, "high/hot_punch", Constants.HEAT_ATTACK_LENGTH / 7);

        highElectricStart = load(textures, 7, "high/electric_activate", Constants.ELECTRIC_START_LENGTH / 7);

        noneWallJump = new Animation<AtlasRegion>(0.1f, textures.findRegion("none/walljump"));
        noneWallJump.setPlayMode(PlayMode.LOOP);
        lowWallJump = new Animation<AtlasRegion>(0.1f, textures.findRegion("low/walljump"));
        lowWallJump.setPlayMode(PlayMode.LOOP);
        medWallJump = new Animation<AtlasRegion>(0.1f, textures.findRegion("med/walljump"));
        medWallJump.setPlayMode(PlayMode.LOOP);
        highWallJump = new Animation<AtlasRegion>(0.1f, textures.findRegion("high/walljump"));
        highWallJump.setPlayMode(PlayMode.LOOP);

        elecBallSpawn = load(textures, 3, "elecball/spawn", Constants.ELECTRIC_START_LENGTH / 3);
        elecBallExist = load(textures, 4, "elecball/exist");
        elecBallRemove = load(textures, 3, "elecball/remove", Constants.ELECTRIC_START_LENGTH / 3);

        TextureAtlas enemyTextures = am.get("textures/enemy/enemy_textures.atlas", TextureAtlas.class);
        enemyIdle = load(enemyTextures, 2, "idle", 0.5f);

        sky = am.get("textures/sky.png");
        title = am.get("textures/title.png");

    }

    public Animation<AtlasRegion> load(TextureAtlas atlas, int numFrames, String name) {
        return load(atlas, numFrames, name, 0.75f/12f);
    }

    public Animation<AtlasRegion> load(TextureAtlas atlas, int numFrames, String name, float frameDur) {
        Array<AtlasRegion> frames = new Array<AtlasRegion>();
        for(int i = 1; i <= numFrames; i++) {
            frames.add(atlas.findRegion(name, i));
        }
        return new Animation<AtlasRegion>(frameDur, frames, PlayMode.LOOP);
    }


}
