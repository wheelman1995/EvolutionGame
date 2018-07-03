package com.evolution.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.evolution.game.units.Cell;
import com.evolution.game.units.Consumable;
import com.evolution.game.units.Enemy;
import com.evolution.game.units.Hero;

public class EvolutionGame extends Game {
    private SpriteBatch batch;

    // Домашнее задание:
    // 1. Разбор кода
    // 2. Добавить 5 жизней игроку, как только игрока съедают жизнь убавляется
    // 3. Сделать GameOver Screen, на котором отобразить счет и уровень в игре
    // 4. Кнопка на GameOver для возврата в меню
    // 5. * Сделать High Score, с отображением в главном меню

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
