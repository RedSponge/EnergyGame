package com.redsponge.energygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.energygame.util.Constants;

public class MenuScreen extends AbstractScreen {

    private FitViewport viewport;
    private Stage stage;
    private boolean loadedResources;

    public MenuScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {
        loadedResources = false;

        viewport = new FitViewport(Constants.MENU_WIDTH, Constants.MENU_HEIGHT);
        stage = new Stage(viewport, batch);

        Button start = new TextButton("Start", new Skin());
    }

    @Override
    public void tick(float delta) {
        if(!assets.update()) {
            Gdx.app.log("MenuScreen", "Loading Assets!");
        } else if(!loadedResources) {
            assets.getResources();
            loadedResources = true;
        } else {
            stage.act(delta);
        }
    }

    @Override
    public void render() {
        if(!assets.update()) {
            Gdx.app.log("MenuScreen", "Loading Assets!");
            return;
        }
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
