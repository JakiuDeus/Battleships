package me.jorlowski;

import java.io.IOException;
import java.net.Socket;

public class Player extends SocketAction {
    private BattleshipsDaemon daemon = null;

    public Player(BattleshipsDaemon server, Socket socket) {
        super(socket);
        daemon = server;
    }

    public void run() {
        daemon.waitForGame(this).playGame(this);
    }

    @Override
    public void closeConnections() {
        super.closeConnections();
        if (outStream != null) {
            send("GAMEOVER");
        }
    }
}
