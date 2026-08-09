package com.bobapuyo;

import java.util.ArrayList;

public class Board {
    private Pearl[] active; // currently falling or being placed pearls
    private int[][] pearls; // already placed pearls
    int phase;

    private ArrayList<Pearl[]> next;

    Pearl center, side;
    int dir; // direction of side pearl

    public void update() {
        switch (phase) {
            case 0:
                center = next.get(0)[0]; side = next.get(0)[1];
                dir = 0; // side on top -> 0
                
                next.add(nextPearl());
                // dropping blocks
                break;
            case 1:
                // popping groups
                break;
            case 2:
                // checking for freefall
                break;
        }
    }

    public void draw() {

    }

    private Pearl[] nextPearl() {
        // first (center) on bottom
        return new Pearl[] {new Pearl(2, 10, (int)Math.floor(Math.random() * 4)),
        new Pearl(2, 11, (int)Math.floor(Math.random() * 4))};
    }

    public Board() {
        pearls = new int[12][6];
        phase = 0;

        for (int i = 0; i < Constants.NEXT_SIZE; i ++) {
            next.add(nextPearl());
        }
    }
}

class Pearl {
    int x; float y;
    float dy;
    int color;

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
