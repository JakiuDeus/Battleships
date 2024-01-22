package me.jorlowski.view;

import me.jorlowski.model.screens.G_ScoreModel;

public interface G_ScoreRenderer {
    void initRender(G_ScoreModel screenModel, int phase);
    void render(int phase);
}
