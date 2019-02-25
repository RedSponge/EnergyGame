package com.redsponge.energygame.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.redsponge.energygame.components.ChainComponent;
import com.redsponge.energygame.components.CircleBottomComponent;
import com.redsponge.energygame.components.ColliderComponent;
import com.redsponge.energygame.components.EnemyComponent;
import com.redsponge.energygame.components.Mappers;
import com.redsponge.energygame.components.PhysicsComponent;
import com.redsponge.energygame.components.PositionComponent;
import com.redsponge.energygame.components.SizeComponent;
import com.redsponge.energygame.components.VelocityComponent;
import com.redsponge.energygame.utils.Constants;
import com.redsponge.energygame.utils.EntityFactory;
import com.redsponge.energygame.utils.GeneralUtils;
import com.redsponge.energygame.utils.SensorFactory;
import org.w3c.dom.css.Rect;

/**
 * Handles gravity and movement
 */
public class PhysicsSystem extends IteratingSystem implements EntityListener {

    private Vector2 gravity;
    private World world;
    private float pixelsPerMeter;

    public PhysicsSystem(Vector2 gravity, float pixelsPerMeter) {
        super(Family.all(PositionComponent.class, SizeComponent.class, VelocityComponent.class, PhysicsComponent.class).get(), Constants.PHYSICS_PRIORITY);
        this.gravity = gravity;
        this.pixelsPerMeter = pixelsPerMeter;
        this.world = new World(this.gravity, true);
    }

    public PhysicsSystem() {
        this(Constants.DEFAULT_GRAVITY, Constants.DEFAULT_PPM);
    }

    @Override
    public void update(float deltaTime) {
        world.step(deltaTime, Constants.PHYSICS_VELOCITY_ITERATIONS, Constants.PHYSICS_POSITION_ITERATIONS);
        super.update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = Mappers.position.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);

        pos.x = physics.body.getPosition().x * pixelsPerMeter;
        pos.y = physics.body.getPosition().y * pixelsPerMeter;
    }

    /**
     * Sets the gravity of the engine.
     * @param gravity The new gravity
     */
    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    /**
     * Returns the system's gravity
     * @return The gravity
     */
    public Vector2 getGravity() {
        return gravity;
    }

    /**
     * Create the platforms for the world
     * @param map - The world map
     */
    public void createWorldPlatforms(TiledMap map) {
        MapLayer layer = map.getLayers().get("Collidables");

        for (PolylineMapObject obj : new ArrayIterator<PolylineMapObject>(layer.getObjects().getByType(PolylineMapObject.class))) {
            Entity platform = PlatformFactory.createChainFloor(obj.getPolyline().getTransformedVertices());
            this.getEngine().addEntity(platform);
        }
        for (PolygonMapObject obj : new ArrayIterator<PolygonMapObject>(layer.getObjects().getByType(PolygonMapObject.class))) {
            Entity platform = PlatformFactory.createChainFloor(obj.getPolygon().getTransformedVertices());
            this.getEngine().addEntity(platform);
        }
    }

    public void createWorldEnemies(TiledMap map) {
        MapLayer layer = map.getLayers().get("Enemies");
        for(RectangleMapObject enemy : new ArrayIterator<RectangleMapObject>(layer.getObjects().getByType(RectangleMapObject.class))) {
            Rectangle rect = enemy.getRectangle();
            Entity e = EntityFactory.getEnemy(rect.x, rect.y, 30, 30);
            this.getEngine().addEntity(e);
        }
    }

    /**
     * Builds the new entity's body
     * @param entity
     */
    @Override
    public void entityAdded(Entity entity) {
        PhysicsComponent physics = Mappers.physics.get(entity);
        SizeComponent size = Mappers.size.get(entity);
        PositionComponent pos = Mappers.position.get(entity);
        ColliderComponent colliderComp = Mappers.collider.get(entity);
        ChainComponent chain = Mappers.chain.get(entity);
        CircleBottomComponent circle = Mappers.circle.get(entity);
        EnemyComponent enemy = Mappers.enemy.get(entity);


        // Body Creation
        BodyDef bdef = new BodyDef();
        bdef.type = physics.type;
        bdef.position.set(pos.x / pixelsPerMeter, pos.y / pixelsPerMeter);

        Body body = world.createBody(bdef);
        body.setUserData(entity);
        physics.body = body;

        FixtureDef collider = new FixtureDef();

        Shape shape = null;
        if(size != null) {
            shape = new PolygonShape();
            ((PolygonShape) shape).setAsBox((size.width / 2 - pixelsPerMeter * 0.01f) / pixelsPerMeter, (size.height / 2 - pixelsPerMeter * 0.01f) / pixelsPerMeter);
        } else if(chain != null){
            Gdx.app.log("PhysicsSystem", "CHAIN!");
            shape = new ChainShape();
            ((ChainShape)shape).createLoop(GeneralUtils.divideAll(chain.vertices, pixelsPerMeter));
        } else {
            Gdx.app.error("PhysicsSystem", "Platform Type Isn't Recognized!", new RuntimeException("Error!"));
            return;
        }
        collider.shape = shape;
        collider.friction = 0;
        collider.isSensor = enemy != null;

        body.createFixture(collider).setUserData(enemy == null ? Constants.BODY_USER_DATA : Constants.ENEMY_DATA_ID);
        if(enemy != null) {
            FixtureDef fdef = new FixtureDef();
            PolygonShape s = new PolygonShape();
            s.setAsBox(1 / pixelsPerMeter, 1 / pixelsPerMeter, new Vector2(0, -size.height / 2 / pixelsPerMeter + 1/pixelsPerMeter), 0);
            fdef.shape = s;
            body.createFixture(fdef).setUserData(Constants.BODY_USER_DATA);
            s.dispose();
        }
        shape.dispose();

        // Circle Creation
        if(circle != null && size != null) {
            FixtureDef c = new FixtureDef();
            c.friction = 0;
            CircleShape circleShape = new CircleShape();
            circleShape.setPosition(new Vector2(0, -size.height / 2 / pixelsPerMeter));
            circleShape.setRadius(circle.radius / pixelsPerMeter);

            c.shape = circleShape;
            circle.circle = body.createFixture(c);
            circle.circle.setUserData(Constants.CIRCLE_DATA_ID);

            circleShape.dispose();

        }

        // Sensors Creation
        if(colliderComp == null || size == null) {
            return;
        }

        float down = -size.height / 2 - (circle != null ? circle.radius : 0);
        float right = size.width / 2;
        float left = -right;
        float up = size.height / 2;
        float cornerSize = 5;

        colliderComp.down = SensorFactory.createCollideFixture(physics.body,  (circle == null ? size.width : 2), size.height, new Vector2(0, down), false, pixelsPerMeter);
        colliderComp.up = SensorFactory.createCollideFixture(physics.body, size.width, size.height, new Vector2(0, up), false, pixelsPerMeter);
        colliderComp.left = SensorFactory.createCollideFixture(physics.body, size.width, size.height, new Vector2(left, 0), true, pixelsPerMeter);
        colliderComp.right = SensorFactory.createCollideFixture(physics.body, size.width, size.height, new Vector2(right, 0), true, pixelsPerMeter);

        colliderComp.rightD = SensorFactory.createCollideFixture(physics.body, cornerSize, cornerSize+10, new Vector2(right, down+2), true, pixelsPerMeter);
        colliderComp.leftD = SensorFactory.createCollideFixture(physics.body, cornerSize, cornerSize+10, new Vector2(left, down+2), true, pixelsPerMeter);
        colliderComp.rightU = SensorFactory.createCollideFixture(physics.body, cornerSize, cornerSize, new Vector2(right, up), true, pixelsPerMeter);
        colliderComp.leftU = SensorFactory.createCollideFixture(physics.body, cornerSize, cornerSize, new Vector2(left, up), true, pixelsPerMeter);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.world.setContactListener(new CollisionManager(engine));
    }

    @Override
    public void entityRemoved(Entity entity) {
        world.destroyBody(Mappers.physics.get(entity).body);
    }

    public World getWorld() {
        return this.world;
    }
}
