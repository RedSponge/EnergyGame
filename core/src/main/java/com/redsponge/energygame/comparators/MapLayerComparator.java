package com.redsponge.energygame.comparators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.Comparator;

/**
 * A comparator for {@link TiledMapTileLayer} which checks the Z property of two layers and returns which smaller
 */
public class MapLayerComparator implements Comparator<TiledMapTileLayer> {

    @Override
    public int compare(TiledMapTileLayer o1, TiledMapTileLayer o2) {
        try {
            int z1 = Integer.parseInt((String) o1.getProperties().get("z"));
            int z2 = Integer.parseInt((String) o2.getProperties().get("z"));
            return z1 - z2;
        } catch (ClassCastException e) {
            Gdx.app.error("Layer Comparator", "Property 'z' of one of the layers is not an integer!", e);
        } catch (NullPointerException e) {
            Gdx.app.error("Layer Comparator", "One of the layers does not contain the property 'z'!", e);
        }
        return 0;
    }

}
