package me.jorlowski.view;

import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.screens.ScreenModel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class M_Swing_MainRenderer implements M_MainRenderer{
    private final JFrame frame;
    private ScreenModel screenModel;
    private Map<ComponentModel, JPanel> componentList = new HashMap<>();
    public M_Swing_MainRenderer(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void render(int lastActionIndex, int currentActionIndex) {
        renderComponent(screenModel.getActions().get(lastActionIndex), false);
        renderComponent(screenModel.getActions().get(currentActionIndex), true);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    @Override
    public void initRender(ScreenModel screenModel) {
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
}
