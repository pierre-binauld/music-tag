package binauld.pierre.musictag.listener;

import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import binauld.pierre.musictag.R;
import binauld.pierre.musictag.activities.MainActivity;
import binauld.pierre.musictag.adapter.LibraryItemAdapter;
import binauld.pierre.musictag.item.LibraryItem;


public class LibraryItemMultiChoiceMode implements AbsListView.MultiChoiceModeListener {
    private LibraryItemAdapter adapter;
    private ListView listView;
    protected MainActivity activity;
    private SparseArray<LibraryItem> selectedItems;

    public LibraryItemMultiChoiceMode(LibraryItemAdapter adapter, ListView listView, MainActivity activity) {
        this.adapter = adapter;
        this.listView = listView;
        this.activity = activity;
        this.selectedItems = new SparseArray<>();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Set the CAB title according to total checked items
        //TODO: Magic String !
        final int checkedCount = listView.getCheckedItemCount();
        mode.setTitle(checkedCount + " Selected");

//      Calls toggleSelection method from ListViewAdapter Class
        toggleSelection(position, checked);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_cab_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                activity.callTagFormActivity(getLibraryItems());
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        clearSelection();
    }

    public void toggleSelection(int position, boolean checked) {
        if (checked) {
            LibraryItem item = (LibraryItem) adapter.getItem(position);
            selectedItems.put(position, item);
        } else {
            selectedItems.remove(position);
        }
    }

    private List<LibraryItem> getLibraryItems() {
        List<LibraryItem> items = new ArrayList<>();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

        int size = checkedItemPositions.size();
        for (int i = 0; i < size; i++) {
            int position = checkedItemPositions.keyAt(i);
            boolean isChecked = checkedItemPositions.get(position);
            if(isChecked) {
                items.add((LibraryItem) adapter.getItem(position));
            }
        }

        return items;
    }

    public void clearSelection() {
        selectedItems.clear();
    }
}
