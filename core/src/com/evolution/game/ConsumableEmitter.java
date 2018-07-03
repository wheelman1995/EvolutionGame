package com.evolution.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Consumable;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    private transient GameScreen gs;
    private transient TextureRegion[] regions;
    private float time;
    private int badFoodChance;

    public void setBadFoodChance(int badFoodChance) {
        this.badFoodChance = badFoodChance;
    }

    public void reloadResources(GameScreen gs) {
        this.gs = gs;
        this.regions = new TextureRegion[3];
        this.regions[Consumable.Type.FOOD.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("Food");
        this.regions[Consumable.Type.BAD_FOOD.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("BadFood");
        this.regions[Consumable.Type.SPEEDUP.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("speedup");
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).reloadResources(gs, regions);
        }
        for (int i = 0; i < freeList.size(); i++) {
            freeList.get(i).reloadResources(gs, regions);
        }
    }

    public ConsumableEmitter(GameScreen gs) {
        this.gs = gs;
        this.regions = new TextureRegion[3];
        this.regions[Consumable.Type.FOOD.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("Food");
        this.regions[Consumable.Type.BAD_FOOD.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("BadFood");
        this.regions[Consumable.Type.SPEEDUP.getTextureIndex()] = Assets.getInstance().getAtlas().findRegion("speedup");
        this.generateConsumable(500);
        this.badFoodChance = 10;
    }

    @Override
    protected Consumable newObject() {
        return new Consumable(gs, regions);
    }


    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void generateConsumable(int count) {
        for (int i = 0; i < count; i++) {
            generateConsumable();
        }
    }

    public void generateConsumable() {
        boolean typeChanged = false;
        Consumable.Type type = Consumable.Type.FOOD;
        if (MathUtils.random(1, 100) <= badFoodChance) {
            type = Consumable.Type.BAD_FOOD;
            typeChanged = true;
        }
        if (!typeChanged && MathUtils.random(1, 100) <= 2) {
            type = Consumable.Type.SPEEDUP;
            typeChanged = true;
        }
        getActiveElement().init(type);
    }

    public void update(float dt) {
        time += dt;
        if (time >= 0.4f) {
            generateConsumable();
            time = 0.0f;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
