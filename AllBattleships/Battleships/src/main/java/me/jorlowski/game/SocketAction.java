package me.jorlowski.game;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketAction extends Thread {
    private DataInputStream inStream = null;
    protected PrintStream outStream = null;
    private Socket socket = null;

    public SocketAction(Socket socket) {
        super("SocketAction");
        try {
            this.socket = socket;
            this.socket.setSoTimeout(50);
            inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 1024));
            outStream = new PrintStream(new BufferedOutputStream(socket.getOutputStream(), 1024), true);

        } catch (IOException e) {
            System.out.println("Couldn't initialize SocketAction: " + e);
            System.exit(1);
        }
    }

    public void send(String s) {
        outStream.println(s);
    }

    public String receive() throws IOException {
        try {
            return inStream.readLine();
        } catch (SocketTimeoutException ste) {
            return "check";
        }
    }

    public void closeConnections() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            System.out.println("Couldn't close socket: " + e);
        }
    }

    public boolean isConnected() {
        return ((inStream != null) && (outStream != null) && (socket != null));
    }
}
