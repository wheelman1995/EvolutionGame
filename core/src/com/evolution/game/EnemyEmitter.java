package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Enemy;

public class EnemyEmitter extends ObjectPool<Enemy> {
    private transient GameScreen gs;
    private float time;

    @Override
    protected Enemy newObject() {
        return new Enemy(gs);
    }

    public void reloadResources(GameScreen gs) {
        this.gs = gs;
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).reloadResources(gs);
        }
        for (int i = 0; i < freeList.size(); i++) {
            freeList.get(i).reloadResources(gs);
        }
    }

    public EnemyEmitter(GameScreen gs) {
        this.gs = gs;
        this.addObjectsToFreeList(20);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time >= 3.0f) {
            time = 0.0f;
            getActiveElement().init();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
