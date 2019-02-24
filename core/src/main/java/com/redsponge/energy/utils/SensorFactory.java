package com.redsponge.energy.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.redsponge.energy.components.ColliderComponent;
import com.redsponge.energy.constants.Constants;

public class SensorFactory {

    /**
     * Utility method to create sensors (For {@link ColliderComponent})
     *
     * @param body - The entity's body
     * @param w - The entity's width
     * @param h - The entity's height
     * @param offset - The offset from the center of the entity
     * @param vertical - Whether the sensor should be vertical
     * @return The sensor fixture for assignment or chaining
     */
    public static Fixture createCollideFixture(Body body, float w, float h, Vector2 offset, boolean vertical, float ppm) {
        float width = 0, height = 0;
        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;

        PolygonShape sensorShape = new PolygonShape();

        if(!vertical) {
            width = w / Constants.DETECTOR_WIDTH;
            height = h / Constants.DETECTOR_HEIGHT;
        } else {
            width = w / Constants.DETECTOR_HEIGHT;
            height = h / Constants.DETECTOR_WIDTH;
        }

        width /= ppm;
        height /= ppm;

        sensorShape.setAsBox(width, height, new Vector2(offset).scl(1/ppm), 0);
        fdef.shape = sensorShape;

        Fixture fixture = body.createFixture(fdef);
        fixture.setUserData(Constants.SENSOR_DATA_ID);

        sensorShape.dispose();

        return fixture;
    }

}
