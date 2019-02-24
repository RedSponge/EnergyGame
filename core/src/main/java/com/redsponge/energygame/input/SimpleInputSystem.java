package com.redsponge.energygame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/**
 * A simple example which inherits from {@link InputSystem}
 */
public class SimpleInputSystem implements InputSystem {

    private final int rightButton;
    private final int leftButton;
    private final int upButton;
    private final int downButton;
    private final int jumpButton;
    private final int heatButton;
    private final int lightButton;
    private final int electricityButton;


    public SimpleInputSystem(int rightButton, int leftButton, int upButton, int downButton, int jumpButton, int heatButton, int lightButton, int electricityButton) {
        this.rightButton = rightButton;
        this.leftButton = leftButton;
        this.upButton = upButton;
        this.downButton = downButton;
        this.jumpButton = jumpButton;
        this.heatButton = heatButton;
        this.lightButton = lightButton;
        this.electricityButton = electricityButton;
    }

    public SimpleInputSystem() {
        this(Keys.RIGHT, Keys.LEFT, Keys.UP, Keys.DOWN, Keys.SPACE, Keys.Z, Keys.X, Keys.C);
    }

    @Override
    public int getHorizontal() {
        int right = Gdx.input.isKeyPressed(rightButton) ? 1 : 0;
        int left = Gdx.input.isKeyPressed(leftButton) ? -1 : 0;
        return right + left;
    }

    @Override
    public int getVertical() {
        int up = Gdx.input.isKeyPressed(upButton) ? 1 : 0;
        int down = Gdx.input.isKeyPressed(downButton) ? -1 : 0;
        return up + down;
    }

    @Override
    public boolean isJumping() {
        return Gdx.input.isKeyPressed(jumpButton);
    }

    @Override
    public boolean isJustJumping() {
        return Gdx.input.isKeyJustPressed(jumpButton);
    }

    @Override
    public boolean isHeatPressed() {
        return Gdx.input.isKeyJustPressed(heatButton);
    }

    @Override
    public boolean isLightPressed() {
        return Gdx.input.isKeyJustPressed(lightButton);
    }

    @Override
    public boolean isElectricity() {
        return Gdx.input.isKeyJustPressed(electricityButton);
    }
}
