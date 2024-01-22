package me.jorlowski;

import me.jorlowski.controller.GameEngine;

public class Main {
    public static final String LANTERNA = "LANTERNA";
    public static final String SWING = "SWING";
    public static void main(String[] args) {
        if (args.length == 1) {
            if (args[0].equals(LANTERNA)) {
                GameEngine gameEngine = new GameEngine(LANTERNA);
                gameEngine.start();
            } else if (args[0].equals(SWING)) {
                GameEngine gameEngine = new GameEngine(SWING);
                gameEngine.start();
            } else {
                System.out.println("Wrong argument number!");
                System.out.println("Use only 1 argument: SWING or LANTERNA");
                System.out.println("Default when 0 is SWING");
                System.exit(1);
            }
        } else if (args.length == 0) {
            GameEngine gameEngine = new GameEngine(SWING);
            gameEngine.start();
        } else {
            System.out.println("Wrong argument number!");
            System.out.println("Use only 1 argument: SWING or LANTERNA");
            System.out.println("Default when 0 is SWING");
            System.exit(1);
        }

    }
}
