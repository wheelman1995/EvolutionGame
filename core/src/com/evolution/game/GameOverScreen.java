package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.evolution.game.units.Hero;

public class GameOverScreen implements Screen {
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font64;
    private BitmapFont font32;
    private GameScreen gs;
    private Hero hero;

    public GameOverScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        gs = ScreenManager.getInstance().getGameScreen();
        hero = gs.getHero();
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        font64 = Assets.getInstance().getAssetManager().get("gomarice64.ttf", BitmapFont.class);
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
        createGUI();
        Gdx.input.setInputProcessor(stage);
    }

    private void createGUI() {
        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font32", font32);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = skin.getFont("font32");
        skin.add("simpleButtonSkin", textButtonStyle);

        Button menuBtn = new TextButton("MENU", skin, "simpleButtonSkin");
        Button playAgainBtn = new TextButton("Play Again", skin, "simpleButtonSkin");
        menuBtn.setPosition(640.0f-160.0f, 10.0f);
        playAgainBtn.setPosition(640.0f-160.0f, 100.0f);
        stage.addActor(menuBtn);
        stage.addActor(playAgainBtn);

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
        playAgainBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gs.setPlayAgain(true);
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        batch.begin();
        font64.draw(batch, "Game Over!", 0.0f, 670.0f, 1280.0f, 1, false);
        font32.draw(batch, "your score - " + hero.getScore(), 0.0f, 410.0f, 1280.0f, 1, false);
        font32.draw(batch, "your level - " + gs.getLevel(), 0.0f, 310.0f, 1280.0f, 1, false);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
