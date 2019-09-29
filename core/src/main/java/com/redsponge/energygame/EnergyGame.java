package com.redsponge.energygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.screen.AbstractScreen;
import com.redsponge.energygame.screen.GameAccessor;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.screen.MenuScreen;
import com.redsponge.energygame.screen.RedSpongeSplashScreenScreen;
import com.redsponge.energygame.screen.ToastySplashScreenScreen;
import com.redsponge.energygame.transition.TransitionFade;

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

    @Override
    public void setScreen(Screen screen) {
        assert screen instanceof AbstractScreen;
        setScreen((AbstractScreen) screen);
    }

    public void setScreen(AbstractScreen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
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