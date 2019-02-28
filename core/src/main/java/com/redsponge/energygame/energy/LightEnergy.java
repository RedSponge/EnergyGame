package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.component.DirectionComponent.Direction;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.GeneralUtils;

public class LightEnergy implements Energy {

    private long superDashStartTime;
    private Entity player;
    private Direction dashDir;

    public LightEnergy() {
        superDashStartTime = 0;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("LightEnergy", "Regular");
        if(gameScreen.getEnergy() > Constants.LIGHT_THRESHOLD) {
            gameScreen.addEnergy(-20);
            superDashStartTime = TimeUtils.nanoTime();
            dashDir = Mappers.direction.get(player).direction;
        }
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
        if(isDashOn()) {
            physics.body.applyLinearImpulse(new Vector2(2 * dashDir.mult, 0), physics.body.getLocalCenter(), true);
        }
    }

    @Override
    public int getMinEnergy() {
        return Constants.LIGHT_THRESHOLD;
    }

    public boolean isDashOn() {
        return GeneralUtils.secondsSince(superDashStartTime) < 0.2f;
    }
}
