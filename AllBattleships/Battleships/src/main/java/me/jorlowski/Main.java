package me.jorlowski;

import me.jorlowski.controller.GameEngine;

public class Main {
    public static final String LANTERNA = "LANTERNA";
    public static final String SWING = "SWING";
    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine(SWING);
        gameEngine.start();
    }
}
