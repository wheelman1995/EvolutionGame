package com.evolution.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Enemy;
import com.evolution.game.units.Hero;

import java.util.List;

public class MiniMap {
    private Vector2 position;
    private Vector2 tmp;
    private GameScreen gs;
    private TextureRegion texBack, texHero, texConsumable, texEnemy;
    float gameScanRadius;
    float mapRadius;

    public MiniMap(GameScreen gs) {
        this.gs = gs;
        this.position = new Vector2(1160, 120);
        this.texBack = Assets.getInstance().getAtlas().findRegion("mapBack");
        this.texHero = Assets.getInstance().getAtlas().findRegion("playerPoint");
        this.texConsumable = Assets.getInstance().getAtlas().findRegion("consumablePoint");
        this.texEnemy = Assets.getInstance().getAtlas().findRegion("enemyPoint");
        this.gameScanRadius = 1000;
        this.mapRadius = 100;
        this.tmp = new Vector2(0, 0);
    }

    public void render(SpriteBatch batch) {
        Hero hero = gs.getHero();
        List<Consumable> consumables = gs.getConsumableEmitter().getActiveList();
        List<Enemy> enemies = gs.getEnemyEmitter().getActiveList();

        batch.draw(texBack, position.x - 100, position.y - 100);
        batch.draw(texHero, position.x - 5, position.y - 5);
        for (int i = 0; i < consumables.size(); i++) {
            Consumable c = consumables.get(i);
            if (hero.getPosition().dst(c.getPosition()) < gameScanRadius) {
                tmp.set(c.getPosition());
                tmp.sub(hero.getPosition());
                tmp.scl(mapRadius / gameScanRadius);
                batch.draw(texConsumable, position.x - 3 + tmp.x, position.y - 3 + tmp.y);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (hero.getPosition().dst(e.getPosition()) < gameScanRadius) {
                tmp.set(e.getPosition());
                tmp.sub(hero.getPosition());
                tmp.scl(mapRadius / gameScanRadius);
                batch.draw(texEnemy, position.x - 3 + tmp.x, position.y - 3 + tmp.y);
            }
        }
    }
}
