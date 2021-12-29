package com.game.com.anish.creatures;

import java.util.ArrayList;
import java.util.Random;
import com.game.com.anish.maze.Node;

public class Bfs {
    int[][] maze;
    ArrayList<Integer> plan;
    Random rand = new Random();
    Calabash[] player;
    int target;

    Bfs(int[][] maze) {
        this.player = null;
        this.maze = new int[maze.length][maze[0].length];
        plan = new ArrayList<Integer>();
        target = -1;
    }

    Bfs(Calabash[] player, int[][] maze) {
        this.player = player;
        this.maze = new int[maze.length][maze[0].length];
        plan = new ArrayList<Integer>();
        target = -1;
    }

    private void setmaze(int[][] maze) {
        for (int i = 0; i < maze.length; ++i)
            for (int j = 0; j < maze[0].length; ++j)
                this.maze[i][j] = maze[i][j];
    }

    public void makePlan(int[][] maze, int curx, int cury) {
        clearPlan();
        setmaze(maze);
        ArrayList<Node> cur;
        cur = new ArrayList<Node>();
        cur.add(new Node(curx, cury));
        this.maze[curx][cury] = -1;
        int depth = -2;
        while (!cur.isEmpty()) {
            ArrayList<Node> nxt;
            nxt = new ArrayList<Node>();
            boolean flag = false;
            for (int i = 0; i < cur.size(); ++i) {
                int x = cur.get(i).x, y = cur.get(i).y;
                int idx = is_close(x, y);
                if (idx != -1) {
                    this.maze[player[idx].getX()][player[idx].getY()] = depth;
                    flag = true;
                    break;
                }
                next(nxt, x, y, depth);
            }
            if (flag)
                break;
            cur.clear();
            for (int i = 0; i < nxt.size(); ++i)
                cur.add(nxt.get(i));
            --depth;
            // for (int i = 0; i < 30; ++i) {
            // for (int j = 0; j < 30; ++j)
            // System.out.print(this.maze[i][j]);
            // System.out.println();
            // }
            // System.out.println(depth);
        }
        if (target == -1 || this.maze[player[target].getX()][player[target].getY()] > 0) {
            plan.add(randomwalk(curx, cury));
            return;
        }
        int x = player[target].getX(), y = player[target].getY();
        while (depth != -1) {
            ++depth;
            switch (findway(depth, x, y)) {
                case "left":
                    --x;
                    break;
                case "right":
                    ++x;
                    break;
                case "up":
                    --y;
                    break;
                case "down":
                    ++y;
                    break;
            }
        }
    }

    public ArrayList<Node> getrange(int[][] maze, int curx, int cury, int radius) {
        setmaze(maze);
        ArrayList<Node> cur;
        cur = new ArrayList<Node>();
        cur.add(new Node(curx, cury));
        this.maze[curx][cury] = -1;
        int depth = -1, i = 0;
        for (; i < cur.size(); ++i) {
            int x = cur.get(i).x, y = cur.get(i).y;
            depth = this.maze[x][y] - 1;
            if (depth + radius > -2)
                explode(cur, x, y, depth);
        }

        return cur;
    }

    private int randomwalk(int x, int y) {
        ArrayList<Integer> able = new ArrayList<Integer>();
        if (x > 0 && maze[x - 1][y] == -2)
            able.add(27);
        if (x < maze.length - 1 && maze[x + 1][y] == -2)
            able.add(26);
        if (y > 0 && maze[x][y - 1] == -2)
            able.add(24);
        if (y < maze[0].length - 1 && maze[x][y + 1] == -2)
            able.add(25);
        if (able.size() > 0)
            return able.get(rand.nextInt(able.size()));
        return 24;
    }

    private void next(ArrayList<Node> nxt, int x, int y, int depth) {
        if (x > 0 && maze[x - 1][y] == 1) {
            maze[x - 1][y] = depth;
            nxt.add(new Node(x - 1, y));
        }
        if (x < maze.length - 1 && maze[x + 1][y] == 1) {
            maze[x + 1][y] = depth;
            nxt.add(new Node(x + 1, y));
        }
        if (y > 0 && maze[x][y - 1] == 1) {
            maze[x][y - 1] = depth;
            nxt.add(new Node(x, y - 1));
        }
        if (y < maze[0].length - 1 && maze[x][y + 1] == 1) {
            maze[x][y + 1] = depth;
            nxt.add(new Node(x, y + 1));
        }
    }

    private void explode(ArrayList<Node> cur, int x, int y, int depth) {
        if (maze[x][y] == 1000)
            return;
        if (x > 0 && ((maze[x - 1][y] >= 0 && maze[x - 1][y] <= 3) || (maze[x - 1][y] >= 5 && maze[x - 1][y] <= 7))) {
            if (maze[x - 1][y] == 0)
                maze[x - 1][y] = 1000;
            else if (maze[x - 1][y] == 3)
                maze[x - 1][y] = -1;
            else
                maze[x - 1][y] = depth;
            cur.add(new Node(x - 1, y));
        }
        if (x < maze.length - 1
                && ((maze[x + 1][y] >= 0 && maze[x + 1][y] <= 3) || (maze[x + 1][y] >= 5 && maze[x + 1][y] <= 7))) {
            if (maze[x + 1][y] == 0)
                maze[x + 1][y] = 1000;
            else if (maze[x + 1][y] == 3)
                maze[x + 1][y] = -1;
            else
                maze[x + 1][y] = depth;
            cur.add(new Node(x + 1, y));
        }
        if (y > 0 && ((maze[x][y - 1] >= 0 && maze[x][y - 1] <= 3) || (maze[x][y - 1] >= 5 && maze[x][y - 1] <= 7))) {
            if (maze[x][y - 1] == 0)
                maze[x][y - 1] = 1000;
            else if (maze[x][y - 1] == 3)
                maze[x][y - 1] = -1;
            else
                maze[x][y - 1] = depth;
            cur.add(new Node(x, y - 1));
        }
        if (y < maze[0].length - 1
                && ((maze[x][y + 1] >= 0 && maze[x][y + 1] <= 3) || (maze[x][y + 1] >= 5 && maze[x][y + 1] <= 7))) {
            if (maze[x][y + 1] == 0)
                maze[x][y + 1] = 1000;
            else if (maze[x][y + 1] == 3)
                maze[x][y + 1] = -1;
            else
                maze[x][y + 1] = depth;
            cur.add(new Node(x, y + 1));
        }
    }

    private String findway(int depth, int x, int y) {
        String pos = "";
        if (x > 0 && maze[x - 1][y] == depth) {
            pos = "left";
            plan.add(0, 26);
        } else if (x < maze.length - 1 && maze[x + 1][y] == depth) {
            pos = "right";
            plan.add(0, 27);
        } else if (y > 0 && maze[x][y - 1] == depth) {
            pos = "up";
            plan.add(0, 25);
        } else if (y < maze[0].length && maze[x][y + 1] == depth) {
            pos = "down";
            plan.add(0, 24);
        }
        // for (int i = 0; i < plan.size(); ++i)
        // System.out.print(plan.get(i) + " ");
        // System.out.println();
        return pos;
    }

    public int is_close(int x, int y) {
        for (int i = 0; i < player.length; ++i)
            if ((Math.abs(player[i].getX() - x) + Math.abs(player[i].getY() - y)) == 1) {
                set_target(i);
                return i;
            }
        return -1;
    }

    public int get_target(int x, int y) {
        if (target == -1)
            return -1;
        if ((Math.abs(player[target].getX() - x) + Math.abs(player[target].getY() - y)) == 1)
            return target;
        return -1;
    }

    public void clearPlan() {
        plan.clear();
    }

    public ArrayList<Integer> getPlan() {
        return plan;
    }

    public void set_target(int target) {
        this.target = target;
    }
}
