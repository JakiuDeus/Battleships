package me.jorlowski.view;


import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.screens.ScreenModel;

import java.io.IOException;

public class M_Lanterna_MainRenderer implements M_MainRenderer {
    private final Screen screen;
    private ScreenModel screenModel;
    public M_Lanterna_MainRenderer(Screen screen) {
        this.screen = screen;
    }

    @Override
    public void render(int lastActionIndex, int currentActionIndex) {
        renderComponent(screenModel.getActions().get(lastActionIndex), false);
        renderComponent(screenModel.getActions().get(currentActionIndex), true);
        try {
            screen.refresh();
        } catch (IOException ignored) {}
    }

    @Override
    public void initRender(ScreenModel screenModel) {
        this.screenModel = screenModel;
        int selectedActionIndex = screenModel.getSelectedActionIndex();
        screen.clear();
        screenModel.getActions().forEach(action -> {
            if (action.equals(screenModel.getActions().get(selectedActionIndex))) {
                renderComponent(action, true);
            } else {
                renderComponent(action);
            }

        });
        screenModel.getLabels().forEach(label -> renderComponent(label, true));
        try {
            screen.refresh();
        } catch (IOException ignored) {
        }
    }
    private void renderComponent(ComponentModel componentModel) {
        renderComponent(componentModel, false);
    }
    private void renderComponent(ComponentModel component, boolean selected) {
        TerminalPosition topLeftPos = component.getTLPos();
        TerminalPosition topRightPos = component.getTRPos();
        TerminalPosition botLeftPos = component.getBLPos();
        TerminalPosition botRightPos = component.getBRPos();
        TextGraphics textGraphics = screen.newTextGraphics();

        // Top Line
        textGraphics.drawLine(
                topLeftPos.withRelativeColumn(1),
                topRightPos.withRelativeColumn(-1),
                selected ? Symbols.DOUBLE_LINE_HORIZONTAL : Symbols.SINGLE_LINE_HORIZONTAL
        );
        // Bottom Line
        textGraphics.drawLine(
                botLeftPos.withRelativeColumn(1),
                botRightPos.withRelativeColumn(-1),
                selected ? Symbols.DOUBLE_LINE_HORIZONTAL : Symbols.SINGLE_LINE_HORIZONTAL
        );
        // Left Line
        textGraphics.drawLine(
                topLeftPos.withRelativeRow(1),
                botLeftPos.withRelativeRow(-1),
                selected ? Symbols.DOUBLE_LINE_VERTICAL : Symbols.SINGLE_LINE_VERTICAL
        );
        // Right Line
        textGraphics.drawLine(
                topRightPos.withRelativeRow(1),
                botRightPos.withRelativeRow(-1),
                selected ? Symbols.DOUBLE_LINE_VERTICAL : Symbols.SINGLE_LINE_VERTICAL
        );
        // Corners
        textGraphics.setCharacter(topLeftPos, selected ? Symbols.DOUBLE_LINE_TOP_LEFT_CORNER : Symbols.SINGLE_LINE_TOP_LEFT_CORNER);
        textGraphics.setCharacter(topRightPos, selected ? Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER : Symbols.SINGLE_LINE_TOP_RIGHT_CORNER);
        textGraphics.setCharacter(botLeftPos, selected ? Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER : Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER);
        textGraphics.setCharacter(botRightPos, selected ? Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER : Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER);
        // Text
        String[] name = component.getName().split("\n");
        for (int index = 0; index < name.length; index++) {
            textGraphics.putString(topLeftPos.withRelative(1, index + 1), name[index]);
        }

    }
}
