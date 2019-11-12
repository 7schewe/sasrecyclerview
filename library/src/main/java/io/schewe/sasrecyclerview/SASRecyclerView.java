package io.schewe.sasrecyclerview;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;


public class SASRecyclerView<T extends ISortedListViewModel> implements SearchView.OnQueryTextListener {


    private SortedRecyclerViewAdapter<T> adapter;
    private RecyclerView recyclerView;
    private String lastQuery;

    public SASRecyclerView(RecyclerView recyclerView, Class<T> t, int itemLayoutID, Comparator<ISortedListViewModel> comparator) {
        this.recyclerView = recyclerView;
        this.lastQuery = "";
        recyclerView.setHasFixedSize(true);
        adapter = new SortedRecyclerViewAdapter<>(t, itemLayoutID, comparator);
        RecyclerView.LayoutManager layoutManager;
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void activateSwipeToDelete(IItemRemoveCallback<T> callback){
        if (callback != null) {
            SwipeItemHelper<T> swipeItemHelper = new SwipeItemHelper<>(adapter, callback);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeItemHelper);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(this.lastQuery.length()>query.length()) adapter.resetFilter();
        filter(query);
        recyclerView.scrollToPosition(0);
        this.lastQuery = query;
        return true;
    }

    private void filter(String query) {
        final String lowerCaseQuery = query.toLowerCase();
        for (T model : adapter.getItems()) {
            if (!model.getFilterString().toLowerCase().contains(lowerCaseQuery)) {
                adapter.filterItemOut(model);
            }
        }
    }

    public static final Comparator<ISortedListViewModel> ALPHABETICAL_COMPARATOR = (a, b) -> a.getSortingString().compareTo(b.getSortingString());

    public static final Comparator<ISortedListViewModel> DATE_COMPARATOR = (a, b) -> {
        long aDate = Long.parseLong(a.getSortingString());
        long bDate = Long.parseLong(b.getSortingString());
        if(aDate==bDate && a.getId() > b.getId())           return -1;
        else if(aDate==bDate && a.getId() < b.getId())      return 1;
        else if(aDate==bDate && a.getId() == b.getId())     return 0;
        else if(aDate==0)                                   return -1;
        else if(bDate==0)                                   return 1;
        else if(aDate>bDate)                                return -1;
        else                                                return 1;
    };

    public SortedRecyclerViewAdapter<T> getAdapter(){
        return this.adapter;
    }
}
