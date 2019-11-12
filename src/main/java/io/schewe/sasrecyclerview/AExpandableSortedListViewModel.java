package io.schewe.sasrecyclerview;

import android.view.View;

import androidx.databinding.BaseObservable;

public abstract class AExpandableSortedListViewModel extends BaseObservable implements IExpandableModel {

    private ExpansionHelper expansionHelper;

    @Override
    public void setExpansionHelper(ExpansionHelper helper) {
        expansionHelper = helper;
    }

    @Override
    public void switchExpansion(){ this.expansionHelper.switchExpansion(); this.notifyChange();}

    protected boolean isExpanded(){return expansionHelper.getExpansion();}

    @Override
    public int getVisibility() { return expansionHelper.getExpansion()? View.VISIBLE: View.GONE;}
}
