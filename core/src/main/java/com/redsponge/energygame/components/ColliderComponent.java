package com.redsponge.energygame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ColliderComponent implements Component {

    public Fixture left;
    public Fixture right;
    public Fixture up;
    public Fixture down;

    public int leftTouches = 0;
    public int rightTouches = 0;
    public int upTouches = 0;
    public int downTouches = 0;
}
