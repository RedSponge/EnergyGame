package com.redsponge.energygame.energy;

import com.badlogic.gdx.Gdx;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.screen.GameScreen;

public class LightEnergy implements Energy {

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("LightEnergy", "Regular");
    }

    @Override
    public void upInitiated(GameScreen gameScreen) {
        Gdx.app.log("LightEnergy", "Up");
    }

    @Override
    public void downInitiated(GameScreen gameScreen) {
        Gdx.app.log("LightEnergy", "Down");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public int getMinEnergy() {
        return Constants.LIGHT_THRESHOLD;
    }
}
