package me.jorlowski.model.screens;

import me.jorlowski.State;
import me.jorlowski.model.components.M_ActionModel;
import me.jorlowski.model.components.M_LabelModel;

import java.util.List;

public interface ScreenModel{
    List<M_ActionModel<State>> getActions();
    List<M_LabelModel> getLabels();
    M_ActionModel<State> getSelectedAction();
    State getState();
    int getSelectedActionIndex();
    void setSelectedAction(int newSelection);
}
