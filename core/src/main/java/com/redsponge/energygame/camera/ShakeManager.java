package com.redsponge.energygame.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class ShakeManager implements CameraEffect {

    private Camera camera;
    private float numShakes;
    private Vector2 cameraOffset;
    private Vector2 desiredOffset;
    private Random random;
    private ShakeMode shakeMode;
    private int strength;

    public ShakeManager(Camera camera) {
        this(camera, ShakeMode.HARD);
    }

    public ShakeManager(Camera camera, ShakeMode shakeMode) {
        this(camera, shakeMode, 3);
    }

    public ShakeManager(Camera camera, ShakeMode shakeMode, int strength) {
        this.camera = camera;
        this.shakeMode = shakeMode;
        this.strength = strength;
        this.cameraOffset = new Vector2();
        this.desiredOffset = new Vector2();
        this.random = new Random();
    }

    @Override
    public void update(float delta) {
        if(numShakes > 0) {
            this.desiredOffset.add(random.nextInt(strength * 2) - strength, random.nextInt(strength * 2) - strength);
            numShakes--;
        } else {
            this.desiredOffset.set(0, 0);
        }

        switch (shakeMode) {
            case HARD:
                this.cameraOffset.set(this.desiredOffset);
                break;
            case LERP:
                this.cameraOffset.lerp(this.desiredOffset, 0.1f);
                break;
            default:
                Gdx.app.error("ShakeManager", "There was an error:", new CameraException("The desired ShakeMode wasn't implemented! <" + shakeMode + ">"));
        }
    }

    @Override
    public Matrix4 getProjectionMatrix() {
        return this.camera.combined.translate(new Vector3(cameraOffset, 0));
    }


    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }

    public void setShakeMode(ShakeMode shakeMode) {
        this.shakeMode = shakeMode;
    }

    public ShakeMode getShakeMode() {
        return shakeMode;
    }

    public void setNumShakes(float numShakes) {
        this.numShakes = numShakes;
    }

    public float getNumShakes() {
        return numShakes;
    }
}
