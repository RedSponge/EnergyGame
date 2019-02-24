package com.redsponge.energygame.energy;

import com.badlogic.gdx.Gdx;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.screen.GameScreen;

public class ElectricEnergy implements Energy {

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("ElectricEnergy", "Regular");
    }

    @Override
    public void upInitiated(GameScreen gameScreen) {
        Gdx.app.log("ElectricEnergy", "Up");
    }

    @Override
    public void downInitiated(GameScreen gameScreen) {
        Gdx.app.log("ElectricEnergy", "Down");
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public int getMinEnergy() {
        return Constants.ELECTRIC_THRESHOLD;
    }
}
