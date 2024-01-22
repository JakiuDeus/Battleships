package me.jorlowski.controller;

import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.*;
import me.jorlowski.model.screens.G_ScoreModel;
import me.jorlowski.view.G_Lanterna_ScoreRenderer;
import me.jorlowski.view.G_ScoreRenderer;
import me.jorlowski.view.G_Swing_ScoreRenderer;

import javax.swing.*;
import java.awt.print.Pageable;
import java.rmi.UnexpectedException;

public class G_ScoreController implements GameController {
    private State state;
    private G_ScoreModel scoreModel;
    private G_ScoreRenderer scoreRenderer;
    private SpecificWindow window;
    private static long timer;
    private static int phase;

    public G_ScoreController(SpecificWindow window, State state, byte[][] result1, byte[][] result2) {
        this.window = window;
        this.state = state;
        scoreModel = new G_ScoreModel(state, result1, result2);
        if (window instanceof LanternaWindow) {
            scoreRenderer = new G_Lanterna_ScoreRenderer((Screen) this.window.getSpecificWindow());
        } else if (window instanceof SwingWindow) {
            scoreRenderer = new G_Swing_ScoreRenderer((JFrame) this.window.getSpecificWindow());
        } else {
            System.exit(1);
        }
        timer = System.currentTimeMillis();
        phase = 0;
    }

    @Override
    public State update(KeyVar key) {
        if (key == null && System.currentTimeMillis() - timer >= 1000) {
            phase = (phase + 1) % 4;
            timer = System.currentTimeMillis();
            return state;
        } else if (key == null) {
            return State.NO_ACTION;
        }
        switch (key) {
            case EXIT -> {
                return State.EXIT;
            }
            case ENTER -> {
                return State.MAIN;
            }
            default -> {
                if (System.currentTimeMillis() - timer >= 1000) {
                    phase = (phase + 1) % 4;
                    timer = System.currentTimeMillis();
                    return state;
                }
                return State.NO_ACTION;
            }
        }
    }

    @Override
    public void initRender() {
        scoreRenderer.initRender(scoreModel, phase);
    }

    @Override
    public void render() {
        scoreRenderer.render(phase);
    }
}
