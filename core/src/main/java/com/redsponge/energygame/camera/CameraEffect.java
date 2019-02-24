package com.redsponge.energygame.camera;

import com.badlogic.gdx.math.Matrix4;

public interface CameraEffect {

    void update(float delta);

    Matrix4 getProjectionMatrix();

}
