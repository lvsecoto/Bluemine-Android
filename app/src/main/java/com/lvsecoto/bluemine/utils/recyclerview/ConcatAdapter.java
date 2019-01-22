package com.lvsecoto.bluemine.utils.recyclerview;

import android.util.SparseArray;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 包装类, 用于连接多个Adapter
 */
public class ConcatAdapter extends Adapter {

    private final ArrayList<Adapter> mChildAdapters;

    /**
     * When {@link #onCreateViewHolder(ViewGroup, int)} we only know the item view type,
     * but we don't know which adapter the view type belong to.
     * So we make a map to record their relation when {@link #getItemViewType(int)}
     */
    private final SparseArray<Adapter> mViewTypeToChildAdapter = new SparseArray<>();

    public ConcatAdapter(Adapter... adapters) {
        mChildAdapters = new ArrayList<>(Arrays.asList(adapters));

        for (Adapter adapter : mChildAdapters) {
            adapter.registerAdapterDataObserver(new InnerAdapterObserver(adapter));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return findChildAdapterByViewType(viewType).onCreateViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        findChildAdapterByPosition(position).onBindViewHolder(holder, findChildPositionByPosition(position));
    }

    @Override
    public int getItemViewType(int position) {
        Adapter adapter = findChildAdapterByPosition(position);
        int itemViewType = adapter.getItemViewType(findChildPositionByPosition(position));
        mViewTypeToChildAdapter.put(itemViewType, adapter);
        return itemViewType;
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (Adapter adapter : mChildAdapters) {
            itemCount += adapter.getItemCount();
        }
        return itemCount;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        for (Adapter adapter : mChildAdapters) {
            adapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        for (Adapter adapter : mChildAdapters) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    private Adapter findChildAdapterByViewType(int viewType) {
        Adapter adapter = mViewTypeToChildAdapter.get(viewType);
        if (adapter == null) {
            throw new IllegalArgumentException("no adapter match this viewType");
        }
        return adapter;
    }

    @NonNull
    private Adapter findChildAdapterByPosition(int position) {
        return mChildAdapters.get(findChildAdapterIndexByPosition(position));
    }

    private int findChildPositionByPosition(int position) {
        int thisAdapterStartAt = 0;
        for (Adapter adapter : mChildAdapters) {
            if (position < thisAdapterStartAt + adapter.getItemCount()) {
                return position - thisAdapterStartAt;
            }
            thisAdapterStartAt += adapter.getItemCount();
        }

        throw new IllegalArgumentException("no adapter match this position");
    }

    private int findChildAdapterIndexByPosition(int position) {
        int nextAdapterStart = 0;
        for (int i = 0; i < mChildAdapters.size(); i++) {
            Adapter adapter = mChildAdapters.get(i);
            nextAdapterStart += adapter.getItemCount();
            if (position < nextAdapterStart) {
                return i;
            }
        }

        throw new IllegalArgumentException("no adapter match this position");
    }

    private int getChildAdapterStartPosition(Adapter adapter) {
        int position = 0;
        for (Adapter a : mChildAdapters) {
            if (a == adapter) {
                return position;
            }
            position += a.getItemCount();
        }
        return position;
    }

    public PositionFilter getFilter(Integer[] thisFilter, PositionFilter... subFilters) {
        return new ConcatPositionFilter(thisFilter, subFilters);
    }

    private class InnerAdapterObserver extends RecyclerView.AdapterDataObserver {

        private Adapter mAdapter;

        InnerAdapterObserver(Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(getChildAdapterStartPosition(mAdapter) + positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(getChildAdapterStartPosition(mAdapter) + positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(getChildAdapterStartPosition(mAdapter) + positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            int adapterStartPosition = getChildAdapterStartPosition(mAdapter);
            notifyItemMoved(adapterStartPosition + fromPosition, adapterStartPosition + toPosition);
        }
    }

    public class ConcatPositionFilter
            implements PositionFilter {

        private final ArrayList<PositionFilter> mSubFilter = new ArrayList<>(mChildAdapters.size());

        private ArrayList<Integer> mThisFilter = new ArrayList<>(mChildAdapters.size());

        ConcatPositionFilter(Integer[] thisFilter, PositionFilter... subFilters) {
            Collections.addAll(mThisFilter, thisFilter);
            Collections.addAll(mSubFilter, subFilters);
        }

        @Override
        public boolean isTrue(int position) {
            int index = findChildAdapterIndexByPosition(position);
            if (filterByThis(index)) {
                return true;
            } else if (filterBySub(index, position)) {
                return true;
            } else {
                return false;
            }
        }

        private boolean filterByThis(int index) {
            return mThisFilter.contains(index);
        }

        private boolean filterBySub(int index, int position) {
            PositionFilter subFilter;
            if (index >= mSubFilter.size()) {
                return false;
            }

            subFilter = mSubFilter.get(index);
            return subFilter != null
                    && subFilter.isTrue(findChildPositionByPosition(position));
        }
    }

    public interface PositionFilter {
        boolean isTrue(int position);
    }
}
