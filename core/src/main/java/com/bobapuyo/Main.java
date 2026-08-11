package com.bobapuyo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    public OrthographicCamera camera;

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

        camera = new OrthographicCamera();
        viewport = new FitViewport(510, 640, camera);
        viewport.apply();
        camera.position.set(510/2, 640/2, 0);

        setScreen(new GameScreen(manager, batch, shape, camera));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
