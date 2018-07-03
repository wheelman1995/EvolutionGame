package com.evolution.game.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.evolution.game.GameScreen;

public abstract class Cell extends GamePoint {
    float angle;
    float acceleration;
    Vector2 tmp;
    float agility;
    boolean accelerated;
    transient Timer.Task task;
    int accelTime;
    float tmpAccel;
    float tmpAgility;

    public void restoreTimer() {
        if(accelerated) {
            task = new Timer.Task() {
                @Override
                public void run() {
                    accelTime--;
                    if(accelTime == 0) {
                        acceleration = tmpAccel;
                        agility = tmpAgility;
                        accelerated = false;
                        accelTime = 16;
                    }
                }
            };
            new Timer().scheduleTask(task, 0, 1, accelTime);
        }
    }

    public Cell(float x, float y, float acceleration) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.acceleration = acceleration;
        this.scale = 1.0f;
        agility = 180.0f;
        accelTime = 16;
        tmpAccel = acceleration;
        tmpAgility = agility;
    }

    public abstract void consumed();

    public void eatConsumable(Consumable.Type type) {
        switch (type) {
            case FOOD:
                grow();
                break;
            case BAD_FOOD:
                decrease();
                break;
            case SPEEDUP:
                speedup();
                break;
        }
    }

    public void grow() {
        scale += 0.05f;
        if (scale > 5.0f) {
            scale = 5.0f;
        }
    }

    public void decrease() {
        scale -= 0.1f;
        if (scale < 0.2f) {
            scale = 0.2f;
        }
    }

    void speedup() {
        if(!accelerated) {
            final float tmpAccel = acceleration;
            final float tmpAgility = agility;
            acceleration *= 1.5f;
            agility *= 2.0f;
            accelerated = true;
            task = new Timer.Task() {
                @Override
                public void run() {
                    accelTime--;
                    if(accelTime == 0) {
                        acceleration = tmpAccel;
                        agility = tmpAgility;
                        accelerated = false;
                        accelTime = 16;
                    }
                }
            };
            new Timer().scheduleTask(task, 0, 1, accelTime);
        } else {
            if(task != null) {
                task.cancel();
                accelTime = 16;
                new Timer().scheduleTask(task, 0, 1, accelTime);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, scale, scale, angle);
    }

    public void update(float dt) {
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        velocity.scl(0.98f);
        if(gs.getMap().isPointEmpty(position.x + velocity.x * dt, position.y + velocity.y * dt, 24.0f * scale)) {
            position.mulAdd(velocity, dt);
        } else {
            float velLen = velocity.len() * dt;
            tmp.set(velocity);
            tmp.nor();
            float nx = tmp.x;
            float ny = tmp.y;
            for (int i = 0; i < velLen; i++) {
                tmp.set(position.x + nx, position.y);
                if (gs.getMap().isPointEmpty(tmp.x, tmp.y, 24.0f * scale)) {
                    position.set(tmp);
                }
                tmp.set(position.x, position.y + ny);
                if (gs.getMap().isPointEmpty(tmp.x, tmp.y, 24.0f * scale)) {
                    position.set(tmp);
                }
            }
        }
        Vector2 vBlocked  = gs.getMap().checkBlockedPoint(position.x, position.y, 24.0f * scale);
        while (vBlocked != null){
            position.add(vBlocked);
            vBlocked = gs.getMap().checkBlockedPoint(position.x, position.y, 24.0f * scale);
        }
    }
}
