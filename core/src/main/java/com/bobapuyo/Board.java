package com.bobapuyo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;
import java.util.*;

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
                    if (!center.moveXY(new float[] {-1, 0}, pearls)) {
                        if (side.moveXY(new float[] {-1, 0}, pearls)) {
                            center.moveXY(new float[] {1, 0}, pearls);
                        }
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    if (!center.moveXY(new float[] {1, 0}, pearls)) {
                        if (side.moveXY(new float[] {1, 0}, pearls)) {
                            center.moveXY(new float[] {-1, 0}, pearls);
                        }
                    }
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
                }
                // dropping blocks
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    center.setDy(Constants.SOFT_GRAV);
                    side.setDy(Constants.SOFT_GRAV);
                } else {
                    center.setDy(Constants.GRAVITY);
                    side.setDy(Constants.GRAVITY);
                }
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
                    if (active.size() == 0) {
                        setNext();
                        phase = 0;
                    } else {
                        phase = 2;
                    }
                } else {
                    TreeMap<Integer, Integer> pcols = new TreeMap<Integer, Integer> ();
                    for (int i = 0; i < pops.size(); i ++) {
                        int px = (int)pops.get(i).x, py = (int)pops.get(i).y;
                        pearls[py][px] = 0;
                        if (!pcols.containsKey(px) || pcols.get(px) > py) {
                            pcols.put(px, py);
                        }
                    }
                    pcols.forEach((px, sy) -> {
                        for (int py = sy + 1; py < Constants.HEIGHT; py ++) {
                            if (pearls[py][px] != 0) {
                                active.add(pearlFactory.spawn(px, py, pearls[py][px]));
                                pearls[py][px] = 0;
                            }
                        }
                    });
                    phase = 2;
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
        // draw sides
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        int height = Constants.CELL_SIZE * Constants.HEIGHT, width = Constants.CELL_SIZE * Constants.WIDTH;
        shape.rect(x - Constants.THICK, y - Constants.THICK, Constants.THICK, height + Constants.THICK);
        shape.rect(x - Constants.THICK, y - Constants.THICK, width + Constants.THICK, Constants.WIDTH);
        shape.rect(x + width, y - Constants.THICK, Constants.THICK, Constants.THICK + height);
        shape.rect(x - Constants.THICK, y + height, Constants.THICK + width, Constants.THICK);
//        shape.rect(x, y, Constants.CELL_SIZE * Constants.WIDTH, Constants.CELL_SIZE * Constants.HEIGHT);
        shape.end();
        batch.begin();
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
        ArrayList<Vector2> ans = new ArrayList<Vector2> ();
        ArrayList<Vector2> cur = new ArrayList<Vector2> ();
        boolean[][] visited = new boolean[Constants.HEIGHT][Constants.WIDTH];
        for (int i = 0; i < Constants.HEIGHT; i ++) {
            for (int j = 0; j < Constants.WIDTH; j ++) {
                if (pearls[i][j] == 0 || visited[i][j]) {
                    continue;
                }
                ArrayDeque<Vector2> q = new ArrayDeque<Vector2> ();
                q.offer(new Vector2(j, i));
                cur.clear();
                while (!q.isEmpty()) {
                    Vector2 front = q.peek(); q.pop();
                    cur.add(front);
                    visited[(int)front.y][(int)front.x] = true;
                    for (int d = 0; d < 4; d ++) {
                        // in case we want puyos to connect diagonally (joke)
                        int nx = (int)Math.round(Math.cos(d * (Math.PI / 2))) + (int)front.x;
                        int ny = (int)Math.round(Math.sin(d * (Math.PI / 2))) + (int)front.y;
                        if (nx < 0 || nx >= Constants.WIDTH || ny < 0 || ny >= Constants.HEIGHT) {
                            continue;
                        }
                        if (!visited[ny][nx] && (pearls[ny][nx] == pearls[(int)front.y][(int)front.x])) {
                            q.offer(new Vector2(nx, ny));
                        }
                    }
                }
                if (cur.size() >= 4) {
                    ans.addAll(cur);
                }
            }
        }
        return ans;
    }

    public Board(int x, int y, AssetManager m) {
        pearlFactory = new PearlFactory(m);
        pearlTextures = new PearlTextures(m);

        pearls = new int[Constants.HEIGHT][Constants.WIDTH];
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
