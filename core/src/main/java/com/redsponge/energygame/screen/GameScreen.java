package com.redsponge.energygame.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.component.PositionComponent;
import com.redsponge.energygame.map.MapFetcher;
import com.redsponge.energygame.map.MapManager;
import com.redsponge.energygame.system.EnemyCleanupSystem;
import com.redsponge.energygame.system.PhysicsDebugSystem;
import com.redsponge.energygame.transition.TransitionFade;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.system.PhysicsSystem;
import com.redsponge.energygame.system.PlayerSystem;
import com.redsponge.energygame.system.RenderingSystem;
import com.redsponge.energygame.util.EntityFactory;
import com.redsponge.energygame.util.GeneralUtils;

public class GameScreen extends AbstractScreen {

    private Engine engine;
    private FitViewport background;
    private FitViewport viewport;
    private FitViewport hudViewport;
    private TiledMap map;
    private float energy;
    private ScalingViewport scale;
    private MapManager mapManager;

    private Entity player;
    private Color barColor;
    private float displayedEnergy;

    private PooledEffect currentParticle;
    private long deathTime;

    private String text;
    private float textLength;

    public GameScreen(GameAccessor ga) {
        super(ga);
    }

    @Override
    public void show() {

    }

    @Override
    public void transitionSwitch() {
        assets.finishLoading();
        assets.getResources();
        assets.getMusics().background.load();
        assets.getMusics().background.getInstance().setLooping(true);
        assets.getMusics().background.getInstance().play();

        viewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        background = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        hudViewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        scale = new ScalingViewport(Scaling.fill, 1, 1);

        engine = new Engine();


        PhysicsSystem ps = new PhysicsSystem(new Vector2(0, -10), Constants.DEFAULT_PPM, null, assets, this);

        mapManager = new MapManager(ps, new TmxMapLoader().load("maps/tutorial/tutorial_shrunk.tmx"), engine);

        ps.setMapManager(mapManager);
        player = EntityFactory.getPlayer(assets);

        engine.addSystem(ps);
        engine.addEntityListener(Family.all(PositionComponent.class, PhysicsComponent.class).get(), ps);


        mapManager.setSystems(engine);
        mapManager.init();

        engine.addSystem(new PlayerSystem(this, assets));
//        engine.addSystem(new PhysicsDebugSystem(ps.getWorld(), viewport));
        engine.addSystem(new EnemyCleanupSystem(assets));
        engine.addSystem(new RenderingSystem(shapeRenderer, batch, viewport, player, mapManager, assets, this));

        engine.addEntity(player);
        barColor = new Color(0, 0, 0, 1);

        displayedEnergy = 0;
        currentParticle = assets.getParticles().sparkle.spawn(new Vector2(0, 0));

        viewport.getCamera().position.set(150, viewport.getWorldHeight(), 0);
    }

    @Override
    public void tick(float delta) {
        energy += 3;
        if(energy > Constants.MAX_ENERGY) {
            energy = Constants.MAX_ENERGY;
        }
        ((OrthographicCamera)viewport.getCamera()).zoom -= 0.01f;
        if(Mappers.player.get(player).dead) {
            if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
                ga.transitionTo(new GameScreen(ga), new TransitionFade(), 2);
            } else if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
                ga.transitionTo(new MenuScreen(ga), new TransitionFade(), 2);
            }
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        background.apply();

        batch.setProjectionMatrix(background.getCamera().combined);
        batch.begin();
        batch.draw(assets.getTextures().sky, 0, 0, background.getWorldWidth(), background.getWorldHeight());
        batch.end();

        engine.update(delta);
        PositionComponent pos = Mappers.position.get(player);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.end();
        assets.getParticles().cleanUp();
        renderHUD();
    }

    private void renderHUD() {
        hudViewport.apply();
        shapeRenderer.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.setProjectionMatrix(hudViewport.getCamera().combined);

        batch.begin();
        Color target = (energy < Constants.HEAT_THRESHOLD ? Constants.NONE_COLOR : energy < Constants.LIGHT_THRESHOLD ? Constants.HEAT_COLOR : energy < Constants.ELECTRIC_THRESHOLD ? Constants.LIGHT_COLOR : Constants.ENERGY_COLOR);
        barColor.lerp(target, 0.1f);

        assets.getTextures().bar.draw(batch, 30, hudViewport.getWorldHeight() - 30, (hudViewport.getWorldWidth() - 50), 16);
        batch.setColor(barColor);
        displayedEnergy = (1-0.1f) * displayedEnergy + 0.1f * energy;
        float progress = displayedEnergy / Constants.MAX_ENERGY;
        assets.getTextures().barFilling.draw(batch, 30, hudViewport.getWorldHeight() - 30, (hudViewport.getWorldWidth() - 50) * progress, 16);

        AtlasRegion face = energy < Constants.HEAT_THRESHOLD ? assets.getTextures().barNone
                : energy < Constants.LIGHT_THRESHOLD ? assets.getTextures().barLow
                : energy < Constants.ELECTRIC_THRESHOLD ? assets.getTextures().barMed
                : assets.getTextures().barHigh;


        batch.setColor(energy < Constants.HEAT_THRESHOLD ? Color.GRAY : Color.WHITE);
        batch.draw(assets.getTextures().barHeat, 30 + (viewport.getWorldWidth() - 40) * Constants.HEAT_THRESHOLD / Constants.MAX_ENERGY, hudViewport.getWorldHeight() - 30);

        batch.setColor(energy < Constants.LIGHT_THRESHOLD ? Color.GRAY : Color.WHITE);
        batch.draw(assets.getTextures().barLight, 30 + (viewport.getWorldWidth() - 40) * Constants.LIGHT_THRESHOLD / Constants.MAX_ENERGY, hudViewport.getWorldHeight() - 30);

        batch.setColor(energy < Constants.ELECTRIC_THRESHOLD ? Color.GRAY : Color.WHITE);
        batch.draw(assets.getTextures().barElectricity, 30 + (viewport.getWorldWidth() - 40) * Constants.ELECTRIC_THRESHOLD / Constants.MAX_ENERGY, hudViewport.getWorldHeight() - 30);

        batch.setColor(Color.WHITE);
        batch.draw(face, 10, hudViewport.getWorldHeight() - 40, face.getRegionWidth() * 2, face.getRegionHeight() * 2);

        if(Mappers.player.get(player).dead) {
            float opacity = GeneralUtils.secondsSince(deathTime) / 1f;
            opacity = opacity > 1 ? 1 : opacity;

            assets.getFonts().titleFont.setColor(0, 0, 0, opacity);
            assets.getFonts().titleFont.draw(batch, "You died.", hudViewport.getWorldWidth() / 2 - 150, 70);
            assets.getFonts().titleFont.getData().setScale(0.2f);
            assets.getFonts().titleFont.draw(batch, "Press space to try again!" ,120, 100);
            assets.getFonts().titleFont.draw(batch, "Press escape to return to menu!" ,130, 130);
            assets.getFonts().titleFont.getData().setScale(1);
        }
        if(textLength > 0) {
            assets.getFonts().pixelMix.setColor(1, 1, 1, 1);
            assets.getFonts().pixelMix.getData().setScale(0.2f);
            GlyphLayout layout = new GlyphLayout(assets.getFonts().pixelMix, text);
            assets.getFonts().pixelMix.draw(batch, text, hudViewport.getWorldWidth() / 2 - layout.width / 2, hudViewport.getWorldHeight() - 30);
            textLength -= Gdx.graphics.getDeltaTime();
        }

        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        background.update(width, height, true);
        hudViewport.update(width, height, true);
        scale.update(width, height, true);
    }

    public void addEnergy(float energy) {
        this.energy += energy;
        this.energy = this.energy < 0 ? 0 : this.energy;
        this.energy = this.energy > Constants.MAX_ENERGY ? Constants.MAX_ENERGY : this.energy;
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

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
        assets.getMusics().background.dispose();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextLength(float textLength) {
        this.textLength = textLength;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }
}