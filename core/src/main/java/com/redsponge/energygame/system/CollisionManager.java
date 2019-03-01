package com.redsponge.energygame.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.redsponge.energygame.component.ColliderComponent;
import com.redsponge.energygame.component.EnemyComponent;
import com.redsponge.energygame.component.EventComponent;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PlayerComponent;
import com.redsponge.energygame.map.MapManager;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.util.Pair;

public class CollisionManager implements ContactListener {

    private Engine engine;
    private MapManager mapManager;
    private RenderingSystem rs;

    public CollisionManager(Engine engine, MapManager mapManager) {
        this.engine = engine;
        this.mapManager = mapManager;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == null || fixB.getUserData() == null) {
            Gdx.app.log("CollisionManager", "Collision Start between fixtures without user data!");
            return;
        }
        if(fixA.getUserData().equals(Constants.SENSOR_DATA_ID) || fixB.getUserData().equals(Constants.SENSOR_DATA_ID)) {
            Fixture sensor = (fixA.getUserData().equals(Constants.SENSOR_DATA_ID) ? fixA : fixB);
            Fixture other = (sensor == fixA) ? fixB : fixA;
            if(Mappers.platform.get((Entity) other.getBody().getUserData()) != null) {
                handleSensorCollision(sensor, true);
            }
        }

        if(fixA.getUserData().equals(Constants.ENEMY_DATA_ID) || fixB.getUserData().equals(Constants.ENEMY_DATA_ID)) {
            Fixture enemy = (fixA.getUserData().equals(Constants.ENEMY_DATA_ID) ? fixA : fixB);
            Fixture other = (enemy == fixA) ? fixB : fixA;
            enemyCollision(enemy, other);
        }

        if(fixA.getUserData().equals(Constants.EVENT_DATA_ID) || fixB.getUserData().equals(Constants.EVENT_DATA_ID)) {
            Fixture event = (fixA.getUserData().equals(Constants.EVENT_DATA_ID) ? fixA : fixB);
            Fixture other = (event == fixA) ? fixB : fixA;
            eventCollision(event, other);
        }
    }

    private void eventCollision(Fixture event, Fixture other) {
        EventComponent eventC = Mappers.event.get((Entity) event.getBody().getUserData());
        PlayerComponent player = Mappers.player.get((Entity) other.getBody().getUserData());

        if(player == null) {
            Gdx.app.log("Event", "Event collided with someone other than player");
        } else if(!eventC.executed){
            eventC.executed = true;
            if(eventC.event.equals("loadnext")) {
                Gdx.app.log("Event", "LoadNext Event Called!");
                mapManager.loadNextMap();
            } else if(eventC.event.equals("zoom")) {
                Gdx.app.log("Event", "Zoom Event Called!");
                this.engine.getSystem(RenderingSystem.class).setDesiredZoom(Float.parseFloat(eventC.props.get("data", String.class)));
            } else if(eventC.event.equals("camera")) {
                Gdx.app.log("Event", "Camera Event Called!");
                this.engine.getSystem(RenderingSystem.class).setCameraModes(eventC.props.get("x", String.class), eventC.props.get("y", String.class));
            }
        }
    }

    /**
     *
     * @return <wanted fixture><other fixture> or null if the user data wasn't there
     */
    public Pair<Fixture, Fixture> getCertainFixture(Contact c, Object data) {
        Fixture fixA = c.getFixtureA();
        Fixture fixB = c.getFixtureB();

        if(fixA.getUserData().equals(data) || fixB.getUserData().equals(data)) {
            Fixture wanted = (fixA.getUserData().equals(data) ? fixA : fixB);
            Fixture other = fixA == wanted ? fixB : fixA;
            return new Pair<Fixture, Fixture>(fixA, fixB);
        } else {
            return null;
        }
    }

    private void enemyCollision(Fixture enemy, Fixture other) {
        Entity e = (Entity) enemy.getBody().getUserData();
        Entity o = (Entity) other.getBody().getUserData();
        PlayerComponent player = Mappers.player.get(o);
        EnemyComponent ec = Mappers.enemy.get(e);
        if(other.getUserData().equals(Constants.ATTACK_DATA_ID)) {
            ec.health = 0;
        }
        else if(player != null) {
            if(player.energy.isSuperDashOn()) {
                ec.health = 0;
            } else {
                player.dead = true;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(fixA.getUserData() == null || fixB.getUserData() == null) {
            Gdx.app.log("CollisionManager", "Collision End between fixtures without user data!");
            return;
        }

        if(fixA.getUserData().equals(Constants.SENSOR_DATA_ID) || fixB.getUserData().equals(Constants.SENSOR_DATA_ID)) {
            Fixture sensor = (fixA.getUserData().equals(Constants.SENSOR_DATA_ID) ? fixA : fixB);
            Fixture other = sensor == fixA ? fixB : fixA;

            if(Mappers.platform.get((Entity) other.getBody().getUserData()) != null) {
                handleSensorCollision(sensor, false);
            }
        }
    }

    public void handleSensorCollision(Fixture sensor, boolean collisionStart) {
        Entity entity = (Entity) sensor.getBody().getUserData();
        ColliderComponent collider = Mappers.collider.get(entity);
        final int adder = collisionStart ? 1 : -1;

        if(sensor == collider.right) {
            Gdx.app.debug("Collision", "Right Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.rightTouches += adder;
        }
        if(sensor == collider.left) {
            Gdx.app.debug("Collision", "Left Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.leftTouches += adder;
        }
        if(sensor == collider.up) {
            Gdx.app.debug("Collision", "Up Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.upTouches += adder;
        }
        if(sensor == collider.down) {
            Gdx.app.debug("Collision", "Down Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.downTouches += adder;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
