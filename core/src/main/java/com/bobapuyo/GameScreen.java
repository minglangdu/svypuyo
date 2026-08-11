package com.bobapuyo;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private Board board;

    private final AssetManager manager;
    private final SpriteBatch batch;
    private final ShapeRenderer shape;

    @Override
    public void show() {
        // Prepare your screen here.
        board = new Board(50, 25, manager);
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    private void update() {
        board.update();
    }
    private void draw() {
        ScreenUtils.clear(new Color(245f/255f, 208f/255f, 132f/255f, 1f));
        batch.begin();
        board.draw(batch, shape);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

    public GameScreen(AssetManager manager, SpriteBatch batch, ShapeRenderer shape) {
        this.manager = manager;
        this.batch = batch;
        this.shape = shape;
    }
}
