package com.redsponge.energygame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array.ArrayIterator;
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
        loadMainMenu();
        Gdx.input.setInputProcessor(stage);

        map = new TmxMapLoader().load("maps/easy/enemy_of_the_hill.tmx");
        mapRenderer = new OffsettedOrthogonalTiledMapRenderer(map, batch);

        assets.getMusics().background.load();
        assets.getMusics().background.getInstance().setLooping(true);
        assets.getMusics().background.getInstance().play();
    }

    public void loadMainMenu() {
        boolean shouldDelay = unloadCurrent();
        float loadDelay = shouldDelay ? 1 : 0;

        Label title = new Label("MicroMania!", new LabelStyle(assets.getFonts().titleFont, Color.BLACK));
        title.setColor(Color.BLACK);
        title.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() + 50, Align.center);
        title.addAction(Actions.delay(loadDelay, Actions.moveTo(title.getX(), viewport.getWorldHeight() - title.getHeight() - 10, 1, Interpolation.swingOut)));
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

        TextButton exitButton = new TextButton("Exit!", assets.getSkins().menu);
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
                assets.getMusics().background.dispose();
                ga.transitionTo(new GameScreen(ga), new TransitionFade(), 1);
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadCredits();
            }
        });

        final TextButton[] buttons = {startButton, optionsButton, creditsButton, exitButton};

        float delay = 0.2f;

        for(int i = 0; i < buttons.length; i++) {
            final int j = i;
            buttons[i].addAction(Actions.delay(i * delay + loadDelay, Actions.moveTo(30, buttons[i].getY(), 1f, Interpolation.swing)));
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
    }

    public boolean unloadCurrent() {
        boolean actorsExisted = false;
        for(Actor a : new ArrayIterator<Actor>(stage.getActors())) {
            actorsExisted = true;
            if(a instanceof Button) {
                ((Button)a).setDisabled(true);
                a.clearListeners();
                a.addAction(Actions.moveTo(-a.getWidth(), a.getY(), 1, Interpolation.swingIn));
            } else if(a instanceof Label) {
                a.addAction(Actions.moveTo(a.getX(), -a.getHeight(), 1, Interpolation.swingIn));
            }
            a.addAction(Actions.delay(1, new Action() {
                @Override
                public boolean act(float delta) {
                    actor.remove();
                    return true;
                }
            }));
        }
        return actorsExisted;
    }

    public void loadCredits() {
        String[][] credits = {
                {"Programming:", "RedSponge"},
                {"Art:", "RedSponge &\nTheCrispyToasty"},
                {"Music:", "TheCrispyToasty"},
                {"", ""},
                {"Tools Used:", "BFXR, Bosca Ceoil, Audacity,\nThe Libgdx Skin Composer\nand Tiled"},
                {"Random Message: ", GeneralUtils.randomFromArr(new String[] {"We hope you enjoy!", "#MicrowaveFTW", "Libgdx is cool", "Press 'Space' To Jump!",
                "I wonder how much\ndoes a Shawarma cost.."})}
        };
        unloadCurrent();
        final float spacing = 40;
        final float fromTop = 60;
        for(int i = 0; i < credits.length; i++) {
            Label l = new Label(credits[i][0], assets.getSkins().menu);
            l.setPosition(viewport.getWorldWidth() / 6, -l.getHeight() - fromTop - spacing * i);
            l.addAction(Actions.delay(1 + i * 0.2f, Actions.moveTo(l.getX(), viewport.getWorldHeight() - fromTop - spacing * i, 0.5f, Interpolation.pow2)));
            stage.addActor(l);

            Label l2 = new Label(credits[i][1], assets.getSkins().menu);
            l2.setPosition(viewport.getWorldWidth() / 6 * 3, -l2.getHeight() - fromTop - spacing * i);
            l2.addAction(Actions.delay(1.5f + i * 0.2f, Actions.moveTo(l2.getX(), viewport.getWorldHeight() - fromTop - spacing * i, 0.5f, Interpolation.pow2)));
            stage.addActor(l2);
        }
        TextButton back = new TextButton("Go Back", assets.getSkins().menu);
        back.setPosition(viewport.getWorldWidth() / 2, -back.getHeight(), Align.center);
        back.addAction(Actions.delay(0.2f * credits.length + 1, Actions.moveTo(back.getX(), 50, 0.5f, Interpolation.pow2)));

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadMainMenu();
            }
        });
        stage.addActor(back);
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
