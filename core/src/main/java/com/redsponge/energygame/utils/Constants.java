package com.redsponge.energygame.utils;

import com.badlogic.gdx.math.Vector2;
import com.redsponge.energygame.systems.PhysicsSystem;

public class Constants {

    /**
     * Default gravity for {@link PhysicsSystem}
     */
    public static final Vector2 DEFAULT_GRAVITY = new Vector2(0, -10);


    public static final int PLAYER_PRIORITY = 1;
    public static final int PHYSICS_PRIORITY = 2;
    public static final int RENDERING_PRIORITY = 4;

    public static final float DETECTOR_WIDTH = 3;
    public static final float DETECTOR_HEIGHT = 10;
    
    public static final int PHYSICS_POSITION_ITERATIONS = 3;
    public static final int PHYSICS_VELOCITY_ITERATIONS = 3;

    public static final int BODY_USER_DATA = 0;
    public static final int SENSOR_DATA_ID = 1;
    public static final int CIRCLE_DATA_ID = 2;
    public static final int ATTACK_DATA_ID = 3;

    public static final float DEFAULT_PPM = 32;
    public static final float FRICTION_MULTIPLIER = 0.9f;
    public static final float CHANGE_DIRECTION_MULTIPLIER = 10;

    public static final float DEFAULT_FALL_AMPLIFIER = -0.5f; // Added to the gravity when the player is falling

    public static final float DEFAULT_JUMP_HEIGHT = 8;
    public static final float DEFAULT_PLAYER_SPEED = 20;
    public static final float DEFAULT_MAX_SPEED = 10;
    public static final float DEFAULT_WALL_HOLD_VELOCITY = -5;

    public static final float GAME_WIDTH = 480;
    public static final float GAME_HEIGHT = 360;

    public static final int HEAT_THRESHOLD = 10;
    public static final int LIGHT_THRESHOLD = 50;
    public static final int ELECTRIC_THRESHOLD = 90;

    public static final float HUD_WIDTH = 640;
    public static final float HUD_HEIGHT = 480;
}
