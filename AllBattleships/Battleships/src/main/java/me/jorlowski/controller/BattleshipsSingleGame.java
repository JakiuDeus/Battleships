package me.jorlowski.controller;

import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.*;
import me.jorlowski.game.Player;
import me.jorlowski.model.components.Board;
import me.jorlowski.model.screens.G_PlayModel;
import me.jorlowski.view.G_GameRenderer;
import me.jorlowski.view.G_Lanterna_GameRenderer;
import me.jorlowski.view.G_Swing_GameRenderer;

import javax.swing.*;

public class BattleshipsSingleGame implements GameController {

    private Player player;
    private SpecificWindow window;
    private G_GameRenderer renderer;
    private G_PlayModel screenModel;
    private int score;
    private int enemyScore;
    private GameState gameState = GameState.YOUR_TURN;
    private static long timer;
    private static boolean timerStarted;


    public BattleshipsSingleGame(SpecificWindow window, Board playersBoard, State levelOfAI) {
        this.window = window;
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
        timerStarted = false;
    }

    @Override
    public State update(KeyVar key) {
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
        if (gameState == GameState.THEIR_TURN) {
            if(timerStarted && System.currentTimeMillis() - timer >= 1000) {
                timerStarted = false;
                switch (screenModel.defend(player.makeMove())) {
                    case MISS -> {
                        player.getConfirmation(HitState.MISS);
                        gameState = GameState.YOUR_TURN;
                        return State.GAME;
                    }
                    case HIT -> {
                        enemyScore++;
                        player.getConfirmation(HitState.HIT);
                        return State.GAME;
                    }
                    case DESTROYED -> {
                        enemyScore++;
                        player.getConfirmation(HitState.DESTROYED);
                        return State.GAME;
                    }
                }
            } else if (!timerStarted){
                timerStarted = true;
                timer = System.currentTimeMillis();
            }
        }
        if (key == null && !timerStarted) {
            return State.GAME;
        }
        if (key != null) {
            switch (key) {
                case EXIT -> {return State.EXIT;}
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
                        HitState hitState = player.attack(screenModel.getCursorCol(), screenModel.getCursorRow());
                        switch (hitState) {
                            case MISS -> {
                                screenModel.youMiss();
                                gameState = GameState.THEIR_TURN;
                            }
                            case HIT -> {
                                screenModel.youHit();
                                score++;
                            }
                            case DESTROYED -> {
                                screenModel.youDestroyed();
                                score++;
                            }
                        }
                    }
                    return State.GAME;
                }
                default -> {
                    return State.NO_ACTION;
                }
            }
        }
        return State.NO_ACTION;
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
