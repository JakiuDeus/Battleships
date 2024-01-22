package me.jorlowski.game;

import me.jorlowski.HitState;
import me.jorlowski.State;

import java.util.Vector;

public abstract class Player {
    public Player() {
    }

    public static Player getAI(State levelOfAI) {
        switch (levelOfAI) {
            case LEVEL_1 -> {
                return new BotLevel1();
            }
            case LEVEL_2 -> {
                return new BotLevel2();
            }
            case LEVEL_3 -> {
                return new BotLevel3();
            }
            case HUMAN -> {
                return new HumanPlayer();
            }
        }
        return null;
    }

    public abstract HitState attack(int col, int row);

    public abstract Vector<Integer> makeMove();
    public abstract void getConfirmation(HitState hitState);
}
