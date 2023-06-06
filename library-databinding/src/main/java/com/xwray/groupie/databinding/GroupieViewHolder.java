package com.xwray.groupie.databinding;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public class GroupieViewHolder<T extends ViewDataBinding> extends com.xwray.groupie.GroupieViewHolder {
    public final T binding;

    public GroupieViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
