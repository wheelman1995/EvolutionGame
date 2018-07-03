package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MenuScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font32;
    private BitmapFont font96;
    private Stage stage;
    private Skin skin;
    private int[] highestScores;

    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
        font96 = Assets.getInstance().getAssetManager().get("gomarice96.ttf", BitmapFont.class);
        createGUI();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font96.draw(batch, "Evolution-Game", 0, 710, 1280, 1, false);
        if(highestScores != null) {
            font32.draw(batch, "highest scores", 0.0f, 520.0f, 1280.0f, 1, false);
            for (int i = 0; i < highestScores.length; i++) {
                font32.draw(batch, i + 1 + ". " + highestScores[i], 0.0f, 480.0f - i * 40.0f, 1280.0f, 1, false);
            }
        }
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font32", font32);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = skin.getFont("font32");
        skin.add("simpleButtonSkin", textButtonStyle);

        final Button btnLoadGame = new TextButton("Load game", skin, "simpleButtonSkin");
        final Button btnNewGame = new TextButton("Start New Game", skin, "simpleButtonSkin");
        final Button btnExitGame = new TextButton("Exit Game", skin, "simpleButtonSkin");
        btnLoadGame.setPosition(640 - 160, 190);
        btnNewGame.setPosition(640 - 160, 100);
        btnExitGame.setPosition(640 - 160, 10);
        stage.addActor(btnLoadGame);
        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        if(!Gdx.files.local("save.dat").exists()) {
            stage.getActors().get(0).setVisible(false);
        }

        if(Gdx.files.local("highestScores.dat").exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(Gdx.files.local("highestScores.dat").read());
                highestScores = (int[]) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        btnLoadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().getGameScreen().setLoadGame(true);
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
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
//        skin.dispose();
    }
}
