package me.jorlowski.game;

import me.jorlowski.HitState;

import java.util.*;

public class BotLevel3 extends Bot {
    private int movesLeft = 100;
    private int huntLvl = 0;
    private int[] moves = new int[100];
    private Vector<Integer> move;
    private int rowOneDir = -1, colOneDir = -1;
    private int rowTwoDir = -1, colTwoDir = -1;
    private int[][] probability = new int[10][10];
    private List<String> shipsLeft = new ArrayList<>();
    private int shipLength;
    private boolean horizontal;
    BotLevel3() {
        super();
        for(int i=0; i<100; i++) {
            moves[i] = i;
        }
        shipsLeft.add("4");
        shipsLeft.add("3");
        shipsLeft.add("3");
        shipsLeft.add("2");
        shipsLeft.add("2");
        shipsLeft.add("2");
        shipsLeft.add("1");
        shipsLeft.add("1");
        shipsLeft.add("1");
        shipsLeft.add("1");
    }

    @Override
    public Vector<Integer> makeMove() {
        if (huntLvl == 0) {
            calculateProbability();
            move = new Vector<>();
            int chosenMove = getMaxProbability();
            int indexMove = 0;
            while (moves[indexMove] != chosenMove) {
                indexMove++;
            }
            int digit = moves[indexMove] % 10;
            move.add(digit);
            move.add((moves[indexMove] - digit) / 10);
            moves[indexMove] = moves[--movesLeft];
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

    private int getMaxProbability() {
        List<Integer> maxProbPos = new ArrayList<>();
        int maxProb = probability[0][0];
        maxProbPos.add(0);
        for(int row=0; row<10; row++) {
            for (int col=0; col<10; col++) {
                if (row==0 && col==0) { continue;}
                if (probability[row][col] > maxProb) {
                    maxProb = probability[row][col];
                    maxProbPos.clear();
                    maxProbPos.add(row*10+col);
                } else if (probability[row][col] == maxProb) {
                    maxProbPos.add(row*10+col);
                }
            }
        }
        return maxProbPos.get(random.nextInt(maxProbPos.size()));
    }

    private void calculateProbability() {
        for(int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                probability[row][col] = 0;
            }
        }
        for (String s : shipsLeft) {
            int len = Integer.parseInt(s);
            // Horizontal
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col + len < 10; col++) {
                    boolean isGood = true;
                    for (int distCol = col; distCol <= col + len; distCol++) {
                        boolean inner = true;
                        for (int i = 0; i < movesLeft; i++) {
                            if (moves[i] == row * 10 + distCol) {
                                inner = false;
                                break;
                            }
                        }
                        if (inner) {
                            isGood = false;
                        }

                    }
                    if (isGood) {
                        for (int distCol = col; distCol <= col + len; distCol++) {
                            probability[row][distCol]++;
                        }
                    }
                }
            }

            // Vertical
            for (int row = 0; row + len < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    boolean isGood = true;
                    for (int distRow = row; distRow <= row + len; distRow++) {
                        boolean inner = true;
                        for (int i = 0; i < movesLeft; i++) {
                            if (moves[i] == distRow * 10 + col) {
                                inner = false;
                                break;
                            }
                        }
                        if (inner) {
                            isGood = false;
                        }
                    }
                    if (isGood) {
                        for (int distRow = row; distRow <= row + len; distRow++) {
                            probability[distRow][col]++;
                        }
                    }
                }
            }

        }
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
                shipLength++;
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
                shipsLeft.removeIf(p -> p.equals(Integer.toString(shipLength + 1)));
                shipLength = 0;
            }
        }
        for (int i=0; i<10; i++) {
            for(int j=0; j<10; j++) {
                System.out.print(probability[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
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
