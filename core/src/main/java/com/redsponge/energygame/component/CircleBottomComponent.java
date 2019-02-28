package com.redsponge.energygame.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class CircleBottomComponent implements Component {

    public Fixture circle;
    public float radius;

    public CircleBottomComponent(float radius) {
        this.radius = radius;
    }

    public CircleBottomComponent() {
        this(10);
    }
}
