package com.redsponge.energy.systems;

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
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.energy.comparators.MapLayerComparator;
import com.redsponge.energy.comparators.ZComparator;
import com.redsponge.energy.components.CircleBottomComponent;
import com.redsponge.energy.components.Mappers;
import com.redsponge.energy.components.PositionComponent;
import com.redsponge.energy.components.SizeComponent;
import com.redsponge.energy.constants.Constants;

public class RenderingSystem extends SortedIteratingSystem {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Entity player;
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private int backgroundForegroundSeparator;

    private MapLayerComparator mapLayerComparator;
    private TiledMapTileLayer[] renderLayers;

    public RenderingSystem(ShapeRenderer shapeRenderer, SpriteBatch batch, Viewport viewport, Entity player, TiledMap map) {
        super(Family.all(PositionComponent.class, SizeComponent.class).get(), new ZComparator(), Constants.RENDERING_PRIORITY);
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.player = player;
        this.viewport = viewport;
        this.map = map;
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, this.batch);
        this.mapLayerComparator = new MapLayerComparator();

        sortLayers();
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

    private void setupCameraAndMatrices() {
        PositionComponent pos = Mappers.position.get(player);

        viewport.getCamera().position.set(new Vector3(pos.x, pos.y, 0));
        viewport.apply();
        mapRenderer.setView((OrthographicCamera) viewport.getCamera());

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    private void renderBackground() {
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
}
