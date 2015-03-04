package binauld.pierre.musictag.service.task;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import binauld.pierre.musictag.composite.LibraryComponent;
import binauld.pierre.musictag.composite.LibraryComposite;
import binauld.pierre.musictag.composite.LibraryLeaf;
import binauld.pierre.musictag.visitor.ComponentVisitor;
import binauld.pierre.musictag.visitor.ItemVisitor;

public class BreadthFirstTask extends Task implements ComponentVisitor {

    private List<ItemVisitor> actions = new ArrayList<>();
    private Queue<LibraryComponent> queue = new LinkedList<>();

    public BreadthFirstTask(LibraryComponent composite) {

        this.queue.add(composite);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!queue.isEmpty()) {
            queue.poll().accept(this);
            publishProgress();
        }

        return null;
    }

    @Override
    public void visit(LibraryLeaf leaf) {
        for (ItemVisitor action : actions) {
            leaf.getItem().accept(action);
        }

    }

    @Override
    public void visit(LibraryComposite composite) {
        for (ItemVisitor action : actions) {
            composite.getItem().accept(action);
        }

        queue.addAll(composite.getChildren());
    }

    public void addAction(ItemVisitor action) {
        actions.add(action);
    }
}
