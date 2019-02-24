package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.components.Mappers;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.components.SizeComponent;
import com.redsponge.energygame.input.InputSystem;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.utils.GeneralUtils;

public class HeatEnergy implements Energy {

    private long regularStartTime;

    private Entity player;
    private Fixture regular;

    private float pixelsPerMeter;

    public HeatEnergy(float pixelsPerMeter) {
        this.pixelsPerMeter = pixelsPerMeter;
        regularStartTime = 0;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("HeatEnergy", "Regular");
        if(regular != null) {
            regularStartTime = TimeUtils.nanoTime();
            //TODO: Decrease Energy For Renewing
            return;
        }

        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;

        PolygonShape shape = new PolygonShape();

        SizeComponent size = Mappers.size.get(player);
        PhysicsComponent physics = Mappers.physics.get(player);

        // TODO: Directions
        shape.setAsBox(size.width / 2 / pixelsPerMeter, size.height / 2 / pixelsPerMeter, new Vector2(size.width / 2 / pixelsPerMeter, 0), 0);
        fdef.shape = shape;

        regular = physics.body.createFixture(fdef);
        regular.setUserData(Constants.ATTACK_DATA_ID);
        regularStartTime = TimeUtils.nanoTime();
    }

    @Override
    public void upInitiated(GameScreen gameScreen) {
        Gdx.app.log("HeatEnergy", "Up");
    }

    @Override
    public void downInitiated(GameScreen gameScreen) {
        Gdx.app.log("HeatEnergy", "Down");
    }

    @Override
    public void update(float delta) {
        PhysicsComponent physics = Mappers.physics.get(player);

        if(GeneralUtils.secondsSince(regularStartTime) > 0.5f && regular != null) {
            physics.body.destroyFixture(regular);
            Gdx.app.log("HeatEnergy", "Removed Regular Attack");
            regular = null;
        }
    }

    @Override
    public int getMinEnergy() {
        return Constants.HEAT_THRESHOLD;
    }
}