package com.redsponge.energygame.map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.energygame.system.PhysicsSystem;

public class MapManager implements Disposable {

    private int currentMapOffset = 0;
    private MapHolder current;
    private MapHolder tail, head;
    private PhysicsSystem ps;
    private Engine engine;

    public MapManager(PhysicsSystem ps, TiledMap initialMap, Engine engine) {
        this.engine = engine;
        this.currentMapOffset = 0;
        this.ps = ps;
        head = new MapHolder(initialMap, ps, 0, engine);
    }

    public void init() {
        head.setEntities(ps.loadNewMap(head.getMap(), currentMapOffset));
        loadNextMap();
    }

    public void loadNextMap() {
        Gdx.app.log("MapManager", "Loading Next Map!!!");
        if(tail != null) {
            Gdx.app.log("MapManager", "Disposing Tail!");
            tail.dispose();
        }
        if(head != null) {
            Gdx.app.log("MapManager", "Shifting Current To Tail!");
            MapProperties prop = head.getMap().getProperties();
            currentMapOffset += prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        }
        tail = current;
        current = head;

        head = loadMap(MapFetcher.getEasyMap());
        head.setEntities(ps.loadNewMap(head.getMap(), currentMapOffset));
    }

    private MapHolder loadMap(String path) {
        return new MapHolder(new TmxMapLoader().load(path), ps, currentMapOffset, engine);
    }

    public void setSystems(Engine engine) {
        this.ps = engine.getSystem(PhysicsSystem.class);
    }

    public MapHolder getCurrentMap() {
        return current;
    }

    public MapHolder getHeadMap() {
        return head;
    }
    public MapHolder getTailMap() {
        return tail;
    }

    public void dispose() {
        head.dispose();
        current.dispose();
        tail.dispose();
    }
}
