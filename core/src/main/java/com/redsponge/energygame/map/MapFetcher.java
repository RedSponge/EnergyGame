package com.redsponge.energygame.map;

import com.badlogic.gdx.Gdx;
import com.redsponge.energygame.util.GeneralUtils;

public class MapFetcher {

    private static final String[] EASY_MAPS = {"bridge", "cave", "path_of_enemies", "the_holes", "Big_Big_hole",
    "sans_undertale", "the_brig", "the_hole_of_shame", "true_friends", "parkour_is_fun_pls", "beach", "wall_jump"};
    private static String lastChosen = "";

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
