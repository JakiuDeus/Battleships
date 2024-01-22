package me.jorlowski.model.screens;

import me.jorlowski.State;
import me.jorlowski.model.components.Board;
import me.jorlowski.model.components.ComponentModel;
import me.jorlowski.model.components.M_ActionModel;
import me.jorlowski.model.components.M_LabelModel;

import java.util.Arrays;
import java.util.List;

public class G_BuildModel{
    private final List<M_ActionModel<Integer>> actions;
    private final List<M_LabelModel> labels;
    private final Board board;
    private int selectedAction;

    public G_BuildModel() {
        board = new Board();
        actions = Arrays.asList(
                new M_ActionModel<>(80, 1, "0: [][][][]", 0),
                new M_ActionModel<>(80, 4, "1: [][][]", 1),
                new M_ActionModel<>(80, 7, "2: [][][]", 2),
                new M_ActionModel<>(80, 10, "3: [][]", 3),
                new M_ActionModel<>(80, 13, "4: [][]", 4),
                new M_ActionModel<>(80, 16, "5: [][]", 5),
                new M_ActionModel<>(80, 19, "6: []", 6),
                new M_ActionModel<>(80, 22, "7: []", 7),
                new M_ActionModel<>(80, 25, "8: []", 8),
                new M_ActionModel<>(80, 28, "9: []", 9),
                new M_ActionModel<>(80, 37, "Start Game", 10),
                new M_ActionModel<>(80, 40, "Return", 11)
        );
        labels = List.of(
                new M_LabelModel(1, 24, 45, 3, "PG_Up and PG_Down to choose button"),
                new M_LabelModel(1, 27, 45, 3, "Enter to create or remove selected ship"),
                new M_LabelModel(1, 30, 45, 3, "Arrows to move selected ship"),
                new M_LabelModel(1, 33, 45, 3, "R to rotate ship (only if it has space)"),
                new M_LabelModel(1, 40, 16, 3, "Version 0.0")

        );
        selectedAction = 0;
    }
    public List<M_ActionModel<Integer>> getActions() {
        return actions;
    }

    public List<M_LabelModel> getLabels() {
        return labels;
    }

    public Board getBoard() {
        return board;
    }
    public M_ActionModel<Integer> getSelectedAction() {
        return actions.get(selectedAction);
    }

    public int getSelectedActionIndex() {
        return selectedAction;
    }

    public void setSelectedAction(int newSelection) {
        selectedAction = newSelection;
    }

    public ComponentModel getHintLabel() {
        return new M_LabelModel(1, 36, 30, 3, "Ships aren't placed properly");

    }
}
