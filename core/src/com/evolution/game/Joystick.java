package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.units.Hero;

/**
 * Created by FlameXander on 30.09.2017.
 */

public class Joystick extends InputAdapter {
    private TextureRegion back;
    private TextureRegion stick;
    private Rectangle rectangle;
    private float joyCenterX, joyCenterY;
    private int lastId;
    private Vector2 vs;
    private Vector2 tmp;

    public boolean isActive() {
        return lastId > -1;
    }

    public float getPower() {
        return vs.len() / 75.0f;
    }

    public Joystick() {
        TextureRegion texture = Assets.getInstance().getAtlas().findRegion("joystick");
        this.back = new TextureRegion(texture, 0, 0, 200, 200);
        this.stick = new TextureRegion(texture, 0, 200, 50, 50);
        this.rectangle = new Rectangle(50, 50, 200, 200);
        this.joyCenterX = rectangle.x + rectangle.width / 2;
        this.joyCenterY = rectangle.y + rectangle.height / 2;
        this.vs = new Vector2(0, 0);
        this.lastId = -1;
        this.tmp = new Vector2(0, 0);
    }

    public void render(SpriteBatch batch) {
        if (lastId != -1) {
            batch.setColor(1, 1, 1, 0.5f);
            batch.draw(back, rectangle.x, rectangle.y);
            batch.setColor(1, 1, 1, 0.7f);
            batch.draw(stick, joyCenterX + vs.x - 25, joyCenterY + vs.y - 25);
            batch.setColor(1, 1, 1, 1f);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tmp.set(screenX, screenY);
        ScreenManager.getInstance().getViewport().unproject(tmp);
        if (lastId == -1) {
            lastId = pointer;
            rectangle.x = tmp.x - rectangle.width / 2;
            rectangle.y = tmp.y - rectangle.height / 2;
            joyCenterX = rectangle.x + rectangle.width / 2;
            joyCenterY = rectangle.y + rectangle.height / 2;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (lastId == pointer) {
            lastId = -1;
            vs.x = 0;
            vs.y = 0;
        }
        return true;
    }

    public float getAngle() {
        return vs.angle();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        tmp.set(screenX, screenY);
        ScreenManager.getInstance().getViewport().unproject(tmp);
        if (lastId > -1) {
            vs.x = tmp.x - joyCenterX;
            vs.y = tmp.y - joyCenterY;
            if (vs.len() > 75) {
                vs.nor().scl(75);
            }
        }
        return true;
    }
}
