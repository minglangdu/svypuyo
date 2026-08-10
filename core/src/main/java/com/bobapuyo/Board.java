package com.bobapuyo;

import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {
    private ArrayList<Pearl> active; // currently falling or being placed pearls
    private int[][] pearls; // already placed pearls
    int phase;

    private ArrayList<Pearl[]> next;

    Pearl center, side;
    int dir; // direction of side pearl

    public void update() {
        switch (phase) {
            case 0:
                // dropping blocks
                center.setDy(Constants.GRAVITY);
                side.setDy(Constants.GRAVITY);
                // check for hitting bottom
                Pearl lower, upper;
                if (center.getXY()[1] < side.getXY()[1]) {
                    lower = center; upper = side;
                } else {
                    lower = side; upper = center;
                }
                if (lower.update(pearls)) {
                    phase = 2;
                    setPearl(lower);
                }
                if (upper.update(pearls)) {
                    phase = 2;
                    setPearl(upper);
                }
                break;
            case 1:
                // popping groups
                ArrayList<Vector2> pops = getPops();
                if (pops.size() == 0) {
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
                        active.remove(i);
                        i --;
                    }
                }
                break;
        }
    }

    public void draw() {

    }

    private void setPearl(Pearl p) {
        pearls[(int)p.getXY()[1]][(int)p.getXY()[0]] = p.getColor();
    }

    private Pearl[] nextPearl() {
        // first (center) on bottom
        return new Pearl[] {new Pearl(2, 10, (int)Math.floor(Math.random() * 4)),
        new Pearl(2, 11, (int)Math.floor(Math.random() * 4))};
    }
    private void setNext() {
        center = next.get(0)[0]; side = next.get(0)[1];
        dir = 0; // side on top -> 0
        active.add(center); active.add(side);
        next.remove(0);
        next.add(nextPearl());
    }

    private ArrayList<Vector2> getPops() {
        return new ArrayList<Vector2> ();
    }

    public Board() {
        pearls = new int[12][6];
        phase = 0;

        for (int i = 0; i < Constants.NEXT_SIZE; i ++) {
            next.add(nextPearl());
        }
        setNext();
    }
}

class Pearl {
    private int x; private float y;
    private float dy;
    private int color;

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

    public float accelerate(float gravity) {
        dy += gravity;
        return dy;
    }

    public boolean update(int[][] pearls) {
        boolean collision = false;
        float ny = y - dy;
        for (int cy = (int)Math.floor(y); cy > Math.max(ny, 0); cy --) {
            if (pearls[cy][x] != 0) {
                collision = true;
                ny = cy;
            }
        }
        y = ny;
        return collision;
    }

    public Pearl(int x, int y, int c) {
        this.x = x;
        this.y = (float)y;
        this.dy = 0.0f;
        this.color = c;
    }
}
