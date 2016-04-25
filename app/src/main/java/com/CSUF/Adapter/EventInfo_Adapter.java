package com.CSUF.Adapter;

/**
 * Created by swapnil on 4/25/16.
 */
public class EventInfo_Adapter  {
//    // region Constants
//    public static final int HEADER = 0;
//    public static final int ITEM = 1;
//    public static final int LOADING = 2;
//    // endregion
//
//    // region Member Variables
//    private List<Events> mVideos;
//    private OnItemClickListener mOnItemClickListener;
//    private boolean mIsLoadingFooterAdded = false;
//    // endregion
//
//    // region Listeners
//    // endregion
//
//    // region Interfaces
//    public interface OnItemClickListener {
//        void onItemClick(int position, View view);
//    }
//    // endregion
//
//    // region Constructors
//    public EventInfo_Adapter() {
//        mVideos = new ArrayList<>();
//    }
//    // endregion
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder viewHolder = null;
//
//        switch (viewType) {
//            case HEADER:
//                break;
//            case ITEM:
//                viewHolder = createVideoViewHolder(parent);
//                break;
//            case LOADING:
//                viewHolder = createLoadingViewHolder(parent);
//                break;
//            default:
//                Timber.e("[ERR] type is not supported!!! type is %d", viewType);
//                break;
//        }
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        switch (getItemViewType(position)) {
//            case HEADER:
//                break;
//            case ITEM:
//                bindVideoViewHolder(viewHolder, position);
//                break;
//            case LOADING:
//                bindLoadingViewHolder(viewHolder);
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mVideos.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return (position == mVideos.size() - 1 && mIsLoadingFooterAdded) ? LOADING : ITEM;
//    }
//
//    // region Helper Methods
//    private void add(Events item) {
//        mVideos.add(item);
//        notifyItemInserted(mVideos.size() - 1);
//    }
//
//    public void addAll(List<Events> videos) {
//        for (Events video : videos) {
//            add(video);
//        }
//    }
//
//    public void remove(Events item) {
//        int position = mVideos.indexOf(item);
//        if (position > -1) {
//            mVideos.remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//    public void clear() {
//        mIsLoadingFooterAdded = false;
//        while (getItemCount() > 0) {
//            remove(getItem(0));
//        }
//    }
//
//    public boolean isEmpty() {
//        return getItemCount() == 0;
//    }
//
//    public void addLoading() {
//        mIsLoadingFooterAdded = true;
//        add(new Events());
//    }
//
//    public void removeLoading() {
//        mIsLoadingFooterAdded = false;
//
//        int position = mVideos.size() - 1;
//        Events item = getItem(position);
//
//        if (item != null) {
//            mVideos.remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//    public Events getItem(int position) {
//        return mVideos.get(position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.mOnItemClickListener = onItemClickListener;
//    }
//
////    private RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent) {
////        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_info, parent, false);
////
////        return new HeaderViewHolder(v);
////    }
//
//    private RecyclerView.ViewHolder createVideoViewHolder(ViewGroup parent) {
//        // create a new view
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventinfo_row, parent, false);
//
//        final VideoViewHolder holder = new VideoViewHolder(v);
//
//        holder.mVideoRowRootLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int adapterPos = holder.getAdapterPosition();
//                if (adapterPos != RecyclerView.NO_POSITION) {
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(adapterPos, holder.itemView);
//                    }
//                }
//            }
//        });
//
//        return holder;
//    }
//
//    private RecyclerView.ViewHolder createLoadingViewHolder(ViewGroup parent) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);
//
//        return new MoreViewHolder(v);
//    }
//
//    private void bindVideoViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        final VideoViewHolder holder = (VideoViewHolder) viewHolder;
//
//        final Events video = mVideos.get(position);
//        if (video != null) {
//            setUpTitle(holder.mTitleTextView, video);
//            setUpSubtitle(holder.mSubtitleTextView, video);
//            setUpVideoThumbnail(holder.mVideoThumbnailImageView, video.getEventImageUrl());
//            setUpDuration(holder.mDurationTextView, Integer.parseInt(video.getEventTime()));
//            setUpUploadedDate(holder.mUploadedDateTextView, video.getEventDate());
//
//            int adapterPos = holder.getAdapterPosition();
//            ViewCompat.setTransitionName(holder.mSubtitleTextView, "myTransition" + adapterPos);
//        }
//    }
//
//    private void bindLoadingViewHolder(RecyclerView.ViewHolder viewHolder) {
//        MoreViewHolder holder = (MoreViewHolder) viewHolder;
//
//        holder.mLoadingImageView.setMaskOrientation(LoadingImageView.MaskOrientation.LeftToRight);
//    }
//
//    private void setUpTitle(TextView tv, Events events) {
//        String name = events.getEventName();
//        if (!TextUtils.isEmpty(name)) {
//            tv.setText(name);
//        }
//    }
//
//    private void setUpSubtitle(TextView tv, Events events) {
//
//        tv.setText(events.getEventLocation());
//    }
//
//    private void setUpVideoThumbnail(ImageView iv, String imageUrl) {
//        boolean isPictureSet = false;
//        if (!TextUtils.isEmpty(imageUrl)) {
//            Glide.with(iv.getContext())
//                    .load(imageUrl)
////                                .placeholder(R.drawable.ic_placeholder)
////                                .error(R.drawable.ic_error)
//                    .into(iv);
//            isPictureSet = true;
//        }
//    if(!isPictureSet)
//
//    {
//        iv.setImageBitmap(null);
//    }
//}
//
//
//    private void setUpDuration(TextView tv, int milesAway) {
//        Integer duration = milesAway;
//
//        tv.setText(milesAway);
//    }
//
//    private void setUpUploadedDate(TextView tv, String date) {
//            tv.setText(date);
//    }
//
//    private String formatViewCount(int viewCount) {
//        String formattedViewCount = "";
//
//        if (viewCount < 1000000000 && viewCount >= 1000000) {
//            formattedViewCount = String.format("%dM Attending", viewCount / 1000000);
//        } else if (viewCount < 1000000 && viewCount >= 1000) {
//            formattedViewCount = String.format("%dK Attending", viewCount / 1000);
//        } else if (viewCount < 1000 && viewCount > 1) {
//            formattedViewCount = String.format("%d Attending", viewCount);
//        } else if (viewCount == 1) {
//            formattedViewCount = String.format("%d Attending", viewCount);
//        }
//
//        return formattedViewCount;
//    }
//    // endregion
//
//    // region Inner Classes
//
//    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
//        public HeaderViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    public static class VideoViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.video_thumbnail_iv)
//        ImageView mVideoThumbnailImageView;
//        @Bind(R.id.title_tv)
//        TextView mTitleTextView;
//        @Bind(R.id.uploaded_date_tv)
//        TextView mUploadedDateTextView;
//        @Bind(R.id.duration_tv)
//        TextView mDurationTextView;
//        @Bind(R.id.subtitle_tv)
//        TextView mSubtitleTextView;
//        @Bind(R.id.video_row_root_ll)
//        LinearLayout mVideoRowRootLinearLayout;
//
//        public VideoViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    public static class MoreViewHolder extends RecyclerView.ViewHolder {
//        @Bind(R.id.loading_iv)
//        LoadingImageView mLoadingImageView;
//
//        public MoreViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//        }
//    }
//
//    // endregion

}
