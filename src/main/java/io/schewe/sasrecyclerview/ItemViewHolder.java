package io.schewe.sasrecyclerview;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;

    ItemViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
    public void bind(Object obj) {
        binding.setVariable(BR.obj,obj);
        binding.executePendingBindings();
        binding.invalidateAll();
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}