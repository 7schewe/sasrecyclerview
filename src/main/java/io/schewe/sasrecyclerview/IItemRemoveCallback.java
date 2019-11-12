package io.schewe.sasrecyclerview;


public interface IItemRemoveCallback<T extends ISortedListViewModel> {

    void onItemRemoved(T item);
    void onUndoItemRemoved(T item);
}