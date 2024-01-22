package me.jorlowski.game;

import me.jorlowski.HitState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class BotLevel2 extends Bot {
    private int movesLeft = 100;
    private int huntLvl = 0;
    private int[] moves = new int[100];
    private Vector<Integer> move;
    private int rowOneDir = -1, colOneDir = -1;
    private int rowTwoDir = -1, colTwoDir = -1;
    private boolean horizontal;
    BotLevel2() {
        super();
        for(int i=0; i<100; i++) {
            moves[i] = i;
        }
    }


    @Override
    public Vector<Integer> makeMove() {
        if (huntLvl == 0) {
            move = new Vector<>();
            int chosenMove = random.nextInt(movesLeft);
            int digit = moves[chosenMove] % 10;
            move.add(digit);
            move.add((moves[chosenMove] - digit) / 10);
            moves[chosenMove] = moves[--movesLeft];
            return move;
        } else if (huntLvl == 1) {
            List<Integer> movesAdjacent = new ArrayList<>();
            List<Integer> movesAvailable = getAvailableMoves();
            movesAvailable.stream()
                    .filter(a -> Arrays.stream(moves).limit(movesLeft).anyMatch(b -> b == a))
                    .forEach(movesAdjacent::add);
            pickMoveAndRemove(movesAdjacent);
            return move;
        } else if (huntLvl == 2) {
            List<Integer> movesAdjacent = new ArrayList<>();
            if (horizontal) {
                Arrays.stream(new int[]{(rowOneDir*10+colOneDir)-1, (rowTwoDir*10+colTwoDir)+1})
                        .filter(a -> Arrays.stream(moves).limit(movesLeft).anyMatch(b -> b == a))
                        .forEach(movesAdjacent::add);
            } else {
                Arrays.stream(new int[]{(rowOneDir*10+colOneDir)-10, (rowTwoDir*10+colTwoDir)+10})
                        .filter(a -> Arrays.stream(moves).limit(movesLeft).anyMatch(b -> b == a))
                        .forEach(movesAdjacent::add);
            }
            pickMoveAndRemove(movesAdjacent);
            return move;
        }
        return null;
    }

    private List<Integer> getAvailableMoves() {
        int shotNumber = rowOneDir*10+colOneDir;
        List<Integer> movesAvailable = new ArrayList<>();
        if (shotNumber%10 < 9) {
            movesAvailable.add(shotNumber + 1);
        }
        if (shotNumber%10 > 0) {
            movesAvailable.add(shotNumber - 1);
        }
        if (shotNumber - 10 >= 0) {
            movesAvailable.add(shotNumber - 10);
        }
        if (shotNumber + 10 <= 99) {
            movesAvailable.add(shotNumber + 10);
        }
        return movesAvailable;
    }

    @Override
    public void getConfirmation(HitState hitState) {
        switch (hitState) {
            case HIT -> {
                switch (huntLvl) {
                    case 0 -> {
                        huntLvl = 1;
                        colOneDir = move.get(0);
                        rowOneDir = move.get(1);
                    }
                    case 1 -> {
                        huntLvl = 2;
                        colTwoDir = move.get(0);
                        rowTwoDir = move.get(1);
                        checkRowColBigger();
                        horizontal = rowOneDir == rowTwoDir;
                    }
                    case 2 -> {
                        if (move.get(0) + 1 == colOneDir || move.get(1) + 1 == rowOneDir) {
                            colOneDir = move.get(0);
                            rowOneDir = move.get(1);
                        } else {
                            colTwoDir = move.get(0);
                            rowTwoDir = move.get(1);
                        }
                    }
                }
            }
            case DESTROYED -> {
                if (huntLvl == 0) {
                    colOneDir = move.get(0);
                    rowOneDir = move.get(1);
                    removeMovesAdjacent(rowOneDir, colOneDir, rowTwoDir, colTwoDir, true);
                    return;
                }
                if (huntLvl == 1) {
                    colTwoDir = move.get(0);
                    rowTwoDir = move.get(1);
                }
                if (move.get(0) + 1 == colOneDir || move.get(1) + 1 == rowOneDir) {
                    colOneDir = move.get(0);
                    rowOneDir = move.get(1);
                } else {
                    colTwoDir = move.get(0);
                    rowTwoDir = move.get(1);
                }
                huntLvl = 0;
                removeMovesAdjacent(rowOneDir, colOneDir, rowTwoDir, colTwoDir, false);
            }
        }
    }
    private void pickMoveAndRemove(List<Integer> movesAdjacent) {
        int chosenMove = movesAdjacent.get(random.nextInt(movesAdjacent.size()));
        int digit = chosenMove % 10;
        move.clear();
        move.add(digit);
        move.add((chosenMove - digit) / 10);
        for(int i=0; i<moves.length; i++) {
            if (moves[i] == chosenMove) {
                moves[i] = moves[--movesLeft];
                break;
            }
        }
    }

    private void checkRowColBigger() {
        if (rowTwoDir < rowOneDir) {
            int temp = rowOneDir;
            rowOneDir = rowTwoDir;
            rowTwoDir = temp;
        }
        if (colTwoDir < colOneDir) {
            int temp = colOneDir;
            colOneDir = colTwoDir;
            colTwoDir = temp;
        }
    }

    private void removeMovesAdjacent(int rowOneDir, int colOneDir, int rowTwoDir, int colTwoDir, boolean single) {
        if (single) {
            for (int row = rowOneDir - 1; row <= rowOneDir + 1; row++) {
                for (int col = colOneDir - 1; col <= colOneDir + 1; col++) {
                    if (row >= 0 && row < 10 && col >= 0 && col < 10) {
                        int number = row*10+col;
                        for (int i=0; i<movesLeft; i++) {
                            if (number == moves[i]) {
                                moves[i] = moves[--movesLeft];
                            }
                        }
                    }
                }
            }
        } else if (horizontal) {
            for (int row = rowOneDir - 1; row <= rowOneDir + 1; row ++) {
                for (int col = colOneDir - 1; col <= colTwoDir + 1; col++) {
                    if (row >= 0 && row < 10 && col >= 0 && col < 10) {
                        int number = row*10+col;
                        for (int i=0; i<movesLeft; i++) {
                            if (number == moves[i]) {
                                moves[i] = moves[--movesLeft];
                            }
                        }
                    }
                }
            }
        } else {
            for (int row = rowOneDir - 1; row <= rowTwoDir + 1; row ++) {
                for (int col = colOneDir - 1; col <= colOneDir + 1; col++) {
                    if (row >= 0 && row < 10 && col >= 0 && col < 10) {
                        int number = row*10+col;
                        for (int i=0; i<movesLeft; i++) {
                            if (number == moves[i]) {
                                moves[i] = moves[--movesLeft];
                            }
                        }
                    }
                }
            }
        }
    }
}
