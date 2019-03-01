package com.redsponge.energygame.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.component.AnimationComponent;
import com.redsponge.energygame.component.ColliderComponent;
import com.redsponge.energygame.component.DirectionComponent;
import com.redsponge.energygame.component.EnemyComponent;
import com.redsponge.energygame.component.EventComponent;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.component.PlayerComponent;
import com.redsponge.energygame.component.PositionComponent;
import com.redsponge.energygame.component.SizeComponent;
import com.redsponge.energygame.component.VelocityComponent;

public class EntityFactory {


    public static Entity getPlayer(Assets assets) {
        Entity player = new Entity();
        player.add(new PositionComponent(100, 200));
        player.add(new VelocityComponent(0, 0));
        player.add(new SizeComponent(16, 24));
        player.add(new PhysicsComponent(BodyType.DynamicBody));
        player.add(new PlayerComponent());
        player.add(new ColliderComponent());
        player.add(new AnimationComponent(assets.getTextures().lowRun));
        player.add(new DirectionComponent());

        return player;
    }
    public static Entity getEnemy(Assets assets, float x, float y, float width, float height) {
        Entity enemy = new Entity();
        enemy.add(new PositionComponent(x, y));
        enemy.add(new SizeComponent(width, height));
        enemy.add(new PhysicsComponent(BodyType.DynamicBody));
        enemy.add(new EnemyComponent());
        enemy.add(new VelocityComponent());
        enemy.add(new AnimationComponent(assets.getTextures().enemyIdle));

        return enemy;
    }

    public static Entity getEventEntity(float x, float y, float width, float height, MapProperties props) {
        Entity sensor = new Entity();
        sensor.add(new PositionComponent(x, y));
        sensor.add(new SizeComponent(width, height));
        sensor.add(new PhysicsComponent(BodyType.StaticBody));
        sensor.add(new EventComponent(props.get("type", String.class), props));

        return sensor;
    }}
