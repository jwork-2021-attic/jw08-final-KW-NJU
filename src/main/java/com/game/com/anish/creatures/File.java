package com.game.com.anish.creatures;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class File {

    int[][] maze;
    Calabash[] player; // 0 x 1 y 2 hp 3 maxbomb 4 curbomb 5 radius 6 enemy killed
    Monster[] monster;
    ArrayList<Bomb> bomb;
    ArrayList<ArrayList<Integer>> playerinfo;
    ArrayList<ArrayList<Integer>> monsterinfo;
    ArrayList<ArrayList<Integer>> bombinfo;
    String file, log;

    public File() {
        playerinfo = new ArrayList<ArrayList<Integer>>();
        log = new String();
        decode();
    }

    private void getstatus(int[][] maze, Calabash[] player, Monster[] monster) {
        this.maze = maze;
        this.player = player;
        this.monster = monster;
        bomb = new ArrayList<Bomb>();
        for (Calabash p : player)
            bomb.addAll(p.bomblist);
    }

    private void encode() {
        file = new String();
        file += (char) maze.length;
        file += (char) maze[0].length;
        for (int i = 0; i < maze.length; ++i) {
            for (int j = 0; j < maze[i].length; ++j) {
                if (maze[i][j] > 10)
                    file += (char) 1;
                else
                    file += (char) (maze[i][j]);
            }
        }
        file += (char) player.length;
        // file += 'p';
        for (Calabash p : player) {
            file += (char) p.getX();
            file += (char) p.getY();
            file += (char) p.gethp();
            file += (char) (p.getbomb() + p.getbombing());
            file += (char) p.getmaxbomb();
            file += (char) p.getradius();
        }
        file += (char) monster.length;
        // file += 'm';
        for (Monster m : monster) {
            file += (char) m.getX();
            file += (char) m.getY();
            file += (char) m.gethp();
        }
        file += (char) bomb.size();
        long now = System.currentTimeMillis();
        for (Bomb b : bomb) {
            if (b.getstate() != 0)
                break;
            file += (char) b.getX();
            file += (char) b.getY();
            file += (char) b.getradius();
            file += (char) b.getplayernum();
            int sec = (int) b.getsec(now), milsec = (int) b.getmilsec(now);
            // System.out.println(sec);
            // System.out.println(milsec);
            int hsec = sec / 100, hmilsec = milsec / 100;
            sec %= 100;
            milsec %= 100;
            file += (char) hsec;
            file += (char) sec;
            file += (char) hmilsec;
            file += (char) milsec;
        }
    }

    public void writelog(int[][] maze, Calabash[] player, Monster[] monster) {
        getstatus(maze, player, monster);
        encode();
        try {
            FileOutputStream fos = new FileOutputStream("save/log.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(file.getBytes(), 0, file.getBytes().length);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getplayernum() {
        return playerinfo.size();
    }

    public void decode() {
        try {
            FileInputStream fis = new FileInputStream("save/log.txt");
            int a = fis.read(), b = fis.read();
            if (a == -1)
                return;
            maze = new int[a][b];
            for (int i = 0; i < a; ++i)
                for (int j = 0; j < b; ++j)
                    maze[i][j] = fis.read();

            a = fis.read();
            playerinfo = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < a; ++i) {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                for (int j = 0; j < 6; ++j)
                    temp.add(fis.read());
                playerinfo.add(temp);
            }
            // for(int i=0;i < playerinfo.size();++i){
            // for(int j=0;j<playerinfo.get(i).size();++j){
            // System.out.print(playerinfo.get(i).get(j));
            // System.out.print(" ");
            // }
            // System.out.println();
            // }
            a = fis.read();
            monsterinfo = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < a; ++i) {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                for (int j = 0; j < 3; ++j)
                    temp.add(fis.read());
                monsterinfo.add(temp);
            }

            // for (int i = 0; i < monsterinfo.size(); ++i) {
            // for (int j = 0; j < monsterinfo.get(i).size(); ++j) {
            // System.out.print(monsterinfo.get(i).get(j));
            // System.out.print(' ');
            // }
            // System.out.println();
            // }

            a = fis.read();
            bombinfo = new ArrayList<ArrayList<Integer>>();
            for (int i = 0; i < a; ++i) {
                ArrayList<Integer> temp = new ArrayList<Integer>();
                for (int j = 0; j < 4; ++j)
                    temp.add(fis.read());
                if (temp.get(0) == -1)
                    break;
                for (int j = 0; j < 2; ++j)
                    temp.add(fis.read() * 100 + fis.read());
                bombinfo.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[][] getmaze() {
        return maze;
    }

    public ArrayList<ArrayList<Integer>> getplayer() {
        return playerinfo;
    }

    public ArrayList<ArrayList<Integer>> getmonster() {
        return monsterinfo;
    }

    public ArrayList<ArrayList<Integer>> getbomb() {
        return bombinfo;
    }
}
