package com.CSUF.EventFy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.CSUF.EventFy_Beans.Comments;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder> {

    List<Comments> contents;

    static final int TYPE_IMAGE = 0;
    static final int TYPE_COMMENT = 1;
    private Context context;
    private int position1;

    private final String android_image_urls[] = {
            "http://api.learn2crack.com/android/images/donut.png",
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png",
            "http://api.learn2crack.com/android/images/ginger.png",
            "http://api.learn2crack.com/android/images/honey.png",
            "http://api.learn2crack.com/android/images/icecream.png",
            "http://api.learn2crack.com/android/images/jellybean.png",
            "http://api.learn2crack.com/android/images/kitkat.png",
            "http://api.learn2crack.com/android/images/lollipop.png",
            "http://api.learn2crack.com/android/images/marshmallow.png"
    };



    public TestRecyclerViewAdapter(Context context ,List<Comments> contents, int position) {
        this.contents = contents;
        this.context = context;
        this.position1 = position;

    }

    @Override
    public int getItemViewType(int position) {

        Log.e("Position :**** ", ""+position1);
        switch (position1) {
            case 0:
                return TYPE_IMAGE;
            default:
                return TYPE_COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public TestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = null;

        Log.e("view type ::::: ",""+viewType);
        switch (viewType)
        {
           // for image
            case 0:
                view = inflater.inflate(R.layout.list_item_card_big, parent, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.custom_loading_list_item, parent, false);
                break;

            case 2:
                view = inflater.inflate(R.layout.list_item_card_big, parent, false);
                break;
        }

        return new ViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case TYPE_HEADER:
//                holder.tv_android.setText("xyz");
//                Picasso.with(context).load("xyz").resize(120, 60).into(holder.img_android);
//                break;
//
//            case TYPE_CELL:
//                holder.tv_android.setText("xyz");
//                Picasso.with(context).load("xyz").resize(120, 60).into(holder.img_android);
//                break;
//        }

        holder.tv_android.setText("xyz");
        Picasso.with(context).load("https://res.cloudinary.com/eventfy/image/upload/v1461550414/yfg5zs58jd709arktqgn.png").into(holder.img_android);

    }

    public void add(List<Comments> items) {
        int previousDataSize = this.contents.size();
        this.contents.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_android;
        ImageView img_android;
        public ViewHolder(View view) {
            super(view);

            if(position1 == 0) {
                tv_android = (TextView) view.findViewById(R.id.comment_img_username);
                img_android = (ImageView) view.findViewById(R.id.comment_img);
            }
            else{
                tv_android = (TextView) view.findViewById(R.id.event_user_name);
                img_android = (ImageView) view.findViewById(R.id.event_user);

            }

            Log.e("tv text **** : ", ""+tv_android);
            Log.e("imf **** : ", ""+img_android);
        }
    }
}