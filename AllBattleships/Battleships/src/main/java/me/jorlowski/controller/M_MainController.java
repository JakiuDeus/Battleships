package me.jorlowski.controller;

import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.*;
import me.jorlowski.model.screens.ScreenModel;
import me.jorlowski.view.M_Lanterna_MainRenderer;
import me.jorlowski.view.M_MainRenderer;
import me.jorlowski.view.M_Swing_MainRenderer;

import javax.swing.*;
import java.awt.*;

public class M_MainController implements GameController {
    private ScreenModel mainMenuModel;
    private M_MainRenderer mainMenuView;
    private SpecificWindow window;
    private int actionLength;
    private int selectedActionIndex;
    private int lastSelectedActionIndex;

    public M_MainController(SpecificWindow window, ScreenModel screenModel) {
        this.window = window;
        mainMenuModel = screenModel;
        if (window instanceof LanternaWindow) {
            mainMenuView = new M_Lanterna_MainRenderer((Screen) this.window.getSpecificWindow());
        } else if (window instanceof SwingWindow) {
            mainMenuView = new M_Swing_MainRenderer((JFrame) this.window.getSpecificWindow());
        } else {
            System.exit(1);
        }
        actionLength = mainMenuModel.getActions().size()-1;
        selectedActionIndex = mainMenuModel.getSelectedActionIndex();
        lastSelectedActionIndex = selectedActionIndex;
    }

    @Override
    public State update(KeyVar key) {
        if (key == null) {
            return State.NO_ACTION;
        }
        switch (key) {
            case EXIT -> {return State.EXIT;}
            case ARROW_UP -> {
                lastSelectedActionIndex = selectedActionIndex;
                selectedActionIndex = selectedActionIndex == 0 ? 0 : selectedActionIndex - 1;
                mainMenuModel.setSelectedAction(selectedActionIndex);
                return mainMenuModel.getState();
            }
            case ARROW_DOWN -> {
                lastSelectedActionIndex = selectedActionIndex;
                selectedActionIndex = selectedActionIndex == actionLength ? actionLength : selectedActionIndex + 1;
                mainMenuModel.setSelectedAction(selectedActionIndex);
                return mainMenuModel.getState();
            }
            case ENTER -> {
                return mainMenuModel.getSelectedAction().onClick();
            }
            default -> {
                return State.NO_ACTION;
            }
        }
    }

    @Override
    public void initRender() {
        mainMenuView.initRender(mainMenuModel);
    }

    @Override
    public void render() {
        mainMenuView.render(lastSelectedActionIndex, selectedActionIndex);
    }


}
