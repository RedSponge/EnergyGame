package com.redsponge.energygame.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.energygame.screen.AbstractScreen;
import com.redsponge.energygame.screen.GameAccessor;

public class TransitionScreen extends AbstractScreen {

    private AbstractScreen from;
    private AbstractScreen to;
    private float transitionLength;
    private float counter;
    private ScalingViewport viewport;
    private Transition transition;
    private boolean initiated;

    public TransitionScreen(AbstractScreen from, AbstractScreen to, float transitionLength, GameAccessor ga, Transition transition) {
        super(ga);
        this.from = from;
        this.to = to;
        this.transitionLength = transitionLength;
        this.transition = transition;
    }

    @Override
    public void show() {
        counter = 0;
        viewport = new ScalingViewport(Scaling.fill, 1, 1);
        initiated = false;
    }

    @Override
    public void tick(float delta) {
        counter += delta;
    }

    @Override
    public void render() {
        if(counter < transitionLength / 2) {
            if(from != null) {
                from.render();
            } else {
                counter = transitionLength / 2;
            }
        } else if(counter < transitionLength) {
            if(to != null) {
                if(!initiated) {
                    from.hide();
                    from.dispose();
                    to.transitionSwitch();
                    to.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                    initiated = true;
                }
                to.tick(Gdx.graphics.getDeltaTime());
                to.render();
            } else {
                throw new RuntimeException("Tried using TransitionScreen but `to` screen is null!");
            }
        } else {
            ga.setScreen(to);
            return;
        }

        transition.render(counter, Interpolation.linear, Interpolation.linear, transitionLength, shapeRenderer);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);
        transition.resize(width, height);
        if(initiated)
            to.resize(width, height);
        else
            from.resize(width, height);
    }
}
