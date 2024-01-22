package me.jorlowski.model.components;

import me.jorlowski.Direction;
import me.jorlowski.game.Ship;

import java.util.Map;

public class Board {
    private byte[][] tab;
    private Map<Integer, Ship> ships;

    public Board() {
        tab = new byte[10][10];
        ships = Map.ofEntries(
                Map.entry(0, new Ship(0, 4)),
                Map.entry(1, new Ship(1, 3)),
                Map.entry(2, new Ship(2, 3)),
                Map.entry(3, new Ship(3, 2)),
                Map.entry(4, new Ship(4, 2)),
                Map.entry(5, new Ship(5, 2)),
                Map.entry(6, new Ship(6, 1)),
                Map.entry(7, new Ship(7, 1)),
                Map.entry(8, new Ship(8, 1)),
                Map.entry(9, new Ship(9, 1))
        );
    }
    public byte[][] getTab() {
        return tab;
    }

    public void toggleShip(int id) {
        Ship s = ships.get(id);
        if (s.exist()) {
            removeShip(s);
            return;
        }
        s.init();
        setCore(s);
    }

    private void removeShip(Ship s) {
        removeCore(s);
        s.clearShip();
    }

    public boolean isValidForGame() {
        int sum = 0;
        // Check for interconnection
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                if (tab[i][j] != 0 && tab[i][j] != 1) {
                    return false;
                }
                sum += tab[i][j];
            }
        }
        // Checksum
        if (sum != 20) {
            return false;
        }
        // Check for touching edges and corners
        for (Ship s : ships.values()) {
            int row = s.getRow();
            int col = s.getCol();
            int length = s.getLength();
            boolean vertical = s.isVertical();
            // Sides
            if (vertical) {
                // Long
                for (int i=row; i<row + length; i++) {
                    if ((col-1 >= 0 && tab[i][col-1] == 1) || (col+1 < 10 && tab[i][col+1] == 1)) {
                        return false;
                    }
                }
                // Short
                if ((row-1 >= 0 && tab[row-1][col] == 1) || (row+length < 10 && tab[row+length][col] == 1)) {
                    return false;
                }
                // Corners
                // Top left
                if ((row-1 >= 0 && col-1 >= 0) && tab[row-1][col-1] == 1) {
                    return false;
                }
                // Top right
                if ((row-1 >= 0 && col+1 < 10) && tab[row-1][col+1] == 1) {
                    return false;
                }
                // Bottom left
                if ((row+length < 10 && col-1 >= 0) && tab[row+length][col-1] == 1) {
                    return false;
                }
                // Bottom right
                if ((row+length < 10 && col+1 < 10) && tab[row+length][col+1] == 1) {
                    return false;
                }
            } else {
                // Long
                for (int i=col; i<col + length; i++) {
                    if ((row-1 >= 0 && tab[row-1][i] == 1) || (row+1 < 10 && tab[row+1][i] == 1)) {
                        return false;
                    }
                }
                // Short
                if ((col-1 >= 0 && tab[row][col-1] == 1) || (col+length < 10 && tab[row][col+length] == 1)) {
                    return false;
                }
                // Corners
                // Top left
                if ((row-1 >= 0 && col-1 >= 0) && tab[row-1][col-1] == 1) {
                    return false;
                }
                // Top right
                if ((row-1 >= 0 && col+length < 10) && tab[row-1][col+length] == 1) {
                    return false;
                }
                // Bottom left
                if ((row+1 < 10 && col-1 >= 0) && tab[row+1][col-1] == 1) {
                    return false;
                }
                // Bottom right
                if ((row+1 < 10 && col+length < 10) && tab[row+1][col+1] == length) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setCore(Ship s) {
        int col = s.getCol();
        int row = s.getRow();
        if (s.isVertical()) {
            for (int i=0; i<s.getLength(); i++) {
                tab[row + i][col]++;
            }
        } else {
            for (int i=0; i<s.getLength(); i++) {
                tab[row][col + i]++;
            }
        }
    }

    private void removeCore(Ship s) {
        int col = s.getCol();
        int row = s.getRow();
        if (s.isVertical()) {
            for (int i=0; i<s.getLength(); i++) {
                tab[row + i][col]--;
            }
        } else {

            for (int i=0; i<s.getLength(); i++) {
                tab[row][col + i]--;
            }
        }
    }

    public void rotateShip(int id) {
        Ship s = ships.get(id);
        if (s.exist() && (s.isVertical() && s.getCol() + s.getLength() -1 < 10 ||
                !s.isVertical() && s.getRow() + s.getLength() -1 < 10)) {
            removeCore(s);
            s.rotate();
            setCore(s);
        }
    }

    public boolean moveShip(int id, Direction direction) {
        Ship s = ships.get(id);
        if (!s.exist()) {
            return false;
        }
        switch (direction) {
            case TOP -> {
                if (s.getRow() > 0) {
                    removeCore(s);
                    s.setRow(s.getRow() - 1);
                    setCore(s);
                }
            }
            case LEFT -> {
                if (s.getCol() > 0) {
                    removeCore(s);
                    s.setCol(s.getCol() - 1);
                    setCore(s);
                }
            }
            case BOTTOM -> {
                if (s.isVertical() && s.getRow() + s.getLength() -1 < 9 ||
                        !s.isVertical() && s.getRow() < 9) {
                    removeCore(s);
                    s.setRow(s.getRow() + 1);
                    setCore(s);
                }
            }
            case RIGHT -> {
                if (!s.isVertical() && s.getCol() + s.getLength() -1 < 9 ||
                        s.isVertical() && s.getCol() < 9) {
                    removeCore(s);
                    s.setCol(s.getCol() + 1);
                    setCore(s);
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                sb.append(tab[i][j]).append(" ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void setCursor(int cursorCol, int cursorRow) {
        tab[cursorRow][cursorCol] += 100;
    }

    public void moveCursor(int lastCursorCol, int lastCursorRow, int cursorCol, int cursorRow) {
        tab[lastCursorRow][lastCursorCol] -= 100;
        tab[cursorRow][cursorCol] += 100;
    }

    public boolean isValidCheck(int cursorCol, int cursorRow) {
        return tab[cursorRow][cursorCol] % 100 == 0;
    }

    public void miss(int cursorCol, int cursorRow) {
        tab[cursorRow][cursorCol]--;
    }

    public void hit(int cursorCol, int cursorRow, int val) {
        tab[cursorRow][cursorCol]+=val;
    }

    public void destroyed(int cursorCol, int cursorRow) {
        if (cursorRow > 9 || cursorRow < 0 || cursorCol > 9 || cursorCol < 0
        || tab[cursorRow][cursorCol]%100 == 0 || tab[cursorRow][cursorCol]%100 == -1
                || tab[cursorRow][cursorCol]%100 == 3) {
            return;
        }
        if (tab[cursorRow][cursorCol]%100 == 1) {
            tab[cursorRow][cursorCol] += 2;
            destroyed(cursorCol+1, cursorRow);
            destroyed(cursorCol-1, cursorRow);
            destroyed(cursorCol, cursorRow+1);
            destroyed(cursorCol, cursorRow-1);
            return;
        }
        if (tab[cursorRow][cursorCol]%100 == 2) {
            tab[cursorRow][cursorCol]++;
            destroyed(cursorCol+1, cursorRow);
            destroyed(cursorCol-1, cursorRow);
            destroyed(cursorCol, cursorRow+1);
            destroyed(cursorCol, cursorRow-1);
        }
    }
}
