package com.redsponge.energygame.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {

    public float x = 0;
    public float y = 0;
    public float z = 0;

    public PositionComponent(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionComponent(float x, float y) {
        this(x, y, 0);
    }

    public PositionComponent() {}

    public PositionComponent(Vector2 pos) {
        this(pos.x, pos.y);
    }
}
