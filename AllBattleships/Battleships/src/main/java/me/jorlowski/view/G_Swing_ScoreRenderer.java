package me.jorlowski.view;

import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.components.M_LabelModel;
import me.jorlowski.model.screens.G_ScoreModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class G_Swing_ScoreRenderer implements G_ScoreRenderer {
    private final JFrame frame;
    private G_ScoreModel screenModel;
    private int lastPhase;
    private List<M_LabelModel> labels;
    private Map<ComponentModel, JPanel> componentList = new HashMap<>();
    private JPanel boardBGPanel1;
    private JPanel boardBGPanel2;
    private Map<Integer, JPanel> fields1 = new HashMap<>();
    private Map<Integer, JPanel> fields2 = new HashMap<>();
    private Map<Integer, JLabel> labels1 = new HashMap<>();
    private Map<Integer, JLabel> labels2 = new HashMap<>();
    private GridBagConstraints gbc;
    public G_Swing_ScoreRenderer(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void initRender(G_ScoreModel screenModel, int phase) {
        this.screenModel = screenModel;
        frame.getContentPane().removeAll();
        labels = screenModel.getLabels();
        lastPhase = phase;
        renderComponent(labels.get(lastPhase), true);
        renderComponent(labels.get(4));
        renderBoard(true);
        renderBoard(false);
        renderShips(screenModel.getResult1(), true);
        renderShips(screenModel.getResult2(), false);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    @Override
    public void render(int phase) {
        if (phase == lastPhase) {
            return;
        }
        hideComponent(labels.get(lastPhase));
        renderComponent(labels.get(phase), true);
        renderComponent(labels.get(4));
        lastPhase = phase;
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    private void renderComponent(ComponentModel componentModel) {
        renderComponent(componentModel, false);
    }

    private void renderComponent(ComponentModel component, boolean selected) {
        JPanel panel;
        if (!componentList.containsKey(component)) {
            panel = new JPanel();
            createPanel(component, selected, panel);
            String[] name = component.getName().split("\n");
            if (name.length > 1) {
                panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
                panel.add(new JLabel("                                                                                               "));
                for (String s : name) {
                    JLabel label = new JLabel("     "+s);
                    label.setFont(new Font("Monospaced", Font.BOLD, 12));
                    label.setForeground(Color.WHITE);
                    panel.add(label);
                }
            } else {
                JLabel label = new JLabel(name[0]);
                label.setFont(new Font("Monospaced", Font.PLAIN, 12));
                label.setForeground(Color.WHITE);
                panel.add(label);
            }
        } else {
            panel = componentList.remove(component);
            frame.remove(panel);
            createPanel(component, selected, panel);
        }


        frame.getContentPane().add(panel);
        componentList.put(component, panel);
    }

    private void createPanel(ComponentModel component, boolean selected, JPanel panel) {
        panel.setBackground(Color.BLACK);
        panel.setBounds((int)(component.getX()*7.7), (int)(component.getY()*17), (int)(component.getXSize()*7.7), (int)(component.getySize()*16.7));
        if (selected) {
            panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
        } else {
            panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        }

    }

    private void renderBoard(boolean p1) {
        gbc = new GridBagConstraints();
        if (p1) {
            boardBGPanel1 = new JPanel();
            boardBGPanel1.setLayout(new GridBagLayout());
            boardBGPanel1.setBounds(5, 300, 364, 364);
            boardBGPanel1.setBackground(Color.GRAY);
            if (fields1.isEmpty()) {
                for (int row=0; row<11; row++) {
                    for (int col=0; col<11; col++) {
                        gbc.gridx = col;
                        gbc.gridy = row;
                        Border border = null;
                        JPanel p = new JPanel();
                        p.setPreferredSize(new Dimension(33, 33));
                        p.setBackground(Color.BLACK);
                        if (row < 10) {
                            if (row == 0 && col == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 2, 2, Color.ORANGE)
                                );
                            } else if (row == 1 && col == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 0, 0, 2, Color.ORANGE)
                                );
                            } else if (row == 0 && col == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 2, 2, 0, Color.ORANGE)
                                );
                            } else if (row == 1 && col == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.ORANGE)
                                );
                            } else if (col == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 0, 2, Color.ORANGE)
                                );
                            } else if (col == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 2, 0, 0, Color.ORANGE)
                                );
                            } else if (row == 0 && col != 10) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 2, 0, Color.ORANGE)
                                );
                            } else if (row == 1 && col != 10) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.ORANGE)
                                );
                            } else if (col < 10) {
                                border = new MatteBorder(2, 2, 0, 0, Color.GRAY);
                            } else if (row == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 2, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 2, 0, Color.ORANGE)
                                );
                            } else if (row == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.ORANGE)
                                );
                            } else {
                                border = new MatteBorder(2, 2, 0, 2, Color.GRAY);
                            }
                        } else if (col == 0) {
                            border = new CompoundBorder(
                                    BorderFactory.createMatteBorder(2, 2, 2, 0, Color.GRAY),
                                    BorderFactory.createMatteBorder(0, 0, 0, 2, Color.ORANGE)
                            );
                        } else if (col == 1) {
                            border = new CompoundBorder(
                                    BorderFactory.createMatteBorder(2, 0, 2, 0, Color.GRAY),
                                    BorderFactory.createMatteBorder(0, 2, 0, 0, Color.ORANGE)
                            );
                        } else if (col != 10) {
                            border = new MatteBorder(2, 2, 2, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(2, 2, 2, 2, Color.GRAY);
                        }
                        p.setBorder(border);
                        fields1.put(row*11+col, p);
                        if (row == 0 && col != 0) {
                            JLabel l = new JLabel(Integer.toString(col));
                            l.setForeground(Color.white);
                            p.add(l);
                        } else if (col == 0 && row != 0) {
                            JLabel l = new JLabel(Character.toString(row-1+'A'));
                            l.setForeground(Color.white);
                            p.add(l);
                        }
                        boardBGPanel1.add(p, gbc);
                    }
                }
            }
            frame.getContentPane().add(boardBGPanel1);
        }
        else {
            boardBGPanel2 = new JPanel();
            boardBGPanel2.setLayout(new GridBagLayout());
            boardBGPanel2.setBounds(395, 300, 364, 364);
            boardBGPanel2.setBackground(Color.GRAY);
            if (fields2.isEmpty()) {
                for (int row=0; row<11; row++) {
                    for (int col=0; col<11; col++) {
                        gbc.gridx = col;
                        gbc.gridy = row;
                        Border border = null;
                        JPanel p = new JPanel();
                        p.setPreferredSize(new Dimension(33, 33));
                        p.setBackground(Color.BLACK);
                        if (row < 10) {
                            if (row == 0 && col == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 2, 2, Color.ORANGE)
                                );
                            } else if (row == 1 && col == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 0, 0, 2, Color.ORANGE)
                                );
                            } else if (row == 0 && col == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 2, 2, 0, Color.ORANGE)
                                );
                            } else if (row == 1 && col == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.ORANGE)
                                );
                            } else if (col == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 0, 2, Color.ORANGE)
                                );
                            } else if (col == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 2, 0, 0, Color.ORANGE)
                                );
                            } else if (row == 0 && col != 10) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 2, 0, Color.ORANGE)
                                );
                            } else if (row == 1 && col != 10) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.ORANGE)
                                );
                            } else if (col < 10) {
                                border = new MatteBorder(2, 2, 0, 0, Color.GRAY);
                            } else if (row == 0) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(2, 2, 0, 2, Color.GRAY),
                                        BorderFactory.createMatteBorder(0, 0, 2, 0, Color.ORANGE)
                                );
                            } else if (row == 1) {
                                border = new CompoundBorder(
                                        BorderFactory.createMatteBorder(0, 2, 0, 2, Color.GRAY),
                                        BorderFactory.createMatteBorder(2, 0, 0, 0, Color.ORANGE)
                                );
                            } else {
                                border = new MatteBorder(2, 2, 0, 2, Color.GRAY);
                            }
                        } else if (col == 0) {
                            border = new CompoundBorder(
                                    BorderFactory.createMatteBorder(2, 2, 2, 0, Color.GRAY),
                                    BorderFactory.createMatteBorder(0, 0, 0, 2, Color.ORANGE)
                            );
                        } else if (col == 1) {
                            border = new CompoundBorder(
                                    BorderFactory.createMatteBorder(2, 0, 2, 0, Color.GRAY),
                                    BorderFactory.createMatteBorder(0, 2, 0, 0, Color.ORANGE)
                            );
                        } else if (col != 10) {
                            border = new MatteBorder(2, 2, 2, 0, Color.GRAY);
                        } else {
                            border = new MatteBorder(2, 2, 2, 2, Color.GRAY);
                        }
                        p.setBorder(border);
                        fields2.put(row*11+col, p);
                        if (row == 0 && col != 0) {
                            JLabel l = new JLabel(Integer.toString(col));
                            l.setForeground(Color.white);
                            p.add(l);
                        } else if (col == 0 && row != 0) {
                            JLabel l = new JLabel(Character.toString(row-1+'A'));
                            l.setForeground(Color.white);
                            p.add(l);
                        }
                        boardBGPanel2.add(p, gbc);
                    }
                }
            }
            frame.getContentPane().add(boardBGPanel2);
        }
    }

    private void renderShips(byte[][] board, boolean p1) {
        if (p1) {
            for (int i=0; i<10; i++) {
                for (int j=0; j<10; j++) {
                    gbc.gridx = j+1;
                    gbc.gridy = i+1;
                    JPanel p = fields1.get(gbc.gridy*11 + gbc.gridx);
                    switch (board[i][j]) {
                        case -1 -> {
                            JLabel l;
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("•");
                                l.setForeground(Color.WHITE);
                                labels1.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.BLACK);
                            } else if (!labels1.get(gbc.gridy*11 + gbc.gridx).getText().equals("•") || p.getBackground() != Color.BLACK){
                                l = labels1.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("•");
                                l.setForeground(Color.WHITE);
                                p.setBackground(Color.BLACK);
                            }
                        }
                        case 1-> {
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                p.setBackground(Color.GREEN);
                            } else if (labels1.get(gbc.gridy*11 + gbc.gridx).getBackground() != Color.GREEN){
                                p.setBackground(Color.GREEN);
                            }
                        }
                        case 2 -> {
                            JLabel l;
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("/");
                                l.setForeground(Color.BLACK);
                                labels1.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.YELLOW);
                            } else if (!labels1.get(gbc.gridy*11 + gbc.gridx).getText().equals("/") || p.getBackground() != Color.YELLOW){
                                l = labels1.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("/");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.YELLOW);
                            }
                        }
                        case 3 -> {
                            JLabel l;
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("X");
                                l.setForeground(Color.BLACK);
                                labels1.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.RED);
                            } else if (!labels1.get(gbc.gridy*11 + gbc.gridx).getText().equals("X") || p.getBackground() != Color.RED){
                                l = labels1.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("X");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.RED);
                            }
                        }
                        case 99 -> {
                            JLabel l;
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("•");
                                l.setForeground(Color.BLACK);
                                labels1.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.WHITE);
                            } else if (!labels1.get(gbc.gridy*11 + gbc.gridx).getText().equals("•") || p.getBackground() != Color.WHITE){
                                l = labels1.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("•");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.WHITE);
                            }
                        }
                        case 100, 101 -> {
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                p.setBackground(Color.WHITE);
                            } else if (labels1.get(gbc.gridy*11 + gbc.gridx).getBackground() != Color.WHITE){
                                p.setBackground(Color.WHITE);
                            }
                        }
                        case 102 -> {
                            JLabel l;
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("/");
                                l.setForeground(Color.BLACK);
                                labels1.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.WHITE);
                            } else if (!labels1.get(gbc.gridy*11 + gbc.gridx).getText().equals("/") || p.getBackground() != Color.WHITE){
                                l = labels1.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("/");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.WHITE);
                            }
                        }
                        case 103 -> {
                            JLabel l;
                            if (!labels1.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("X");
                                l.setForeground(Color.BLACK);
                                labels1.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.WHITE);
                            } else if (!labels1.get(gbc.gridy*11 + gbc.gridx).getText().equals("X") || p.getBackground() != Color.WHITE){
                                l = labels1.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("X");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.WHITE);
                            }
                        }
                        default -> p.setBackground(Color.BLACK);
                    }
                }
            }
        } else {
            for (int i=0; i<10; i++) {
                for (int j=0; j<10; j++) {
                    gbc.gridx = j+1;
                    gbc.gridy = i+1;
                    JPanel p = fields2.get(gbc.gridy*11 + gbc.gridx);
                    switch (board[i][j]) {
                        case -1 -> {
                            JLabel l;
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("•");
                                l.setForeground(Color.WHITE);
                                labels2.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.BLACK);
                            } else if (!labels2.get(gbc.gridy*11 + gbc.gridx).getText().equals("•") || p.getBackground() != Color.BLACK){
                                l = labels2.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("•");
                                l.setForeground(Color.WHITE);
                                p.setBackground(Color.BLACK);
                            }
                        }
                        case 1-> {
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                p.setBackground(Color.GREEN);
                            } else if (labels2.get(gbc.gridy*11 + gbc.gridx).getBackground() != Color.GREEN){
                                p.setBackground(Color.GREEN);
                            }
                        }
                        case 2 -> {
                            JLabel l;
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("/");
                                l.setForeground(Color.BLACK);
                                labels2.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.YELLOW);
                            } else if (!labels2.get(gbc.gridy*11 + gbc.gridx).getText().equals("/") || p.getBackground() != Color.YELLOW){
                                l = labels2.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("/");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.YELLOW);
                            }
                        }
                        case 3 -> {
                            JLabel l;
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("X");
                                l.setForeground(Color.BLACK);
                                labels2.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.RED);
                            } else if (!labels2.get(gbc.gridy*11 + gbc.gridx).getText().equals("X") || p.getBackground() != Color.RED){
                                l = labels2.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("X");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.RED);
                            }
                        }
                        case 99 -> {
                            JLabel l;
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("•");
                                l.setForeground(Color.BLACK);
                                labels2.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.WHITE);
                            } else if (!labels2.get(gbc.gridy*11 + gbc.gridx).getText().equals("•") || p.getBackground() != Color.WHITE){
                                l = labels2.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("•");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.WHITE);
                            }
                        }
                        case 100, 101 -> {
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                p.setBackground(Color.WHITE);
                            } else if (labels2.get(gbc.gridy*11 + gbc.gridx).getBackground() != Color.WHITE){
                                p.setBackground(Color.WHITE);
                            }
                        }
                        case 102 -> {
                            JLabel l;
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("/");
                                l.setForeground(Color.BLACK);
                                labels2.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.WHITE);
                            } else if (!labels2.get(gbc.gridy*11 + gbc.gridx).getText().equals("/") || p.getBackground() != Color.WHITE){
                                l = labels2.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("/");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.WHITE);
                            }
                        }
                        case 103 -> {
                            JLabel l;
                            if (!labels2.containsKey(gbc.gridy*11 + gbc.gridx)) {
                                p.removeAll();
                                l = new JLabel("X");
                                l.setForeground(Color.BLACK);
                                labels2.put(gbc.gridy*11 + gbc.gridx, l);
                                p.add(l);
                                p.setBackground(Color.WHITE);
                            } else if (!labels2.get(gbc.gridy*11 + gbc.gridx).getText().equals("X") || p.getBackground() != Color.WHITE){
                                l = labels2.get(gbc.gridy*11 + gbc.gridx);
                                l.setText("X");
                                l.setForeground(Color.BLACK);
                                p.setBackground(Color.WHITE);
                            }
                        }
                        default -> p.setBackground(Color.BLACK);
                    }
                }
            }
        }
    }
    private void hideComponent(ComponentModel component) {
        if (componentList.containsKey(component)) {
            JPanel p = componentList.remove(component);
            frame.remove(p);
        }
    }
}
