package com.redsponge.energygame.map;

import com.badlogic.gdx.Gdx;
import com.redsponge.energygame.util.GeneralUtils;

public class MapFetcher {

    private static final String[] EASY_MAPS = {"bridge", "cave", "cliff", "path_of_enemies", "the_holes"};
    private static String lastChosen = EASY_MAPS[0];

    public static String getEasyMap() {
        String choice = "";
        do {
            choice = GeneralUtils.randomFromArr(EASY_MAPS);
        } while(choice.equals(lastChosen));
        lastChosen = choice;
        Gdx.app.log("MapFetcher", "Chosen map " + choice);
        return "maps/easy/" + choice + ".tmx";
    }

}
