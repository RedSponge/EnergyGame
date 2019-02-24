package com.redsponge.energy.utils;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.redsponge.energy.components.CircleBottomComponent;
import com.redsponge.energy.components.ColliderComponent;
import com.redsponge.energy.components.PhysicsComponent;
import com.redsponge.energy.components.PlayerComponent;
import com.redsponge.energy.components.PositionComponent;
import com.redsponge.energy.components.SizeComponent;
import com.redsponge.energy.components.VelocityComponent;

public class EntityFactory {


    public static Entity getPlayer() {
        Entity player = new Entity();
        player.add(new PositionComponent(100, 100));
        player.add(new VelocityComponent(0, 0));
        player.add(new SizeComponent(20, 20));
        player.add(new PhysicsComponent(BodyType.DynamicBody));
        player.add(new PlayerComponent());
        player.add(new ColliderComponent());
        player.add(new CircleBottomComponent(10));

        return player;
    }
}
