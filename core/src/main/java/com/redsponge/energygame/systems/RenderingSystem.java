package com.redsponge.energygame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.energygame.comparators.MapLayerComparator;
import com.redsponge.energygame.comparators.ZComparator;
import com.redsponge.energygame.components.*;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.maps.OffsettedOrthogonalTiledMapRenderer;

public class RenderingSystem extends SortedIteratingSystem {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Entity player;
    private Viewport viewport;

    private TiledMap map;
    private OffsettedOrthogonalTiledMapRenderer mapRenderer;

    private int backgroundForegroundSeparator;

    private MapLayerComparator mapLayerComparator;
    private TiledMapTileLayer[] renderLayers;
    private int currentMapOffset;

    public RenderingSystem(ShapeRenderer shapeRenderer, SpriteBatch batch, Viewport viewport, Entity player) {
        super(Family.all(PositionComponent.class, SizeComponent.class).get(), new ZComparator(), Constants.RENDERING_PRIORITY);
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.player = player;
        this.viewport = viewport;
        this.mapLayerComparator = new MapLayerComparator();
        this.currentMapOffset = 0;
    }

    private void sortLayers() {
        Array<TiledMapTileLayer> layers = this.map.getLayers().getByType(TiledMapTileLayer.class);
        layers.sort(mapLayerComparator);
        renderLayers = layers.toArray(TiledMapTileLayer.class);

        // Finding Separator
        int lastZ = -1;
        boolean found = false;
        int i = 0;

        for(i = 0; i < renderLayers.length && !found; i++) {
                final int z = Integer.parseInt((String) renderLayers[i].getProperties().get("z"));
            if(z > 0 && lastZ < 0) {
                found = true;
                backgroundForegroundSeparator = i;
                Gdx.app.log("Rendering System", "Found: " + z + " " + lastZ + " " + i);
            } else {
                lastZ = z;
            }
            Gdx.app.log("Rendering System", "" + found);
        }
        Gdx.app.log("Rendering System", "Map Layer Foreground Background Separator Is " + backgroundForegroundSeparator);
    }


    @Override
    public void update(float deltaTime) {
        setupCameraAndMatrices();
        AnimatedTiledMapTile.updateAnimationBaseTime();
        renderBackground();

        // TODO: Render all entities based on texture / animation components

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        SizeComponent size = Mappers.size.get(player);
        PositionComponent pos = Mappers.position.get(player);
        CircleBottomComponent circle = Mappers.circle.get(player);

        shapeRenderer.rect(pos.x-size.width/2, pos.y-size.height/2 - (circle != null ? circle.radius : 0), size.width, size.height + (circle == null ? 0 : circle.radius));
        //super.update(deltaTime);
        shapeRenderer.end();

        renderForeground();
    }


    private float speed = 2;

    private void setupCameraAndMatrices() {
        PositionComponent pos = Mappers.position.get(player);
        PlayerComponent p = Mappers.player.get(player);
        if(p.energy.isSuperDashOn()) {
            float zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
            ((OrthographicCamera) viewport.getCamera()).zoom += (1.1f - zoom) * .1f;
        } else {
            float zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
            ((OrthographicCamera) viewport.getCamera()).zoom += (1 - zoom) * .1f;
        }
        viewport.getCamera().position.x+=speed;
        viewport.getCamera().position.y = viewport.getWorldHeight() / 2;
//        speed += 0.001f;

        viewport.apply();
    }

    private void renderBackground() {
        mapRenderer.setOffsetX(currentMapOffset);
        mapRenderer.setView((OrthographicCamera) viewport.getCamera());
        batch.begin();
        for(int i = 0; i < backgroundForegroundSeparator; i++) {
            mapRenderer.renderTileLayer(renderLayers[i]);
        }
        batch.end();
    }

    private void renderForeground() {
        batch.begin();
        for(int i = backgroundForegroundSeparator; i < renderLayers.length; i++) {
            mapRenderer.renderTileLayer(renderLayers[i]);
        }
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = Mappers.position.get(entity);
        SizeComponent size = Mappers.size.get(entity);
        CircleBottomComponent circle = Mappers.circle.get(entity);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(pos.x-size.width/2, pos.y-size.height/2 - (circle != null ? circle.radius : 0), size.width, size.height + (circle == null ? 0 : circle.radius));
    }

    @Override
    public void removedFromEngine(Engine engine) {
        mapRenderer.dispose();
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public void setCurrentMap(TiledMap currentMap) {
        this.map = currentMap;
        if (this.mapRenderer == null) {
            this.mapRenderer = new OffsettedOrthogonalTiledMapRenderer(this.map, this.batch);
        } else {
            this.mapRenderer.setMap(map);
        }

        sortLayers();
    }

    public void setCurrentMapOffset(int currentMapOffset) {
        this.currentMapOffset = currentMapOffset;
    }
}
