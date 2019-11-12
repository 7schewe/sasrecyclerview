package io.schewe.sasrecyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SortedRecyclerViewAdapter<T extends ISortedListViewModel> extends RecyclerView.Adapter<ItemViewHolder> {

    private SortedList<T> sortedList;
    private final int itemLayoutId;
    private List<T> filteredOutList;

    private void initSortedList(Class<T> typeParameterClass){
     sortedList = new SortedList<>(typeParameterClass, new SortedList.Callback<T>() {
        @Override
        public int compare(T a, T b) {
            return comparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(T oldItem, T newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(T item1, T item2) {
            return item1 == item2;
        }
    });
    }
    private final Comparator<ISortedListViewModel> comparator;
    private ExpansionManager expansionManager;

    SortedRecyclerViewAdapter(Class<T> typeParameterClass, int itemLayoutId, Comparator<ISortedListViewModel> comparator) {
        this.itemLayoutId = itemLayoutId;
        this.comparator = comparator;
        this.filteredOutList = new LinkedList<>();
        this.initSortedList(typeParameterClass);
        this.setHasStableIds(true);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.expansionManager = new ExpansionManager(recyclerView);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, itemLayoutId,parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final T model = sortedList.get(position);
        ExpansionHelper expansionHelper = new ExpansionHelper(expansionManager, position);
        if(model instanceof IExpandableModel) ((IExpandableModel) model).setExpansionHelper(expansionHelper);
        holder.bind(model);
    }

    public void addItem(T model) {
        sortedList.add(model);
    }

    public void removeItem(T model) {
        sortedList.remove(model);
    }

    public void addItems(List<T> models) {
        sortedList.addAll(models);
    }

    public T getItem(int position){
        return sortedList.get(position);
    }

    public List<T> getItems(){
        List<T> allItems = new LinkedList<>();
        for(int i=0; i<sortedList.size(); i++) allItems.add(sortedList.get(i));
        return new LinkedList<>(allItems);
    }

    public void removeItems(List<T> models) {
        sortedList.beginBatchedUpdates();
        for (T model : models) {
            this.removeItem(model);
        }
        sortedList.endBatchedUpdates();
    }

    public void replaceAllItems(List<T> models) {
        sortedList.beginBatchedUpdates();
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final T model = sortedList.get(i);
            if (!models.contains(model)) {
                this.removeItem(model);
            }
        }
        sortedList.addAll(models);
        sortedList.endBatchedUpdates();
    }

    public void filterItemOut(T model){
        this.filteredOutList.add(model);
        this.sortedList.remove(model);
    }

    public void resetFilter(){
        this.sortedList.addAll(this.filteredOutList);
        this.filteredOutList.clear();
    }

    public int getItemPosition(T item){
        return this.sortedList.indexOf(item);
    }

    public void expandItem(T item){
        expansionManager.setExpandedPosition(this.getItemPosition(item));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    @Override
    public long getItemId(int position) {
        return sortedList.get(position).getId();
    }
}