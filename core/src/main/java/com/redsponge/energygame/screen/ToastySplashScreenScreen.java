package com.redsponge.energygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.energygame.splashscreen.RedSpongeSplashScreenRenderer;
import com.redsponge.energygame.splashscreen.ToastySplashScreenRenderer;
import com.redsponge.energygame.transition.TransitionFade;

public class ToastySplashScreenScreen extends AbstractScreen {

    private ToastySplashScreenRenderer toastySplashScreenRenderer;
    private ScalingViewport scalingViewport;
    private AssetManager am;
    private boolean begin;

    public ToastySplashScreenScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void transitionSwitch() {
        am = new AssetManager();
        toastySplashScreenRenderer = new ToastySplashScreenRenderer(batch, am);
        this.scalingViewport = new ScalingViewport(Scaling.fill, 1, 1);
        am.load("textures/splashscreen/splashscreen_textures.atlas", TextureAtlas.class);
        am.finishLoading();
        toastySplashScreenRenderer.begin();
    }

    @Override
    public void show() {
        begin = true;
    }

    @Override
    public void tick(float delta) {
        if(!begin) return;
        toastySplashScreenRenderer.tick(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(35/255f, 175/255f, 150/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!begin) return;

        if(!toastySplashScreenRenderer.isComplete() && !Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            toastySplashScreenRenderer.render();
        } else if(!transitioning) {
            ga.transitionTo(new MenuScreen(ga), new TransitionFade(), 2);
        }
    }

    @Override
    public void resize(int width, int height) {
        toastySplashScreenRenderer.resize(width, height);
        scalingViewport.update(width, height, true);
    }


    @Override
    public void dispose() {
//        redSpongeSplashScreenRenderer.dispose();
        toastySplashScreenRenderer.dispose();
        am.dispose();
    }
}