package com.redsponge.energygame.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.redsponge.energygame.assets.Assets;
import com.redsponge.energygame.component.EnemyComponent;
import com.redsponge.energygame.component.Mappers;
import com.redsponge.energygame.component.PositionComponent;
import com.redsponge.energygame.screen.GameScreen;
import com.redsponge.energygame.util.Constants;
import com.redsponge.energygame.util.GeneralUtils;

public class EnemyCleanupSystem extends IteratingSystem {

    private Assets assets;
    private GameScreen gameScreen;

    public EnemyCleanupSystem(Assets assets, GameScreen gameScreen) {
        super(Family.all(EnemyComponent.class).get(), Constants.ENEMY_PRIORITY);
        this.assets = assets;
        this.gameScreen = gameScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemy = Mappers.enemy.get(entity);
        PositionComponent pos = Mappers.position.get(entity);
        if(enemy.health < 1) {
            getEngine().removeEntity(entity);
            assets.getParticles().popcorn.spawn(new Vector2(pos.x, pos.y));
            GeneralUtils.playSoundRandomlyPitched(assets.getSounds().enemyKill, 1);
            gameScreen.addScore(150);
            assets.getParticles().enemyKillScore.spawn(new Vector2(pos.x, pos.y));
            RenderingSystem.shake = 5;
        }
    }
}
