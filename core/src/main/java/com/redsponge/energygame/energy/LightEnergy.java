package com.redsponge.energygame.energy;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.component.DirectionComponent.Direction;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.component.PositionComponent;
import com.redsponge.energygame.component.SizeComponent;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.GeneralUtils;

public class LightEnergy implements Energy {

    private long superDashStartTime;
    private Entity player;
    private Direction dashDir;
    private Fixture attacker;
    private float pixelsPerMeter;

    public LightEnergy(float pixelsPerMeter) {
        this.pixelsPerMeter = pixelsPerMeter;
        superDashStartTime = 0;
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    @Override
    public void regularInitiated(GameScreen gameScreen) {
        Gdx.app.log("LightEnergy", "Regular");
        if(gameScreen.getEnergy() > Constants.LIGHT_THRESHOLD && GeneralUtils.secondsSince(superDashStartTime) > Constants.DASH_COOLDOWN) {
            gameScreen.addEnergy(-20);
            superDashStartTime = TimeUtils.nanoTime();
            dashDir = Mappers.direction.get(player).direction;

            PositionComponent pos = Mappers.position.get(player);
            SizeComponent size = Mappers.size.get(player);
            PhysicsComponent phy = Mappers.physics.get(player);

            FixtureDef def = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1 / pixelsPerMeter, size.height / 2 / pixelsPerMeter, new Vector2(size.width / 2 / pixelsPerMeter, 0), 0);
            def.shape = shape;
            def.isSensor = true;

            attacker = phy.body.createFixture(def);
            attacker.setUserData(Constants.ATTACK_DATA_ID);
            shape.dispose();

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
        } else if(attacker != null) {
            physics.body.destroyFixture(attacker);
            attacker = null;
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
