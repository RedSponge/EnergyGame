package com.redsponge.energy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;

public class ParticleManager {

    private ParticleEffectPool pool;
    private String name;
    private DelayedRemovalArray<PooledEffect> spawned;

    public ParticleManager(String name) {
        this.name = name;
        spawned = new DelayedRemovalArray<PooledEffect>();
    }

    public void getResources(TextureAtlas atlas) {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal("particles/" + name), atlas);
        pool = new ParticleEffectPool(effect, 3, 10);
    }

    public void render(float delta, SpriteBatch batch) {
        for(PooledEffect p : spawned) {
            p.update(delta);
            p.draw(batch);
            if(p.isComplete()) {
                p.free();
                spawned.removeValue(p, true);
            }
        }
    }

    public PooledEffect spawn(Vector2 pos) {
        return spawn(pos, -1);
    }

    public PooledEffect spawn(Vector2 pos, int duration) {
        PooledEffect p = pool.obtain();
        p.setPosition(pos.x,pos.y);
        if(duration != -1) {
            p.setDuration(duration);
        }
        p.start();
        spawned.add(p);

        return p;
    }
}
