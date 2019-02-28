package com.redsponge.energygame.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class AnimationComponent implements Component {

    public Animation<AtlasRegion> animation;
    public float timeSinceStart;

    public AnimationComponent(Animation<AtlasRegion> animation) {
        this.animation = animation;
    }
}
