package com.redsponge.energygame.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.redsponge.energygame.utils.HashCollections;

public class Mappers {

    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<SizeComponent> size = ComponentMapper.getFor(SizeComponent.class);
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<ColliderComponent> collider = ComponentMapper.getFor(ColliderComponent.class);
    public static final ComponentMapper<ChainComponent> chain = ComponentMapper.getFor(ChainComponent.class);
    public static final ComponentMapper<CircleBottomComponent> circle = ComponentMapper.getFor(CircleBottomComponent.class);
    public static final ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<EventComponent> event = ComponentMapper.getFor(EventComponent.class);
}
