package com.CSUF.EventFy.fragment;

import android.content.Context;
import android.os.AsyncTask;
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

import com.CSUF.Adapter.Attendance_tab_adapter;
import com.CSUF.EventFy.R;
import com.CSUF.EventFy.TestRecyclerViewAdapter;
import com.CSUF.EventFy_Beans.Comments;
import com.CSUF.EventFy_Beans.SignUp;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


public class Attendance_tab extends Fragment implements Paginate.Callbacks{

    private static final int GRID_SPAN = 3;
    //private RecyclerEventCommentAdapter adapter;
    private boolean loading = false;
    private int page = 0;
    private Handler handler;
    private Paginate paginate;
    List<Comments> lst;
    protected int threshold = 4;
    protected int totalPages = 5;
    protected boolean reverseLayout = false;
    protected boolean addLoadingRow = true;
    protected long networkDelay = 10;
    protected boolean customLoadingListItem = false;
    private TestRecyclerViewAdapter adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private int position;
    private   int ITEM_COUNT = 0;
    private String eventId;
    private List<SignUp> mContentItems;
    private Context context;
    private GetUsersForEvent getUsersForEvent;

    public static Attendance_tab newInstance(int count, Context context, int position, String eventId) {
        Attendance_tab r = new Attendance_tab();
        r.setITEM_COUNT(count);
         r.context = context;
        r.position = position;
        r.eventId = eventId;
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

            ITEM_COUNT=3;
            Log.e("count in ", "" + ITEM_COUNT);

      //  getUsersForEvent = new GetUsersForEvent(true);
      //  getUsersForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
           // getUsersForEvent = new GetUsersForEvent(true);
           // getUsersForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private class GetUsersForEvent extends AsyncTask<Void, Void, Void> {

        public GetUsersForEvent(String str) {

        }

        public GetUsersForEvent(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.get_user_for_event);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

            headers.add("Content-Type", "text/plain");

            Log.e("ing event id : ", ""+eventId);
            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<String> request = new HttpEntity<>(eventId, headers);


            ResponseEntity<SignUp []> response = restTemplate.exchange(url, HttpMethod.POST, request, SignUp[].class);

            SignUp [] signUp = response.getBody();

            mContentItems =  Arrays.asList(signUp);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if(mContentItems!=null) {
                Log.e("data loaded :::  ", ""+mContentItems.size());
               if(mAdapter==null) {
                   mAdapter = new RecyclerViewMaterialAdapter(new Attendance_tab_adapter(context, mContentItems, position));
                   mRecyclerView.setAdapter(mAdapter);
                   setupPagination();
               }
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}