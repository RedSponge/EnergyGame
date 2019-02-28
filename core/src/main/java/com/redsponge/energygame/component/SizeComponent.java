package com.redsponge.energygame.component;

import com.badlogic.ashley.core.Component;

public class SizeComponent implements Component {

    public float width;
    public float height;

    public SizeComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public SizeComponent() {}
}
