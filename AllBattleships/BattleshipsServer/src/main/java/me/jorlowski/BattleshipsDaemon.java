package me.jorlowski;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BattleshipsDaemon extends Thread {
    public static final int PORTNUM = 1234;
    private ServerSocket port;
    private Player playerWaiting = null;
    private Game thisGame = null;

    public BattleshipsDaemon() throws IOException {
        port = new ServerSocket(PORTNUM);
    }
    public void run() {
        Socket clientSocket;
        while(true) {
            if (port == null) {
                System.out.println("Sorry, the port disappeared");
                System.exit(1);
            }
            try {
                clientSocket = port.accept();
                new Player(this, clientSocket).start();
            } catch (IOException e) {
                System.out.println("Couldn't connect player: " + e);
                System.exit(1);
            }
        }
    }

    public synchronized Game waitForGame(Player p) {
        Game retval = null;
        if (playerWaiting == null) {
            playerWaiting = p;
            thisGame = null;
            p.send("PLSWAIT");
            while (playerWaiting != null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                }
            }
            return thisGame;
        } else {
            thisGame = new Game(playerWaiting, p);
            retval = thisGame;
            playerWaiting = null;
            notify();
            return retval;
        }
    }

    protected void finalize() {
        if (port != null) {
            try {
                port.close();
            } catch (IOException e) {
                System.out.println("Error closing port: " + e);
            }
            port = null;
        }
    }
}
