package me.jorlowski;

import java.io.IOException;

public class BattleshipsServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Running");
        new BattleshipsDaemon().start();
    }
}
