package com.redsponge.energygame.map;

import com.redsponge.energygame.util.GeneralUtils;

public class MapFetcher {

    private static final String[] EASY_MAPS = {"cave", "cliff", "enemy_of_the_hill", "science_of_death"};
    private static String lastChosen = EASY_MAPS[0];

    public static String getEasyMap() {
        String choice = "";
        do {
            choice = GeneralUtils.randomFromArr(EASY_MAPS);
        } while(choice.equals(lastChosen));
        lastChosen = choice;
        System.out.println(choice);
        return "maps/easy/" + "science_of_death" + ".tmx";
    }

}
