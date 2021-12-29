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
    int lasttime;

    public WorldScreen() {
        dimension = 30;
        pnum = 0;
        mnum = 9;
        mnum0 = mnum;
        start = 1;
        file = new File();
        cnt = 0;
        playerid = 0;
        lasttime = file.getplayernum();
        startgame();
    }

    public void startgame() {
        world = new World();
        for (int i = 0; i < World.WIDTH; ++i)
            for (int j = 0; j < World.HEIGHT; ++j)
                world.put(new Character(world, ' '), i, j);
        world.put(new Character(world, 'C'), 11, 6);
        world.put(new Character(world, 'A'), 12, 6);
        world.put(new Character(world, 'L'), 13, 6);
        world.put(new Character(world, 'A'), 14, 6);
        world.put(new Character(world, 'B'), 15, 6);
        world.put(new Character(world, 'A'), 16, 6);
        world.put(new Character(world, 'S'), 17, 6);
        world.put(new Character(world, 'H'), 18, 6);
        for (int i = 11; i < 19; ++i)
            world.put(new Character(world, '-'), i, 7);
        world.put(new Character(world, 'B'), 11, 8);
        world.put(new Character(world, 'O'), 12, 8);
        world.put(new Character(world, 'M'), 13, 8);
        world.put(new Character(world, 'B'), 14, 8);
        world.put(new Character(world, 'M'), 16, 8);
        world.put(new Character(world, 'A'), 17, 8);
        world.put(new Character(world, 'N'), 18, 8);

        world.put(new Character(world, (char) 102), 11, 10);
        world.put(new Character(world, (char) 103), 12, 10);
        world.put(new Character(world, (char) 101), 13, 10);
        world.put(new Character(world, (char) 106), 14, 10);
        world.put(new Character(world, (char) 107), 15, 10);
        world.put(new Character(world, (char) 108), 16, 10);
        world.put(new Character(world, (char) 104), 17, 10);
        world.put(new Character(world, (char) 105), 18, 10);

        world.put(new Character(world, (char) 16), 10, 14);
        world.put(new Character(world, (char) 17), 19, 14);
        world.put(new Character(world, 'N'), 11, 14);
        world.put(new Character(world, 'E'), 12, 14);
        world.put(new Character(world, 'W'), 13, 14);
        world.put(new Character(world, 'G'), 15, 14);
        world.put(new Character(world, 'A'), 16, 14);
        world.put(new Character(world, 'M'), 17, 14);
        world.put(new Character(world, 'E'), 18, 14);
        world.put(new Character(world, 'C'), 11, 17);
        world.put(new Character(world, 'O'), 12, 17);
        world.put(new Character(world, 'N'), 13, 17);
        world.put(new Character(world, 'T'), 14, 17);
        world.put(new Character(world, 'I'), 15, 17);
        world.put(new Character(world, 'N'), 16, 17);
        world.put(new Character(world, 'U'), 17, 17);
        world.put(new Character(world, 'E'), 18, 17);

        world.put(new Character(world, '('), 5, 18);
        world.put(new Character(world, 'O'), 6, 18);
        world.put(new Character(world, 'N'), 7, 18);
        world.put(new Character(world, 'L'), 8, 18);
        world.put(new Character(world, 'Y'), 9, 18);
        world.put(new Character(world, 'A'), 11, 18);
        world.put(new Character(world, 'V'), 12, 18);
        world.put(new Character(world, 'A'), 13, 18);
        world.put(new Character(world, 'I'), 14, 18);
        world.put(new Character(world, 'L'), 15, 18);
        world.put(new Character(world, 'A'), 16, 18);
        world.put(new Character(world, 'B'), 17, 18);
        world.put(new Character(world, 'L'), 18, 18);
        world.put(new Character(world, 'E'), 19, 18);
        world.put(new Character(world, 'W'), 21, 18);
        world.put(new Character(world, 'H'), 22, 18);
        world.put(new Character(world, 'E'), 23, 18);
        world.put(new Character(world, 'N'), 24, 18);
        world.put(new Character(world, 'C'), 9, 19);
        world.put(new Character(world, 'U'), 10, 19);
        world.put(new Character(world, 'R'), 11, 19);
        world.put(new Character(world, 'R'), 12, 19);
        world.put(new Character(world, 'E'), 13, 19);
        world.put(new Character(world, 'N'), 14, 19);
        world.put(new Character(world, 'T'), 15, 19);
        world.put(new Character(world, '='), 16, 19);
        world.put(new Character(world, 'L'), 17, 19);
        world.put(new Character(world, 'A'), 18, 19);
        world.put(new Character(world, 'S'), 19, 19);
        world.put(new Character(world, 'T'), 20, 19);
        world.put(new Character(world, ')'), 21, 19);
        for (int i = 9; i < 21; ++i)
            world.put(new Character(world, '-'), i, 22);
        world.put(new Character(world, 'C'), 7, 25);
        world.put(new Character(world, 'U'), 8, 25);
        world.put(new Character(world, 'R'), 9, 25);
        world.put(new Character(world, 'R'), 10, 25);
        world.put(new Character(world, 'E'), 11, 25);
        world.put(new Character(world, 'N'), 12, 25);
        world.put(new Character(world, 'T'), 13, 25);
        world.put(new Character(world, 'P'), 15, 25);
        world.put(new Character(world, 'L'), 16, 25);
        world.put(new Character(world, 'A'), 17, 25);
        world.put(new Character(world, 'Y'), 18, 25);
        world.put(new Character(world, 'E'), 19, 25);
        world.put(new Character(world, 'R'), 20, 25);
        world.put(new Character(world, ':'), 21, 25);
        world.put(new Character(world, (char) (pnum + '0')), 22, 25);
        world.put(new Character(world, 'L'), 10, 26);
        world.put(new Character(world, 'A'), 11, 26);
        world.put(new Character(world, 'S'), 12, 26);
        world.put(new Character(world, 'T'), 13, 26);
        world.put(new Character(world, 'T'), 15, 26);
        world.put(new Character(world, 'I'), 16, 26);
        world.put(new Character(world, 'M'), 17, 26);
        world.put(new Character(world, 'E'), 18, 26);
        world.put(new Character(world, ':'), 19, 26);
        world.put(new Character(world, (char) (lasttime + '0')), 20, 26);
        world.put(new Character(world, 'B'), 8, 30);
        world.put(new Character(world, 'Y'), 9, 30);
        world.put(new Character(world, '1'), 11, 30);
        world.put(new Character(world, '9'), 12, 30);
        world.put(new Character(world, '1'), 13, 30);
        world.put(new Character(world, '2'), 14, 30);
        world.put(new Character(world, '2'), 15, 30);
        world.put(new Character(world, '0'), 16, 30);
        world.put(new Character(world, '1'), 17, 30);
        world.put(new Character(world, '0'), 18, 30);
        world.put(new Character(world, '9'), 19, 30);
        world.put(new Character(world, 'K'), 21, 30);
        world.put(new Character(world, 'W'), 22, 30);
        world.put(new Character(world, 'F'), 10, 31);
        world.put(new Character(world, 'R'), 11, 31);
        world.put(new Character(world, 'O'), 12, 31);
        world.put(new Character(world, 'M'), 13, 31);
        world.put(new Character(world, 'N'), 16, 31);
        world.put(new Character(world, 'J'), 17, 31);
        world.put(new Character(world, 'U'), 18, 31);
        world.put(new Character(world, 'C'), 19, 31);
        world.put(new Character(world, 'S'), 20, 31);

    }

    public void newgame() {
        player = new Calabash[pnum];
        monster = new Monster[mnum];
        bomb = new ArrayList<Bomb>();
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
        file.decode();
        ArrayList<ArrayList<Integer>> playerinfo = file.getplayer();
        ArrayList<ArrayList<Integer>> monsterinfo = file.getmonster();
        ArrayList<ArrayList<Integer>> bombinfo = file.getbomb();
        getkilled(monsterinfo);
        Calabash.setcnt(pnum);
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
        Calabash.setcnt(pnum);
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
        for (int i = 0; i < playerinfo.size(); ++i)
            player[i] = new Calabash(world, maze, bomb, i, playerinfo.get(i).get(2), playerinfo.get(i).get(3),
                    playerinfo.get(i).get(4), playerinfo.get(i).get(5));
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
        for (int i = 0; i < World.WIDTH; ++i)
            for (int j = 30; j < World.HEIGHT; ++j)
                world.put(new Character(world, ' '), i, j);
        world.put(new Character(world, 'S'), 0, dimension);
        world.put(new Character(world, 'T'), 1, dimension);
        world.put(new Character(world, 'A'), 2, dimension);
        world.put(new Character(world, 'T'), 3, dimension);
        world.put(new Character(world, 'U'), 4, dimension);
        world.put(new Character(world, 'S'), 5, dimension);
        world.put(new Character(world, 'H'), 10, dimension);
        world.put(new Character(world, 'P'), 11, dimension);
        world.put(new Character(world, '/'), 12, dimension);
        world.put(new Character(world, 'M'), 13, dimension);
        world.put(new Character(world, 'A'), 14, dimension);
        world.put(new Character(world, 'X'), 15, dimension);
        world.put(new Character(world, 'B'), 18, dimension);
        world.put(new Character(world, 'O'), 19, dimension);
        world.put(new Character(world, 'M'), 20, dimension);
        world.put(new Character(world, 'B'), 21, dimension);
        world.put(new Character(world, 'R'), 24, dimension);
        world.put(new Character(world, 'A'), 25, dimension);
        world.put(new Character(world, 'D'), 26, dimension);
        world.put(new Character(world, 'I'), 27, dimension);
        world.put(new Character(world, 'U'), 28, dimension);
        world.put(new Character(world, 'S'), 29, dimension);
        world.put(new Character(world, 'M'), 0, dimension + 5);
        world.put(new Character(world, 'O'), 1, dimension + 5);
        world.put(new Character(world, 'N'), 2, dimension + 5);
        world.put(new Character(world, 'S'), 3, dimension + 5);
        world.put(new Character(world, 'T'), 4, dimension + 5);
        world.put(new Character(world, 'E'), 5, dimension + 5);
        world.put(new Character(world, 'R'), 6, dimension + 5);
        world.put(new Character(world, (char) 108), 7, dimension + 5);
        world.put(new Character(world, ':'), 8, dimension + 5);
        // world.put(new Character(world, (char) (mnum - mnum0 + '0')), 10, dimension +
        // 5);
        world.put(new Character(world, (char) (mnum + '0')), 10, dimension + 5);
        world.put(new Character(world, '/'), 11, dimension + 5);
        world.put(new Character(world, (char) (mnum + '0')), 12, dimension + 5);
        for (int i = 0; i < 4; ++i) {
            world.put(new Character(world, 'P'), 0, dimension + i + 1);
            world.put(new Character(world, 'L'), 1, dimension + i + 1);
            world.put(new Character(world, 'A'), 2, dimension + i + 1);
            world.put(new Character(world, 'Y'), 3, dimension + i + 1);
            world.put(new Character(world, 'E'), 4, dimension + i + 1);
            world.put(new Character(world, 'R'), 5, dimension + i + 1);
            world.put(new Character(world, (char) (i + 1 + '0')), 6, dimension + i + 1);
            world.put(new Character(world, (char) (102 + i)), 7, dimension + i + 1);
            world.put(new Character(world, ':'), 8, dimension + i + 1);
        }
        for (int i = 0; i < player.length; ++i) {
            player[i].printhp(9, dimension + i + 1);
            world.put(new Character(world, '/'), 12, dimension + i + 1);
            player[i].printmaxhp(13, dimension + i + 1);
            player[i].printmaxbomb(19, dimension + i + 1);
            player[i].printr(26, dimension + i + 1);
        }
        for (int i = player.length; i < 4; ++i) {
            for (int j = 9; j < 17; ++j)
                world.put(new Character(world, '-'), j, dimension + i + 1);
            world.put(new Character(world, 'N'), 17, dimension + i + 1);
            world.put(new Character(world, 'O'), 18, dimension + i + 1);
            world.put(new Character(world, 'N'), 19, dimension + i + 1);
            world.put(new Character(world, 'E'), 20, dimension + i + 1);
            for (int j = 21; j < 29; ++j)
                world.put(new Character(world, '-'), j, dimension + i + 1);
        }
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
        // world.put(new Character(world, (char) (Monster.cnt + '0')), 10, dimension +
        // 5);
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
        if (start == -1)
            return this;
        if (start > 0) {
            switch (keycode) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                    start = 3 - start;
                    break;
                case KeyEvent.VK_SPACE:
                    if (start == 1)
                        newgame();
                    else if (start == 2) {
                        if (pnum == lasttime)
                            oldgame();
                        else
                            break;
                    }
                    start = 0;
                    return this;
            }
            if (start == 1) {
                world.put(new Character(world, (char) 16), 10, 14);
                world.put(new Character(world, (char) 17), 19, 14);
                world.put(new Character(world, ' '), 4, 18);
                world.put(new Character(world, ' '), 25, 18);
            } else if (start == 2) {
                world.put(new Character(world, ' '), 10, 14);
                world.put(new Character(world, ' '), 19, 14);
                world.put(new Character(world, (char) 16), 4, 18);
                world.put(new Character(world, (char) 17), 25, 18);
            }
            return this;
        }
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

    public void check() {
        if (start > 0 || start == -1)
            return;
        if (Calabash.getplayer() == 0 && Monster.hasmonster()) {
            world.put(new Character(world, 'M'), 16, dimension + 5);
            world.put(new Character(world, 'O'), 17, dimension + 5);
            world.put(new Character(world, 'N'), 18, dimension + 5);
            world.put(new Character(world, 'S'), 19, dimension + 5);
            world.put(new Character(world, 'T'), 20, dimension + 5);
            world.put(new Character(world, 'E'), 21, dimension + 5);
            world.put(new Character(world, 'R'), 22, dimension + 5);
            world.put(new Character(world, 'S'), 23, dimension + 5);
            world.put(new Character(world, (char) 108), 24, dimension + 5);
            world.put(new Character(world, 'W'), 26, dimension + 5);
            world.put(new Character(world, 'I'), 27, dimension + 5);
            world.put(new Character(world, 'N'), 28, dimension + 5);
            world.put(new Character(world, '!'), 29, dimension + 5);
            start = -1;
        } else if (Calabash.getplayer() == 1 && !Monster.hasmonster()) {
            int num = -1;
            for (int i = 0; i < player.length; ++i) {
                if (player[i].is_alive()) {
                    num = i;
                    break;
                }
            }
            if (num == -1)
                return;
            world.put(new Character(world, 'P'), 17, dimension + 5);
            world.put(new Character(world, 'L'), 18, dimension + 5);
            world.put(new Character(world, 'A'), 19, dimension + 5);
            world.put(new Character(world, 'Y'), 20, dimension + 5);
            world.put(new Character(world, 'E'), 21, dimension + 5);
            world.put(new Character(world, 'R'), 22, dimension + 5);
            world.put(new Character(world, (char) (num + 1 + '0')), 23, dimension + 5);
            world.put(new Character(world, (char) (102 + num)), 24, dimension + 5);
            world.put(new Character(world, 'W'), 26, dimension + 5);
            world.put(new Character(world, 'I'), 27, dimension + 5);
            world.put(new Character(world, 'N'), 28, dimension + 5);
            world.put(new Character(world, '!'), 29, dimension + 5);
            start = -1;
        } else if (Calabash.getplayer() == 0 && !Monster.hasmonster()) {
            int num = Calabash.getdeath();
            world.put(new Character(world, 'P'), 17, dimension + 5);
            world.put(new Character(world, 'L'), 18, dimension + 5);
            world.put(new Character(world, 'A'), 19, dimension + 5);
            world.put(new Character(world, 'Y'), 20, dimension + 5);
            world.put(new Character(world, 'E'), 21, dimension + 5);
            world.put(new Character(world, 'R'), 22, dimension + 5);
            world.put(new Character(world, (char) (num + 1 + '0')), 23, dimension + 5);
            world.put(new Character(world, (char) (102 + num)), 24, dimension + 5);
            world.put(new Character(world, 'W'), 26, dimension + 5);
            world.put(new Character(world, 'I'), 27, dimension + 5);
            world.put(new Character(world, 'N'), 28, dimension + 5);
            world.put(new Character(world, '!'), 29, dimension + 5);
            start = -1;
        }
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

    public void addplayer() {
        ++pnum;
        --mnum;
        --mnum0;
        world.put(new Character(world, (char) (pnum + '0')), 22, 25);
    }

    public int getplayerid() {
        int res = -1;
        if (start > 0) {
            res = playerid;
            ++playerid;
            addplayer();
        }
        return res;
    }
}
