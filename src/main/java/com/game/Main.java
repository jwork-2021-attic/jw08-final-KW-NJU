package com.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import com.game.com.anish.creatures.World;
import com.game.com.anish.screen.Screen;
import com.game.com.anish.screen.WorldScreen;

import com.game.asciiPanel.AsciiFont;
import com.game.asciiPanel.AsciiPanel;

public class Main extends JFrame implements KeyListener {

    private AsciiPanel terminal;
    private Screen screen;

    public class Game implements Runnable {
        public void run() {
            while (true) {
                try {
                    repaint();
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public class Server implements Runnable {
        WorldScreen worldscreen;
        String hostName;
        int portNumber;
        Selector selector;
        ServerSocketChannel serverChannel;

        public Server() {
            worldscreen = (WorldScreen) screen;
            hostName = "LAPTOP-4TH1DQVM";
            portNumber = 3000;
            try {
                selector = Selector.open();
                serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                InetSocketAddress hostAddress = new InetSocketAddress(hostName, portNumber);
                serverChannel.bind(hostAddress);
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (true) {
                    int readyCount = selector.select();
                    if (readyCount == 0) {
                        continue;
                    }
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    java.util.Iterator<SelectionKey> iterator = readyKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, SelectionKey.OP_READ);
                            continue;
                        }
                        if (key.isReadable()) {
                            try {
                                SocketChannel client = (SocketChannel) key.channel();
                                ByteBuffer readbuffer = ByteBuffer.allocate(12);
                                client.read(readbuffer);
                                readbuffer.flip();
                                ByteBuffer writebuffer;
                                switch (readbuffer.getInt()) {
                                    case 0:
                                        writebuffer = ByteBuffer.allocate(8);
                                        writebuffer.putInt(worldscreen.getplayerid());
                                        writebuffer.flip();
                                        client.write(writebuffer);
                                        break;
                                    case 1:
                                        writebuffer = ByteBuffer.allocate(World.WIDTH * World.HEIGHT * 16 + 4);
                                        writebuffer.putInt(1);
                                        for (int i = 0; i < World.WIDTH; ++i) {
                                            for (int j = 0; j < World.HEIGHT; ++j) {
                                                writebuffer.putInt(worldscreen.getworld().get(i, j).getGlyph());
                                                writebuffer
                                                        .putInt(worldscreen.getworld().get(i, j).getColor().getRed());
                                                writebuffer
                                                        .putInt(worldscreen.getworld().get(i, j).getColor().getGreen());
                                                writebuffer
                                                        .putInt(worldscreen.getworld().get(i, j).getColor().getBlue());
                                            }
                                        }
                                        writebuffer.flip();
                                        client.write(writebuffer);
                                        worldscreen.writelog();
                                        break;
                                    case 2:
                                        worldscreen.respondToClient(readbuffer.getInt(), readbuffer.getInt());
                                        break;
                                    default:
                                        assert (false);
                                }
                            } catch (Exception e) {
                                // e.printStackTrace();
                                continue;
                            }
                        }
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Main() {
        super();
        terminal = new AsciiPanel(World.WIDTH, World.HEIGHT, AsciiFont.TALRYTH_15_15);
        add(terminal);
        pack();
        addKeyListener(this);
        screen = new WorldScreen();
        setVisible(true);
        repaint();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Game());
        exec.execute(new Server());
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
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // app.setVisible(true);
    }

}
