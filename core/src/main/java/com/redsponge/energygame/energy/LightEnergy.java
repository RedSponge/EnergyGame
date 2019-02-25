package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.components.Mappers;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.utils.GeneralUtils;

public class LightEnergy implements Energy {

    private long superDashStartTime;
    private Entity player;

    public LightEnergy() {
        superDashStartTime = 0;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("LightEnergy", "Regular");
        superDashStartTime = TimeUtils.nanoTime();
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
        PhysicsComponent physics = Mappers.physics.get(player);
        if(GeneralUtils.secondsSince(superDashStartTime) < 0.1f) {
            physics.body.applyLinearImpulse(new Vector2(10, 0), physics.body.getLocalCenter(), true);
            physics.body.setTransform(physics.body.getPosition().x + 1, physics.body.getPosition().y, 0);
        }
    }

    @Override
    public int getMinEnergy() {
        return Constants.LIGHT_THRESHOLD;
    }
}
