package io.schewe.sasrecyclerview;

import android.transition.Fade;
import android.transition.TransitionManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

class ExpansionManager {

    private int expandedPosition = -1;
    private RecyclerView recyclerView;

    ExpansionManager(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    int getExpandedPosition() {
        return expandedPosition;
    }

    void setExpandedPosition(int expandedPosition) {
        int oldPosition = this.expandedPosition;
        if(oldPosition!=expandedPosition) {
            this.expandedPosition = expandedPosition;
            TransitionManager.beginDelayedTransition(recyclerView, new Fade());
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(oldPosition);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(expandedPosition);
        }
    }
}