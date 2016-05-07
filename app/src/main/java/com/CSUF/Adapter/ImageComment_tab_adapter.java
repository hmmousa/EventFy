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
import com.CSUF.EventFy_Beans.Comments;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swapnil on 4/28/16.
 */
public class ImageComment_tab_adapter extends RecyclerView.Adapter<ImageComment_tab_adapter.ViewHolder> {

    List<Comments> commentes = new ArrayList<Comments>();

    static final int TYPE_IMAGE = 0;
    static final int TYPE_COMMENT = 1;
    private Context context;
    private int position1;

    public ImageComment_tab_adapter(Context context ,List<Comments> contents, int position) {
        this.commentes = contents;
        this.context = context;
        this.position1 = position;

    }

    @Override
    public int getItemViewType(int position) {

        if(commentes.get(position).getIsImage().equals("true"))
            return TYPE_IMAGE;
        else
            return TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return commentes.size();
    }

    @Override
    public ImageComment_tab_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = null;

        switch (viewType)
        {
            case 0:
                view = inflater.inflate(R.layout.tab_image_comments, parent, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.tab_text_comments, parent, false);
                break;
        }

        return new ViewHolder(view, viewType) {
        };
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.commentUserName.setText(commentes.get(position).getUserName());
        Log.e("holder data : ", ""+commentes.size());
        Log.e("holder data : ", ""+commentes.get(0));
        if(commentes.get(position).getIsImage().equals("true")) {

            Picasso.with(context).load(commentes.get(position).getCommentText()).into(holder.img_android);
        }
        else
        {
            holder.commentUserText.setText(commentes.get(position).getCommentText());
        }
    }

    public void add(List<Comments> items) {
        int previousDataSize = this.commentes.size();
        this.commentes.addAll(items);
        notifyItemRangeInserted(previousDataSize, items.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView commentUserName;
        ImageView img_android;
        TextView commentUserText;
        public ViewHolder(View view, int viewType) {
            super(view);

            if(viewType == 0) {
                commentUserName = (TextView) view.findViewById(R.id.comment_img_username);
                img_android = (ImageView) view.findViewById(R.id.comment_img);
            }
            else{
                commentUserName = (TextView) view.findViewById(R.id.comment_user_name);
                commentUserText = (TextView) view.findViewById(R.id.comment_comment_text);

            }
        }
    }


}
