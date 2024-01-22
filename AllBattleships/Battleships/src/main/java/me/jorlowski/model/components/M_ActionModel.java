package me.jorlowski.model.components;

import me.jorlowski.State;

public class M_ActionModel<T> extends ComponentModel {

    private final T onClickAction;
    public M_ActionModel(int x, int y, String name, T action) {
        super(x, y, 16, 3, name);
        this.onClickAction = action;
    }

    public T onClick() {
        return onClickAction;
    }
}
