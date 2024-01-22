package me.jorlowski.controller;

import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.*;
import me.jorlowski.game.ClientConnection;
import me.jorlowski.game.Player;
import me.jorlowski.model.components.Board;
import me.jorlowski.model.screens.G_PlayModel;
import me.jorlowski.view.G_GameRenderer;
import me.jorlowski.view.G_Lanterna_GameRenderer;
import me.jorlowski.view.G_Swing_GameRenderer;

import javax.swing.*;
import java.io.IOException;
import java.util.Vector;

public class BattleshipsMultiGame implements GameController {
    private Player player;
    private SpecificWindow window;
    private G_GameRenderer renderer;
    private G_PlayModel screenModel;
    private int score;
    private int enemyScore;
    private int status = -2;
    private int posCol;
    private int posRow;
    private GameState gameState;
    private ClientConnection connection;

    public BattleshipsMultiGame(SpecificWindow window, Board playersBoard, State levelOfAI) {
        this.window = window;
        try {
            connection = new ClientConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player = Player.getAI(levelOfAI);
        if (window instanceof LanternaWindow) {
            renderer = new G_Lanterna_GameRenderer((Screen) this.window.getSpecificWindow());
        } else if (window instanceof SwingWindow) {
            renderer = new G_Swing_GameRenderer((JFrame) this.window.getSpecificWindow());
        } else {
            System.exit(1);
        }
        screenModel = new G_PlayModel(playersBoard);
        score = 0;
        enemyScore = 0;
    }

    @Override
    public State update(KeyVar key) {
        int newStatus;
        if (connection.isConnected()) {
            newStatus = connection.getTheirMove();
            if (status != newStatus && newStatus != ClientConnection.NOINPUT) {
                status = newStatus;
                System.out.println(status);
            }
        } else {
            System.out.println("Connection lost!");
            return State.MAIN;
        }
        switch (status) {
            case ClientConnection.PLSWAIT -> {
                gameState = GameState.WAITING;
            }
            case ClientConnection.THEIRTURN -> {
                gameState = GameState.THEIR_TURN;
            }
            case ClientConnection.YOURTURN -> {
                gameState = GameState.YOUR_TURN;
            }
            case ClientConnection.THEYWON -> {
                gameState = GameState.THEY_WIN;
            }
            case ClientConnection.THEYQUIT -> {
                gameState = GameState.YOU_WIN;
            }
            case ClientConnection.ERROR -> {

            }
            case ClientConnection.HIT -> {
                screenModel.youHit();
                score++;
            }
            case ClientConnection.MISS -> {
                screenModel.youMiss();
                gameState = GameState.THEIR_TURN;
            }
            case ClientConnection.DESTROYED -> {
                screenModel.youDestroyed();
                score++;
            }
            default -> {
                Vector<Integer> v = new Vector<>();
                v.add(status%10);
                v.add(status/10);
                HitState hitState = screenModel.defend(v);
                switch (hitState) {
                    case HIT -> {
                        connection.send("HIT");
                        enemyScore++;
                    }
                    case MISS -> {
                        connection.send("MISS");
                    }
                    case DESTROYED -> {
                        connection.send("DESTROYED");
                        enemyScore++;
                    }
                }
            }
        }
        if (score >= 20) {
            gameState = GameState.YOU_WIN;
        }
        if (enemyScore >= 20) {
            gameState = GameState.THEY_WIN;
        }
        if (gameState == GameState.YOU_WIN) {
            GameControllerFactory.getBoardsForResult(screenModel);
            return State.WIN;
        }
        if (gameState == GameState.THEY_WIN) {
            GameControllerFactory.getBoardsForResult(screenModel);
            return State.LOSE;
        }
        if (key == null) {
            return State.GAME;
        }
        switch (key) {
            case EXIT -> {
                return State.EXIT;
            }
            case ARROW_LEFT -> {
                if (screenModel.move(Direction.LEFT)) {
                    return State.GAME;
                }
                return State.NO_ACTION;
            }
            case ARROW_RIGHT -> {
                if (screenModel.move(Direction.RIGHT)) {
                    return State.GAME;
                }
                return State.NO_ACTION;
            }
            case ARROW_UP -> {
                if (screenModel.move(Direction.TOP)) {
                    return State.GAME;
                }
                return State.NO_ACTION;
            }
            case ARROW_DOWN -> {
                if (screenModel.move(Direction.BOTTOM)) {
                    return State.GAME;
                }
                return State.NO_ACTION;
            }
            case ENTER -> {
                if (gameState == GameState.YOUR_TURN && screenModel.isValidCheck()) {
                    posCol = screenModel.getCursorCol();
                    posRow = screenModel.getCursorRow();
                    connection.send(Integer.toString(posRow*10+posCol));
                    gameState = GameState.THEIR_TURN;
//                    HitState hitState = player.attack(screenModel.getCursorCol(), screenModel.getCursorRow());
//                    switch (hitState) {
//                        case MISS -> {
//                            screenModel.youMiss();
//                            gameState = GameState.THEIR_TURN;
//                        }
//                        case HIT -> {
//                            screenModel.youHit();
//                            score++;
//                        }
//                        case DESTROYED -> {
//                            screenModel.youDestroyed();
//                            score++;
//                        }
//                    }
                }
                return State.GAME;
            }
            default -> {
                return State.NO_ACTION;
            }
        }
    }

    @Override
    public void initRender() {
        renderer.initRender(screenModel, gameState);
    }

    @Override
    public void render() {
        renderer.render(gameState);
    }
}
