package com.CSUF.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.CSUF.EventFy.R;
import com.CSUF.EventFy_Beans.SignUp;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by swapnil on 4/28/16.
 */
public class Attendance_tab_adapter  extends RecyclerView.Adapter<Attendance_tab_adapter.ViewHolder> {

    List<SignUp> signUpLst;

    static final int TYPE_IMAGE = 0;
    static final int TYPE_COMMENT = 1;
    private Context context;
    private int position;




    public Attendance_tab_adapter(Context context ,List<SignUp> contents, int position) {
        this.signUpLst = contents;
        this.context = context;
        this.position = position;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
if(signUpLst!=null)
        return signUpLst.size();
     return 0;
    }

    @Override
    public Attendance_tab_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = null;

        view = inflater.inflate(R.layout.tab_attendance, parent, false);

        return new ViewHolder(view, viewType) {
        };
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_android.setText(signUpLst.get(position).getUserName());
        Picasso.with(context).load("https://res.cloudinary.com/eventfy/image/upload/v1461550414/yfg5zs58jd709arktqgn.png").into(holder.img_android);

    }

    public void add(List<SignUp> items) {
        int previousDataSize = this.signUpLst.size();
        this.signUpLst.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_android;
        ImageView img_android;
        public ViewHolder(View view, int viewType) {
            super(view);

                tv_android = (TextView) view.findViewById(R.id.event_user_name);
                img_android = (ImageView) view.findViewById(R.id.event_user);


            Log.e("imf **** : ", ""+img_android);
        }
    }


}
