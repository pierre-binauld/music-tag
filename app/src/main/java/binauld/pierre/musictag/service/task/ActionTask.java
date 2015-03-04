package binauld.pierre.musictag.service.task;

import java.util.ArrayList;
import java.util.List;

public abstract class ActionTask<Action> extends Task {

    protected List<Action> actions = new ArrayList<>();

    public void addAction(Action action) {
        actions.add(action);
    }
}