package com.redsponge.energygame.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

public class ColliderComponent implements Component {

    public Fixture left;
    public Fixture right;
    public Fixture up;
    public Fixture down;

    public Fixture rightD;
    public Fixture rightU;
    public Fixture leftD;
    public Fixture leftU;

    public int leftTouches = 0;
    public int rightTouches = 0;
    public int upTouches = 0;
    public int downTouches = 0;

    public int leftDTouches = 0;
    public int leftUTouches = 0;
    public int rightDTouches = 0;
    public int rightUTouches = 0;
}
