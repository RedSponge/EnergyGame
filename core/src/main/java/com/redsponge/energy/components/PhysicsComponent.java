package com.redsponge.energy.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Must be added in order for the body to be in physics and collision calculations
 */
public class PhysicsComponent implements Component {

    /**
     * The type of the body
     */

    public BodyType type = BodyType.DynamicBody;
    public Body body;

    public PhysicsComponent(BodyType type) {
        this.type = type;
    }

    public PhysicsComponent() {}
}
