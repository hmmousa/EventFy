package com.CSUF.EventFy;

/**
 * Created by swapnil on 4/25/16.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;

public class RecyclerViewExampleActivity extends Fragment implements Paginate.Callbacks {

    private static final int GRID_SPAN = 3;

    private RecyclerView recyclerView;
    private boolean loading = false;
    private int page = 0;
    private Handler handler;
    private Paginate paginate;
    protected int threshold = 4;
    protected int totalPages = 3;
    protected int itemsPerPage = 10;
    protected boolean reverseLayout = false;
    protected boolean addLoadingRow = true;
    protected long networkDelay = 1000;
    protected boolean customLoadingListItem = false;

    public static RecyclerViewExampleActivity newInstance(int count) {
        RecyclerViewExampleActivity r = new RecyclerViewExampleActivity();

        return r;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        handler = new Handler();

        setupPagination();
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    protected void setupPagination() {
        // If RecyclerView was recently bound, unbind
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
       // adapter = new RecyclerEventCommentAdapter(lst);
        loading = false;
        page = 0;
        int layoutOrientation;
        layoutOrientation = OrientationHelper.VERTICAL;


        RecyclerView.LayoutManager layoutManager = null;
        layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);

        ((LinearLayoutManager) layoutManager).setReverseLayout(reverseLayout);


        recyclerView.setLayoutManager(layoutManager);
       // recyclerView.setItemAnimator(new SlideInUpAnimator());
        //recyclerView.setAdapter(adapter);

        paginate = Paginate.with(recyclerView, this)
                .setLoadingTriggerThreshold(threshold)
                .addLoadingListItem(addLoadingRow)
                .setLoadingListItemCreator(customLoadingListItem ? new CustomLoadingListItemCreator() : null)
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return GRID_SPAN;
                    }
                })
                .build();
    }

    @Override
    public synchronized void onLoadMore() {
        Log.d("Paginate", "onLoadMore");
        loading = true;
        // Fake asynchronous loading that will generate page of random data after some delay
        handler.postDelayed(fakeCallback, networkDelay);
    }

    @Override
    public synchronized boolean isLoading() {
        return loading; // Return boolean weather data is already loading or not
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page == totalPages; // If all pages are loaded return true
    }

    private Runnable fakeCallback = new Runnable() {
        @Override
        public void run() {
            page++;

            //adapter.add(lst);
            loading = false;
        }
    };

    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.custom_loading_list_item, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            //vh.tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", m.getItemCount()));

            // This is how you can make full span if you are using StaggeredGridLayoutManager
            if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvLoading;

        public VH(View itemView) {
            super(itemView);
            //tvLoading = (TextView) itemView.findViewById(R.id.tv_loading_text);
        }
    }

}