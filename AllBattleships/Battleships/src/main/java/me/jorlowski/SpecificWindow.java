package me.jorlowski;

import com.googlecode.lanterna.screen.Screen;

public interface SpecificWindow {
    void close();
    KeyVar getInputKey();
    void refresh();
    void clear();
    boolean shouldResize();
    Object getSpecificWindow();
}
