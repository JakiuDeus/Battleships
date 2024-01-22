package me.jorlowski.model.screens;

import me.jorlowski.State;
import me.jorlowski.model.components.M_ActionModel;
import me.jorlowski.model.components.M_LabelModel;

import java.util.Arrays;
import java.util.List;

public class M_MultiModel implements ScreenModel{
    private final List<M_ActionModel<State>> actions;
    private final List<M_LabelModel> labels;
    private int selectedAction;

    public M_MultiModel() {
        actions = Arrays.asList(
                new M_ActionModel<>(43, 20, "Create Lobby", State.CREATE_LOBBY),
                new M_ActionModel<>(43, 23, "Join Lobby", State.JOIN_LOBBY),
                new M_ActionModel<>(43, 26, "Return", State.MAIN)
        );
        labels = Arrays.asList(
                new M_LabelModel(1, 1, 100, 13, """
                        $$$$$$$\\              $$\\       $$\\     $$\\            $$$$$$\\  $$\\       $$\\
                        $$  __$$\\             $$ |      $$ |    $$ |          $$  __$$\\ $$ |      \\__|
                        $$ |  $$ | $$$$$$\\  $$$$$$\\   $$$$$$\\   $$ | $$$$$$\\  $$ /  \\__|$$$$$$$\\  $$\\  $$$$$$\\   $$$$$$$\\
                        $$$$$$$\\ | \\____$$\\ \\_$$  _|  \\_$$  _|  $$ |$$  __$$\\ \\$$$$$$\\  $$  __$$\\ $$ |$$  __$$\\ $$  _____|
                        $$  __$$\\  $$$$$$$ |  $$ |      $$ |    $$ |$$$$$$$$ | \\____$$\\ $$ |  $$ |$$ |$$ /  $$ |\\$$$$$$\\
                        $$ |  $$ |$$  __$$ |  $$ |$$\\   $$ |$$\\ $$ |$$   ____|$$\\   $$ |$$ |  $$ |$$ |$$ |  $$ | \\____$$\\
                        $$$$$$$  |\\$$$$$$$ |  \\$$$$  |  \\$$$$  |$$ |\\$$$$$$$\\ \\$$$$$$  |$$ |  $$ |$$ |$$$$$$$  |$$$$$$$  |
                        \\_______/  \\_______|   \\____/    \\____/ \\__| \\_______| \\______/ \\__|  \\__|\\__|$$  ____/ \\_______/
                                                                                                      $$ |
                                                                                                      $$ |
                                                                                                      \\__|
                        """),
                new M_LabelModel(1, 40, 16, 3, "Version 1.0")
        );
        selectedAction = 0;
    }

    public List<M_ActionModel<State>> getActions() {
        return actions;
    }
    public List<M_LabelModel> getLabels() {
        return labels;
    }
    public M_ActionModel<State> getSelectedAction() {
        return actions.get(selectedAction);
    }

    @Override
    public State getState() {
        return State.MULTI;
    }

    @Override
    public int getSelectedActionIndex() {
        return selectedAction;
    }
    public void setSelectedAction(int newSelection) {
        selectedAction = newSelection;
    }
}
