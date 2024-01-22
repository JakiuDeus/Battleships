package me.jorlowski.controller;

import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.*;
import me.jorlowski.model.screens.G_BuildModel;
import me.jorlowski.view.*;

import javax.swing.*;

public class BattleshipsGameCreator implements GameController {

    private State levelState;
    private SpecificWindow window;
    private G_GameBuildRenderer gameRenderer;
    private G_BuildModel screenModel;
    private int actionLength;
    private int selectedActionIndex;
    private int lastSelectedActionIndex;
    private boolean hintLabel;
    public BattleshipsGameCreator(SpecificWindow window, State state) {
        levelState = state;
        this.window = window;
        screenModel = new G_BuildModel();
        if (window instanceof LanternaWindow) {
            gameRenderer = new G_Lanterna_GameBuildRenderer((Screen) this.window.getSpecificWindow());
        } else if (window instanceof SwingWindow) {
            gameRenderer = new G_Swing_GameBuildRenderer((JFrame) this.window.getSpecificWindow());
        } else {
            System.exit(1);
        }
        selectedActionIndex = screenModel.getSelectedActionIndex();
        lastSelectedActionIndex = selectedActionIndex;
        actionLength = screenModel.getActions().size()-1;
        hintLabel = false;
    }


    @Override
    public State update(KeyVar key) {
        if (key == null) {
            return State.NO_ACTION;
        }
        switch (key) {
            case EXIT -> {return State.EXIT;}
            case PAGE_UP -> {
                lastSelectedActionIndex = selectedActionIndex;
                selectedActionIndex = selectedActionIndex == 0 ? 0 : selectedActionIndex - 1;
                screenModel.setSelectedAction(selectedActionIndex);
                return levelState;
            }
            case PAGE_DOWN -> {
                lastSelectedActionIndex = selectedActionIndex;
                selectedActionIndex = selectedActionIndex == actionLength ? actionLength : selectedActionIndex + 1;
                screenModel.setSelectedAction(selectedActionIndex);
                return levelState;
            }
            case ARROW_LEFT -> {
                hintLabel = false;
                int action = screenModel.getSelectedAction().onClick();
                if (action < 10) {
                    boolean success = screenModel.getBoard().moveShip(action, Direction.LEFT);
                    if (success) {
                        return levelState;
                    }
                }
                return State.NO_ACTION;
            }
            case ARROW_RIGHT -> {
                hintLabel = false;
                int action = screenModel.getSelectedAction().onClick();
                if (action < 10) {
                    boolean success = screenModel.getBoard().moveShip(action, Direction.RIGHT);
                    if (success) {
                        return levelState;
                    }
                }
                return State.NO_ACTION;
            }
            case ARROW_UP -> {
                hintLabel = false;
                int action = screenModel.getSelectedAction().onClick();
                if (action < 10) {
                    boolean success = screenModel.getBoard().moveShip(action, Direction.TOP);
                    if (success) {
                        return levelState;
                    }
                }
                return State.NO_ACTION;
            }
            case ARROW_DOWN -> {
                hintLabel = false;
                int action = screenModel.getSelectedAction().onClick();
                if (action < 10) {
                    boolean success = screenModel.getBoard().moveShip(action, Direction.BOTTOM);
                    if (success) {
                        return levelState;
                    }
                }
                return State.NO_ACTION;
            }
            case ENTER -> {
                int action = screenModel.getSelectedAction().onClick();
                switch (action) {
                    case 10 -> {
                        if (screenModel.getBoard().isValidForGame()) {
                            GameControllerFactory.setBoard(screenModel.getBoard(), levelState);
                            return State.GAME;
                        } else {
                            hintLabel = true;
                        }

                    }
                    case 11 -> {
                        if (levelState == State.LEVEL_1 || levelState == State.LEVEL_2 || levelState == State.LEVEL_3) {
                            return State.SINGLE;
                        } else if (levelState == State.CREATE_LOBBY || levelState == State.JOIN_LOBBY) {
                            return State.MULTI;
                        } else {
                            System.exit(1);
                        }
                    }
                    default -> screenModel.getBoard().toggleShip(action);
                }
                return levelState;
            }
            case R_KEY -> {
                int action = screenModel.getSelectedAction().onClick();
                if (action < 10) {
                    screenModel.getBoard().rotateShip(action);
                    return levelState;
                }
            }
            default -> {
                return State.NO_ACTION;
            }
        }
        return null;
    }

    @Override
    public void initRender() {
        gameRenderer.initRender(screenModel, hintLabel);
    }

    @Override
    public void render() {
        gameRenderer.render(lastSelectedActionIndex, selectedActionIndex, hintLabel);
        lastSelectedActionIndex = selectedActionIndex;
    }
}
