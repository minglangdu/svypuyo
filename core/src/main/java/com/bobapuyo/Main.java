package com.bobapuyo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shape;
    public AssetManager manager;

    public FitViewport viewport;

    public TextureRegion[] colors;  // weird hack, replace

    @Override
    public void create() {
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        manager = new AssetManager();
        manager.load("black.png", Texture.class);
        manager.load("brown.png", Texture.class);
        manager.load("gold.png", Texture.class);
        manager.load("green.png", Texture.class);
        manager.finishLoading();

        viewport = new FitViewport(640, 480);

        setScreen(new GameScreen(manager, batch, shape));
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
