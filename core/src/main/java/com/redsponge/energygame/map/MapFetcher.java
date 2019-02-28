package com.redsponge.energygame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.redsponge.energygame.util.GeneralUtils;

public class MapFetcher {

    public static String getEasyMap() {
        FileHandle handles = Gdx.files.local("../assets/maps/easy");
        FileHandle handle = GeneralUtils.randomFromArr(handles.list());
        return handle.path().substring("../assets/".length());
    }

}
