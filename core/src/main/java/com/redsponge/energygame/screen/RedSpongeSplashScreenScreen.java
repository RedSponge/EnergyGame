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

public class RedSpongeSplashScreenScreen extends AbstractScreen {

    private RedSpongeSplashScreenRenderer redSpongeSplashScreenRenderer;
    private ScalingViewport scalingViewport;
    private AssetManager am;

    public RedSpongeSplashScreenScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {
        super.show();
        am = new AssetManager();
        this.scalingViewport = new ScalingViewport(Scaling.fill, 1, 1);
        am.load("textures/splashscreen/splashscreen_textures.atlas", TextureAtlas.class);
        am.finishLoading();
        redSpongeSplashScreenRenderer = new RedSpongeSplashScreenRenderer(batch, am);
        redSpongeSplashScreenRenderer.begin();
    }

    @Override
    public void tick(float delta) {
        redSpongeSplashScreenRenderer.tick(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(255/255f, 237/255f, 178/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!redSpongeSplashScreenRenderer.isComplete() && !Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            redSpongeSplashScreenRenderer.render();
        } else if(!transitioning) {
            ga.transitionTo(new ToastySplashScreenScreen(ga), new TransitionFade(), 2);
        }
    }

    @Override
    public void resize(int width, int height) {
        redSpongeSplashScreenRenderer.resize(width, height);
        scalingViewport.update(width, height, true);
    }


    @Override
    public void dispose() {
        redSpongeSplashScreenRenderer.dispose();
        am.dispose();
    }
}