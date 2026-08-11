package com.bobapuyo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class PearlFactory {
    // Decouples color logic from Board
    private final PearlTextures pTextures;

    public Pearl spawn(int x, int y, int c) {
        Texture tex = pTextures.get(c - 1);
        return new Pearl(x, y, c, tex);
    }

    public PearlFactory(AssetManager m) {
        pTextures = new PearlTextures(m);
    }
}
