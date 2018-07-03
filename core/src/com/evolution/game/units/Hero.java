package com.evolution.game.units;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.Assets;
import com.evolution.game.GameScreen;
import com.evolution.game.Joystick;
import com.evolution.game.Rules;
import com.evolution.game.ScreenManager;

public class Hero extends Cell {
    private transient TextureRegion[] regions;
    private float animationTimer;
    private float timePerFrame;
    private StringBuilder guiString;
    private int score;
    private int showedScore;
    private transient Joystick joystick;
    private transient TextureRegion health;
    private transient TextureRegion emptyHealth;
    private int hp;
    private int lostHP;
    private int heroScoreId;

    public void setHeroScoreId(int heroScoreId) {
        this.heroScoreId = heroScoreId;
    }

    public int getHeroScoreId() {
        return heroScoreId;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void reloadResources(GameScreen gs, Joystick joystick) {
        this.gs = gs;
        this.joystick = joystick;
        this.regions = new TextureRegion(Assets.getInstance().getAtlas().findRegion("Char")).split(64, 64)[0];
        this.health = new TextureRegion(Assets.getInstance().getAtlas().findRegion("health"));
        this.emptyHealth = new TextureRegion(Assets.getInstance().getAtlas().findRegion("emptyHealth"));
    }

    public Hero(GameScreen gs, Joystick joystick) {
        super(640.0f, 360.0f, 300.0f);
        this.gs = gs;
        this.regions = new TextureRegion(Assets.getInstance().getAtlas().findRegion("Char")).split(64, 64)[0];
        this.timePerFrame = 0.1f;
        this.scale = 1.0f;
        this.guiString = new StringBuilder(200);
        this.joystick = joystick;
        this.health = new TextureRegion(Assets.getInstance().getAtlas().findRegion("health"));
        this.emptyHealth = new TextureRegion(Assets.getInstance().getAtlas().findRegion("emptyHealth"));
        hp = 5;
        heroScoreId = gs.getHighestScores().length - 1;
    }

    @Override
    public void consumed() {
        position.set(MathUtils.random(0, Rules.WORLD_WIDTH), MathUtils.random(0, Rules.WORLD_HEIGHT));
        scale = 1.0f;
        lostHP++;
        hp--;
        if(hp == 0) {
            gs.saveHeroScoreId(heroScoreId);
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER);
        }
    }

    @Override
    public void eatConsumable(Consumable.Type type) {
        super.eatConsumable(type);
        switch (type) {
            case FOOD:
                score += 1000;
                gs.checkHighestScores(score, heroScoreId);
                break;
        }
    }

    public void update(float dt) {
        super.update(dt);
        animationTimer += dt;
        if (showedScore < score) {
            int delta = (int) ((score - showedScore) * 0.02f);
            if (delta < 4) {
                delta = 4;
            }
            showedScore += delta;
            if (showedScore > score) {
                showedScore = score;
            }
        }
        if (joystick.isActive()) {
            float angleToTarget = joystick.getAngle();
            if (angle > angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle -= 180.0f * dt;
                } else {
                    angle += 180.0f * dt;
                }
            }
            if (angle < angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle += 180.0f * dt;
                } else {
                    angle -= 180.0f * dt;
                }
            }
            acceleration = joystick.getPower() * 300;
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
        gs.getParticleEmitter().setup(position.x, position.y, MathUtils.random(-10, 10), MathUtils.random(-10, 10), 0.5f, 5f, 2f, 0.3f, 0.3f, 0, 0.2f, 0.2f, 0.2f, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        int currentFrame = (int) (animationTimer / timePerFrame) % regions.length;
        batch.draw(regions[currentFrame], position.x - 32f, position.y - 32f, 32, 32, 64, 64, scale, scale, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        guiString.setLength(0);
        guiString.append("Score: ").append(showedScore);
        font.draw(batch, guiString, 20, 700);
        for (int i = 0; i < hp; i++) {
            batch.draw(health, 10.0f + i * 42.0f, 620.0f);
        }
        for (int i = 0; i < lostHP; i++) {
            batch.draw(emptyHealth, 10.0f + hp * 42.0f + i * 42.0f, 620.0f);
        }
    }

}