package com.bobapuyo;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    final public static int COLORS = 4;
    final public static String[] PEARLS = new String[] {"black.png", "brown.png", "gold.png", "green.png"};
    final public static int NEXT_SIZE = 2;

    final public static float GRAVITY = 0.04f; // cells per second
    final public static float SOFT_GRAV = 0.15f; // cells per second in softdrop
    final public static float GRAV_ACC = 0.005f; // increase in gravity while in freefall

    final public static int CELL_SIZE = 45;
    final public static int WIDTH = 6;
    final public static int HEIGHT = 12;
}
