package com.bobapuyo;

public class Board {
    private Pearl[] active; // currently falling or being placed pearls
    private int[][] pearls; // already placed pearls
    int phase;

    public void update() {
        switch (phase) {
            case 0:
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

    public Board() {
        pearls = new int[12][6];
        phase = 0;
    }
}

class Pearl {
    int x; float y;
    float dy;
    public Pearl(int x, int y) {
        this.x = x;
        this.y = (float)y;
        this.dy = 0.0f;
    }
}
