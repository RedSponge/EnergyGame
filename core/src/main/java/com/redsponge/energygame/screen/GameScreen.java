package com.redsponge.energygame.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.components.PositionComponent;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.systems.PhysicsDebugSystem;
import com.redsponge.energygame.systems.PhysicsSystem;
import com.redsponge.energygame.systems.PlayerSystem;
import com.redsponge.energygame.systems.RenderingSystem;
import com.redsponge.energygame.utils.EntityFactory;

public class GameScreen extends AbstractScreen {

    private Engine engine;
    private FitViewport viewport;
    private FitViewport hudViewport;
    private TiledMap map;
    private float energy;

    public GameScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {
        assets.finishLoading();
        assets.getResources();

        viewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        hudViewport = new FitViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);

        engine = new Engine();

        PhysicsSystem ps = new PhysicsSystem(new Vector2(0, -10), Constants.DEFAULT_PPM);
        engine.addSystem(ps);
        engine.addEntityListener(Family.all(PositionComponent.class, PhysicsComponent.class).get(), ps);
        engine.addSystem(new PlayerSystem(this));
        engine.addSystem(new PhysicsDebugSystem(ps.getWorld(), viewport));

        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/debugmap.tmx");

        Entity player = EntityFactory.getPlayer();
        ps.createWorldObjects(map);
        engine.addSystem(new RenderingSystem(shapeRenderer, batch, viewport, player, map));
        engine.addEntity(player);
        engine.addEntity(EntityFactory.getEnemy(200, 100, 30, 30));
        engine.addEntity(EntityFactory.getEnemy(300, 100, 30, 30));
        engine.addEntity(EntityFactory.getEnemy(400, 100, 30, 30));
        engine.addEntity(EntityFactory.getEnemy(500, 100, 30, 30));
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void render() {
        addEnergy(10);
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        renderHUD();
    }

    private void renderHUD() {
        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);

        batch.begin();
        assets.getFonts().pixelMix.setColor(Color.BLACK);
        assets.getFonts().pixelMix.getData().setScale(0.5f);
        assets.getFonts().pixelMix.draw(batch, "Energy: " + energy, 10, hudViewport.getWorldHeight() - 30);
        assets.getFonts().pixelMix.draw(batch, "Can Heat: " + (energy >= Constants.HEAT_THRESHOLD), 10, hudViewport.getWorldHeight() - 60);
        assets.getFonts().pixelMix.draw(batch, "Can Light: " + (energy >= Constants.LIGHT_THRESHOLD), 10, hudViewport.getWorldHeight() - 90);
        assets.getFonts().pixelMix.draw(batch, "Can Elec: " + (energy >= Constants.ELECTRIC_THRESHOLD), 10, hudViewport.getWorldHeight() - 120);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hudViewport.update(width, height, true);
    }

    public void addEnergy(float energy) {
        this.energy += energy;
        this.energy = this.energy < 0 ? 0 : this.energy;
    }

    public float getEnergy() {
        return energy;
    }

    public void addToEngine(Entity entity) {
        engine.addEntity(entity);
    }

    public void removeFromEngine(Entity entity) {
        engine.removeEntity(entity);
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}