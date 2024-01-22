package me.jorlowski.view;

import me.jorlowski.model.screens.G_BuildModel;

public interface G_GameBuildRenderer {
    void initRender(G_BuildModel screenModel, boolean hintLabel);
    void render(int lastActionIndex, int currentActionIndex, boolean hintLabel);
}
