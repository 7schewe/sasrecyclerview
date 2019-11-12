package io.schewe.sasrecyclerview;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ExpansionHelper extends BaseObservable {

    private ExpansionManager expansionManager;
    private int position;
    private long lastClick;

    public ExpansionHelper(ExpansionManager expansionManager, int position) {
        this.expansionManager = expansionManager;
        this.position = position;
    }

    @Bindable
    public boolean getExpansion() { return this.expansionManager.getExpandedPosition() == this.position; }

    public void switchExpansion() {
        long rightNow = System.currentTimeMillis();
        if(rightNow-lastClick<1000) return;
        if (this.expansionManager.getExpandedPosition() == position)    this.expansionManager.setExpandedPosition(-1);
        else                                                            this.expansionManager.setExpandedPosition(position);
        notifyPropertyChanged(BR.obj);
        lastClick = System.currentTimeMillis();
    }


}

