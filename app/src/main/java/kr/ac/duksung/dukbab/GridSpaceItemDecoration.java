package kr.ac.duksung.dukbab;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public final class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int spanCount;
    private final int space;

    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % this.spanCount + 1;
        if (position >= this.spanCount) {
            outRect.top = this.space;
        }

        if (column != 1) {
            outRect.left = this.space;
        }

    }

    public GridSpaceItemDecoration(int spanCount, int space) {
        this.spanCount = spanCount;
        this.space = space;
    }
}