package com.xwray.groupie.example.databinding;

import android.graphics.drawable.Animatable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.xwray.groupie.ExpandableGroup;
import com.xwray.groupie.ExpandableItem;
import com.xwray.groupie.example.core.R;
import com.xwray.groupie.example.databinding.databinding.ItemHeaderBinding;
import com.xwray.groupie.example.databinding.item.HeaderItem;

public class ExpandableHeaderItem extends HeaderItem implements ExpandableItem {

    private ExpandableGroup expandableGroup;

    public ExpandableHeaderItem(@StringRes int titleStringResId, @StringRes int subtitleResId) {
        super(titleStringResId, subtitleResId);
    }

    @Override
    public void bind(@NonNull final ItemHeaderBinding viewBinding, int position) {
        super.bind(viewBinding, position);

        // Initial icon state -- not animated.
        viewBinding.icon.setVisibility(View.VISIBLE);
        viewBinding.icon.setImageResource(expandableGroup.isExpanded() ?
                R.drawable.collapse : R.drawable.expand);
        viewBinding.icon.setOnClickListener(view -> {
            expandableGroup.onToggleExpanded();
            bindIcon(viewBinding);
        });
    }

    private void bindIcon(ItemHeaderBinding viewBinding) {
        viewBinding.icon.setVisibility(View.VISIBLE);
        viewBinding.icon.setImageResource(expandableGroup.isExpanded() ?
                R.drawable.collapse_animated : R.drawable.expand_animated);
        Animatable drawable = (Animatable) viewBinding.icon.getDrawable();
        drawable.start();
    }

    @Override
    public void setExpandableGroup(@NonNull ExpandableGroup onToggleListener) {
        this.expandableGroup = onToggleListener;
    }
}
