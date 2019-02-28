package com.redsponge.energygame.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.TimeUtils;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.component.AnimationComponent;
import com.redsponge.energygame.component.ColliderComponent;
import com.redsponge.energygame.component.DirectionComponent;
import com.redsponge.energygame.component.DirectionComponent.Direction;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PlayerComponent;
import com.redsponge.energygame.energy.EnergyManager;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.energy.Energy;
import com.redsponge.energygame.input.InputSystem;
import com.redsponge.energygame.input.SimpleInputSystem;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.GeneralUtils;

public class PlayerSystem extends IteratingSystem {


    private float jumpHeight;
    private float speed;
    private float maxSpeed;
    private float jumpMaxTime;
    private long jumpStartTime;
    private long wallJumpStartTime;
    private boolean takeControlWhileWallJump;
    private float wallHoldVelocity;

    private float pixelsPerMeter;


    private float fallAmplifier;

    // Flags
    private boolean onGround;
    private boolean jumping;
    private boolean holdingWall;

    // Show Debug Messages
    private static final boolean DEBUG = true;


    private InputSystem input;
    private float wallJumpLength;
    private GameScreen gameScreen;

    private long lastMoved;

    private EnergyManager energy;
    private Assets assets;

    public PlayerSystem(float jumpHeight, float speed, float maxSpeed, float jumpMaxTime, float pixelsPerMeter, float fallAmplifier, float wallHoldVelocity, InputSystem inputSystem, GameScreen gameScreen, Assets assets) {
        super(Family.all(PlayerComponent.class).get(), Constants.PLAYER_PRIORITY);
        this.jumpHeight = jumpHeight;
        this.speed = speed;
        this.maxSpeed = maxSpeed;
        this.jumpMaxTime = jumpMaxTime;
        this.pixelsPerMeter = pixelsPerMeter;
        this.fallAmplifier = fallAmplifier;
        this.wallHoldVelocity = wallHoldVelocity;
        this.input = inputSystem;
        this.gameScreen = gameScreen;
        this.assets = assets;
        this.jumpStartTime = 0;
        this.wallJumpStartTime = 0;
        this.takeControlWhileWallJump = false;
        this.wallJumpLength = 0.2f;
        this.lastMoved = 0;


        this.energy = new EnergyManager(gameScreen, pixelsPerMeter);
    }

    public PlayerSystem(GameScreen gameScreen, Assets assets) {
        this(Constants.DEFAULT_JUMP_HEIGHT, Constants.DEFAULT_PLAYER_SPEED, Constants.DEFAULT_MAX_SPEED, 0.15f, Constants.DEFAULT_PPM, Constants.DEFAULT_FALL_AMPLIFIER, Constants.DEFAULT_WALL_HOLD_VELOCITY, new SimpleInputSystem(), gameScreen, assets);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Body body = Mappers.physics.get(entity).body;
        ColliderComponent collider = Mappers.collider.get(entity);
        PlayerComponent player = Mappers.player.get(entity);
        player.energy = energy;
        
        updateFlags(collider);

        energy.setPlayer(entity);
        energy.setOnGround(onGround);

        updateJumping(body, deltaTime);
        updateWallJumping(collider, body);
        // TODO: Add Power System, inputs are done.

        updateStrafing(entity, body, deltaTime);
        updateAttacks();
        energy.update(deltaTime);

        applyFriction(body);
        updateFallVelocity(body);

        clampSpeed(body);

        updateAnimation(entity, player, body);
    }

    private void updateAnimation(Entity entity, PlayerComponent playerC, Body body) {
        AnimationComponent animation = Mappers.animation.get(entity);
        boolean idle = body.getLinearVelocity().x == 0;

        if(gameScreen.getEnergy() < Constants.LIGHT_THRESHOLD) {
            if(playerC.energy.isSuperDashOn()) {
                animation.animation = assets.getTextures().lowDash;
            } else {
                animation.animation = assets.getTextures().lowRun;
            }
        } else if(gameScreen.getEnergy() < Constants.ELECTRIC_THRESHOLD) {
            if(playerC.energy.isSuperDashOn()) {
                animation.animation = assets.getTextures().medDash;
            } else {
                animation.animation = assets.getTextures().medRun;
            }
        } else {
            if(playerC.energy.isSuperDashOn()) {
                animation.animation = assets.getTextures().highDash;
            } else {
                animation.animation = assets.getTextures().highRun;
            }
        }
    }

    private void updateAttacks() {
        if(input.isHeatPressed()) {
            processEnergyInput(energy.getHeat());
        } else if(input.isLightPressed()) {
            processEnergyInput(energy.getLight());
        } else if(input.isElectricity()){
            processEnergyInput(energy.getElectric());
        }
    }

    private void processEnergyInput(Energy energy) {
        if(gameScreen.getEnergy() < energy.getMinEnergy()) {
            return;
        }
        switch (input.getVertical()) {
            case 1:
                energy.upInitiated(gameScreen);
                break;
            case -1:
                energy.downInitiated(gameScreen);
                break;
            default:
                energy.regularInitiated(gameScreen);
                break;
        }
    }

    /**
     * Updates the flags of the player
     * @param collider - The {@link ColliderComponent} instance of the {@link Entity}
     */
    private void updateFlags(ColliderComponent collider) {
        onGround = collider.downTouches > 0;
        holdingWall = collider.rightTouches > 0 || collider.leftTouches > 0;
    }



    //region Input && Movement
    /**
     * Detects jumps and updates jump status
     * @param body - The player's {@link Body}
     * @param deltaTime The delta time since the last frame
     */
    private void updateJumping(Body body, float deltaTime) {
        if(input.isJumping()) {
            if(onGround && !jumping) {
                startJump(body, deltaTime);
            } else if(jumping) {
                continueJump(body, deltaTime);
            }
        } else {
            if(jumping) {
                endJump(body, true);
            }
        }
    }

    /**
     * Detects wall jumps and updates wall jump status
     * @param collider - The player's {@link ColliderComponent}
     * @param body - The player's {@link Body}
     */
    private void updateWallJumping(ColliderComponent collider, Body body) {
        if(input.isJustJumping() && !jumping && holdingWall) {
            int side = collider.rightTouches > 0 ? -1 : 1;
            if(input.getHorizontal() != 0) {
                takeControlWhileWallJump = true;
            } else {
                takeControlWhileWallJump = false;
            }
            wallJumpStartTime = TimeUtils.nanoTime();
            body.setLinearVelocity(5 * side, 15);
        }
        if(GeneralUtils.secondsSince(wallJumpStartTime) > wallJumpLength) {
            takeControlWhileWallJump = false;
        }
    }

    /**
     * Detects strafing input and updates movement
     * @param body - The player's {@link Body}
     * @param deltaTime - The delta time since the last frame
     */
    private void updateStrafing(Entity e, Body body, float deltaTime) {
        if(!takeControlWhileWallJump) {
            int horiz = input.getHorizontal();
            float energyMultiplier = 1;//gameScreen.getEnergy() / 2 + 0.1f;

            if(horiz != 0) {
                if(horiz != Math.signum(body.getLinearVelocity().x)) {
                    body.applyLinearImpulse(new Vector2(horiz * speed * deltaTime * Constants.CHANGE_DIRECTION_MULTIPLIER, 0), body.getWorldCenter(), true);
                } else {
                    body.applyLinearImpulse(new Vector2(horiz * speed * deltaTime * energyMultiplier, 0), body.getWorldCenter(), true);
                    if (onGround) {
                        gameScreen.addEnergy(0.1f);
                    }
                }
                lastMoved = TimeUtils.nanoTime();
                DirectionComponent dir = Mappers.direction.get(e);
                dir.direction = Direction.fromNumber(horiz);
            }
        }

        if(GeneralUtils.secondsSince(lastMoved) > 1) {
            gameScreen.addEnergy(-0.05f * GeneralUtils.secondsSince(lastMoved));
        }
    }
    //endregion

    //region Velocity tweaking
    /**
     * Applies friction to the player's character when on ground and not moving
     * @param body - The player's {@link Body}
     */
    private void applyFriction(Body body) {
        if(onGround && input.getHorizontal() == 0) {
            body.setLinearVelocity(body.getLinearVelocity().x * Constants.FRICTION_MULTIPLIER, body.getLinearVelocity().y);
        }
    }

    /**
     * Updates the player's y velocity:
     * if the player is air-born and isn't jumping: stronger gravity
     * if the player is holding a wall: softer gravity
     * @param body - The player's {@link Body}
     */
    private void updateFallVelocity(Body body) {
        if(GeneralUtils.secondsSince(wallJumpStartTime) < wallJumpLength) {
            body.applyLinearImpulse(new Vector2(0, 0), body.getWorldCenter(), true);
        } else if(!onGround && !jumping) {
            body.applyLinearImpulse(new Vector2(0, fallAmplifier), body.getWorldCenter(), true);
        }

        if(holdingWall && input.getHorizontal() != 0) {
            if(body.getLinearVelocity().y < wallHoldVelocity) {
                body.setLinearVelocity(body.getLinearVelocity().x, wallHoldVelocity);
            }
        }
    }

    /**
     * Clamps the speed of the player to {@link PlayerSystem#maxSpeed}
     * @param body - The player's {@link Body}
     */
    private void clampSpeed(Body body) {
        float newVx = body.getLinearVelocity().x;
        float newVy = body.getLinearVelocity().y;

        if(Math.abs(newVx) > maxSpeed && !energy.isSuperDashOn())
        {
            newVx = maxSpeed * Math.signum(newVx);
        }
        if(newVy > maxSpeed && !energy.isSuperJumpOn() && !jumping)
        {
            newVy = maxSpeed * Math.signum(newVy);
        }

        body.setLinearVelocity(newVx, newVy);
    }
    //endregion

    //region Jumping
    /////////////////////// JUMP METHODS ////////////////////////////////
    /**
     * Begins a jump with a small boost
     * @param body - The box2d {@link Body} of the player
     */
    private void startJump(Body body, float delta) {
        jumping = true;
        jumpStartTime = TimeUtils.nanoTime();
        body.applyLinearImpulse(new Vector2(0, jumpHeight * delta * 50), body.getWorldCenter(), true);
    }

    /**
     * Continues a jump if the player is jumping
     * @param body - The box2d {@link Body} of the player
     * @param delta - The delta time
     */
    private void continueJump(Body body, float delta) {
        if(!jumping) {
            return;
        }

        float timeSince = TimeUtils.timeSinceNanos(jumpStartTime) / 1000000000f;
        if(timeSince > jumpMaxTime) {
            endJump(body, false);
            return;
        }
        body.applyLinearImpulse(new Vector2(0, jumpHeight * delta), body.getWorldCenter(), true);
    }

    /**
     * Ends a jump if the player is currently jumping
     * @param body - The player's body
     * @param forceDown - Whether or not to cancel all upwards momentum (to control the jump height more)
     */
    private void endJump(Body body, boolean forceDown) {
        if(!jumping) {
            return;
        }
        jumping = false;
        if(forceDown) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0.5f);
        }
    }
    //endregion

    /////////////////////////// Utility //////////////////////////////

    private void _DEBUG(String toDebug) {
        if(DEBUG) {
            Gdx.app.log("Player", toDebug);
        }
    }
}
