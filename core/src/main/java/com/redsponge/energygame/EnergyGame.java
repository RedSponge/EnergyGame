package com.redsponge.energygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.screen.GameAccessor;
import com.redsponge.energygame.screen.GameScreen;

public class EnergyGame extends Game {

    private GameAccessor ga;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Assets assets;

    @Override
    public void create() {
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.assets = new Assets();

        this.ga = new GameAccessor(this);
        setScreen(new GameScreen(ga));
    }

    @Override
    public void render() {
        this.assets.update();
        super.render();
    }

    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public Assets getAssets() {
        return assets;
    }
}