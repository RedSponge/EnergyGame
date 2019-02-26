package com.redsponge.energygame.maps;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.redsponge.energygame.systems.PhysicsSystem;
import com.redsponge.energygame.systems.RenderingSystem;

public class MapManager {

    private int currentMapOffset = 0;
    private TiledMap currentMap;
    private PhysicsSystem ps;
    private RenderingSystem rs;

    public void loadNextMap() {
        if(currentMap != null) {
            MapProperties prop = currentMap.getProperties();
            currentMapOffset += prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        }
        currentMap = loadMap("maps/random1.tmx");
        ps.loadNewMap(currentMap, currentMapOffset);
        rs.setCurrentMap(currentMap);
        rs.setCurrentMapOffset(currentMapOffset);

        // TODO: Set map and offset for systems
    }

    public static TiledMap loadMap(String path) {
        return new TmxMapLoader().load(path);
    }

    public void setSystems(Engine engine) {
        this.ps = engine.getSystem(PhysicsSystem.class);
        this.rs = engine.getSystem(RenderingSystem.class);
    }
}
