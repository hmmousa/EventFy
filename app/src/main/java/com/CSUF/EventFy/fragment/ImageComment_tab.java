package com.CSUF.EventFy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.CSUF.Adapter.ImageComment_tab_adapter;
import com.CSUF.EventFy.R;
import com.CSUF.EventFy.TestRecyclerViewAdapter;
import com.CSUF.EventFy_Beans.Comments;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ImageComment_tab extends Fragment implements Paginate.Callbacks{

    private static final int GRID_SPAN = 3;
    //private RecyclerEventCommentAdapter adapter;
    private boolean loading = false;
    private int page = 0;
    private Handler handler;
    private Paginate paginate;
    protected int threshold = 4;
    protected int totalPages = 5;
    protected int itemsPerPage = 3;
    protected boolean reverseLayout = false;
    protected boolean addLoadingRow = true;
    protected long networkDelay = 2000;
    protected boolean customLoadingListItem = false;
    private TestRecyclerViewAdapter adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int position;
    private   int ITEM_COUNT = 0;

    private List<Comments> mContentItems = new ArrayList<>();
    private Context context;

    public static ImageComment_tab newInstance(int count, Context context, int position) {
        ImageComment_tab r = new ImageComment_tab();
        r.setITEM_COUNT(count);
        r.context = context;
        r.position = position;
        return r;
    }
    public void setITEM_COUNT(int count)
    {
        this.ITEM_COUNT = count;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        handler = new Handler();


            Log.e("count in ", "" + ITEM_COUNT);

            for (int i = 0; i < ITEM_COUNT; ++i)
            {

                Comments c = new Comments();
                c.setIsImage("true");
                mContentItems.add(c);
                Log.e("item len ", ""+mContentItems.size() );
            }

        mAdapter = new RecyclerViewMaterialAdapter(new ImageComment_tab_adapter(context, mContentItems, position));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        setupPagination();
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

    }


    protected void setupPagination() {
        // If RecyclerView was recently bound, unbind
        if (paginate != null) {
            paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        //adapter = new (lst);
        loading = false;
        page = 0;
        int layoutOrientation;
        layoutOrientation = OrientationHelper.VERTICAL;


        RecyclerView.LayoutManager layoutManager = null;
        layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);

        ((LinearLayoutManager) layoutManager).setReverseLayout(reverseLayout);


        paginate = Paginate.with(mRecyclerView, this)
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
        Log.e("in paginate :::: ", ""+page);
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
            for (int i = 0; i < ITEM_COUNT; i++) {
                Comments c = new Comments();
                c.setUserName("abc " + i);
                c.setIsImage("false");
                c.setCommentText("text "+i);
                Log.e("adding element : ", "****");
                mContentItems.add(c);
                mAdapter.notifyDataSetChanged();
            }
            loading = false;
        }
    };

    private class CustomLoadingListItemCreator implements LoadingListItemCreator {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.tab_text_comments, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh = (ViewHolder) holder;
            //   vh.tvLoading.setText(String.format("Total items loaded: %d.\nLoading more...", mAdapter.getItemCount()));

            vh.tv_android.setText("xyz");
            Picasso.with(context).load("http://api.learn2crack.com/android/images/donut.png").resize(120, 60).into(vh.img_android);

            // This is how you can make full span if you are using StaggeredGridLayoutManager
            if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_android;
        ImageView img_android;
        public ViewHolder(View view) {
            super(view);

            //  tv_android = (TextView)view.findViewById(R.id.tv_android);
            //  img_android = (ImageView)view.findViewById(R.id.img_android);
        }
    }
}