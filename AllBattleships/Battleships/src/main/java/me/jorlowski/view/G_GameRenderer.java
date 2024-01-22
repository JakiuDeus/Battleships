package me.jorlowski.view;

import me.jorlowski.GameState;
import me.jorlowski.model.screens.G_PlayModel;

public interface G_GameRenderer {
    void initRender(G_PlayModel screenModel, GameState yourTurn);
    void render(GameState yourTurn);
}
