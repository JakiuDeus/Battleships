package me.jorlowski.controller;

import com.googlecode.lanterna.input.KeyStroke;
import me.jorlowski.*;

import javax.swing.*;
import java.io.IOException;

public class GameEngine {
    private SpecificWindow window;
    private State state;
    private GameController controller;

    public GameEngine(String screenType) {
        if (screenType.equals("LANTERNA")) {
            window = new LanternaWindow();
        } else if (screenType.equals("SWING")) {
            window = new SwingWindow();
        } else {
            System.exit(1);
        }
        state = State.MAIN;
        controller = GameControllerFactory.createInstance(window, state);
    }


    // Starts Main Engine Loop
    public void start() {
        State updateState;
        KeyVar key;
        controller.initRender();
        while (getState() != State.EXIT) {
            if (window.shouldResize()) {
                System.out.println("Resize");
                controller.initRender();
                window.refresh();
            }
            key = window.getInputKey();
            updateState = controller.update(key);
            if (updateState == State.NO_ACTION) {
                continue;
            }
            if (updateState != state && updateState != State.EXIT) {
                state = updateState;
                controller = GameControllerFactory.createInstance(window, state);
                controller.initRender();
            } else if (updateState == State.EXIT) {
                state = updateState;
            } else {
                controller.render();
            }
        }
        window.close();
    }

    private State getState() {
        return state;
    }
}
