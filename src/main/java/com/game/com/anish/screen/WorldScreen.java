package com.game.com.anish.screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.game.com.anish.creatures.Bomb;
import com.game.com.anish.creatures.Calabash;
import com.game.com.anish.creatures.World;
import com.game.com.anish.creatures.Wall;
import com.game.com.anish.creatures.Floor;
import com.game.com.anish.creatures.Character;
import com.game.com.anish.creatures.File;
import com.game.com.anish.creatures.Monster;
import com.game.asciiPanel.AsciiPanel;

import com.game.com.anish.maze.MazeGenerator;
import com.game.com.anish.maze.Node;

public class WorldScreen implements Screen {

    int dimension;
    World world;
    Calabash[] player;
    Monster[] monster;
    ArrayList<Bomb> bomb;
    ExecutorService exec;
    static int[][] maze;// 0墙 1地板 2玩家 3炸弹 4爆炸 5加HP 6加炸弹 7加范围
    ArrayList<Node> playerlist;
    Random rand;
    int pnum, mnum, mnum0;
    int start;
    File file;
    int cnt;
    int playerid;

    public WorldScreen() {
        dimension = 30;
        pnum = 0;
        mnum = 9;
        mnum0 = mnum;
        start = 1;
        cnt = 0;
        playerid = 0;
        startgame();
    }

    public void startgame() {
        world = new World();
        for (int i = 0; i < 30; ++i)
            for (int j = 0; j < 32; ++j)
                world.put(new Character(world, ' '), i, j);
        world.put(new Character(world, (char) 16), 10, 13);
        world.put(new Character(world, 'N'), 11, 13);
        world.put(new Character(world, 'E'), 12, 13);
        world.put(new Character(world, 'W'), 13, 13);
        world.put(new Character(world, 'G'), 15, 13);
        world.put(new Character(world, 'A'), 16, 13);
        world.put(new Character(world, 'M'), 17, 13);
        world.put(new Character(world, 'E'), 18, 13);
        world.put(new Character(world, 'C'), 11, 17);
        world.put(new Character(world, 'O'), 12, 17);
        world.put(new Character(world, 'N'), 13, 17);
        world.put(new Character(world, 'T'), 14, 17);
        world.put(new Character(world, 'I'), 15, 17);
        world.put(new Character(world, 'N'), 16, 17);
        world.put(new Character(world, 'U'), 17, 17);
        world.put(new Character(world, 'E'), 18, 17);
    }

    public void newgame() {
        player = new Calabash[pnum];
        monster = new Monster[mnum];
        bomb = new ArrayList<Bomb>();
        file = new File();
        world = new World();
        playerlist = new ArrayList<Node>();
        rand = new Random();
        randomaddplayer();
        init_world();
        exec = Executors.newCachedThreadPool();
        init_creature();
        exec.shutdown();
    }

    public void oldgame() {
        player = new Calabash[pnum];
        monster = new Monster[mnum];
        bomb = new ArrayList<Bomb>();
        file = new File();
        file.decode();
        ArrayList<ArrayList<Integer>> playerinfo = file.getplayer();
        ArrayList<ArrayList<Integer>> monsterinfo = file.getmonster();
        ArrayList<ArrayList<Integer>> bombinfo = file.getbomb();
        getkilled(monsterinfo);
        Monster.setcnt(mnum0, mnum);
        world = new World();
        recover_world(playerinfo);
        exec = Executors.newCachedThreadPool();
        recover_creature(playerinfo, monsterinfo);
        recover_bomb(bombinfo);
        exec.shutdown();
    }

    private void getkilled(ArrayList<ArrayList<Integer>> monsterinfo) {
        for (int i = 0; i < monsterinfo.size(); ++i)
            if (monsterinfo.get(i).get(2) == 0)
                --mnum0;
    }

    private void randomaddplayer() {
        Monster.setcnt(mnum, mnum);
        if (pnum > 0)
            playerlist.add(new Node(4, 4));
        if (pnum > 1)
            playerlist.add(new Node(25, 25));
        if (pnum > 2)
            playerlist.add(new Node(4, 25));
        if (pnum > 3)
            playerlist.add(new Node(25, 4));
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int len = dimension / 3;
        for (int i = 0; i < mnum; ++i) {
            if (i % 8 == 0)
                temp.clear();
            int xrand = rand.nextInt(3), yrand = rand.nextInt(3);
            while (temp.contains(xrand * 3 + yrand) || judge_player(xrand, yrand)) {
                xrand = rand.nextInt(3);
                yrand = rand.nextInt(3);
            }
            temp.add(xrand * 3 + yrand);
            int x = xrand * len + rand.nextInt(len), y = yrand * len + rand.nextInt(len);
            playerlist.add(new Node(x, y));
        }
    }

    private boolean judge_player(int xrand, int yrand) {
        if (pnum == 1)
            return xrand == 0 && yrand == 0;
        if (pnum == 2)
            return (xrand == 0 && yrand == 0) || (xrand == 2 && yrand == 2);
        if (pnum == 3)
            return (xrand == 0 && yrand == 0) || (xrand == 2 && yrand == 2) || (xrand == 0 && yrand == 2);
        if (pnum == 4)
            return (xrand == 0 && yrand == 0) || (xrand == 2 && yrand == 2) || (xrand == 0 && yrand == 2)
                    || (xrand == 2 && yrand == 0);
        return true;
    }

    private void init_world() {
        MazeGenerator mazeGenerator = new MazeGenerator(dimension);
        mazeGenerator.addplayer(playerlist);
        mazeGenerator.generateMaze();
        maze = mazeGenerator.getMaze();
        for (int i = 0; i < player.length; ++i)
            player[i] = new Calabash(world, maze, bomb, i);
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (maze[i][j] == 0) {
                    Wall wall = new Wall(world);
                    world.put(wall, i, j);
                } else {
                    Floor floor = new Floor(world);
                    world.put(floor, i, j);
                }
            }
        }
        printinfo();
    }

    private void recover_world(ArrayList<ArrayList<Integer>> playerinfo) {
        maze = file.getmaze();
        for (int i = 0; i < playerinfo.size(); ++i) {
            player[i] = new Calabash(world, maze, bomb, i, playerinfo.get(i).get(2), playerinfo.get(i).get(3),
                    playerinfo.get(i).get(4), playerinfo.get(i).get(5));
            // System.out.print(playerinfo.get(i).get(2));
            // System.out.print(playerinfo.get(i).get(3));
            // System.out.print(playerinfo.get(i).get(4));
            // System.out.print(playerinfo.get(i).get(5));
        }
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (maze[i][j] == 0) {
                    Wall wall = new Wall(world);
                    world.put(wall, i, j);
                } else {
                    Floor floor = new Floor(world);
                    world.put(floor, i, j);
                }
            }
        }

        printinfo();

    }

    private void printinfo() {
        world.put(new Character(world, 'H'), 0, dimension);
        world.put(new Character(world, 'P'), 1, dimension);
        world.put(new Character(world, ':'), 2, dimension);
        player[0].printhp(3, dimension);
        world.put(new Character(world, '/'), 6, dimension);
        player[0].printmaxhp(7, dimension);
        world.put(new Character(world, ' '), 10, dimension);
        world.put(new Character(world, ' '), 11, dimension);
        world.put(new Character(world, 'B'), 12, dimension);
        world.put(new Character(world, 'O'), 13, dimension);
        world.put(new Character(world, 'M'), 14, dimension);
        world.put(new Character(world, 'B'), 15, dimension);
        world.put(new Character(world, ':'), 16, dimension);
        player[0].printmaxbomb(17, dimension);
        world.put(new Character(world, ' '), 19, dimension);
        world.put(new Character(world, ' '), 20, dimension);
        world.put(new Character(world, 'R'), 21, dimension);
        world.put(new Character(world, 'A'), 22, dimension);
        world.put(new Character(world, 'D'), 23, dimension);
        world.put(new Character(world, 'I'), 24, dimension);
        world.put(new Character(world, 'U'), 25, dimension);
        world.put(new Character(world, 'S'), 26, dimension);
        world.put(new Character(world, ':'), 27, dimension);
        player[0].printr(28, dimension);
        for (int i = 30; i < dimension; ++i)
            world.put(new Character(world, ' '), i, dimension);
        world.put(new Character(world, 'E'), 0, dimension + 1);
        world.put(new Character(world, 'N'), 1, dimension + 1);
        world.put(new Character(world, 'E'), 2, dimension + 1);
        world.put(new Character(world, 'M'), 3, dimension + 1);
        world.put(new Character(world, 'Y'), 4, dimension + 1);
        world.put(new Character(world, ' '), 5, dimension + 1);
        world.put(new Character(world, 'K'), 6, dimension + 1);
        world.put(new Character(world, 'I'), 7, dimension + 1);
        world.put(new Character(world, 'L'), 8, dimension + 1);
        world.put(new Character(world, 'L'), 9, dimension + 1);
        world.put(new Character(world, 'E'), 10, dimension + 1);
        world.put(new Character(world, 'D'), 11, dimension + 1);
        world.put(new Character(world, ':'), 12, dimension + 1);
        world.put(new Character(world, ' '), 13, dimension + 1);
        world.put(new Character(world, (char) (mnum - mnum0 + '0')), 14, dimension + 1);
        world.put(new Character(world, '/'), 15, dimension + 1);
        world.put(new Character(world, (char) (mnum + '0')), 16, dimension + 1);
        world.put(new Character(world, ' '), 17, dimension + 1);
        for (int i = 18; i < dimension; ++i)
            world.put(new Character(world, ' '), i, dimension + 1);
    }

    private void init_creature() {
        for (int i = 0; i < player.length; ++i) {
            exec.execute(player[i]);
            world.put(player[i], playerlist.get(i).x, playerlist.get(i).y);
        }
        for (int i = pnum; i < playerlist.size(); ++i) {
            monster[i - pnum] = new Monster(world, player, maze);
            exec.execute(monster[i - pnum]);
            world.put(monster[i - pnum], playerlist.get(i).x, playerlist.get(i).y);
        }
        file.writelog(maze, player, monster);
    }

    private void recover_creature(ArrayList<ArrayList<Integer>> playerinfo, ArrayList<ArrayList<Integer>> monsterinfo) {
        for (int i = 0; i < player.length; ++i) {
            exec.execute(player[i]);
            world.put(player[i], playerinfo.get(i).get(0), playerinfo.get(i).get(1));
        }
        for (int i = 0; i < monsterinfo.size(); ++i) {
            monster[i] = new Monster(world, player, maze, monsterinfo.get(i).get(2));
            exec.execute(monster[i]);
            world.put(monster[i], monsterinfo.get(i).get(0), monsterinfo.get(i).get(1));
        }
    }

    private void recover_bomb(ArrayList<ArrayList<Integer>> bombinfo) {
        for (int i = 0; i < bombinfo.size(); ++i) {
            Bomb bomb = new Bomb(player[bombinfo.get(i).get(3)], world, bombinfo.get(i).get(2), bombinfo.get(i).get(3),
                    bombinfo.get(i).get(4), bombinfo.get(i).get(5));
            this.bomb.add(bomb);
            world.put(bomb, bombinfo.get(i).get(0), bombinfo.get(i).get(1));
            exec.execute(bomb);
        }
    }

    public static int[][] getmaze() {
        return maze;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    public Screen respondToClient(int playerid, int keycode) {
        System.out.print(playerid);
        System.out.print(' ');
        System.out.println(keycode);
        if (start > 0) {
            switch (keycode) {
                case KeyEvent.VK_UP:
                    world.put(new Character(world, (char) 16), 10, 13);
                    world.put(new Character(world, ' '), 10, 17);
                    start = 1;
                    break;
                case KeyEvent.VK_DOWN:
                    world.put(new Character(world, (char) 16), 10, 17);
                    world.put(new Character(world, ' '), 10, 13);
                    start = 2;
                    break;
                case KeyEvent.VK_SPACE:
                    if (start == 1)
                        newgame();
                    else
                        oldgame();
                    start = 0;
                    break;
            }
            return this;
        }
        if (!Monster.hasmonster())
            return this;
        switch (keycode) {
            case KeyEvent.VK_LEFT:
                player[playerid].move(world, 27);
                break;
            case KeyEvent.VK_RIGHT:
                player[playerid].move(world, 26);
                break;
            case KeyEvent.VK_UP:
                player[playerid].move(world, 24);
                break;
            case KeyEvent.VK_DOWN:
                player[playerid].move(world, 25);
                break;
            case KeyEvent.VK_SPACE:
                player[playerid].setbomb(world);
                break;
        }
        return this;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return this;
    }

    public void writelog() {
        file.writelog(maze, player, monster);
    }

    public World getworld() {
        return world;
    }

    public Calabash[] getplayer() { // for test
        return player;
    }

    public Monster[] getmonster() {
        return monster;
    }

    private void addplayer() {
        ++pnum;
        --mnum;
        --mnum0;
    }

    public int getplayerid() {
        int res = -1;
        if (start != 0) {
            res = playerid;
            ++playerid;
            addplayer();
        }
        return res;
    }
}
