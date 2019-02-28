package com.redsponge.energygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.energygame.splashscreen.SplashScreenRenderer;
import com.redsponge.energygame.transition.TransitionFade;

public class SplashScreenScreen extends AbstractScreen {
    private SplashScreenRenderer splashScreenRenderer;
    private ScalingViewport scalingViewport;

    public SplashScreenScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {
        super.show();
        this.scalingViewport = new ScalingViewport(Scaling.fill, 1, 1);
        splashScreenRenderer = new SplashScreenRenderer(batch);
        splashScreenRenderer.begin();
    }

    @Override
    public void tick(float delta) {
        splashScreenRenderer.tick(delta);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(255/255f, 237/255f, 178/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!splashScreenRenderer.isComplete() && !Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            splashScreenRenderer.render();
        } else if(!transitioning) {
            ga.transitionTo(new MenuScreen(ga), new TransitionFade(), 2);
        }
    }

    @Override
    public void resize(int width, int height) {
        splashScreenRenderer.resize(width, height);
        scalingViewport.update(width, height, true);
    }


    @Override
    public void dispose() {
        splashScreenRenderer.dispose();
    }
}