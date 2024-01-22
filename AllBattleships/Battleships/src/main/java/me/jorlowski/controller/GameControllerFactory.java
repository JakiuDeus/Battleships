package me.jorlowski.controller;

import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.SpecificWindow;
import me.jorlowski.State;
import me.jorlowski.model.components.Board;
import me.jorlowski.model.screens.*;

public class GameControllerFactory {
    private static Board mainBoard = null;
    private static State levelOfAI = null;
    private static byte[][] result1 = null;
    private static byte[][] result2 = null;
    public static GameController createInstance(SpecificWindow window, State state) {
        switch (state) {
            case MAIN:
                return new M_MainController(window, new M_MainModel());
            case SINGLE:
                return new M_MainController(window, new M_BotModel());
            case MULTI:
                return new M_MainController(window, new M_MultiModel());
            case OPTIONS:
                //return new M_MainController(screen, new M_SettingsModel());
                return new M_MainController(window, new M_MainModel());
            case CREATE_LOBBY, JOIN_LOBBY, HUMAN:
                return new BattleshipsGameCreator(window, State.HUMAN);
            case LEVEL_1, LEVEL_2, LEVEL_3:
                return new BattleshipsGameCreator(window, state);
            case GAME:
                return getGame(window, mainBoard, levelOfAI);
            case WIN, LOSE:
                return new G_ScoreController(window, state, result1, result2);

            default:
                throw new IllegalArgumentException("Invalid state: " + state);
        }
    }

    public static void setBoard(Board board, State levelState) {
        mainBoard = board;
        levelOfAI = levelState;
    }
    private static GameController getGame(SpecificWindow window, Board mainBoard, State levelOfAI) {
        switch (levelOfAI) {
            case HUMAN -> {
                return new BattleshipsMultiGame(window, mainBoard, levelOfAI);
            }
            case LEVEL_1, LEVEL_2, LEVEL_3 -> {
                return new BattleshipsSingleGame(window, mainBoard, levelOfAI);
            }
        }
        return null;
    }
    public static void getBoardsForResult(G_PlayModel screenModel) {
        result1 = screenModel.getBoard1().getTab();
        result2 = screenModel.getBoard2().getTab();
    }
}
