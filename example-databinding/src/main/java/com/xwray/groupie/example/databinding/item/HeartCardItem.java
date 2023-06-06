package com.xwray.groupie.example.databinding.item;

import android.graphics.drawable.Animatable;
import androidx.annotation.NonNull;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.example.core.R;
import com.xwray.groupie.example.databinding.MainActivity;
import com.xwray.groupie.example.databinding.databinding.ItemHeartCardBinding;
import java.util.List;

public class HeartCardItem extends BindableItem<ItemHeartCardBinding> {

    public static final String FAVORITE = "FAVORITE";

    private final OnFavoriteListener onFavoriteListener;
    private boolean checked = false;
    private boolean inProgress = false;

    public HeartCardItem(long id, OnFavoriteListener onFavoriteListener) {
        super(id);
        this.onFavoriteListener = onFavoriteListener;
        getExtras().put(MainActivity.INSET_TYPE_KEY, MainActivity.INSET);
    }

    @Override
    public int getLayout() {
        return com.xwray.groupie.example.databinding.R.layout.item_heart_card;
    }

    @Override
    public void bind(@NonNull final ItemHeartCardBinding binding, int position) {
        //binding.getRoot().setBackgroundColor(colorRes);
        bindHeart(binding);
        binding.text.setText(String.valueOf(getId() + 1));

        binding.favorite.setOnClickListener(v -> {
            inProgress = true;
            animateProgress(binding);

            onFavoriteListener.onFavorite(HeartCardItem.this, !checked);
        });
    }

    private void bindHeart(ItemHeartCardBinding binding) {
        if (inProgress) {
            animateProgress(binding);
        } else {
            binding.favorite.setImageResource(R.drawable.favorite_state_list);
        }
        binding.favorite.setChecked(checked);
    }

    private void animateProgress(@NonNull ItemHeartCardBinding binding) {
        binding.favorite.setImageResource(R.drawable.avd_favorite_progress);
        ((Animatable) binding.favorite.getDrawable()).start();
    }

    public void setFavorite(boolean favorite) {
        inProgress = false;
        checked = favorite;
    }

    @Override
    public void bind(
            @NonNull ItemHeartCardBinding binding,
            int position,
            @NonNull List<Object> payloads
    ) {
        if (payloads.contains(FAVORITE)) {
            bindHeart(binding);
        } else {
            bind(binding, position);
        }
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    public interface OnFavoriteListener {
        void onFavorite(HeartCardItem item, boolean favorite);
    }
}
