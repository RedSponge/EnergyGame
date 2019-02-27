package com.redsponge.energygame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.energygame.comparators.ZComparator;
import com.redsponge.energygame.components.*;
import com.redsponge.energygame.maps.MapManager;
import com.redsponge.energygame.maps.MapManagerRenderer;
import com.redsponge.energygame.utils.Constants;

public class RenderingSystem extends SortedIteratingSystem {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Entity player;
    private Viewport viewport;

    private MapManager mapManager;
    private MapManagerRenderer mapRenderer;

    public RenderingSystem(ShapeRenderer shapeRenderer, SpriteBatch batch, Viewport viewport, Entity player, MapManager mapManager) {
        super(Family.all(PositionComponent.class, SizeComponent.class).get(), new ZComparator(), Constants.RENDERING_PRIORITY);
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.player = player;
        this.viewport = viewport;
        this.mapManager = mapManager;
        this.mapRenderer = new MapManagerRenderer(mapManager, batch);
    }


    @Override
    public void update(float deltaTime) {
        setupCameraAndMatrices();
        AnimatedTiledMapTile.updateAnimationBaseTime();
        mapRenderer.renderBackground(viewport);

        // TODO: Render all entities based on texture / animation components

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        SizeComponent size = Mappers.size.get(player);
        PositionComponent pos = Mappers.position.get(player);
        CircleBottomComponent circle = Mappers.circle.get(player);

        shapeRenderer.rect(pos.x - size.width / 2, pos.y - size.height / 2 - (circle != null ? circle.radius : 0), size.width, size.height + (circle == null ? 0 : circle.radius));
        //super.update(deltaTime);
        shapeRenderer.end();

        mapRenderer.renderForeground(viewport);
    }


    private float speed = 2;

    private void setupCameraAndMatrices() {
        PositionComponent pos = Mappers.position.get(player);
        PlayerComponent p = Mappers.player.get(player);
        if (p.energy.isSuperDashOn()) {
            float zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
            ((OrthographicCamera) viewport.getCamera()).zoom += (1.1f - zoom) * .1f;
        } else {
            float zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
            ((OrthographicCamera) viewport.getCamera()).zoom += (1 - zoom) * .1f;
        }
        viewport.getCamera().position.x += speed;
        viewport.getCamera().position.y = viewport.getWorldHeight() / 2;
//        speed += 0.001f;

        viewport.apply();
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = Mappers.position.get(entity);
        SizeComponent size = Mappers.size.get(entity);
        CircleBottomComponent circle = Mappers.circle.get(entity);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(pos.x - size.width / 2, pos.y - size.height / 2 - (circle != null ? circle.radius : 0), size.width, size.height + (circle == null ? 0 : circle.radius));
    }

    @Override
    public void removedFromEngine(Engine engine) {
        mapManager.dispose();
        mapRenderer.dispose();
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

}