package me.jorlowski.view;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import me.jorlowski.GameState;
import me.jorlowski.SpecificWindow;
import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.screens.G_PlayModel;

import java.io.IOException;



public class G_Lanterna_GameRenderer implements G_GameRenderer {
    private final Screen screen;
    private G_PlayModel screenModel;
    private GameState yourTurn;
    public G_Lanterna_GameRenderer(Screen screen) {
        this.screen = screen;
    }

    public void render(GameState yourTurn) {
        this.yourTurn = yourTurn;
        renderShips(screenModel.getBoard1().getTab(), 1, 1);
        renderShips(screenModel.getBoard2().getTab(), 50, 1);
        hideComponent(screenModel.getLabels().get(0));
        renderComponent(screenModel.getLabels().get(0), true);
        try {
            screen.refresh();
        } catch (IOException ignored) {
        }
    }

    public void initRender(G_PlayModel screenModel, GameState yourTurn) {
        this.yourTurn = yourTurn;
        this.screenModel = screenModel;
        screen.clear();
        screenModel.getLabels().forEach(label -> renderComponent(label, true));
        renderBoard(1, 1);
        renderBoard(50, 1);
        renderShips(screenModel.getBoard1().getTab(), 1, 1);
        renderShips(screenModel.getBoard2().getTab(), 50, 1);
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
            for(int i=0; i<name[index].length(); i++) {
                if (name[index].charAt(i) == '¾') {
                    if (yourTurn == GameState.YOUR_TURN) {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(1+i)
                                        .withRelativeRow(1+index),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                    } else {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(1+i)
                                        .withRelativeRow(1+index),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                    }
                }
            }
        }
    }
    private void renderBoard(int col, int row) {
        TerminalPosition topLeftPos = new TerminalPosition(col, row);
        TerminalPosition topRightPos = new TerminalPosition(col+44, row);
        TerminalPosition botLeftPos = new TerminalPosition(col, row+22);
        TerminalPosition botRightPos = new TerminalPosition(col+44, row+22);
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
                textGraphics.setCharacter(topLeftPos
                                .withRelativeColumn(i*4+4)
                                .withRelativeRow(j*2+2),
                        Symbols.SINGLE_LINE_CROSS
                );
            }
        }
    }

    private void renderShips(byte[][] board, int col, int row) {
        TerminalPosition topLeftPos = new TerminalPosition(col, row);
        for (int i=0; i<10; i++) {
            for (int j=0; j<10; j++) {
                switch (board[i][j]) {
                    case -1 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter('•',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                    }
                    case 1-> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                    }
                    case 2 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.YELLOW));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter('/',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.YELLOW));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.YELLOW));
                    }
                    case 3 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter('X',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                    }
                    case 99 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter('•',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                    }
                    case 100 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                    }
                    case 101 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.GREEN));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                    }
                    case 102 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter('/',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.YELLOW));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                    }
                    case 103 -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter('X',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.RED));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.WHITE));
                    }
                    default -> {
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(5+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(6+j*4)
                                        .withRelativeRow(3+i*2),
                                new TextCharacter(' ',
                                        TextColor.ANSI.DEFAULT,
                                        TextColor.ANSI.BLACK));
                        screen.setCharacter(topLeftPos
                                        .withRelativeColumn(7+j*4)
                                        .withRelativeRow(3+i*2),
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
