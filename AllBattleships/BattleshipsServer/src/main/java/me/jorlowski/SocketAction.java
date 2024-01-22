package me.jorlowski;

import java.io.*;
import java.net.Socket;

public class SocketAction extends Thread {
    private DataInputStream inStream = null;
    protected PrintStream outStream = null;
    private Socket socket = null;

    public SocketAction(Socket socket) {
        super("SocketAction");
        try {
            inStream = new DataInputStream(new BufferedInputStream(socket.getInputStream(), 1024));
            outStream = new PrintStream(new BufferedOutputStream(socket.getOutputStream(), 1024), true);
            this.socket = socket;
        } catch (IOException e) {
            System.out.println("Couldn't initialize SocketAction: " + e);
            System.exit(1);
        }
    }

    public void send(String s) {
        outStream.println(s);
    }

    public String receive() throws IOException {
        return inStream.readLine();
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

    protected void finalize() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close socket: " + e);
            }
            socket = null;
        }
    }
}
