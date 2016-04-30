package com.CSUF.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.CSUF.EventFy_Beans.Events;

/**
 * Created by swapnil on 4/25/16.
 */



public class EventInfo_Adapter extends RecyclerView.Adapter<EventInfo_Adapter.ViewHolder> {

    static final int TYPE_IMAGE = 0;
    static final int TYPE_COMMENT = 1;
    private Context context;
    private Events event;
    private int position1;




    public EventInfo_Adapter(Context context , Events event) {
        this.event = event;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        Log.e("position is : ", ""+position);
        return 0;

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public EventInfo_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return null;
       // View view = null;

       // view = inflater.inflate(R.layout.tab_event_info, parent, false);

//        return new ViewHolder(view) {
//        };
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    //    holder.tv_android.setText();
     //   Picasso.with(context).load("https://res.cloudinary.com/eventfy/image/upload/v1461550414/yfg5zs58jd709arktqgn.png").into(holder.img_android);

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_android;
        ImageView img_android;
        public ViewHolder(View view) {
            super(view);

          //  tv_android = (TextView) view.findViewById(R.id.event_user_name);
           // img_android = (ImageView) view.findViewById(R.id.event_user);


            //Log.e("imf **** : ", ""+img_android);
        }
    }
}
