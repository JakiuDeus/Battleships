package me.jorlowski;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class SwingWindow implements SpecificWindow {

    private JFrame frame;
    private Queue<Integer> inputQueue;

    public SwingWindow() {
        frame = new JFrame("Battleships in SWING");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(800, 800);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);
        inputQueue = new ArrayBlockingQueue<>(32);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                inputQueue.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        frame.setVisible(true);
    }

    @Override
    public void close() {
        System.exit(1);
    }

    @Override
    public KeyVar getInputKey() {
        if (inputQueue.peek() == null) {
            return null;
        }
        switch (inputQueue.poll()) {
            case KeyEvent.VK_ENTER -> {
                return KeyVar.ENTER;
            }
            case KeyEvent.VK_UP -> {
                return KeyVar.ARROW_UP;
            }
            case KeyEvent.VK_DOWN -> {
                return KeyVar.ARROW_DOWN;
            }
            case KeyEvent.VK_LEFT -> {
                return KeyVar.ARROW_LEFT;
            }
            case KeyEvent.VK_RIGHT -> {
                return KeyVar.ARROW_RIGHT;
            }
            case KeyEvent.VK_PAGE_UP -> {
                return KeyVar.PAGE_UP;
            }
            case KeyEvent.VK_PAGE_DOWN -> {
                return KeyVar.PAGE_DOWN;
            }
            case KeyEvent.VK_R -> {
                return KeyVar.R_KEY;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void refresh() {

    }

    @Override
    public void clear() {
        frame.removeAll();
    }

    @Override
    public boolean shouldResize() {
        return false;
    }

    @Override
    public Object getSpecificWindow() {
        return frame;
    }
}
