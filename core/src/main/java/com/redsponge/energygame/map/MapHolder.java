package com.redsponge.energygame.map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.energygame.comparator.MapLayerComparator;
import com.redsponge.energygame.system.PhysicsSystem;

public class MapHolder implements Disposable {

    private TiledMap map;
    private PhysicsSystem ps;
    private Entity[] entities;
    private TiledMapTileLayer[] renderLayers;
    private int backgroundForegroundSeparator;
    private float offset;
    private Engine engine;

    private static final MapLayerComparator mapLayerComparator = new MapLayerComparator();

    public MapHolder(TiledMap map, PhysicsSystem ps, float offset, Engine engine) {
        this.map = map;
        this.ps = ps;
        this.offset = offset;
        this.engine = engine;
        sortLayers();
    }

    /**
     * Finds the separator between layers in the {@link MapHolder#map}
     */
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
            } else {
                lastZ = z;
            }
        }
        Gdx.app.log("Rendering System", "Map Layer Foreground Background Separator Is " + backgroundForegroundSeparator);
    }

    /**
     * Renders all the background layers based on offset
     * @param renderer - The Offsettable Renderer
     * @param viewport - The Viewport
     */
    public void renderBackground(OffsettedOrthogonalTiledMapRenderer renderer, Viewport viewport) {
        renderer.setOffsetX(offset);
        renderer.setView((OrthographicCamera) viewport.getCamera());
        for(int i = 0; i < backgroundForegroundSeparator; i++) {
            renderer.renderTileLayer(renderLayers[i]);
        }
    }

    /**
     * Renders all the foreground layers based on offset
     * @param renderer - The Offsettable Renderer
     * @param viewport - The Viewport
     */
    public void renderForeground(OffsettedOrthogonalTiledMapRenderer renderer, Viewport viewport) {
        renderer.setOffsetX(offset);
        renderer.setView((OrthographicCamera) viewport.getCamera());
        for(int i = backgroundForegroundSeparator; i < renderLayers.length; i++) {
            renderer.renderTileLayer(renderLayers[i]);
        }
    }

    public void setEntities(Entity[] entities) {
        this.entities = entities;
    }

    public TiledMap getMap() {
        return map;
    }

    @Override
    public void dispose() {
        Gdx.app.log("MapHolder", "Disposed Bodies And Map!");
        for (Entity e : entities) {
            engine.removeEntity(e);
        }
        map.dispose();
    }
}
