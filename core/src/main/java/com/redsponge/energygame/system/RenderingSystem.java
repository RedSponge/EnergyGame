package com.redsponge.energygame.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.camera.CameraMode;
import com.redsponge.energygame.comparator.ZComparator;
import com.redsponge.energygame.component.*;
import com.redsponge.energygame.map.MapManager;
import com.redsponge.energygame.map.MapManagerRenderer;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.util.GeneralUtils;

public class RenderingSystem extends SortedIteratingSystem {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Entity player;
    private Viewport viewport;

    private MapManager mapManager;
    private MapManagerRenderer mapRenderer;
    private Assets assets;

    private CameraMode xMode, yMode;
    private int fixedCamX, fixedCamY;

    private PooledEffect sparkParticle;
    private PooledEffect electricParticle;

    private GameScreen gameScreen;

    private boolean sparkNeedsRestart;
    private boolean electricNeedsRestart;

    public static int shake;
    private Vector3 camPos = new Vector3();

    public RenderingSystem(ShapeRenderer shapeRenderer, SpriteBatch batch, Viewport viewport, Entity player, MapManager mapManager, Assets assets, GameScreen gameScreen) {
        super(Family.all(PositionComponent.class, SizeComponent.class, AnimationComponent.class).get(), new ZComparator(), Constants.RENDERING_PRIORITY);
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.player = player;
        this.viewport = viewport;
        this.mapManager = mapManager;
        this.assets = assets;
        this.gameScreen = gameScreen;
        this.mapRenderer = new MapManagerRenderer(mapManager, batch);
        this.xMode = this.yMode = CameraMode.AUTO;

        this.sparkNeedsRestart = true;
        this.electricNeedsRestart = true;
    }


    @Override
    public void update(float deltaTime) {
        setupCameraAndMatrices();
        AnimatedTiledMapTile.updateAnimationBaseTime();
        viewport.apply();
        mapRenderer.renderBackground(viewport);

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        SizeComponent size = Mappers.size.get(player);
        PositionComponent pos = Mappers.position.get(player);
        AnimationComponent animation = Mappers.animation.get(player);
        DirectionComponent direction = Mappers.direction.get(player);
        PlayerComponent pc = Mappers.player.get(player);

        if(pc.dead) {
            AtlasRegion f = assets.getTextures().dead.getKeyFrames()[0];
            batch.draw(f, pos.x - f.getRegionWidth() / 2f, pos.y - f.getRegionHeight() / 2f + 4);
        } else {
            if (!sparkNeedsRestart)
                sparkParticle.setPosition(pos.x, pos.y);
            if (!electricNeedsRestart)
                electricParticle.setPosition(pos.x, pos.y);
            assets.getParticles().render(deltaTime, batch);

            float energy = gameScreen.getEnergy();

            if (sparkParticle != null && sparkParticle.isComplete() || sparkNeedsRestart) {
                if (energy > Constants.HEAT_THRESHOLD) {
                    if (sparkNeedsRestart) {
                        sparkNeedsRestart = false;
                        sparkParticle = assets.getParticles().sparkle.spawn(new Vector2());
                    } else {
                        sparkParticle.reset();
                    }
                } else {
                    sparkNeedsRestart = true;
                }
            }

            if (electricParticle != null && electricParticle.isComplete() || electricNeedsRestart) {
                if (energy > Constants.ELECTRIC_THRESHOLD) {
                    if (electricNeedsRestart) {
                        electricNeedsRestart = false;
                        electricParticle = assets.getParticles().electric.spawn(new Vector2());
                    } else {
                        electricParticle.reset();
                    }
                } else {
                    electricNeedsRestart = true;
                }
            }

            assets.getParticles().cleanUp();


            animation.timeSinceStart += deltaTime;
            AtlasRegion frame = animation.animation.getKeyFrame(animation.timeSinceStart);

            batch.draw(frame, pos.x - (size.width) * direction.direction.mult, pos.y - size.height / 2 - Constants.PLAYER_LOWER_PIXELS, frame.getRegionWidth() * direction.direction.mult, frame.getRegionHeight());
            if (pc.energy.isElectricFieldOn() || pc.energy.getElectric().isRemoving()) {
                float time = GeneralUtils.secondsSince(pc.energy.getElectric().getStartTime());

                AtlasRegion f;
                if (pc.energy.getElectric().isChargingField()) {
                    f = assets.getTextures().elecBallSpawn.getKeyFrame(time);
                } else if (pc.energy.getElectric().isRemoving()) {
                    f = assets.getTextures().elecBallRemove.getKeyFrame(time - pc.energy.getElectric().getLength());
                } else {
                    f = assets.getTextures().elecBallExist.getKeyFrame(time);
                }

                batch.draw(f, pos.x - f.getRegionWidth(), pos.y - f.getRegionHeight(), f.getRegionWidth() * 2, f.getRegionHeight() * 2);
            }
        }
        super.update(deltaTime);
        batch.end();

        if(pos.x < viewport.getCamera().position.x - viewport.getWorldWidth() * ((OrthographicCamera) viewport.getCamera()).zoom / 2 - 50 && !pc.dead) {
            pc.dead = true;
            gameScreen.setDeathTime(TimeUtils.nanoTime());
            assets.getMusics().background.dispose();
            assets.getSounds().death.play(1);
        }

        mapRenderer.renderForeground(viewport);

        if(shake > 0) {
            viewport.getCamera().position.set(camPos);
            shake--;
        }
    }


    private float speed = 1f;
    private float desiredZoom = 1;

    private void setupCameraAndMatrices() {
        PositionComponent pos = Mappers.position.get(player);
        PlayerComponent p = Mappers.player.get(player);


        float zoom = ((OrthographicCamera) viewport.getCamera()).zoom;
        ((OrthographicCamera) viewport.getCamera()).zoom = (1-.1f) * zoom + 0.1f * desiredZoom;


        if(xMode == CameraMode.AUTO) {
            viewport.getCamera().position.x += speed;
            gameScreen.addScore(speed);
        } else if(xMode == CameraMode.PLAYER) {
            viewport.getCamera().position.x = (1-.1f) * viewport.getCamera().position.x + 0.1f * pos.x;
        } else {
            viewport.getCamera().position.x = (1-.1f) * viewport.getCamera().position.x + 0.1f * fixedCamX;
        }

        if(yMode == CameraMode.AUTO) {
            viewport.getCamera().position.y = (1-.1f) * viewport.getCamera().position.y + 0.1f * viewport.getWorldHeight();
        } else if(yMode == CameraMode.PLAYER) {
            viewport.getCamera().position.y = (1-.1f) * viewport.getCamera().position.y + 0.1f * pos.y;
        } else {
            viewport.getCamera().position.y = (1-.1f) * viewport.getCamera().position.y + 0.1f * fixedCamY;
        }

        if(pos.x > viewport.getCamera().position.x) {
            viewport.getCamera().position.x = pos.x;
        }

        PlayerComponent pc = Mappers.player.get(player);

        if(pc.dead) {
            viewport.getCamera().position.lerp(new Vector3(pos.x + 10, pos.y - 10, 0), 0.1f);
            desiredZoom = 0.3f;
        }

        if(viewport.getCamera().position.x - viewport.getWorldWidth() / 2 * ((OrthographicCamera) viewport.getCamera()).zoom < 0) {
            viewport.getCamera().position.x = viewport.getWorldWidth() / 2 * ((OrthographicCamera)viewport.getCamera()).zoom;
        }
        if(viewport.getCamera().position.y - viewport.getWorldHeight() / 2 * ((OrthographicCamera) viewport.getCamera()).zoom < 0) {
            viewport.getCamera().position.y = viewport.getWorldHeight() / 2 * ((OrthographicCamera)viewport.getCamera()).zoom;
        }
        camPos.set(viewport.getCamera().position);

        if(shake > 0) {
            viewport.getCamera().position.x += MathUtils.random(-10, 10);
            viewport.getCamera().position.y += MathUtils.random(-10, 10);
        }
        viewport.apply();
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(entity == player) return;
        PositionComponent pos = Mappers.position.get(entity);
        SizeComponent size = Mappers.size.get(entity);
        AnimationComponent anim = Mappers.animation.get(entity);

        anim.timeSinceStart += deltaTime;
        batch.draw(anim.animation.getKeyFrame(anim.timeSinceStart), pos.x - size.width / 2, pos.y - size.height / 2);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        mapManager.dispose();
        mapRenderer.dispose();
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }

    public void setDesiredZoom(float desiredZoom) {
        this.desiredZoom = desiredZoom;
    }

    public void setCameraModes(String x, String y) {
        CameraMode xm = getCameraMode(x);
        if(xm == CameraMode.FIXED) {
            fixedCamX = Integer.parseInt(x);
        }
        this.xMode = xm;

        CameraMode ym = getCameraMode(y);
        if(ym == CameraMode.FIXED) {
            fixedCamY = Integer.parseInt(y);
        }
        this.yMode = ym;
        Gdx.app.log("RenderingSystem", "Changed Camera Modes: " + xMode + "," + yMode);
    }

    private CameraMode getCameraMode(String s) {
        if(s.equals("player")) {
            return CameraMode.PLAYER;
        } else if(s.equals("auto")) {
            return CameraMode.AUTO;
        } else {
            return CameraMode.FIXED;
        }
    }

    public void addSpeed(float speed) {
        this.speed += speed;
        if(this.speed > 2.5f) {
            this.speed = 2.5f;
        }
    }

    public float getCameraSpeed() {
        return speed;
    }
}