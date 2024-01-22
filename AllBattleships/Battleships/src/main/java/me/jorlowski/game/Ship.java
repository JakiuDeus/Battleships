package me.jorlowski.game;

public class Ship {
    private boolean exists;
    private int startCol;
    private int startRow;
    private int length;
    private boolean vertical;
    private int id;
    public Ship(int id, int length) {
        startCol = 0;
        startRow = 0;
        this.length = length;
        vertical = true;
        exists = false;
        this.id = id;
    }

    public int getCol() {
        return startCol;
    }

    public int getRow() {
        return startRow;
    }
    public boolean exist() {
        return exists;
    }
    public void init() {
        exists = true;
    }

    public void setCol(int col) {
        this.startCol = col;
    }

    public void setRow(int row) {
        this.startRow = row;
    }

    public int getLength() {
        return length;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void rotate() {
        vertical = !vertical;
    }

    public void clearShip() {
        startCol = 0;
        startRow = 0;
        vertical = true;
        exists = false;
    }

    public int getId() {
        return id;
    }
}
