package com.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


import javax.swing.JFrame;

import com.game.Client.Client;
import com.game.com.anish.creatures.Bomb;
import com.game.com.anish.creatures.Calabash;
import com.game.com.anish.creatures.Monster;
import com.game.com.anish.screen.WorldScreen;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */

    @Test
    void testwin() {
        assertEquals(1, 1);
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Test
    void testscreen() {
        WorldScreen w = new WorldScreen();
        w.addplayer();
        w.newgame();
        w.oldgame();
    }

    @Test
    void testplayer() {
        WorldScreen w = new WorldScreen();
        w.addplayer();
        w.newgame();
        Calabash player = w.getplayer()[0];
        player.move(w.getworld(), 24);
        player.move(w.getworld(), 25);
        player.move(w.getworld(), 26);
        player.move(w.getworld(), 27);
        player.gethurt();
        assertEquals(true, player.is_alive());
        player.is_dead();
        player.setbomb(w.getworld());
    }

    @Test
    void testmonster() {
        WorldScreen w = new WorldScreen();
        w.addplayer();
        w.newgame();
        Monster monster = w.getmonster()[0];
        monster.attack(w.getplayer()[0]);
        monster.gethurt();
        monster.createitem(0, 0);
        monster.printcnt(0, 0);
    }

    @Test
    void testbomb() {
        WorldScreen w = new WorldScreen();
        w.addplayer();
        w.newgame();
        Bomb bomb = new Bomb(w.getplayer()[0], w.getworld(), 1, 0, 2000, 300);
        assertEquals(0, bomb.getstate());
        assertEquals(0, bomb.getplayernum());
        assertEquals(1, bomb.getradius());
        assertEquals(300, bomb.getmilsec(0));
        bomb.setstate(1);
        assertEquals(0, bomb.getsec(0));
        bomb.setmaze();
    }

    @Test
    void testclient() {
        Main app = new Main();
        Client client = new Client();
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
