package com.redsponge.energy.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.redsponge.energy.EnergyGame;
import com.redsponge.energy.assets.Assets;
import com.redsponge.energy.transitions.Transition;
import com.redsponge.energy.transitions.TransitionScreen;

public class GameAccessor {

    private EnergyGame game;

    public GameAccessor(EnergyGame game) {
        this.game = game;
    }

    public void transitionTo(AbstractScreen to, Transition transition, float length) {
        AbstractScreen s = (AbstractScreen) game.getScreen();
        s.beginTransition();
        setScreen(new TransitionScreen(s, to, length, this, transition));
    }

    public void setScreen(ScreenAdapter screen) {
        game.setScreen(screen);
    }

    public SpriteBatch getSpriteBatch() {
        return game.getSpriteBatch();
    }

    public ShapeRenderer getShapeRenderer() {
        return game.getShapeRenderer();
    }

    public Assets getAssets() {
        return game.getAssets();
    }
}
