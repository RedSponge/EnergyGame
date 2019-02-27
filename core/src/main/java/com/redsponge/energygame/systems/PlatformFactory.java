package com.redsponge.energygame.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.redsponge.energygame.components.ChainComponent;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.components.PlatformComponent;
import com.redsponge.energygame.components.PositionComponent;
import com.redsponge.energygame.components.SizeComponent;
import com.redsponge.energygame.components.VelocityComponent;

public class PlatformFactory {

    public static Entity createRectanglePlatform(float x, float y, float width, float height) {
        Entity e = new Entity();
        e.add(new PositionComponent(x, y));
        e.add(new SizeComponent(width, height));
        e.add(new PhysicsComponent(BodyType.StaticBody));
        e.add(new PlatformComponent());
        e.add(new VelocityComponent());
        return e;
    }

    public static Entity createChainFloor(float[] vertex) {
        assert vertex.length >= 4;

        Entity e = new Entity();
        e.add(new PositionComponent(0, 0));
        e.add(new PhysicsComponent(BodyType.StaticBody));
        e.add(new VelocityComponent());
        e.add(new PlatformComponent());
        e.add(new ChainComponent(vertex));

        return e;
    }

}
