package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.components.Mappers;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.utils.GeneralUtils;

public class ElectricEnergy implements Energy {

    private Fixture protectionField;
    private Entity player;
    private long protectionStartTime;
    private float protectionLength;

    public ElectricEnergy() {
        protectionStartTime = 0;
        protectionLength = 2;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("ElectricEnergy", "Regular");
        if(GeneralUtils.secondsSince(protectionStartTime) > protectionLength) {
            FixtureDef fdef = new FixtureDef();
            fdef.isSensor = true;
            CircleShape circle = new CircleShape();
            circle.setRadius(3);
            fdef.shape = circle;
            PhysicsComponent p = Mappers.physics.get(player);
            protectionField = p.body.createFixture(fdef);
            protectionField.setUserData(Constants.ATTACK_DATA_ID);
            circle.dispose();
        }
        protectionStartTime = TimeUtils.nanoTime();
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

    public boolean isFieldOn() {
        return GeneralUtils.secondsSince(protectionStartTime) <= protectionLength;
    }
}
