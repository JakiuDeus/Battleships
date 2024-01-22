package me.jorlowski.model.components;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;

public abstract class ComponentModel {
    private int xSize;
    private int ySize;
    private int xPos;
    private int yPos;
    private String name;

    public ComponentModel(int xPos, int yPos, int xSize, int ySize, String name) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.xPos = xPos;
        this.yPos = yPos;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public TerminalPosition getTLPos() {
        return new TerminalPosition(xPos, yPos);
    }
    public TerminalPosition getTRPos() {
        return new TerminalPosition(xPos+xSize-1, yPos);
    }
    public TerminalPosition getBLPos() {
        return new TerminalPosition(xPos, yPos+ySize-1);
    }
    public TerminalPosition getBRPos() {
        return new TerminalPosition(xPos+xSize-1, yPos+ySize-1);
    }

    public int getX() {
        return xPos;
    }
    public int getY() {
        return yPos;
    }
    public int getXSize() {
        return xSize;
    }
    public int getySize() {
        return ySize;
    }

}
