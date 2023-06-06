package com.xwray.groupie;

import android.view.View;
import androidx.annotation.NonNull;

public interface OnItemClickListener {
    void onItemClick(@NonNull Item<?> item, @NonNull View view);
}
