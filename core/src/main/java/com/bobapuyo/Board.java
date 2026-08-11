package com.bobapuyo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private ArrayList<Pearl> active; // currently falling or being placed pearls
    private int[][] pearls; // already placed pearls
    PearlFactory pearlFactory;
    PearlTextures pearlTextures;

    int phase;

    private ArrayList<Pearl[]> next;

    Pearl center, side;
    int dir; // direction of side pearl

    int x, y; // bottom left coords

    public void update() {
        switch (phase) {
            case 0:
                // move pieces
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    center.moveXY(new float[] {-1, 0}, pearls);
                    side.moveXY(new float[] {-1, 0}, pearls);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    center.moveXY(new float[] {1, 0}, pearls);
                    side.moveXY(new float[] {1, 0}, pearls);
                }
                int newdir = dir;
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    newdir += 3; // clockwise
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                    newdir ++; // counterclockwise
                }
                newdir %= 4;
                float[] offset = {(float)Math.round(Math.cos((Math.PI / 2) * newdir) - Math.cos((Math.PI / 2) * dir)),
                    (float)Math.round(Math.sin((Math.PI / 2) * newdir) - Math.sin((Math.PI / 2) * dir))};
                if (!side.moveXY(offset, pearls)) {
                    dir = newdir;
                } else {
                    System.out.println(offset[0] + ", " + offset[1]);
                }
                // dropping blocks
                center.setDy(Constants.GRAVITY);
                side.setDy(Constants.GRAVITY);
                // check for hitting bottom
                boolean hit = false;
                Pearl lower, upper;
                if (center.getXY()[1] < side.getXY()[1]) {
                    lower = center; upper = side;
                } else {
                    lower = side; upper = center;
                }
                if (lower.update(pearls)) {
                    hit = true;
                    setPearl(lower);
                    active.remove(lower);
                }
                if (upper.update(pearls)) {
                    hit = true;
                    setPearl(upper);
                    active.remove(upper);
                }
                if (hit) {
                    phase = 2;
                }
                break;
            case 1:
                // popping groups
                ArrayList<Vector2> pops = getPops();
                if (pops.size() == 0) {
                    setNext();
                    phase = 0;
                }
                break;
            case 2:
                // freefall
                if (active.size() == 0) {
                    phase = 1;
                }
                for (int i = 0; i < active.size(); i ++) {
                    Pearl cur = active.get(i);
                    cur.setDy(cur.getDy() + Constants.GRAV_ACC);
                    if (cur.update(pearls)) {
                        setPearl(cur);
                        active.remove(cur);
                        i --;
                    }
                }
                break;
        }
    }

    public void draw(SpriteBatch batch, ShapeRenderer shape) {
        // draw active pearls
        for (int i = 0; i < active.size(); i ++) {
            active.get(i).draw(batch, x, y);
        }
        for (int i = 0; i < pearls.length; i ++) {
            for (int j = 0; j < pearls[i].length; j ++) {
                if (pearls[i][j] != 0) {
                    batch.draw(pearlTextures.get(pearls[i][j] - 1), x + Constants.CELL_SIZE * j,
                        y + Constants.CELL_SIZE * i, Constants.CELL_SIZE, Constants.CELL_SIZE);
                }
            }
        }
    }

    private void setPearl(Pearl p) {
        pearls[(int)p.getXY()[1]][(int)p.getXY()[0]] = p.getColor();
    }

    private Pearl[] nextPearl() {
        // first (center) on bottom
        int col1 = (int)Math.floor(Math.random() * Constants.COLORS);
        int col2 = (int)Math.floor(Math.random() * Constants.COLORS);
        return new Pearl[] {pearlFactory.spawn(2, 10, col1 + 1),
        pearlFactory.spawn(2, 11, col2 + 1)};
    }
    private void setNext() {
        center = next.get(0)[0]; side = next.get(0)[1];
        dir = 1; // side on top -> 1
        active.add(center); active.add(side);
        next.remove(0);
        next.add(nextPearl());
    }

    private ArrayList<Vector2> getPops() {
        return new ArrayList<Vector2> ();
    }

    public Board(int x, int y, AssetManager m) {
        pearlFactory = new PearlFactory(m);
        pearlTextures = new PearlTextures(m);

        pearls = new int[12][6];
        phase = 0;

        next = new ArrayList<Pearl[]> ();
        active = new ArrayList<Pearl> ();
        for (int i = 0; i < Constants.NEXT_SIZE; i ++) {
            next.add(nextPearl());
        }
        setNext();

        this.x = x; this.y = y;
    }
}
