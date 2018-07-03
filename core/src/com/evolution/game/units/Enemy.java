package com.evolution.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.evolution.game.Assets;
import com.evolution.game.GameScreen;
import com.evolution.game.Poolable;
import com.evolution.game.Rules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Enemy extends Cell {
    private Hero hero;
    private List<Enemy> enemies;
    private  List<Consumable> consumables;

    public Enemy(GameScreen gs) {
        super(0, 0, 100.0f);
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("Enemy");
        this.active = false;
    }

    public void reloadResources(GameScreen gs) {
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("Enemy");
    }

    @Override
    public void consumed() {
        active = false;
    }

    public void init() {
        position.set(MathUtils.random(0, Rules.GLOBAL_WIDTH), MathUtils.random(0, Rules.GLOBAL_HEIGHT));
        scale = 1.0f + MathUtils.random(0.0f, 0.4f);
        active = true;
        hero = gs.getHero();
        enemies = gs.getEnemyEmitter().getActiveList();
        consumables = gs.getConsumableEmitter().getActiveList();
    }

    public void update(float dt) {
        super.update(dt);

        if (scale < 0.2f) {
            active = false;
        }

        GamePoint closestPoint = getClosestPoint();
        tmp.set(closestPoint.position);
        float angleToTarget = tmp.sub(this.position).angle();

        float runAngle = angleToTarget + 180;
        if(runAngle > 360) runAngle -= 360;

        switch (closestPoint.getClass().getSimpleName()) {
            case "Consumable":
                switch (((Consumable) closestPoint).getType()) {
                    case FOOD:
                        move(dt, angleToTarget);
                        break;
                    case BAD_FOOD:
                        move(dt, runAngle);
                        break;
                }
                break;
            case "Hero":
                if(this.scale > closestPoint.getScale()) move(dt, angleToTarget);
                else move(dt, runAngle);
                break;
            case "Enemy":
                if(this.scale > closestPoint.getScale()) move(dt, angleToTarget);
                else move(dt, runAngle);
                break;
        }
    }

    private void move(float dt, float moveAngle){
        if(angle < moveAngle) {
            if(Math.abs(angle - moveAngle) <= 180) {
                angle += 180 * dt;
            } else {
                angle -= 180 * dt;
            }
        }
        if(angle > moveAngle) {
            if(Math.abs(angle - moveAngle) <= 180) {
                angle -= 180 * dt;
            } else {
                angle += 180 * dt;
            }
        }
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
    }

    private GamePoint getClosestPoint(){
        GamePoint closestPoint = hero;
        float closestDistance = this.position.dst(hero.position);
        for (int i = 0; i < enemies.size(); i++) {
            if(enemies.get(i) == this) continue;
            if(this.position.dst(enemies.get(i).position) < closestDistance) {
                closestDistance = this.position.dst(enemies.get(i).position);
                closestPoint = enemies.get(i);
            }
        }
        for (int i = 0; i < consumables.size(); i++) {
            if(this.position.dst(consumables.get(i).position) < closestDistance){
                closestDistance = this.position.dst(consumables.get(i).position);
                closestPoint = consumables.get(i);
            }
        }
        return closestPoint;
    }
}
