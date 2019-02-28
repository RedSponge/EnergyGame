package com.redsponge.energygame.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.energygame.util.Constants;

public class PhysicsDebugSystem extends EntitySystem {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Viewport renderViewport;

    public PhysicsDebugSystem(World world, Viewport renderViewport) {
        super(100);
        this.world = world;
        this.renderViewport = renderViewport;
        this.debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void update(float deltaTime) {
        debugRenderer.render(world, renderViewport.getCamera().combined.scl(Constants.DEFAULT_PPM));
    }

    @Override
    public void removedFromEngine(Engine engine) {
        debugRenderer.dispose();
    }
}
