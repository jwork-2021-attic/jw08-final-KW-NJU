package com.game.com.anish.creatures;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class World {

    public static final int WIDTH = 30;
    public static final int HEIGHT = 36;
    Lock lock = new ReentrantLock();

    private Tile<Thing>[][] tiles;
    File file;

    public World() {

        if (tiles == null) {
            tiles = new Tile[WIDTH][HEIGHT];
        }

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                tiles[i][j] = new Tile<>(i, j);
                tiles[i][j].setThing(new Floor(this));
            }
        }
    }

    public Thing get(int x, int y) {
        return this.tiles[x][y].getThing();
    }

    public void put(Thing t, int x, int y) {
        this.tiles[x][y].setThing(t);
    }

}
