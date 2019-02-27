package com.redsponge.energygame.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Packer {

    public static void main(String[] args) {
        TexturePacker.processIfModified("raw_textures", "../assets/textures/", "game_textures");
    }

}
