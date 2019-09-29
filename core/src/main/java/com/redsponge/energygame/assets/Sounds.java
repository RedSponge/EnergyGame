package com.redsponge.energygame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class Sounds implements AssetLoader {

    public Sound dash, jump, electric_activate, heatAttack, enemyKill, death, buttonSelect;

    @Override
    public void load(AssetManager am) {
        Gdx.app.log("Sounds", "Loading Sounds!");
        am.load("sounds/dash.wav", Sound.class);
        am.load("sounds/fire.wav", Sound.class);
        am.load("sounds/jump.wav", Sound.class);
        am.load("sounds/electric_activate.wav", Sound.class);
        am.load("sounds/enemy_hit.wav", Sound.class);
        am.load("sounds/death_sound.ogg", Sound.class);
        am.load("sounds/button_select.wav", Sound.class);

    }

    @Override
    public void getResources(AssetManager am) {
        Gdx.app.log("Sounds", "Retrieving Sounds!");
        dash = am.get("sounds/dash.wav", Sound.class);
        jump = am.get("sounds/jump.wav", Sound.class);
        electric_activate = am.get("sounds/electric_activate.wav", Sound.class);
        heatAttack = am.get("sounds/fire.wav", Sound.class);
        enemyKill = am.get("sounds/enemy_hit.wav", Sound.class);
        death = am.get("sounds/death_sound.ogg", Sound.class);
        buttonSelect = am.get("sounds/button_select.wav", Sound.class);
    }
}
