package com.redsponge.energygame.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MapManagerRenderer implements Disposable {

    private MapManager mapManager;
    private OffsettedOrthogonalTiledMapRenderer renderer;
    private SpriteBatch batch;

    public MapManagerRenderer(MapManager mapManager, SpriteBatch batch) {
        this.mapManager = mapManager;
        this.batch = batch;
        renderer = new OffsettedOrthogonalTiledMapRenderer(mapManager.getCurrentMap().getMap(), batch);
    }

    public void renderBackground(Viewport viewport) {
        batch.begin();
        if(mapManager.getTailMap() != null) mapManager.getTailMap().renderBackground(renderer, viewport);
        mapManager.getCurrentMap().renderBackground(renderer, viewport);
        mapManager.getHeadMap().renderBackground(renderer, viewport);
        batch.end();
    }

    public void renderForeground(Viewport viewport) {
        batch.begin();
        if(mapManager.getTailMap() != null) mapManager.getTailMap().renderForeground(renderer, viewport);
        mapManager.getCurrentMap().renderForeground(renderer, viewport);
        mapManager.getHeadMap().renderForeground(renderer, viewport);
        batch.end();
    }


    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
