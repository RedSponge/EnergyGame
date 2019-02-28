package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.component.SizeComponent;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.util.GeneralUtils;

public class HeatEnergy implements Energy {

    private long regularStartTime;

    private Entity player;
    private Fixture regular;

    private float pixelsPerMeter;
    private boolean onGround;

    private float wantedY;
    private long superJumpStarted;

    public HeatEnergy(float pixelsPerMeter) {
        this.pixelsPerMeter = pixelsPerMeter;
        regularStartTime = 0;
        superJumpStarted = 0;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        if(gameScreen.getEnergy() > Constants.HEAT_THRESHOLD) {
            gameScreen.addEnergy(-5);
        } else {
            return;
        }
        if(regular == null) {
            FixtureDef fdef = new FixtureDef();
            fdef.isSensor = true;

            PolygonShape shape = new PolygonShape();

            SizeComponent size = Mappers.size.get(player);
            PhysicsComponent physics = Mappers.physics.get(player);

            // TODO: Directions
            shape.setAsBox(size.width / pixelsPerMeter, size.height / 2 / pixelsPerMeter, new Vector2(size.width / 2 / pixelsPerMeter, 0), 0);
            fdef.shape = shape;

            regular = physics.body.createFixture(fdef);
            regular.setUserData(Constants.ATTACK_DATA_ID);
        }

        regularStartTime = TimeUtils.nanoTime();
        Mappers.animation.get(player).timeSinceStart = 0;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void upInitiated(GameScreen gameScreen) {
        if(!onGround) {
            return;
        }
        superJumpStarted = TimeUtils.nanoTime();
        PhysicsComponent p = Mappers.physics.get(player);
        wantedY = p.body.getPosition().y + 4;
    }

    @Override
    public void downInitiated(GameScreen gameScreen) {
        Gdx.app.log("HeatEnergy", "Down");
    }

    @Override
    public void update(float delta) {
        PhysicsComponent physics = Mappers.physics.get(player);

        if(!isPunchOn() && regular != null) {
            physics.body.destroyFixture(regular);
            Gdx.app.log("HeatEnergy", "Removed Regular Attack");
            regular = null;
        }
    }

    public boolean isPunchOn() {
        return GeneralUtils.secondsSince(regularStartTime) < Constants.HEAT_ATTACK_LENGTH && regular != null;
    }

    public boolean isJumpOn() {
        PhysicsComponent physics = Mappers.physics.get(player);
        return (wantedY - physics.body.getPosition().y) > 0.7f && GeneralUtils.secondsSince(superJumpStarted) < 2;
    }

    @Override
    public int getMinEnergy() {
        return Constants.HEAT_THRESHOLD;
    }
}
