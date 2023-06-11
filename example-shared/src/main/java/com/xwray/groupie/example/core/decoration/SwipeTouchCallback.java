package com.xwray.groupie.example.core.decoration;

import android.graphics.Canvas;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.xwray.groupie.TouchCallback;

public abstract class SwipeTouchCallback extends TouchCallback {
    public SwipeTouchCallback() {
        super();
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        return false;
    }


    @Override
    public void onChildDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            float dX,
            float dY,
            int actionState,
            boolean isCurrentlyActive
    ) {
        if (ItemTouchHelper.ACTION_STATE_SWIPE == actionState) {
            View child = viewHolder.itemView;

            // Fade out the item
            child.setAlpha(1 - (Math.abs(dX) / (float) child.getWidth()));
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
