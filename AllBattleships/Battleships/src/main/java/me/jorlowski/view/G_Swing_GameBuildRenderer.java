package me.jorlowski.view;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.screens.G_BuildModel;
import me.jorlowski.model.screens.ScreenModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class G_Swing_GameBuildRenderer implements G_GameBuildRenderer {
    private final JFrame frame;
    private G_BuildModel screenModel;
    private JPanel boardBGPanel;
    private Map<ComponentModel, JPanel> componentList = new HashMap<>();
    private Map<Integer, JPanel> fields = new HashMap<>();
    private GridBagConstraints gbc;
    public G_Swing_GameBuildRenderer(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void render(int lastActionIndex, int currentActionIndex, boolean hintLabel) {
        if (lastActionIndex == currentActionIndex) {
            renderShips(screenModel.getBoard().getTab());
        } else {
            renderComponent(screenModel.getActions().get(lastActionIndex), false);
            renderComponent(screenModel.getActions().get(currentActionIndex), true);
        }
        if (hintLabel) {
            renderComponent(screenModel.getHintLabel());
        } else if (lastActionIndex == currentActionIndex) {
            hideComponent(screenModel.getHintLabel());
        }
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    @Override
    public void initRender(G_BuildModel screenModel, boolean hintLabel) {
        this.screenModel = screenModel;
        int selectedActionIndex = screenModel.getSelectedActionIndex();
        frame.getContentPane().removeAll();
        screenModel.getActions().forEach(action -> {
            if (action.equals(screenModel.getActions().get(selectedActionIndex))) {
                renderComponent(action, true);
            } else {
                renderComponent(action, false);
            }
        });
        screenModel.getLabels().forEach(label -> renderComponent(label, true));
        renderBoard();
        renderShips(screenModel.getBoard().getTab());
        if (hintLabel) {
            renderComponent(screenModel.getHintLabel());
        } else {
            hideComponent(screenModel.getHintLabel());
        }
        frame.revalidate();
        frame.repaint();
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

    private void renderBoard() {
        boardBGPanel = new JPanel();
        boardBGPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        boardBGPanel.setBounds(5, 5, 364, 364);
        boardBGPanel.setBackground(Color.GRAY);
        if (fields.isEmpty()) {
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
                    fields.put(row*11+col, p);
                    if (row == 0 && col != 0) {
                        JLabel l = new JLabel(Integer.toString(col));
                        l.setForeground(Color.white);
                        p.add(l);
                    } else if (col == 0 && row != 0) {
                        JLabel l = new JLabel(Character.toString(row-1+'A'));
                        l.setForeground(Color.white);
                        p.add(l);
                    }
                    boardBGPanel.add(p, gbc);
                }
            }
        }
        frame.getContentPane().add(boardBGPanel);

    }

    private void renderShips(byte[][] board) {
        for (int row=0; row<10; row++) {
            for (int col=0; col<10; col++) {
                gbc.gridx = col+1;
                gbc.gridy = row+1;
                JPanel p = fields.get(gbc.gridy*11 + gbc.gridx);
                p.removeAll();
                switch (board[row][col]) {
                    case 1 -> {
                        p.setBackground(Color.GREEN);
                    }
                    case 2, 3, 4, 5, 6, 7, 8, 9, 10 -> {
                        p.setBackground(Color.RED);
                    }
                    default -> {
                        p.setBackground(Color.BLACK);
                    }
                }
            }
        }
    }

    private void hideComponent(ComponentModel component) {
        if (componentList.containsKey(component)) {
            frame.getContentPane().remove(componentList.remove(component));
        }
    }
}
