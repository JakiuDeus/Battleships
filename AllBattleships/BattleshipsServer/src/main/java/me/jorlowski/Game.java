package me.jorlowski;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Vector;

public class Game {
    public static final int ERROR = -1;
    public static final int IWON = -2;
    public static final int IQUIT = -3;
    public static final int YOURTURN = -4;
    public static final int SENTSTRING = -5;

    private final Player player1;
    private final Player player2;
    private final Vector<Integer> p1Queue ;
    private final Vector<Integer> p2Queue;
    private String sentString;
    public static final int MYMOVE = 1;
    public static final int WAITING_FOR_RESPONSE = 2;
    public static final int MY_RESPONSE = 3;
    public static final int THEIRMOVE = 4;

    public Game(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;
        p1Queue = new Vector<>();
        p2Queue = new Vector<>();
    }

    public void playGame(Player me) {
        String instr;
        boolean playGame = true;
        int myTurn = MYMOVE;
        int stat;
        try {
            if (me == player2) {
                myTurn = THEIRMOVE;
            } else if (me != player1) {
                System.out.println("Illegal call to playGame!");
                return;
            }

            while (playGame) {
                if (myTurn == MYMOVE) {
                    me.send("YOURTURN");
                    instr = me.receive();
                    sentString = instr;
                    sendStatus(me, SENTSTRING);
                    myTurn = WAITING_FOR_RESPONSE;
                } else if (myTurn == WAITING_FOR_RESPONSE) {
                    stat = getStatus(me);
                    if (stat == SENTSTRING) {
                        me.send(sentString);
                        if (sentString.equals("MISS")) {
                            myTurn = THEIRMOVE;
                        } else {
                            myTurn = MYMOVE;
                        }
                    }
                } else if (myTurn == MY_RESPONSE) {
                    instr = me.receive();
                    sentString = instr;
                    sendStatus(me, SENTSTRING);
                    if (sentString.equals("MISS")) {
                        myTurn = MYMOVE;
                    } else {
                        myTurn = THEIRMOVE;
                    }
                } else {
                    me.send("THEIRTURN");
                    stat = getStatus(me);
                    if (stat == SENTSTRING) {
                        me.send(sentString);
                        myTurn = MY_RESPONSE;
                    }

                }

            }
            me.closeConnections();
            return;
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
            System.exit(1);
        }
    }

    private synchronized int getStatus(Player me) {
        Vector<Integer> ourVector = ((me == player1) ? p1Queue : p2Queue);
        while (ourVector.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e);
            }
        }
        try {
            Integer retval = ourVector.firstElement();
            try {
                ourVector.removeElementAt(0);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Array index out of bounds: " + e);
                System.exit(1);
            }
            return retval;
        } catch (NoSuchElementException e) {
            System.out.println("Couldn't get first element: " + e);
            System.exit(1);
            return 0;
        }
    }

    private synchronized void sendStatus(Player me, int message) {
        Vector<Integer> theirVector = ((me == player1) ? p2Queue : p1Queue);
        theirVector.addElement(message);
        notify();
    }
}
