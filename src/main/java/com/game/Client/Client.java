package com.game.Client;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.game.asciiPanel.AsciiFont;
import com.game.asciiPanel.AsciiPanel;
import com.game.com.anish.creatures.World;
import com.game.com.anish.screen.PlayerScreen;
import com.game.com.anish.screen.Screen;

public class Client extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;
    private Lock lock;
    SocketChannel client;
    int playerid;

    public class Game implements Runnable {
        public void run() {
            PlayerScreen playerscreen = (PlayerScreen) screen;
            while (true) {
                try {
                    while (true) {
                        lock.lock();
                        try {
                            ByteBuffer writebuffer = ByteBuffer.allocate(4);
                            writebuffer.putInt(1);
                            writebuffer.flip();
                            client.write(writebuffer);
                            ByteBuffer readbuffer = ByteBuffer.allocate(World.WIDTH * World.HEIGHT * 16 + 4);
                            client.read(readbuffer);
                            readbuffer.flip();
                            if (readbuffer.getInt() == 0)
                                continue;
                            for (int i = 0; i < World.WIDTH; ++i) {
                                for (int j = 0; j < World.HEIGHT; ++j) {
                                    playerscreen.setGlyph(i, j, (char) readbuffer.getInt());
                                    playerscreen.setColor(i, j,
                                            new Color(readbuffer.getInt(), readbuffer.getInt(), readbuffer.getInt()));
                                    // System.out.print(i);
                                    // System.out.print(' ');
                                    // System.out.println(j);
                                }
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        lock.unlock();
                        repaint();
                        TimeUnit.MILLISECONDS.sleep(20);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public Client() {// 0添加玩家 1请求界面 2移动
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        addKeyListener(this);
        screen = new PlayerScreen();
        setVisible(true);
        lock = new ReentrantLock();
        repaint();
        playerid = -1;
        ExecutorService exec = Executors.newCachedThreadPool();
        String hostName = "LAPTOP-4TH1DQVM";
        int portNumber = 3000;
        SocketAddress hostAddress = new InetSocketAddress(hostName, portNumber);
        lock.lock();
        try {
            client = SocketChannel.open(hostAddress);
            ByteBuffer writebuffer = ByteBuffer.allocate(4);
            writebuffer.putInt(0);
            writebuffer.flip();
            client.write(writebuffer);
            ByteBuffer readbuffer = ByteBuffer.allocate(4);
            client.read(readbuffer);
            readbuffer.flip();
            playerid = readbuffer.getInt();
            if (playerid == -1)
                System.out.println(111);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        lock.unlock();

        exec.execute(new Game());
    }

    @Override
    public void repaint() {
        // terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (playerid == -1)
            return;
        lock.lock();
        try {
            ByteBuffer writebuffer = ByteBuffer.allocate(12);
            writebuffer.putInt(2);
            writebuffer.putInt(playerid);
            writebuffer.putInt(e.getKeyCode());
            writebuffer.flip();
            client.write(writebuffer);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        lock.unlock();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        Client app = new Client();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}