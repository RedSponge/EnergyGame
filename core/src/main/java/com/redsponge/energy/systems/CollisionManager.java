package com.redsponge.energy.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Logger;
import com.redsponge.energy.components.ColliderComponent;
import com.redsponge.energy.components.Mappers;
import com.redsponge.energy.constants.Constants;

import static com.badlogic.gdx.Application.LOG_DEBUG;

public class CollisionManager implements ContactListener {

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

            handleSensorCollision(sensor, true);
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

            handleSensorCollision(sensor, false);
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

        if(sensor == collider.rightU) {
            Gdx.app.debug("Collision", "RightUp Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.rightUTouches += adder;
        }
        if(sensor == collider.leftU) {
            Gdx.app.debug("Collision", "LeftUp Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.leftUTouches += adder;
        }
        if(sensor == collider.rightD) {
            Gdx.app.debug("Collision", "RightDown Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.rightDTouches += adder;
        }
        if(sensor == collider.leftD) {
            Gdx.app.debug("Collision", "LeftDown Sensor Collision " + (collisionStart ? "Start" : "End") + "!");
            collider.leftDTouches += adder;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
