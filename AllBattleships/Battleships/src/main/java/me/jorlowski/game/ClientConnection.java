package me.jorlowski.game;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection extends SocketAction {

    public static final int PORTNUM = 1234;
    public static final int ERROR = -1;
    public static final int PLSWAIT = -2;
    public static final int YOURTURN = -3;
    public static final int THEIRTURN = -4;
    public static final int THEYWON = -5;
    public static final int THEYQUIT = -6;
    public static final int GAMEOVER = -7;
    public static final int NOINPUT = -8;
    public static final int HIT = -100;
    public static final int MISS = -200;
    public static final int DESTROYED = -300;

    public ClientConnection() throws IOException {
        super(new Socket("127.0.0.1", PORTNUM));
    }

    public int getTheirMove() {
        if (!isConnected()) {
            throw new NullPointerException("Attempted to read closed socket!");
        }
        try {
            String s = receive();
            if (s == null) {
                return GAMEOVER;
            }
            s.trim();
            try {
                return (Integer.parseInt(s));
            } catch (NumberFormatException e) {
                return getStatus(s);
            }
        } catch (IOException e) {
            System.out.println("I/O Error: + e");
            System.exit(1);
            return 0;
        }
    }

    private int getStatus(String s) {
        s = s.trim();
        if (s.startsWith("PLSWAIT")) {
            return PLSWAIT;
        }if (s.startsWith("THEIRTURN")) {
            return THEIRTURN;
        }if (s.startsWith("YOURTURN")) {
            return YOURTURN;
        }if (s.startsWith("THEYWON")) {
            return THEYWON;
        }if (s.startsWith("THEYQUIT")) {
            return THEYQUIT;
        }if (s.startsWith("GAMEOVER")) {
            return GAMEOVER;
        }if (s.startsWith("check")) {
            return NOINPUT;
        }if (s.startsWith("HIT")) {
            return HIT;
        }if (s.startsWith("MISS")) {
            return MISS;
        }if (s.startsWith("DESTROYED")) {
            return DESTROYED;
        }

        System.out.println("Received invalid status from server: " + s);
        return ERROR;
    }

    public void sendMove(int cell) {
        String s = Integer.toString(cell);
        send(s);
    }

    public void sendIQUIT() {
        send("IQUIT");
    }

    public void sendIWON() {
        send("IWON");
    }

}
