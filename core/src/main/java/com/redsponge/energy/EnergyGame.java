package com.redsponge.energy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.redsponge.energy.assets.Assets;
import com.redsponge.energy.screen.GameAccessor;
import com.redsponge.energy.screen.GameScreen;
import com.redsponge.energy.screen.SplashScreenScreen;

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