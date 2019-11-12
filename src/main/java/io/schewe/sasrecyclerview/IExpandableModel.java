package io.schewe.sasrecyclerview;

import androidx.databinding.Observable;

interface IExpandableModel extends Observable, ISortedListViewModel {
    void setExpansionHelper(ExpansionHelper helper);

    void switchExpansion();

    int getVisibility();
}
