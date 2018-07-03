package com.evolution.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.GameScreen;
import com.evolution.game.Rules;

public class Consumable extends GamePoint {
    public enum Type {
        FOOD(0), BAD_FOOD(1);

        private int textureIndex;

        public int getTextureIndex() {
            return textureIndex;
        }

        Type(int textureIndex) {
            this.textureIndex = textureIndex;
        }
    }

    private Type type;
    private transient TextureRegion[] regions;

    public Type getType() {
        return type;
    }

    public void reloadResources(GameScreen gs, TextureRegion[] regions) {
        this.gs = gs;
        this.regions = regions;
        this.texture = regions[type.textureIndex];
    }

    public Consumable(GameScreen gs, TextureRegion[] regions) {
        this.gs = gs;
        this.regions = regions;
        this.texture = regions[0];
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.type = Type.FOOD;
        this.active = false;
        this.scale = 1.0f;
    }

    public void consumed() {
        active = false;
    }

    public void init(Type type) {
        this.position.set(MathUtils.random(0, Rules.GLOBAL_WIDTH), MathUtils.random(0, Rules.GLOBAL_HEIGHT));
        this.velocity.set(MathUtils.random(-30.0f, 30.0f), MathUtils.random(-30.0f, 30.0f));
        this.type = type;
        this.texture = regions[type.textureIndex];
        this.active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0) {
            position.x = Rules.GLOBAL_WIDTH;
        }
        if (position.y < 0) {
            position.y = Rules.GLOBAL_HEIGHT;
        }
        if (position.x > Rules.GLOBAL_WIDTH) {
            position.x = 0;
        }
        if (position.y > Rules.GLOBAL_HEIGHT) {
            position.y = 0;
        }
    }
}
