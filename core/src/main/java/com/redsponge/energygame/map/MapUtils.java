package com.redsponge.energygame.map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapUtils {

    public static int getMapWidth(TiledMap map) {
        MapProperties p = map.getProperties();
        return p.get("tilewidth", Integer.class) * p.get("width", Integer.class);
    }

}
