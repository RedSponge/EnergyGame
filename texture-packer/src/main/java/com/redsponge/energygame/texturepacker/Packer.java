package com.redsponge.energygame.texturepacker;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Packer {

    public static void main(String[] args) {
        TexturePacker.processIfModified("raw_textures/player", "../assets/textures/player", "game_textures");
        TexturePacker.processIfModified("raw_textures/particle", "../assets/particles", "textures");
    }

}
