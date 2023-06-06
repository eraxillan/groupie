package com.xwray.groupie;

import android.view.View;
import androidx.annotation.NonNull;

public interface OnItemLongClickListener {
    boolean onItemLongClick(@NonNull Item<?> item, @NonNull View view);
}
