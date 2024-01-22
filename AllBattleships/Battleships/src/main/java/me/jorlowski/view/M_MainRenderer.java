package me.jorlowski.view;

import me.jorlowski.model.screens.ScreenModel;

public interface M_MainRenderer {
    void render(int lastActionIndex, int currentActionIndex);
    void initRender(ScreenModel screenModel);
}
