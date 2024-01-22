package me.jorlowski.model.screens;

import me.jorlowski.Direction;
import me.jorlowski.GameState;
import me.jorlowski.HitState;
import me.jorlowski.State;
import me.jorlowski.model.components.Board;
import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.components.M_ActionModel;
import me.jorlowski.model.components.M_LabelModel;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class G_PlayModel {

    private final List<M_LabelModel> labels;
    private final Board board1;
    private final Board board2;
    private int cursorCol;
    private int cursorRow;
    private int lastCursorCol;
    private int lastCursorRow;
    public G_PlayModel(Board playersBoard) {
        board1 = new Board();
        board1.setCursor(cursorCol, cursorRow);
        board2 = playersBoard;
        labels = List.of(new M_LabelModel(1, 24, 16, 3, "Your move: ¾¾¾"),
                new M_LabelModel(1, 40, 16, 3, "Version 0.0"));
        cursorCol = 0;
        lastCursorCol = 0;
        cursorRow = 0;
        lastCursorRow = 0;
    }

    public List<M_LabelModel> getLabels() {
        return labels;
    }
    public Board getBoard1() {
        return board1;
    }
    public Board getBoard2() {
        return board2;
    }

    public int getCursorCol() {
        return cursorCol;
    }

    public int getCursorRow() {
        return cursorRow;
    }

    public boolean move(Direction direction) {
        switch (direction) {
            case TOP -> {
                if (cursorRow > 0) {
                    lastCursorRow = cursorRow--;
                    board1.moveCursor(cursorCol, lastCursorRow, cursorCol, cursorRow);
                    return true;
                }
                return false;
            }
            case BOTTOM -> {
                if (cursorRow < 9) {
                    lastCursorRow = cursorRow++;
                    board1.moveCursor(cursorCol, lastCursorRow, cursorCol, cursorRow);
                    return true;
                }
                return false;
            }
            case LEFT -> {
                if (cursorCol > 0) {
                    lastCursorCol = cursorCol--;
                    board1.moveCursor(lastCursorCol, cursorRow, cursorCol, cursorRow);
                    return true;
                }
                return false;
            }
            case RIGHT -> {
                if (cursorCol < 9) {
                    lastCursorCol = cursorCol++;
                    board1.moveCursor(lastCursorCol, cursorRow, cursorCol, cursorRow);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean isValidCheck() {
        return board1.isValidCheck(cursorCol, cursorRow);
    }

    public void youMiss() {
        board1.miss(cursorCol, cursorRow);
    }

    public void youHit() {
        board1.hit(cursorCol, cursorRow, 2);
    }

    public void youDestroyed() {
        board1.hit(cursorCol, cursorRow, 2);
        board1.destroyed(cursorCol, cursorRow);
    }

    public HitState defend(Vector<Integer> move) {
        int col = move.get(0);
        int row = move.get(1);
        int value = board2.getTab()[row][col];
        if (value == 0) {
            board2.miss(col, row);
            return HitState.MISS;
        } else if (value == 1) {
            if (isDestroyedFirst(col, row)) {
                board2.destroyed(col, row);
                return HitState.DESTROYED;
            } else {
                board2.hit(col, row, 1);
                return HitState.HIT;
            }
        }
        return null;
    }

    private boolean isDestroyedFirst(int col, int row) {
        if (col < 0 || col > 9 || row < 0 || row > 9) {
            return true;
        }
        return isDestroyed(col-1, row, Direction.LEFT)
                && isDestroyed(col+1, row, Direction.RIGHT)
                && isDestroyed(col, row-1, Direction.TOP)
                && isDestroyed(col, row+1, Direction.BOTTOM);
    }
    private boolean isDestroyed(int col, int row, Direction dir) {
        byte[][] board = board2.getTab();
        if (col < 0 || col > 9 || row < 0 || row > 9) {
            return true;
        }
        if (board[row][col] == 0 || board[row][col] == -1) {
            return true;
        }
        if (board[row][col] == 1) {
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

}
