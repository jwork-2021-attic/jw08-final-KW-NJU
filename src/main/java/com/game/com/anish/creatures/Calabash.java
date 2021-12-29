package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Calabash extends Creature implements Runnable {

    ExecutorService exec;
    int maxhp;
    int maxbomb;
    int bomb;
    int radius;
    int playernum;
    static int cnt;
    static int death;

    ArrayList<Bomb> bomblist;

    public Calabash(World world, int[][] maze, ArrayList<Bomb> bomblist, int playernum) {
        super(Color.GREEN, (char) (2 + playernum), world);
        hp = 100;
        maxhp = 100;
        this.maze = maze;
        exec = Executors.newCachedThreadPool();
        radius = 1;
        attack = 10;
        setbomb(1, 1);
        this.bomblist = bomblist;
        this.playernum = playernum;
        Calabash.death = -1;
    }

    public Calabash(World world, int[][] maze, ArrayList<Bomb> bomblist, int playernum, int hp, int bomb, int maxbomb,
            int radius) {
        super(Color.GREEN, (char) (2 + playernum), world);
        this.hp = hp;
        maxhp = 100;
        this.maze = maze;
        exec = Executors.newCachedThreadPool();
        this.radius = radius;
        attack = 10;
        setbomb(bomb, maxbomb);
        this.bomblist = bomblist;
        this.playernum = playernum;
    }

    public void run() {
        maze[this.getX()][this.getY()] = 2;
        while (is_alive() && (Calabash.cnt > 1 || Monster.hasmonster())) {
            gethurt();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (!is_alive())
            is_dead();
    }

    public static boolean hasplayer() {
        return Calabash.cnt > 0;
    }

    @Override
    public void move(World world, int pos) {
        if (!is_alive())
            return;
        int x = this.getX();
        int y = this.getY();
        int offsetx = 0, offsety = 0;
        if (pos == 24)
            offsety = -1;
        else if (pos == 25)
            offsety = 1;
        else if (pos == 26)
            offsetx = 1;
        else if (pos == 27)
            offsetx = -1;
        int newx = x + offsetx;
        int newy = y + offsety;
        try {
            world.lock.lock();

            if (newx >= 0 && newx < maze.length && newy >= 0 && newy < maze[x].length && (maze[newx][newy] == 1
                    || maze[newx][newy] == 5 || maze[newx][newy] == 6 || maze[newx][newy] == 7)) {
                this.moveTo(x + offsetx, y + offsety);
                if (maze[x][y] == 3)
                    world.put(new Bomb(this, world, 1, playernum), x, y);
                else {
                    world.put(new Floor(world), x, y);
                    maze[x][y] = 1;
                }
                if (maze[newx][newy] == 5) {
                    hp = Math.min(hp + 10, maxhp);
                    printhp(9, maze.length + playernum + 1);
                } else if (maze[newx][newy] == 6) {
                    bombitem();
                    printmaxbomb(19, maze.length + playernum + 1);
                } else if (maze[newx][newy] == 7) {
                    addradius();
                    printr(26, maze.length + playernum + 1);
                }
                maze[newx][newy] = 2;
            }
        } finally {
            world.lock.unlock();

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
            world.put(this, this.getX(), this.getY());
        }
    }

    @Override
    public void getattack(int num) {
        hp -= num;
        printhp(9, maze.length + playernum + 1);
    }

    public int getmaxhp() {
        return maxhp;
    }

    private void addradius() {
        ++radius;
    }

    public void is_dead() {
        try {
            world.lock.lock();
            hp = 0;
            --Calabash.cnt;
            if (Calabash.cnt == 0)
                Calabash.death = playernum;
            printdead(9, maze.length + playernum + 1);
            world.put(new Floor(world), this.getX(), this.getY());
        } finally {
            world.lock.unlock();
        }
    }

    public void printhp(int x, int y) {
        int temp = Math.max(0, hp);
        String str = toString(temp);
        for (int i = 0; i < str.length(); ++i)
            world.put(new Character(world, str.charAt(i)), i + x, y);
    }

    public void printmaxhp(int x, int y) {
        String str = toString(maxhp);
        for (int i = 0; i < str.length(); ++i)
            world.put(new Character(world, str.charAt(i)), i + x, y);
    }

    private void printdead(int x, int y) {
        world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, '-'), x + 1, y);
        world.put(new Character(world, 'D'), x + 2, y);
        world.put(new Character(world, 'E'), x + 3, y);
        world.put(new Character(world, 'A'), x + 4, y);
        world.put(new Character(world, 'D'), x + 5, y);
        world.put(new Character(world, '-'), x + 6, y);
    }

    public void printbomb(int x, int y) {
        int cnt = getbomb();
        if (cnt > 9)
            world.put(new Character(world, (char) (cnt / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (cnt % 10 + '0')), x + 1, y);
    }

    public void printmaxbomb(int x, int y) {
        int cnt = getmaxbomb();
        if (cnt > 9)
            world.put(new Character(world, (char) (cnt / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (cnt % 10 + '0')), x + 1, y);
    }

    public void printr(int x, int y) {
        if (radius > 9)
            world.put(new Character(world, (char) (radius / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (radius % 10 + '0')), x + 1, y);
    }

    public int getradius() {
        return radius;
    }

    public boolean nobomb() {
        return bomb == 0;
    }

    public int getbomb() {
        return bomb;
    }

    public int getbombing() {
        int cnt = 0;
        for (Bomb b : bomblist)
            if (b.getstate() == 1) {
                ++cnt;
                break;
            }
        return cnt;
    }

    public int getmaxbomb() {
        return maxbomb;
    }

    private void setbomb(int bomb, int maxbomb) {
        this.bomb = bomb;
        this.maxbomb = maxbomb;
    }

    public void bombitem() {
        ++maxbomb;
        ++bomb;
    }

    public void addbomb() {
        ++bomb;
        for (Bomb b : bomblist)
            if (b.getstate() == 2) {
                bomblist.remove(b);
                break;
            }
    }

    public void usebomb() {
        --bomb;
    }

    private String toString(int num) {
        int a = num % 10;
        num /= 10;
        int b = num % 10;
        num /= 10;
        int c = num;
        String str = "";
        if (c == 0 && b == 0) {
            str += "  ";
            str += (char) (a + '0');
        } else if (c == 0) {
            str += ' ';
            str += (char) (b + '0');
            str += (char) (a + '0');
        } else
            str += "100";
        return str;
    }

    public void setbomb(World world) {
        if (nobomb())
            return;
        int x = this.getX();
        int y = this.getY();
        if (maze[x][y] == 3)
            return;
        maze[x][y] = 3;
        Bomb bomb = new Bomb(this, world, radius, playernum);
        bomblist.add(bomb);
        world.put(bomb, x, y);
        exec.execute(bomb);
        usebomb();
    }

    public static void setcnt(int pnum) {
        Calabash.cnt = pnum;
    }

    public static int getplayer() {
        return cnt;
    }

    public static int getdeath() {
        return Calabash.death;
    }
}
