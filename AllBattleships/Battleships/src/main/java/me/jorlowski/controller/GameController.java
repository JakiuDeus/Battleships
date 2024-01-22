package me.jorlowski.controller;

import com.googlecode.lanterna.input.KeyStroke;
import me.jorlowski.KeyVar;
import me.jorlowski.State;

public interface GameController {
    State update(KeyVar key);
    void initRender();
    void render();
}
