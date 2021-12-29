package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.game.com.anish.maze.Node;
import com.game.com.anish.screen.WorldScreen;

public class Bomb extends Thing implements Runnable {

    int[][] maze;
    int attack;
    int radius;
    int sec, milsec;
    Calabash player;
    int playernum;
    long time;
    int state; // 0 未爆 1 正爆 2 已爆

    public Bomb(Calabash player, World world, int radius, int playernum) {
        super(Color.YELLOW, (char) 6, world);
        this.player = player;
        this.attack = 25;
        this.radius = radius;
        this.playernum = playernum;
        this.sec = 2000;
        this.milsec = 300;
        this.state = 0;
        this.time = System.currentTimeMillis();
    }

    public Bomb(Calabash player, World world, int radius, int playernum, int sec, int milsec) {
        super(Color.YELLOW, (char) 6, world);
        this.player = player;
        this.attack = 25;
        this.radius = radius;
        this.playernum = playernum;
        this.sec = sec;
        this.milsec = milsec;
        this.state = (sec == 0 ? 1 : 0);
        this.time = System.currentTimeMillis();
    }

    public void run() {
        int x = this.getX(), y = this.getY();
        if (sec > 0) {
            setstate(0);
            try {
                TimeUnit.MILLISECONDS.sleep(sec);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            setmaze();
            setstate(1);
            if (maze[x][y] == 3)
                explode(new Character(world, (char) 7), new Floor(world), attack, 1, x, y);
        }
        setstate(2);
        player.addbomb();
    }

    public void explode(Thing thing1, Thing thing2, int stat1, int stat2, int x, int y) {
        Bfs bfs = new Bfs(maze);
        ArrayList<Node> arr = bfs.getrange(maze, x, y, radius);
        for (int i = 0; i < arr.size(); ++i) {
            int a = arr.get(i).x, b = arr.get(i).y;
            if (maze[a][b] != 2)
                world.put(thing1, a, b);
            maze[a][b] = stat1;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(milsec);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < arr.size(); ++i) {
            int a = arr.get(i).x, b = arr.get(i).y;
            if (maze[a][b] != 2)
                world.put(thing2, a, b);
            maze[a][b] = stat2;
        }
    }

    public int getplayernum() {
        return playernum;
    }

    public int getattack() {
        return attack;
    }

    public int getradius() {
        return radius;
    }

    public long getsec(long time) {
        return state == 0 ? (sec - time + this.time) : 0;
    }

    public long getmilsec(long time) {
        return state == 1 ? (milsec - time + this.time) : milsec;
    }

    public void setmaze() {
        this.maze = WorldScreen.getmaze();
    }

    public void setstate(int state) {
        this.state = state;
        this.time = System.currentTimeMillis();
    }

    public int getstate() {
        return state;
    }
}
