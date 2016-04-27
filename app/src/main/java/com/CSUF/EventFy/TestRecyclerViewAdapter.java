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

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private Context context;

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



    public TestRecyclerViewAdapter(Context context ,List<Comments> contents) {
        this.contents = contents;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public TestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_loading_list_item, parent, false);

//        switch (viewType) {
//            case TYPE_HEADER: {
//                view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_item_card_big, parent, false);
//
//            }
//            case TYPE_CELL: {
//                view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.list_item_card_small, parent, false);
//
//            }
//        }

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
        Picasso.with(context).load("http://api.learn2crack.com/android/images/donut.png").resize(120, 60).into(holder.img_android);

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

            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView)view.findViewById(R.id.img_android);

            Log.e("tv text **** : ", ""+tv_android);
            Log.e("imf **** : ", ""+img_android);
        }
    }
}