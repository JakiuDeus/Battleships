package me.jorlowski;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class LanternaWindow implements SpecificWindow {

    private Screen screen;
    public LanternaWindow() {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(new TerminalSize(102, 44));
        try {
            Terminal terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
            screen.setCursorPosition(null);
        } catch (IOException ignored) {
        }
    }

    @Override
    public void close() {
        try {
            screen.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public KeyVar getInputKey() {
        try {
            KeyStroke key = screen.pollInput();
            if (key == null) {
                return null;
            }
            switch (key.getKeyType()) {
                case EOF -> {
                    return KeyVar.EXIT;
                }
                case ArrowUp -> {
                    return KeyVar.ARROW_UP;
                }
                case ArrowDown -> {
                    return KeyVar.ARROW_DOWN;
                }
                case ArrowLeft -> {
                    return KeyVar.ARROW_LEFT;
                }
                case ArrowRight -> {
                    return KeyVar.ARROW_RIGHT;
                }
                case Enter -> {
                    return KeyVar.ENTER;
                }
                case PageUp -> {
                    return KeyVar.PAGE_UP;
                }
                case PageDown -> {
                    return KeyVar.PAGE_DOWN;
                }
                case Character -> {
                    if (key.getCharacter() == 'r') {
                        return KeyVar.R_KEY;
                    }
                    return null;
                }
                default -> {
                    return null;
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    @Override
    public void refresh() {
        try {
            screen.refresh();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void clear() {
        screen.clear();
    }

    @Override
    public boolean shouldResize() {
        return screen.doResizeIfNecessary() != null;
    }

    @Override
    public Object getSpecificWindow() {
        return screen;
    }
}
