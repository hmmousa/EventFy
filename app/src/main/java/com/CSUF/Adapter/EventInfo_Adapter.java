package com.CSUF.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CSUF.EventFy.R;
import com.CSUF.EventFy_Beans.Events;
import com.CSUF.EventFy_Beans.SignUp;

/**
 * Created by swapnil on 4/25/16.
 */



public class EventInfo_Adapter extends RecyclerView.Adapter<EventInfo_Adapter.ViewHolder> {

    static final int TYPE_IMAGE = 0;
    static final int TYPE_COMMENT = 1;
    private Context context;
    private Events event;
    private int position1;
    private SignUp signUp;



    public EventInfo_Adapter(Context context , Events event, SignUp signUp) {
        this.event = event;
        this.context = context;
        this.signUp = signUp;
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

        View view = null;

        view = inflater.inflate(R.layout.tab_event_info, parent, false);

        return new ViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.eventName.setText(event.getEventName());
        holder.eventDate.setText(event.getEventDate());
    //    holder.tv_android.setText();

     //   Picasso.with(context).load("https://res.cloudinary.com/eventfy/image/upload/v1461550414/yfg5zs58jd709arktqgn.png").into(holder.img_android);

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eventLocation;
        TextView eventName;
        TextView eventDistance;
        TextView eventType;
        TextView eventDate;
        TextView eventDescription;



        public ViewHolder(View view) {
            super(view);
            eventName = (TextView) view.findViewById(R.id.txt_event_info_details_name);
            eventLocation = (TextView) view.findViewById(R.id.txt_event_info_details_location);
            //eventDescription = view.findViewById(R.id.EventInfoDetails);
            eventDistance = (TextView) view.findViewById(R.id.txt_input_event_info_details_distance);
            eventDate = (TextView) view.findViewById(R.id.txt_input_event_info_details_date);
        }
    }
}
