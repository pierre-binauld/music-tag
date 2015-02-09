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
import binauld.pierre.musictag.adapter.LibraryComponentAdapter;
import binauld.pierre.musictag.composite.LibraryComponent;


public class ItemMultiChoiceMode implements AbsListView.MultiChoiceModeListener {
    private LibraryComponentAdapter adapter;
    private ListView listView;
    private MainActivity activity;
    private SparseArray<LibraryComponent> selectedItems;

    private String selectedItemString;
    private String selectedItemsString;

    public ItemMultiChoiceMode(LibraryComponentAdapter adapter, ListView listView, MainActivity activity) {
        this.adapter = adapter;
        this.listView = listView;
        this.activity = activity;
        this.selectedItems = new SparseArray<>();
        selectedItemString = activity.getResources().getString(R.string.selected_item);
        selectedItemsString = activity.getResources().getString(R.string.selected_items);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Set the CAB title according to total checked items
        final int checkedCount = listView.getCheckedItemCount();
        if (checkedCount > 1) {
            mode.setTitle(checkedCount + " " + selectedItemsString);
        } else {
            mode.setTitle(checkedCount + " " + selectedItemString);
        }

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
            case R.id.action_organisation:
                activity.callOrganisationActivity(getLibraryItems());
                return true;
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
            LibraryComponent item = (LibraryComponent) adapter.getItem(position);
            selectedItems.put(position, item);
        } else {
            selectedItems.remove(position);
        }
    }

    private List<LibraryComponent> getLibraryItems() {
        List<LibraryComponent> items = new ArrayList<>();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

        int size = checkedItemPositions.size();
        for (int i = 0; i < size; i++) {
            int position = checkedItemPositions.keyAt(i);
            boolean isChecked = checkedItemPositions.get(position);
            if(isChecked) {
                items.add((LibraryComponent) adapter.getItem(position));
            }
        }

        return items;
    }

    public void clearSelection() {
        selectedItems.clear();
    }
}
