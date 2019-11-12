package io.schewe.sasrecyclerview;

import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;


public class SwipeItemHelper<T extends ISortedListViewModel> extends ItemTouchHelper.SimpleCallback {

    private SortedRecyclerViewAdapter<T> adapter;
    private IItemRemoveCallback<T> callback;

    SwipeItemHelper(SortedRecyclerViewAdapter<T> adapter, IItemRemoveCallback<T> callback) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.callback = callback;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
        int swipedPosition = viewHolder.getAdapterPosition();
        final T removedItem = adapter.getItem(swipedPosition);
        adapter.removeItem(removedItem);
        callback.onItemRemoved(removedItem);
        final View parent = (View) viewHolder.itemView.getParent();
        final Snackbar snackbar = Snackbar.make(parent, R.string.item_removed, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.undo_button, view -> {
            adapter.addItem(removedItem);
            callback.onUndoItemRemoved(removedItem);
            snackbar.dismiss();
            Snackbar snackbar1 = Snackbar.make(parent, R.string.undo_item_removed, Snackbar.LENGTH_SHORT);
            snackbar1.show();
        });
        snackbar.show();
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        boolean removable;
        int adapterPosition = viewHolder.getAdapterPosition();
        removable = adapter.getItem(adapterPosition).isRemovable();
        return removable ? super.getSwipeDirs(recyclerView, viewHolder) : 0;
    }
}

