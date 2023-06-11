package com.xwray.groupie.example.core;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int currentPage = 0;
    private final LinearLayoutManager linearLayoutManager;
    private final Runnable loadMore = () -> onLoadMore(currentPage);

    public InfiniteScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal || totalItemCount == 0) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        // End has been reached
        // The minimum amount of items to have below your current scroll position before loading more.
        int visibleThreshold = 5;
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            currentPage++;
            recyclerView.post(loadMore);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
