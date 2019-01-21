package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.letuscs.R;
import com.app.letuscs.models.nestedCommentModel.ModelNestedCommentNestReply;
import com.app.letuscs.models.postDetailModel.ModelPostDetailComment;
import com.app.letuscs.utility.HelperMethods;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNestedComments extends RecyclerView.Adapter<AdapterNestedComments.MyViewHolder> {

    List<ModelNestedCommentNestReply> modelNestedCommentNestReplies;
    Context mContext;

    public AdapterNestedComments(List<ModelNestedCommentNestReply> modelNestedCommentNestReplies, Context mContext) {
        this.modelNestedCommentNestReplies = modelNestedCommentNestReplies;
        this.mContext = mContext;
    }

    @Override
    public AdapterNestedComments.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_item_nested_reply, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterNestedComments.MyViewHolder holder, int position) {
        Glide.with(mContext)
                .load(modelNestedCommentNestReplies.get(position).getUser().getImgUrl())
                .centerCrop()
                .into(holder.civNestedProfile);
        holder.tvNestedName.setText(modelNestedCommentNestReplies.get(position).getUser().getName());
        holder.tvNestedComment.setText(modelNestedCommentNestReplies.get(position).getContent());

        String timeAgo = "";
        try {
            timeAgo = new HelperMethods(mContext).getDateStatus(modelNestedCommentNestReplies.get(position).getCreatedAt());
            //timeAgo=getDateStatus(modelNestComments.get(position).getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvTime.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return modelNestedCommentNestReplies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civNestedProfile;
        private TextView tvNestedName, tvNestedComment, tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            civNestedProfile = itemView.findViewById(R.id.design_item_nested_reply_civNestedProfile);
            tvNestedName = itemView.findViewById(R.id.design_item_nested_reply_tvNestedName);
            tvNestedComment = itemView.findViewById(R.id.design_item_nested_reply_tvNestedComment);
            tvTime = itemView.findViewById(R.id.design_item_more_comment_tvTime);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        }
    }
}
