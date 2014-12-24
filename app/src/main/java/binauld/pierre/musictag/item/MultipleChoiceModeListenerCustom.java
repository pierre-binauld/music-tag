package binauld.pierre.musictag.item;

import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;


/**
 * Created by Quentin on 24/12/2014.
 */
public abstract class MultipleChoiceModeListenerCustom implements AbsListView.MultiChoiceModeListener{
    private LibraryItemAdapter adapter;
    private ObservableListView listView;
    protected Activity activity;

    public MultipleChoiceModeListenerCustom(LibraryItemAdapter adapter, ObservableListView listView, Activity activity) {
        this.adapter = adapter;
        this.listView = listView;
        this.activity = activity;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Capture total checked items
        final int checkedCount = listView.getCheckedItemCount();
        // Set the CAB title according to total checked items
        mode.setTitle(checkedCount + " Selected");
        // Calls toggleSelection method from ListViewAdapter Class
        adapter.toggleSelection(position);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_main, menu);
        adapter.resetSelection();
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

}
