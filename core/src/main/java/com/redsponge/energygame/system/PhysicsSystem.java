package com.redsponge.energygame.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.redsponge.energygame.component.ChainComponent;
import com.redsponge.energygame.component.CircleBottomComponent;
import com.redsponge.energygame.component.ColliderComponent;
import com.redsponge.energygame.component.EnemyComponent;
import com.redsponge.energygame.component.EventComponent;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PhysicsComponent;
import com.redsponge.energygame.component.PlatformComponent;
import com.redsponge.energygame.component.PositionComponent;
import com.redsponge.energygame.component.SizeComponent;
import com.redsponge.energygame.component.VelocityComponent;
import com.redsponge.energygame.map.MapManager;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.util.EntityFactory;
import com.redsponge.energygame.util.GeneralUtils;
import com.redsponge.energygame.util.SensorFactory;

/**
 * Handles gravity and movement
 */
public class PhysicsSystem extends IteratingSystem implements EntityListener {

    private Vector2 gravity;
    private World world;
    private float pixelsPerMeter;

    /**
     * The map manager
     * <bold>Must be set BEFORE the system is added to the engine!</bold>
     */
    private MapManager mapManager;

    public PhysicsSystem(Vector2 gravity, float pixelsPerMeter, MapManager mapManager) {
        super(Family.all(PositionComponent.class, SizeComponent.class, VelocityComponent.class, PhysicsComponent.class).get(), Constants.PHYSICS_PRIORITY);
        this.gravity = gravity;
        this.pixelsPerMeter = pixelsPerMeter;
        this.mapManager = mapManager;
        this.world = new World(this.gravity, true);
    }

    public PhysicsSystem(MapManager mapManager) {
        this(Constants.DEFAULT_GRAVITY, Constants.DEFAULT_PPM, mapManager);
    }

    public void setMapManager(MapManager mapManager) {
        this.mapManager = mapManager;
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
     * @return The gravity8
     */
    public Vector2 getGravity() {
        return gravity;
    }

    /**
     * Create the platforms for the world
     * @param map - The world map
     */
    public Entity[] createWorldPlatforms(TiledMap map, int offset) {
        Array<Entity> entities = new Array<Entity>();
        MapLayer layer = map.getLayers().get("Collidables");
        for (PolygonMapObject obj : new ArrayIterator<PolygonMapObject>(layer.getObjects().getByType(PolygonMapObject.class))) {
            Entity platform = PlatformFactory.createChainFloor(GeneralUtils.transformAll(obj.getPolygon().getTransformedVertices(), offset, 0));
            entities.add(platform);
            this.getEngine().addEntity(platform);
        }
        return entities.toArray(Entity.class);
    }

    public Entity[] createWorldEnemies(TiledMap map, float offset) {
        Array<Entity> entities = new Array<Entity>();
        MapLayer layer = map.getLayers().get("Enemies");
        for(RectangleMapObject enemy : new ArrayIterator<RectangleMapObject>(layer.getObjects().getByType(RectangleMapObject.class))) {
            Rectangle rect = enemy.getRectangle();
            Entity e = EntityFactory.getEnemy(rect.x + offset, rect.y, 30, 30);
            entities.add(e);
            this.getEngine().addEntity(e);
        }
        return entities.toArray(Entity.class);
    }

    public Entity[] createWorldEvents(TiledMap map, float offset) {
        Array<Entity> entities = new Array<Entity>();
        MapLayer layer = map.getLayers().get("Events");
        for(RectangleMapObject event : new ArrayIterator<RectangleMapObject>(layer.getObjects().getByType(RectangleMapObject.class))) {
            Rectangle rect = event.getRectangle();
            Entity e = EntityFactory.getEventEntity(rect.x + offset + rect.width / 2, rect.y + rect.height / 2, rect.width, rect.height, event.getProperties());
            entities.add(e);
            this.getEngine().addEntity(e);
        }
        //TODO: Get bodies!
        return entities.toArray(Entity.class);
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
        EventComponent event = Mappers.event.get(entity);
        PlatformComponent platform = Mappers.platform.get(entity);


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
        collider.isSensor = enemy != null || event != null;

        body.createFixture(collider).setUserData(enemy == null ? event != null ? Constants.EVENT_DATA_ID : platform != null ? Constants.PLATFORM_DATA_ID : Constants.BODY_DATA_ID : Constants.ENEMY_DATA_ID);
        if(enemy != null) {
            FixtureDef fdef = new FixtureDef();
            PolygonShape s = new PolygonShape();
            s.setAsBox(1 / pixelsPerMeter, 1 / pixelsPerMeter, new Vector2(0, -size.height / 2 / pixelsPerMeter + 1/pixelsPerMeter), 0);
            fdef.shape = s;
            body.createFixture(fdef).setUserData(Constants.BODY_DATA_ID);
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

        colliderComp.down = SensorFactory.createCollideFixture(physics.body,  (circle == null ? size.width : 2), size.height, new Vector2(0, down), false, pixelsPerMeter);
        colliderComp.up = SensorFactory.createCollideFixture(physics.body, size.width, size.height, new Vector2(0, up), false, pixelsPerMeter);
        colliderComp.left = SensorFactory.createCollideFixture(physics.body, size.width, size.height, new Vector2(left, 0), true, pixelsPerMeter);
        colliderComp.right = SensorFactory.createCollideFixture(physics.body, size.width, size.height, new Vector2(right, 0), true, pixelsPerMeter);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.world.setContactListener(new CollisionManager(engine, mapManager));
    }

    @Override
    public void entityRemoved(Entity entity) {
        world.destroyBody(Mappers.physics.get(entity).body);
    }

    public World getWorld() {
        return this.world;
    }

    public Entity[] loadNewMap(TiledMap map, int offset) {
        Array<Entity> entities = new Array<Entity>();
        entities.addAll(createWorldPlatforms(map, offset));
        entities.addAll(createWorldEnemies(map, offset));
        entities.addAll(createWorldEvents(map, offset));
        return entities.toArray(Entity.class);
    }
}
