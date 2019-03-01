package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.GeneralUtils;

public class ElectricEnergy implements Energy {

    private Fixture protectionField;
    private Entity player;
    private long protectionStartTime;
    private float protectionLength;
    private float pixelsPerMeter;
    private Assets assets;

    public ElectricEnergy(float pixelsPerMeter, Assets assets) {
        this.pixelsPerMeter = pixelsPerMeter;
        this.assets = assets;
        protectionStartTime = 0;
        protectionLength = 5;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("ElectricEnergy", "Regular");
        if(gameScreen.getEnergy() > Constants.ELECTRIC_THRESHOLD && GeneralUtils.secondsSince(protectionStartTime) > Constants.ELECTRICITY_DELAY + protectionLength) {
            gameScreen.addEnergy(-50);
        } else {
            return;
        }
        if(GeneralUtils.secondsSince(protectionStartTime) > protectionLength) {
            FixtureDef fdef = new FixtureDef();
            fdef.isSensor = true;
            CircleShape circle = new CircleShape();
            circle.setRadius(44 / pixelsPerMeter);
            fdef.shape = circle;
            PhysicsComponent p = Mappers.physics.get(player);
            protectionField = p.body.createFixture(fdef);
            protectionField.setUserData(Constants.ATTACK_DATA_ID);
            circle.dispose();
        }
        GeneralUtils.playSoundRandomlyPitched(assets.getSounds().electric_activate);
        protectionStartTime = TimeUtils.nanoTime();
        Mappers.animation.get(player).timeSinceStart = 0;
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
        if(!isFieldOn() && protectionField != null) {
            PhysicsComponent p = Mappers.physics.get(player);
            p.body.destroyFixture(protectionField);
            protectionField = null;
        }
    }

    @Override
    public int getMinEnergy() {
        return Constants.ELECTRIC_THRESHOLD;
    }

    public boolean isChargingField() {
        return GeneralUtils.secondsSince(protectionStartTime) < Constants.ELECTRIC_START_LENGTH;
    }

    public boolean isRemoving() {
        return !isFieldOn() && GeneralUtils.secondsSince(protectionStartTime) < Constants.ELECTRIC_START_LENGTH + protectionLength;
    }

    public boolean isFieldOn() {
        return GeneralUtils.secondsSince(protectionStartTime) <= protectionLength;
    }

    public long getStartTime() {
        return protectionStartTime;
    }

    public float getLength() {
        return protectionLength;
    }
}
