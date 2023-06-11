package com.xwray.groupie.example.core.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.xwray.groupie.example.core.Prefs;
import com.xwray.groupie.example.core.R;
import java.util.Objects;

public class DebugItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint = new Paint();
    private final Prefs prefs;
    private final int leftColor;
    private final int topColor;
    private final int rightColor;
    private final int bottomColor;

    public DebugItemDecoration(Context context) {
        prefs = Prefs.get(context);
        leftColor = ContextCompat.getColor(context, R.color.red_200);
        topColor = ContextCompat.getColor(context, R.color.pink_200);
        rightColor = ContextCompat.getColor(context, R.color.purple_200);
        bottomColor = ContextCompat.getColor(context, R.color.indigo_200);
    }

    @Override
    public void onDrawOver(
            @NonNull Canvas c,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state
    ) {
        if (!(prefs.getShowBounds() || prefs.getShowOffsets())) return;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutManager lm = parent.getLayoutManager();
            Objects.requireNonNull(lm);
            int decoratedLeft = lm.getDecoratedLeft(child) + (int) child.getTranslationX();
            int decoratedTop = lm.getDecoratedTop(child) + (int) child.getTranslationY();
            int decoratedRight = lm.getDecoratedRight(child) + (int) child.getTranslationX();
            int decoratedBottom = lm.getDecoratedBottom(child) + (int) child.getTranslationY();

            int left = child.getLeft() + (int) child.getTranslationX();
            int top = child.getTop() + (int) child.getTranslationY();
            int right = child.getRight() + (int) child.getTranslationX();
            int bottom = child.getBottom() + (int) child.getTranslationY();

            if (prefs.getShowBounds()) {
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1);
                c.drawRect(decoratedLeft, decoratedTop, decoratedRight, decoratedBottom, paint);
            }

            if (prefs.getShowOffsets()) {
                paint.setStyle(Paint.Style.FILL);

                paint.setColor(leftColor);
                c.drawRect(decoratedLeft, decoratedTop, left, decoratedBottom, paint);

                paint.setColor(topColor);
                c.drawRect(decoratedLeft, decoratedTop, decoratedRight, top, paint);

                paint.setColor(rightColor);
                c.drawRect(right, decoratedTop, decoratedRight, decoratedBottom, paint);

                paint.setColor(bottomColor);
                c.drawRect(decoratedLeft, bottom, decoratedRight, decoratedBottom, paint);
            }
        }
    }
}
