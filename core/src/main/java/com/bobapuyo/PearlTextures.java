package com.bobapuyo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class PearlTextures {
    private final ArrayList<Texture> pearlTextures;

    public PearlTextures(AssetManager manager) {
        pearlTextures = new ArrayList<Texture> ();
        for (int i = 0; i < Constants.COLORS; i ++) {
            pearlTextures.add(manager.get(Constants.PEARLS[i], Texture.class));
        }
    }

    public Texture get(int color) {
        return pearlTextures.get(color);
    }
}
