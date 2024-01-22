package me.jorlowski.game;

import me.jorlowski.Direction;
import me.jorlowski.HitState;
import me.jorlowski.State;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public abstract class Bot extends Player {

    private short[][] board;
    protected byte[][] finalBoard;
    protected Random random;
    protected Bot() {
        random = new Random(System.currentTimeMillis());
        finalBoard = new byte[10][10];
        board = new short[10][10];
        generateWave();
        placeShips();
    }
    @Override
    public HitState attack(int col, int row) {
        switch (finalBoard[row][col]) {
            case 0 -> {
                return HitState.MISS;
            }
            case 1 -> {
                if (isDestroyedFirst(col, row)) {
                    destroy(col, row);
                    return HitState.DESTROYED;
                } else {
                    finalBoard[row][col] = 2;
                    return HitState.HIT;
                }
            }
        }
        return null;
    }

    private void placeShips() {
        int index = 0;
        int col;
        int row;
        // 0 - horizontal, 1 - vertical
        int orientation;
        boolean possible;
        int[] count = {1, 2, 3, 4};
        while (index < 4) {
            col = random.nextInt(0, 10);
            row = random.nextInt(0, 10);
            orientation = random.nextInt(0, 2);

            possible = ((board[row][col] >> 2*(3-index)+orientation) & 1) != 0;
            if (possible) {
                if (orientation == 0) {
                    for (int i=0; i<4-index; i++){
                        finalBoard[row][col+i] = 1;
                    }
                    updateWater(col, row, 4-index+1, orientation);
                } else {
                    for (int i=0; i<4-index; i++){
                        finalBoard[row+i][col] = 1;
                    }
                    updateWater(col, row, 4-index+1, orientation);
                }
                count[index]--;
                if (count[index] == 0) {
                    index++;
                }
            }
        }
    }

    private void updateWater(int col, int row, int length, int orientation) {
        if (orientation == 1) {
            // Update around
            for (int i=row-1; i<=row+length; i++) {
                for (int j=col-1; j<=col+1; j++) {
                    if (i>=0 && i<=9 && j>=0 && j<=9) {
                        board[i][j] = Short.parseShort("00000000", 2);
                    }
                }
            }
            // Update left
            for (int i=row-1; i<=row+length; i++) {
                if (i>=0 && i<=9 && col-2>=0 && col-2<=9) {
                    board[i][col - 2] &= Short.parseShort("10101011", 2);
                }
            }
            for (int i=row-1; i<=row+length; i++) {
                if (i>=0 && i<=9 && col-3>=0 && col-3<=9) {
                    board[i][col - 3] &= Short.parseShort("10101111", 2);
                }
            }
            for (int i=row-1; i<=row+length; i++) {
                if (i>=0 && i<=9 && col-4>=0 && col-4<=9) {
                    board[i][col - 4] &= Short.parseShort("10111111", 2);
                }
            }
            // Update top
            for (int i=col-1; i<=col+1; i++) {
                if (i>=0 && i<=9 && row-2>=0 && row-2<=9) {
                    board[row - 2][i] &= Short.parseShort("01010111", 2);
                }
            }
            for (int i=col-1; i<=col+1; i++) {
                if (i>=0 && i<=9 && row-3>=0 && row-3<=9) {
                    board[row - 3][i] &= Short.parseShort("01011111", 2);
                }
            }
            for (int i=col-1; i<=col+1; i++) {
                if (i>=0 && i<=9 && row-4>=0 && row-4<=9) {
                    board[row - 4][i] &= Short.parseShort("01111111", 2);
                }
            }
        } else {
            // Update around
            for (int i=row-1; i<=row+1; i++) {
                for (int j=col-1; j<=col+length; j++) {
                    if (i>=0 && i<=9 && j>=0 && j<=9) {
                        board[i][j] = Short.parseShort("00000000", 2);
                    }
                }
            }
            // Update left
            for (int i=row-1; i<=row+1; i++) {
                if (i>=0 && i<=9 && col-2>=0 && col-2<=9) {
                    board[i][col - 2] &= Short.parseShort("10101011", 2);
                }
            }
            for (int i=row-1; i<=row+1; i++) {
                if (i>=0 && i<=9 && col-3>=0 && col-3<=9) {
                    board[i][col - 3] &= Short.parseShort("10101111", 2);
                }
            }
            for (int i=row-1; i<=row+1; i++) {
                if (i>=0 && i<=9 && col-4>=0 && col-4<=9) {
                    board[i][col - 4] &= Short.parseShort("10111111", 2);
                }
            }
            // Update top
            for (int i=col-1; i<=col+length; i++) {
                if (i>=0 && i<=9 && row-2>=0 && row-2<=9) {
                    board[row - 2][i] &= Short.parseShort("01010111", 2);
                }
            }
            for (int i=col-1; i<=col+length; i++) {
                if (i>=0 && i<=9 && row-3>=0 && row-3<=9) {
                    board[row - 3][i] &= Short.parseShort("01011111", 2);
                }
            }
            for (int i=col-1; i<=col+length; i++) {
                if (i>=0 && i<=9 && row-4>=0 && row-4<=9) {
                    board[row - 4][i] &= Short.parseShort("01111111", 2);
                }
            }
        }
    }

    private void generateWave() {
        for(int i=0; i<7; i++) {
            for(int j=0; j<7; j++) {
                board[i][j] = Short.parseShort("11111111",2);
            }
        }
        for(int i=0; i<7; i++) {
            board[7][i] = Short.parseShort("01111111", 2);
        }
        for(int i=0; i<7; i++) {
            board[8][i] = Short.parseShort("01011111", 2);
        }
        for(int i=0; i<7; i++) {
            board[9][i] = Short.parseShort("01010111", 2);
        }

        for(int i=0; i<7; i++) {
            board[i][7] = Short.parseShort("10111111", 2);
        }
        for(int i=0; i<7; i++) {
            board[i][8] = Short.parseShort("10101111", 2);
        }
        for(int i=0; i<7; i++) {
            board[i][9] = Short.parseShort("10101011", 2);
        }
        board[7][7] = Short.parseShort("00111111", 2);
        board[7][8] = Short.parseShort("00101111", 2);
        board[7][9] = Short.parseShort("00101011", 2);

        board[8][7] = Short.parseShort("00011111", 2);
        board[8][8] = Short.parseShort("00001111", 2);
        board[8][9] = Short.parseShort("00001011", 2);

        board[9][7] = Short.parseShort("00010111", 2);
        board[9][8] = Short.parseShort("00000111", 2);
        board[9][9] = Short.parseShort("00000011", 2);
    }

    public byte[][] getFinalBoard() {
        return finalBoard;
    }

    private boolean isDestroyedFirst(int col, int row) {
        if (col < 0 || col > 9 || row < 0 || row > 9) {
            return true;
        }
        if (finalBoard[row][col] == 0) {
            return true;
        }
        return isDestroyed(col-1, row, Direction.LEFT)
                && isDestroyed(col+1, row, Direction.RIGHT)
                && isDestroyed(col, row-1, Direction.TOP)
                && isDestroyed(col, row+1, Direction.BOTTOM);
    }
    private boolean isDestroyed(int col, int row, Direction dir) {
        if (col < 0 || col > 9 || row < 0 || row > 9) {
            return true;
        }
        if (finalBoard[row][col] == 0) {
            return true;
        }
        if (finalBoard[row][col] == 1) {
            return false;
        }
        switch (dir) {
            case LEFT -> {
                return isDestroyed(col-1, row, Direction.LEFT);
            }
            case RIGHT -> {
                return isDestroyed(col+1, row, Direction.RIGHT);
            }
            case TOP -> {
                return isDestroyed(col, row-1, Direction.TOP);
            }
            case BOTTOM -> {
                return isDestroyed(col, row+1, Direction.BOTTOM);
            }
        }
        return false;
    }

    private void destroy(int col, int row) {
        if (col < 0 || col > 9 || row < 0 || row > 9) {
            return;
        }
        if (finalBoard[row][col] == 0 || finalBoard[row][col] == 3) {
            return;
        }
        if (finalBoard[row][col] == 1 || finalBoard[row][col] == 2) {
            finalBoard[row][col] = 3;
            destroy(col-1, row);
            destroy(col+1, row);
            destroy(col, row-1);
            destroy(col, row+1);
        }
    }
}
