package com.xwray.groupie.example.databinding.item;

import static com.xwray.groupie.example.databinding.MainActivity.INSET;
import static com.xwray.groupie.example.databinding.MainActivity.INSET_TYPE_KEY;

import androidx.annotation.NonNull;
import com.xwray.groupie.databinding.BindableItem;
import com.xwray.groupie.example.databinding.R;
import com.xwray.groupie.example.databinding.databinding.ItemCardBinding;

public class CardItem extends BindableItem<ItemCardBinding> {

    private final CharSequence text;

    public CardItem() {
        this("");
    }

    public CardItem(CharSequence text) {
        this.text = text;
        getExtras().put(INSET_TYPE_KEY, INSET);
    }

    @Override
    public int getLayout() {
        return R.layout.item_card;
    }

    @Override
    public void bind(@NonNull ItemCardBinding viewBinding, int position) {
        viewBinding.text.setText(text);
    }

    public final CharSequence getText() {
        return text;
    }
}
