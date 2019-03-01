package com.redsponge.energygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.energygame.map.MapFetcher;
import com.redsponge.energygame.map.MapUtils;
import com.redsponge.energygame.map.OffsettedOrthogonalTiledMapRenderer;
import com.redsponge.energygame.transition.TransitionFade;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.util.GeneralUtils;

public class MenuScreen extends AbstractScreen {

    private FitViewport viewport;
    private FitViewport mapViewport;
    private Stage stage;
    private boolean loadedResources;
    private OffsettedOrthogonalTiledMapRenderer mapRenderer;
    private int offsetX;
    private TiledMap map;

    public MenuScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void transitionSwitch() {
        viewport = new FitViewport(Constants.MENU_WIDTH, Constants.MENU_HEIGHT);
        mapViewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        loadedResources = false;
    }

    public void initMenu() {
        stage = new Stage(viewport, batch);

        Image title = new Image(assets.getTextures().title);
        title.setPosition(10, viewport.getWorldHeight() - 150);
        title.setScale(2);
        stage.addActor(title);

        TextButton startButton = new TextButton("Run!", assets.getSkins().menu);
        startButton.setPosition(-startButton.getWidth(), viewport.getWorldHeight() - 200);
        stage.addActor(startButton);

        TextButton optionsButton = new TextButton("Options!", assets.getSkins().menu);
        optionsButton.setPosition(-optionsButton.getWidth(), viewport.getWorldHeight() - 250);
        stage.addActor(optionsButton);

        TextButton creditsButton = new TextButton("Credits!", assets.getSkins().menu);
        creditsButton.setPosition(-creditsButton.getWidth(), viewport.getWorldHeight() - 300);
        stage.addActor(creditsButton);

        TextButton exitButton = new TextButton("Exit ;-; (pls no i has wif an kid)", assets.getSkins().menu);
        exitButton.setPosition(-exitButton.getWidth(), viewport.getWorldHeight() - 350);
        stage.addActor(exitButton);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ga.transitionTo(new GameScreen(ga), new TransitionFade(), 1);
            }
        });

        final TextButton[] buttons = {startButton, optionsButton, creditsButton, exitButton};

        float delay = 0.2f;

        for(int i = 0; i < buttons.length; i++) {
            final int j = i;
            buttons[i].addAction(Actions.delay(i * delay, Actions.moveTo(30, buttons[i].getY(), 1f, Interpolation.swing)));
            buttons[i].addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    buttons[j].addAction(Actions.moveTo(50, buttons[j].getY(), 0.2f, Interpolation.exp5));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    buttons[j].addAction(Actions.moveTo(30, buttons[j].getY(), 0.2f, Interpolation.exp5));
                }
            });
        }

        Gdx.input.setInputProcessor(stage);

        map = new TmxMapLoader().load("maps/easy/enemy_of_the_hill.tmx");
        mapRenderer = new OffsettedOrthogonalTiledMapRenderer(map, batch);

        assets.getMusics().background.load();
        assets.getMusics().background.getInstance().setLooping(true);
        assets.getMusics().background.getInstance().play();

    }

    @Override
    public void tick(float delta) {
        if(!assets.update()) {
        } else if(!loadedResources) {
            assets.getResources();
            loadedResources = true;
            initMenu();
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        batch.begin();
        batch.draw(assets.getTextures().sky, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        mapViewport.getCamera().position.set(offsetX++ + mapViewport.getWorldWidth() / 2, 200, 0);
        mapViewport.apply();
        mapRenderer.setView((OrthographicCamera) mapViewport.getCamera());
        mapRenderer.setOffsetX(0);
        mapRenderer.render();
        mapRenderer.setOffsetX(MapUtils.getMapWidth(map));
        mapRenderer.render();

        if(offsetX > MapUtils.getMapWidth(map) * 1.5f)
            offsetX -= MapUtils.getMapWidth(map);

        viewport.apply();
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        mapViewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        map.dispose();
        assets.getMusics().background.dispose();
    }
}
