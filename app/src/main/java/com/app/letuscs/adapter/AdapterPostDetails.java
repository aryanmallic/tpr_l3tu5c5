package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.letuscs.R;
import com.app.letuscs.models.postDetailModel.ModelPostDetailComment;
import com.app.letuscs.utility.HelperMethods;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPostDetails extends RecyclerView.Adapter<AdapterPostDetails.MyViewHolder> {

    List<ModelPostDetailComment> modelPostDetailComments;
    Context mContext;
    SetOnAdapterPostDetailsClickListner setOnAdapterPostDetailsClickListner;

    public AdapterPostDetails(List<ModelPostDetailComment> modelPostDetailComments, Context mContext, SetOnAdapterPostDetailsClickListner setOnAdapterPostDetailsClickListner) {
        this.modelPostDetailComments = modelPostDetailComments;
        this.mContext = mContext;
        this.setOnAdapterPostDetailsClickListner = setOnAdapterPostDetailsClickListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_item_more_comment, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load(modelPostDetailComments.get(position).getModelPostDetalUser().getImgUrl())
                .centerCrop()
                .into(holder.civProfile);
        holder.tvName.setText(modelPostDetailComments.get(position).getModelPostDetalUser().getName());
        holder.tvComment.setText(modelPostDetailComments.get(position).getContent());

        String timeAgo = "";
        try {
            timeAgo = new HelperMethods(mContext).getDateStatus(modelPostDetailComments.get(position).getCreatedAt());
            //timeAgo=getDateStatus(modelNestComments.get(position).getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvTime.setText(timeAgo);

        switch (Integer.parseInt(modelPostDetailComments.get(position).getNestCommentCount())) {

            case 0:
                holder.tvRepliesCount.setVisibility(View.GONE);
                holder.llSingleComment.setVisibility(View.GONE);
                break;
            case 1:
                holder.tvRepliesCount.setVisibility(View.VISIBLE);
                int count1 = Integer.parseInt(modelPostDetailComments.get(position).getNestCommentCount());
                String text1 = "View " + count1 + " Reply";
                holder.tvRepliesCount.setText(text1);

                break;
            default:
                holder.tvRepliesCount.setVisibility(View.VISIBLE);
                int countdefault = Integer.parseInt(modelPostDetailComments.get(position).getNestCommentCount());
                String textDef = "View " + countdefault + " Replies";
                holder.tvRepliesCount.setText(textDef);

                /*holder.llSingleComment.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(modelPostDetailComments.get(position).getModelPostDetalUser().getImgUrl())
                        .centerCrop()
                        .into(holder.civNestedProfile);
                holder.tvNestedName.setText(modelPostDetailComments.get(position).getModelPostDetalUser().getName());
                holder.tvNestedComment.setText(modelPostDetailComments.get(position).getContent());*/
                break;
        }
    }

    @Override
    public int getItemCount() {
        return modelPostDetailComments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civProfile, civNestedProfile;
        private TextView tvName, tvReply, tvTime, tvComment, tvRepliesCount;

        private TextView tvNestedName, tvNestedComment;
        private LinearLayout llSingleComment;

        public MyViewHolder(final View itemView) {
            super(itemView);

            civProfile = itemView.findViewById(R.id.design_item_more_comment_civProfile);
            tvName = itemView.findViewById(R.id.design_item_more_comment_tvName);
            tvReply = itemView.findViewById(R.id.design_item_more_comment_tvReply);
            tvTime = itemView.findViewById(R.id.design_item_more_comment_tvTime);
            tvComment = itemView.findViewById(R.id.design_item_more_comment_tvComment);
            tvRepliesCount = itemView.findViewById(R.id.design_item_more_comment_tvRepliesCount);

            civNestedProfile = itemView.findViewById(R.id.design_item_more_comment_civNestedProfile);
            tvNestedName = itemView.findViewById(R.id.design_item_more_comment_tvNestedName);
            tvNestedComment = itemView.findViewById(R.id.design_item_more_comment_tvNestedComment);
            llSingleComment = itemView.findViewById(R.id.design_item_more_comment_llSingleComment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setOnAdapterPostDetailsClickListner.getCommentId(modelPostDetailComments.get(getAdapterPosition()).getId());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    setOnAdapterPostDetailsClickListner.getCommentIdForLongClick(modelPostDetailComments.get(getAdapterPosition()).getId()
                            , itemView
                            , modelPostDetailComments.get(getAdapterPosition()).getModelPostDetalUser().getId()
                            , modelPostDetailComments.get(getAdapterPosition()).getContent());
                    return true;
                }
            });
        }
    }

    public interface SetOnAdapterPostDetailsClickListner {
        void getCommentId(int commentId);

        void getCommentIdForLongClick(int commentId, View v, int userId, String comment);
    }
}
