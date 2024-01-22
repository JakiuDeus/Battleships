package me.jorlowski.view;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.screens.G_BuildModel;

import java.io.IOException;

public class G_Lanterna_GameBuildRenderer implements G_GameBuildRenderer {

    private final Screen screen;
    private G_BuildModel screenModel;
    public G_Lanterna_GameBuildRenderer(Screen screen) {
        this.screen = screen;
    }

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
        try {
            screen.refresh();
        } catch (IOException ignored) {}
    }

    public void initRender(G_BuildModel screenModel, boolean hintLabel) {
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
        renderBoard();
        renderShips(screenModel.getBoard().getTab());
        if (hintLabel) {
            renderComponent(screenModel.getHintLabel());
        } else {
            hideComponent(screenModel.getHintLabel());
        }
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
    private void renderBoard() {
        TerminalPosition topLeftPos = new TerminalPosition(1, 1);
        TerminalPosition topRightPos = new TerminalPosition(45, 1);
        TerminalPosition botLeftPos = new TerminalPosition(1, 23);
        TerminalPosition botRightPos = new TerminalPosition(45, 23);
        TextGraphics textGraphics = screen.newTextGraphics();

        // Top Line
        textGraphics.drawLine(
                topLeftPos.withRelativeColumn(1),
                topRightPos.withRelativeColumn(-1),
                Symbols.DOUBLE_LINE_HORIZONTAL
        );
        // Bottom Line
        textGraphics.drawLine(
                botLeftPos.withRelativeColumn(1),
                botRightPos.withRelativeColumn(-1),
                Symbols.DOUBLE_LINE_HORIZONTAL
        );
        // Left Line
        textGraphics.drawLine(
                topLeftPos.withRelativeRow(1),
                botLeftPos.withRelativeRow(-1),
                Symbols.DOUBLE_LINE_VERTICAL
        );
        // Right Line
        textGraphics.drawLine(
                topRightPos.withRelativeRow(1),
                botRightPos.withRelativeRow(-1),
               Symbols.DOUBLE_LINE_VERTICAL
        );
        // Corners
        textGraphics.setCharacter(topLeftPos, Symbols.DOUBLE_LINE_TOP_LEFT_CORNER);
        textGraphics.setCharacter(topRightPos, Symbols.DOUBLE_LINE_TOP_RIGHT_CORNER);
        textGraphics.setCharacter(botLeftPos, Symbols.DOUBLE_LINE_BOTTOM_LEFT_CORNER);
        textGraphics.setCharacter(botRightPos, Symbols.DOUBLE_LINE_BOTTOM_RIGHT_CORNER);

        for (int i=0; i<10; i++) {
            // Horizontal lines
            textGraphics.drawLine(
                    topLeftPos.withRelativeColumn(1).withRelativeRow(2+2*i),
                    topRightPos.withRelativeColumn(-1).withRelativeRow(2+2*i),
                    Symbols.SINGLE_LINE_HORIZONTAL
            );
            // Vertical lines
            textGraphics.drawLine(
                    topLeftPos.withRelativeRow(1).withRelativeColumn(4+4*i),
                    botLeftPos.withRelativeRow(-1).withRelativeColumn(4+4*i),
                    Symbols.SINGLE_LINE_VERTICAL
            );
            // Horizontal coordinates
            if (i==9) {
                textGraphics.setCharacter(topLeftPos
                                .withRelativeColumn(5+4*i)
                                .withRelativeRow(1),
                        '1'
                );
                textGraphics.setCharacter(topLeftPos
                                .withRelativeColumn(6+4*i)
                                .withRelativeRow(1),
                        '0'
                );
            } else {
                textGraphics.setCharacter(topLeftPos
                                .withRelativeColumn(6+4*i)
                                .withRelativeRow(1),
                        (char) (i+'1')
                );
            }
            // Vertical coordinates
            textGraphics.setCharacter(topLeftPos
                            .withRelativeRow(3+2*i)
                            .withRelativeColumn(2),
                    (char) (i+'A')
            );
        }
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                textGraphics.setCharacter(new TerminalPosition(i*4, j*2)
                            .withRelativeColumn(5)
                            .withRelativeRow(3),
                        Symbols.SINGLE_LINE_CROSS
                );
            }
        }
    }

    private void renderShips(byte[][] board) {
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                switch (board[i][j]) {
                    case 1 -> {
                        screen.setCharacter(6+j*4, i*2+4,
                                new TextCharacter(' ',
                                TextColor.ANSI.DEFAULT,
                                TextColor.ANSI.GREEN));
                        screen.setCharacter(7+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                        screen.setCharacter(8+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                    }
                    case 2, 3, 4, 5, 6, 7, 8, 9, 10 -> {
                        screen.setCharacter(6+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                        screen.setCharacter(7+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                        screen.setCharacter(8+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                    }
                    default -> {
                        screen.setCharacter(6+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(7+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(8+j*4, i*2+4,
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                    }
                }
            }
        }
    }
    private void hideComponent(ComponentModel component) {
        TerminalPosition topLeftPos = component.getTLPos();
        TerminalPosition topRightPos = component.getTRPos();
        TerminalPosition botLeftPos = component.getBLPos();
        TerminalPosition botRightPos = component.getBRPos();
        TextGraphics textGraphics = screen.newTextGraphics();

        // Top Line
        textGraphics.drawLine(
                topLeftPos.withRelativeColumn(1),
                topRightPos.withRelativeColumn(-1),
                ' '
        );
        // Bottom Line
        textGraphics.drawLine(
                botLeftPos.withRelativeColumn(1),
                botRightPos.withRelativeColumn(-1),
                ' '
        );
        // Left Line
        textGraphics.drawLine(
                topLeftPos.withRelativeRow(1),
                botLeftPos.withRelativeRow(-1),
                ' '
        );
        // Right Line
        textGraphics.drawLine(
                topRightPos.withRelativeRow(1),
                botRightPos.withRelativeRow(-1),
                ' '
        );
        // Corners
        textGraphics.setCharacter(topLeftPos, ' ');
        textGraphics.setCharacter(topRightPos, ' ');
        textGraphics.setCharacter(botLeftPos, ' ');
        textGraphics.setCharacter(botRightPos, ' ');
        // Text
        String[] name = component.getName().split("\n");
        for (int index = 0; index < name.length; index++) {
            for (int j = 0; j < name[index].length(); j++) {
                textGraphics.setCharacter(topLeftPos.withRelative(1+j, index+1), ' ');
            }
        }
    }
}
