package com.redsponge.energygame.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.components.PositionComponent;
import com.redsponge.energygame.maps.MapManager;
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
    private ScalingViewport scale;
    private MapManager mapManager;

    private Entity player;

    public GameScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {
        assets.finishLoading();
        assets.getResources();

        viewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        hudViewport = new FitViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);
        scale = new ScalingViewport(Scaling.fill, 1, 1);

        engine = new Engine();

        mapManager = new MapManager();

        PhysicsSystem ps = new PhysicsSystem(new Vector2(0, -10), Constants.DEFAULT_PPM, mapManager);
        engine.addSystem(ps);
        engine.addEntityListener(Family.all(PositionComponent.class, PhysicsComponent.class).get(), ps);
        engine.addSystem(new PlayerSystem(this));
        engine.addSystem(new PhysicsDebugSystem(ps.getWorld(), viewport));


        player = EntityFactory.getPlayer();
        engine.addSystem(new RenderingSystem(shapeRenderer, batch, viewport, player));

        mapManager.setSystems(engine);
        mapManager.loadNextMap();

        engine.addEntity(player);
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scale.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        shapeRenderer.end();

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
        scale.update(width, height, true);
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