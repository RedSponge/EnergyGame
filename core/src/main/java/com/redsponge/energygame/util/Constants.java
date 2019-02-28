package com.redsponge.energygame.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.energygame.system.PhysicsSystem;

public class Constants {

    /**
     * Default gravity for {@link PhysicsSystem}
     */
    public static final Vector2 DEFAULT_GRAVITY = new Vector2(0, -0);


    public static final int PLAYER_PRIORITY = 1;
    public static final int PHYSICS_PRIORITY = 2;
    public static final int RENDERING_PRIORITY = 4;

    public static final float DETECTOR_WIDTH = 3;
    public static final float DETECTOR_HEIGHT = 10;
    
    public static final int PHYSICS_POSITION_ITERATIONS = 3;
    public static final int PHYSICS_VELOCITY_ITERATIONS = 3;

    public static final int BODY_DATA_ID = 0;
    public static final int SENSOR_DATA_ID = 1;
    public static final int CIRCLE_DATA_ID = 2;
    public static final int ATTACK_DATA_ID = 3;
    public static final int ENEMY_DATA_ID = 4;
    public static final int PLATFORM_DATA_ID = 5;
    public static final int EVENT_DATA_ID = 6;

    public static final float DEFAULT_PPM = 32;
    public static final float FRICTION_MULTIPLIER = 0.9f;
    public static final float CHANGE_DIRECTION_MULTIPLIER = 10;

    public static final float DEFAULT_FALL_AMPLIFIER = -0.3f; // Added to the gravity when the player is falling

    public static final float DEFAULT_JUMP_HEIGHT = 10;
    public static final float DEFAULT_PLAYER_SPEED = 2;
    public static final float DEFAULT_MAX_SPEED = 5;
    public static final float DEFAULT_WALL_HOLD_VELOCITY = -5;

    public static final float GAME_WIDTH = 384;
    public static final float GAME_HEIGHT = 216;


    public static final float MAX_ENERGY = 200;

    public static final int HEAT_THRESHOLD = (int) MAX_ENERGY / 10;
    public static final int LIGHT_THRESHOLD = (int) MAX_ENERGY / 2;
    public static final int ELECTRIC_THRESHOLD = (int) MAX_ENERGY / 10 * 9;

    public static final Color NONE_COLOR = new Color(0x474747FF);
    public static final Color HEAT_COLOR = new Color(0xede07dFF);
    public static final Color LIGHT_COLOR = new Color(0xff891cFF);
    public static final Color ENERGY_COLOR = new Color(0xff1c1cFF);

    public static final float PLAYER_LOWER_PIXELS = 2;

    public static final float MENU_WIDTH = 480;
    public static final float MENU_HEIGHT = 360;

    public static final float HEAT_ATTACK_LENGTH = 0.5f;

    public static final float ELECTRIC_START_LENGTH = 0.5f;
}
