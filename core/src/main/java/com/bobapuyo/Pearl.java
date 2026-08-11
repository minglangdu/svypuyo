package com.bobapuyo;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pearl {
    private int x; private float y;
    private float dy;
    private int color;
    Texture tex;

    public float[] getXY() {
        return new float[] {x, y};
    }
    public int getColor() {
        return color;
    }
    public float getDy() {
        return dy;
    }

    public void setDy(float n) {
        dy = n;
    }
    public boolean moveXY(float[] c, int[][] pearls) {
        x += (int)c[0];
        y += c[1];
        int appy = (int)Math.floor(y);
        if (appy < 0 || appy >= Constants.HEIGHT || x < 0 || x >= Constants.WIDTH) {
            x -= (int)c[0];
            y -= c[1];
            return true;
        }
        if (pearls[appy][x] != 0) {
            x -= (int)c[0];
            y -= c[1];
            return true;
        }
        return false;
    }

    public float accelerate(float gravity) {
        dy += gravity;
        return dy;
    }

    public boolean update(int[][] pearls) {
        boolean collision = false;
        float ny = y - dy;
        for (int cy = (int)Math.floor(y); cy > Math.max(ny, 0); cy --) {
            if (pearls[cy - 1][x] != 0) {
                collision = true;
                ny = cy;
                break;
            }
        }
        y = ny;
        if (y <= 0) {
            collision = true;
        }
        return collision;
    }

    public void draw(SpriteBatch batch, int ox, int oy) {
        float bx = ox + Constants.CELL_SIZE * x;
        float by = oy + Constants.CELL_SIZE * y;
        batch.draw(tex, bx, by, Constants.CELL_SIZE, Constants.CELL_SIZE);
    }

    public Pearl(int x, int y, int c, Texture tex) {
        this.x = x;
        this.y = (float)y;
        this.dy = 0.0f;
        this.color = c;
        this.tex = tex;
    }
}
