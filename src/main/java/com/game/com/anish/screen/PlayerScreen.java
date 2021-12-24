package com.game.com.anish.screen;

import java.awt.event.KeyEvent;
import java.awt.Color;

import com.game.com.anish.creatures.World;
import com.game.asciiPanel.AsciiPanel;

public class PlayerScreen implements Screen {

    char[][] worldGlyph;
    Color[][] worldColor;

    public PlayerScreen() {
        worldGlyph = new char[World.WIDTH][World.HEIGHT];
        worldColor = new Color[World.WIDTH][World.HEIGHT];
    }

    public void setGlyph(int x, int y, char c) {
        worldGlyph[x][y] = c;
    }

    public void setColor(int x, int y, Color c) {
        worldColor[x][y] = c;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int i = 0; i < World.WIDTH; ++i) {
            for (int j = 0; j < World.HEIGHT; ++j) {

                terminal.write(worldGlyph[i][j], i, j, worldColor[i][j]);

            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return this;
    }

}
