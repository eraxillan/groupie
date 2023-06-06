package com.xwray.groupie;

import static org.junit.Assert.assertEquals;

import androidx.annotation.NonNull;
import org.junit.Test;

public class ViewTypeTest {

    @Test
    public void noViewTypeOverrideReturnsLayout() {
        Item<GroupieViewHolder> item = new ItemWithoutViewTypeOverride(42);
        assertEquals(item.getViewType(), 42);
    }

    @Test
    public void viewTypeOverrideReturnsViewType() {
        Item<GroupieViewHolder> item = new ItemWithViewTypeOverride(42, 20);
        assertEquals(item.getViewType(), 20);
    }

    static class ItemWithoutViewTypeOverride extends Item<GroupieViewHolder> {
        private final int layout;

        ItemWithoutViewTypeOverride(int layout) {
            this.layout = layout;
        }

        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            return layout;
        }
    }

    static class ItemWithViewTypeOverride extends Item<GroupieViewHolder> {
        private final int layout;
        private final int viewType;

        ItemWithViewTypeOverride(int layout, int viewType) {
            this.layout = layout;
            this.viewType = viewType;
        }


        @Override
        public void bind(@NonNull GroupieViewHolder viewHolder, int position) {

        }

        @Override
        public int getViewType() {
            return viewType;
        }

        @Override
        public int getLayout() {
            return layout;
        }
    }
}
