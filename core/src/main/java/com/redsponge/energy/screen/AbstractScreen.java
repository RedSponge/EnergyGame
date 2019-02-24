package com.redsponge.energy.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.redsponge.energy.assets.Assets;

public abstract class AbstractScreen extends ScreenAdapter {

    protected ShapeRenderer shapeRenderer;
    protected SpriteBatch batch;
    protected Assets assets;

    protected GameAccessor ga;
    protected boolean transitioning;

    public AbstractScreen(GameAccessor ga) {
        this.ga = ga;
        this.shapeRenderer = ga.getShapeRenderer();
        this.batch = ga.getSpriteBatch();
        this.assets = ga.getAssets();
    }

    @Override
    public void show() {
        transitioning = false;
    }

    @Override
    public final void render(float delta) {
        tick(delta);
        render();
    }

    public abstract void tick(float delta);
    public abstract void render();

    public void beginTransition() {
        transitioning = true;
    }
}
