package com.xwray.groupie.example.core.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;

public class HeaderItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int sidePaddingPixels;
    private final int headerViewType;

    public HeaderItemDecoration(
            @ColorInt int background,
            int sidePaddingPixels,
            @LayoutRes int headerViewType
    ) {
        this.sidePaddingPixels = sidePaddingPixels;
        this.headerViewType = headerViewType;
        paint = new Paint();
        paint.setColor(background);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isHeader(View child, @NonNull RecyclerView parent) {
        var lm = parent.getLayoutManager();
        Objects.requireNonNull(lm);
        int viewType = lm.getItemViewType(child);
        return viewType == headerViewType;
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        if (!isHeader(view, parent)) return;

        outRect.left = sidePaddingPixels;
        outRect.right = sidePaddingPixels;
    }

    @Override
    public void onDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (!isHeader(child, parent)) continue;

            RecyclerView.LayoutManager lm = parent.getLayoutManager();
            Objects.requireNonNull(lm);

            float top = lm.getDecoratedTop(child) + child.getTranslationY();
            float bottom = lm.getDecoratedBottom(child) + child.getTranslationY();
            if (i == parent.getChildCount() - 1) {
                // Draw to bottom if last item
                bottom = Math.max(parent.getHeight(), bottom);
            }
            float right = lm.getDecoratedRight(child) + child.getTranslationX();
            float left = lm.getDecoratedLeft(child) + child.getTranslationX();
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}
