package me.jorlowski.game;

import me.jorlowski.HitState;

import java.util.Vector;

public class BotLevel1 extends Bot {
    private int movesLeft = 100;
    private int[] moves = new int[100];
    BotLevel1() {
        super();
        for(int i=0; i<100; i++) {
            moves[i] = i;
        }
    }

    @Override
    public Vector<Integer> makeMove() {
        Vector<Integer> move = new Vector<>();
        int chosenMove = random.nextInt(0, movesLeft);
        int digit = moves[chosenMove] % 10;
        move.add(digit);
        move.add((moves[chosenMove] - digit) / 10);
        moves[chosenMove] = moves[--movesLeft];
        return move;
    }

    @Override
    public void getConfirmation(HitState hitState) {

    }
}
