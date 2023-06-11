package com.xwray.groupie.example.core.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xwray.groupie.GroupieViewHolder;
import java.util.Objects;

/**
 * An ItemDecoration which applies an even visual padding on the left and right edges of a grid and
 * between each item, while also applying an even amount of inset to each item.  This ensures that
 * all items remain the same size.
 * <p>
 * It assumes all items in a row have the same span size, and it assumes it's the only item
 * decorator.
 */
public class InsetItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int padding;
    private final String key;
    private final String value;

    public InsetItemDecoration(
            @ColorInt int backgroundColor,
            @Dimension int padding,
            String key,
            String value
    ) {
        this.key = key;
        this.value = value;
        paint = new Paint();
        paint.setColor(backgroundColor);
        this.padding = padding;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isInset(View view, @NonNull RecyclerView parent) {
        GroupieViewHolder viewHolder = (GroupieViewHolder) parent.getChildViewHolder(view);
        if (viewHolder.getExtras().containsKey(key)) {
            return Objects.equals(viewHolder.getExtras().get(key), value);
        } else {
            return false;
        }
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        if (!isInset(view, parent)) return;

        GridLayoutManager.LayoutParams layoutParams =
                (GridLayoutManager.LayoutParams) view.getLayoutParams();
        GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        Objects.requireNonNull(gridLayoutManager);
        float spanSize = layoutParams.getSpanSize();
        float totalSpanSize = gridLayoutManager.getSpanCount();

        float n = totalSpanSize / spanSize; // num columns
        float c = layoutParams.getSpanIndex() / spanSize; // column index

        float leftPadding = padding * ((n - c) / n);
        float rightPadding = padding * ((c + 1) / n);

        outRect.left = (int) leftPadding;
        outRect.right = (int) rightPadding;
        outRect.bottom = padding;
    }

    @Override
    public void onDraw(
            @NonNull Canvas c,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        Objects.requireNonNull(lm);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (!isInset(child, parent)) continue;
            if (child.getTranslationX() != 0 || child.getTranslationY() != 0) {
                c.drawRect(
                        lm.getDecoratedLeft(child),
                        lm.getDecoratedTop(child),
                        lm.getDecoratedRight(child),
                        lm.getDecoratedBottom(child),
                        paint
                );
                continue;
            }

            boolean isLast = i == childCount - 1;
            float top = child.getTop() + child.getTranslationY();
            float bottom = child.getBottom() + child.getTranslationY();

            // Left border
            c.drawRect(
                    lm.getDecoratedLeft(child) + child.getTranslationX(),
                    top,
                    child.getLeft() + child.getTranslationX(),
                    bottom,
                    paint
            );

            float right = lm.getDecoratedRight(child) + child.getTranslationX();
            if (isLast) {
                right = Math.max(right, parent.getWidth());
            }

            // Right border
            c.drawRect(
                    child.getRight() + child.getTranslationX(),
                    top,
                    right,
                    bottom,
                    paint
            );

            // Bottom border
            c.drawRect(
                    lm.getDecoratedLeft(child) + child.getTranslationY(),
                    bottom,
                    right,
                    lm.getDecoratedBottom(child) + child.getTranslationY(),
                    paint
            );
        }
    }
}
