package com.CSUF.EventFy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.CSUF.Adapter.ImageComment_tab_adapter;
import com.CSUF.EventFy.R;
import com.CSUF.EventFy_Beans.Comments;
import com.CSUF.EventFy_Beans.Events;
import com.CSUF.EventFy_Beans.SignUp;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class ImageComment_tab extends Fragment implements Paginate.Callbacks {

private static final int GRID_SPAN = 3;
    protected int threshold = 4;
    protected int totalPages = 3;
    protected long networkDelay =1000;
    protected boolean addLoadingRow = true;
    protected boolean customLoadingListItem = false;
    private GetUsersForEvent getUsersForEvent;
    private List<Comments> commentList = new ArrayList<Comments>();
    private Context context;
    private int position;
    private Bitmap bm;
    private int ITEM_COUNT;
    private ProgressDialog progressDialog;
    private TextView commentTxtView;
    private Button commentTextButton;
    private Button commentImageButton;
    private SignUp signUp;
    private String imageUrl;
    private Events event;
    private String commentTxt;
private PostUsersComment postUsersComment;
private RecyclerView recyclerView;
private boolean loading = false;
private int page = 0;
private Handler handler;
private Paginate paginate;
    private RecyclerView.Adapter adapter;
    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    protected Orientation orientation = Orientation.VERTICAL;
    protected boolean reverseLayout = false;


    public static ImageComment_tab newInstance(int count, Context context, int position, Events event) {
        ImageComment_tab r = new ImageComment_tab();
        r.context = context;
        r.position = position;
        r.event = event;
        return r;
    }
    public void setITEM_COUNT(int count)
    {
        this.ITEM_COUNT = count;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        commentTxtView = (TextView) v.findViewById(R.id.comment_txt);
        commentTextButton = (Button) v.findViewById(R.id.comment_text_post_button);
        commentImageButton = (Button) v.findViewById(R.id.comment_image_post_button);

        commentTextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity

                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("posting...");
                progressDialog.setCancelable(false);
                commentTxt = commentTxtView.getText().toString();

                postUsersComment = new PostUsersComment(true);
                postUsersComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            }
        });

        commentImageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity

                progressDialog = new ProgressDialog(getActivity(),
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("posting...");
                progressDialog.setCancelable(false);
                commentTxt = commentTxtView.getText().toString();

                postUsersComment = new PostUsersComment(true);
                postUsersComment.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            }
        });


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        handler = new Handler();


        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
        setupPagination();


    }



protected void setupPagination() {
        // If RecyclerView was recently bound, unbind


        if (paginate != null) {
        paginate.unbind();
        }
        handler.removeCallbacks(fakeCallback);
        adapter  = new RecyclerViewMaterialAdapter(new ImageComment_tab_adapter(context, commentList, position));
        loading = false;
        page = 0;

        int layoutOrientation;
        switch (orientation) {
            case VERTICAL:
                layoutOrientation = OrientationHelper.VERTICAL;
                break;
            case HORIZONTAL:
                layoutOrientation = OrientationHelper.HORIZONTAL;
                break;
            default:
                layoutOrientation = OrientationHelper.VERTICAL;
        }

        RecyclerView.LayoutManager layoutManager = null;
        layoutManager = new LinearLayoutManager(getActivity(), layoutOrientation, false);

    ((LinearLayoutManager) layoutManager).setReverseLayout(reverseLayout);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator());
        recyclerView.setAdapter(adapter);

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

    adapter.notifyDataSetChanged();

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

        getUsersForEvent = new GetUsersForEvent(true);
        getUsersForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.get_comment_for_event);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();

            headers.add("Content-Type", "text/plain");

            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            HttpEntity<String> request = new HttpEntity<>(""+event.getEventId(), headers);
            Log.e("event id get : ", ""+url);
            Log.e("event id get : ", ""+event.getEventId());
            ResponseEntity<Comments []> response = restTemplate.exchange(url, HttpMethod.POST, request, Comments[].class);

            Comments [] comments = response.getBody();


            if(commentList!= null && commentList.size()>0 && commentList.get(0).getCommentId()== -1)
                commentList.remove(0);

            else if(commentList.size()<=0)
                commentList = new ArrayList<Comments>();



            commentList = Arrays.asList(comments);

            Log.e("comment size is : ", ""+commentList.size());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("data comment : ", ""+commentList.size());
            page = totalPages;
            if(commentList!=null && commentList.size()<=0) {


                Comments c = new Comments();
                c.setCommentText("No Comments so far");
                c.setIsImage("false");
                c.setCommentId(-1);
                commentList.add(c);
                page = totalPages;
            }
//            else{
//                page = 0;
//            }

                adapter = new RecyclerViewMaterialAdapter(new ImageComment_tab_adapter(context, commentList, position));
                recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    private class PostUsersComment extends AsyncTask<Void, Void, Void> {

        public PostUsersComment(String str) {

        }

        public PostUsersComment(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = getResources().getString(R.string.ip_local) + getResources().getString(R.string.add_comment_in_event);


            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Comments comments = new Comments();

            comments.setCommentText(commentTxt);
            comments.setUserID("xyz");
            comments.setUserID("xyz");
            comments.setIsImage("false");
            comments.setVoteDown("0");
            comments.setVoteDown("0");
            comments.setEvents(event);
            comments.setEventId(event.getEventId());
            Log.e("event id is : ", ""+event.getEventId());
            HttpEntity<Comments> request = new HttpEntity<Comments>(comments);

            ResponseEntity<Comments> response = restTemplate.exchange(url, HttpMethod.POST, request, Comments.class);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("after data is  : ", ""+commentList.size());

            getUsersForEvent = new GetUsersForEvent(true);
            getUsersForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }



    private class UploadImage extends AsyncTask<Void, Void, Void> {

        public UploadImage(String url) {

        }

        public UploadImage(boolean b) {
            super();

        }

        @Override
        protected Void doInBackground(Void... params) {
            Map config = new HashMap();
            config.put("cloud_name", "eventfy");
            config.put("api_key", "338234664624354");
            config.put("api_secret", "clA_O7equySs8LDK0hJNmmK62J8");
            Cloudinary cloudinary = new Cloudinary(config);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();

            try {
                Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());
                imageUrl = (String) uploadResult.get("secure_url");


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getUsersForEvent = new GetUsersForEvent(true);
            getUsersForEvent.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

}

