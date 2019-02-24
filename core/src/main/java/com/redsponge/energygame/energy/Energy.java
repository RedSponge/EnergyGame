package com.redsponge.energygame.energy;

import com.redsponge.energygame.screen.GameScreen;

public interface Energy {

    void regularInitiated(GameScreen gameScreen);
    void upInitiated(GameScreen gameScreen);
    void downInitiated(GameScreen gameScreen);

    void update(float delta);

    int getMinEnergy();

}
