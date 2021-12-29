package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Monster extends Creature implements Runnable {

    Random rand;

    Calabash[] player;
    Bfs bfs;
    static int maxcnt;
    static int cnt;

    public Monster(World world, Calabash[] player, int[][] maze) {
        super(Color.RED, (char) 8, world);
        rand = new Random();
        this.maze = maze;
        this.player = player;
        this.hp = 20;
        bfs = new Bfs(player, maze);
        attack = 15;
    }

    public Monster(World world, Calabash[] player, int[][] maze, int hp) {
        super(Color.RED, (char) 8, world);
        rand = new Random();
        this.maze = maze;
        this.player = player;
        this.hp = hp;
        bfs = new Bfs(player, maze);
        attack = 15;
    }

    public void run() {
        if (!is_alive()) {
            int x = this.getX(), y = this.getY();
            if (maze[x][y] == 1)
                world.put(new Floor(world), this.getX(), this.getY());
            else if (maze[x][y] == 2) {
                for (Calabash c : player)
                    world.put(c, this.getX(), this.getY());
            } else if (maze[x][y] == 5)
                world.put(new Thing(Color.ORANGE, (char) 9, world), x, y);
            else if (maze[x][y] == 6)
                world.put(new Thing(Color.ORANGE, (char) 10, world), x, y);
            else if (maze[x][y] == 7)
                world.put(new Thing(Color.ORANGE, (char) 11, world), x, y);
            printcnt(9, maze.length + 5);
            return;
        }
        ArrayList<Integer> plan = new ArrayList<Integer>();
        while (is_alive() && Calabash.hasplayer()) {
            world.lock.lock();
            // idx = bfs.getnear(player, maze, this.getX(), this.getY());
            // int x = player[idx].getX(), y = player[idx].getY();
            // if (bfs.has_changed(x, y) || plan.isEmpty())
            bfs.makePlan(maze, this.getX(), this.getY());
            world.lock.unlock();
            plan = bfs.getPlan();
            if (!plan.isEmpty()) {
                move(world, plan.get(0));
                plan.remove(0);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int idx = bfs.get_target(this.getX(), this.getY());
            if (idx != -1) {
                attack(player[idx]);
            }
            gethurt();
        }
        try {
            world.lock.lock();
            if (!is_alive()) {
                hp = 0;
                --cnt;
                printcnt(9, maze.length + 5);
            }
            if (Calabash.hasplayer())
                createitem(this.getX(), this.getY());
        } finally {
            world.lock.unlock();
        }
    }

    public void attack(Calabash player) {
        player.getattack(attack);
        getattack(player.attack);
        if (is_alive()) {
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void gethurt() {
        int x = this.getX(), y = this.getY();
        if (maze[x][y] >= 10) {
            getattack(maze[x][y]);
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // world.put(this, this.getX(), this.getY());
        }
    }

    public void createitem(int i, int j) {
        int seed = rand.nextInt(10);
        if (cnt == 0 || seed < 3) {
            world.put(new Floor(world), this.getX(), this.getY());
            maze[this.getX()][this.getY()] = 1;
        } else if (seed < 6) {
            world.put(new Thing(Color.ORANGE, (char) 9, world), i, j);
            maze[i][j] = 5;
        } else if (seed < 8) {
            world.put(new Thing(Color.ORANGE, (char) 10, world), i, j);
            maze[i][j] = 6;
        } else if (seed < 10) {
            world.put(new Thing(Color.ORANGE, (char) 11, world), i, j);
            maze[i][j] = 7;
        }
    }

    public void printcnt(int x, int y) {
        // int num = maxcnt - cnt;
        if (cnt > 9)
            world.put(new Character(world, (char) (cnt / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (cnt % 10 + '0')), x + 1, y);
    }

    public static void setcnt(int cnt, int maxcnt) {
        Monster.cnt = cnt;
        Monster.maxcnt = maxcnt;
    }

    public static boolean hasmonster() {
        return Monster.cnt > 0;
    }

    // public boolean player_alive() {
    // for (Calabash c : player) {
    // if (c.is_alive())
    // return true;
    // }
    // return false;
    // }
}