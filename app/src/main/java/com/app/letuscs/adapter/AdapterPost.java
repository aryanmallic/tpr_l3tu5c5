package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.letuscs.R;
import com.app.letuscs.models.postsModel.ModelPosts;
import com.app.letuscs.utility.HelperMethods;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyViewHolder> {
    private static final String TAG = AdapterPost.class.getName();
    List<ModelPosts> modelPosts;
    Context mContext;
    PostSetOnItemClickListner postSetOnItemClickListner;
    private boolean firstload = true;


    public AdapterPost(List<ModelPosts> modelPosts, Context mContext, PostSetOnItemClickListner postSetOnItemClickListner) {
        this.modelPosts = modelPosts;
        this.mContext = mContext;
        this.postSetOnItemClickListner = postSetOnItemClickListner;
    }

    public void loadMoreList(List<ModelPosts> list) {
        ///because in on click firstload is made false
        //firstload=true;
        this.modelPosts.addAll(list);
        notifyDataSetChanged();
        //notifyItemInserted(modelPosts.size()-1);
    }

    public void setPostList(List<ModelPosts> modelPosts) {
        //this.modelPosts.addAll(modelPosts);
        this.modelPosts = modelPosts;
        notifyDataSetChanged();
    }

    @Override
    public AdapterPost.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_item_post, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterPost.MyViewHolder holder, int position) {


        //final String comment=modelPosts.get(position).getContent();
        Glide.with(mContext)
                .load(modelPosts.get(position).getUser().getImgUrl())
                .centerCrop()
                .into(holder.civProfile);

        holder.tvName.setText(modelPosts.get(position).getUser().getName());
        if (modelPosts.get(position).getContent() != null &&
                !modelPosts.get(position).getContent().equals("")) {
            //holder.rlImage.setVisibility(View.GONE);
            holder.tvComment.setVisibility(View.VISIBLE);
            holder.tvComment.setText(modelPosts.get(position).getContent());
        } else {
            holder.tvComment.setVisibility(View.GONE);
        }

        if (modelPosts.get(position).getImage() != null &&
                modelPosts.get(position).getImage().size() > 0) {
            holder.rlImage.setVisibility(View.VISIBLE);
            //holder.tvComment.setVisibility(View.GONE);

            //If viewpager view is alloted
            /*AdapterImageSlider adapterImageSlider = new AdapterImageSlider(mContext, modelPosts.get(position).getImage());
            holder.vpImage.setAdapter(adapterImageSlider);
            holder.indicator.setViewPager(holder.vpImage);*/

            //If Normal view is alloted
            switch (modelPosts.get(position).getImage().size()) {
                case 1:
                    holder.llOneImage.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(0).getImagePath())
                            .centerCrop()
                            .into(holder.llOneImage_ivOne);
                    break;
                case 2:
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(0).getImagePath())
                            .centerCrop()
                            .into(holder.llTwoImage_ivOne);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(1).getImagePath())
                            .centerCrop()
                            .into(holder.llTwoImage_ivTwo);
                    holder.llTwoImage.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(0).getImagePath())
                            .centerCrop()
                            .into(holder.llThreeImage_ivOne);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(1).getImagePath())
                            .centerCrop()
                            .into(holder.llThreeImage_ivTwo);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(2).getImagePath())
                            .centerCrop()
                            .into(holder.llThreeImage_ivThree);
                    holder.llThreeImage.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(0).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivOne);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(1).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivTwo);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(2).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivThree);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(3).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivFour);
                    holder.llFourImage.setVisibility(View.VISIBLE);
                    break;
                default:
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(0).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivOne);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(1).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivTwo);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(2).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivThree);
                    Glide.with(mContext).load(modelPosts.get(position).getImage().get(3).getImagePath())
                            .centerCrop()
                            .into(holder.llFourImage_ivFour);
                    holder.llFourImage.setVisibility(View.VISIBLE);
                    break;
            }


            if (modelPosts.get(position).getImage().size() >= 5) {
                holder.tvImageCount.setVisibility(View.VISIBLE);

                int more = modelPosts.get(position).getImage().size() - 4;
                String text = "+" + more;
                holder.tvImageCount.setText(text);
                /*final int[] current = {1};
                current[0] = 1;
                int total= modelPosts.get(position).getImage().size();
                String text= current[0] +"/"+total;
                holder.tvImageCount.setText(text);
                holder.vpImage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        current[0] = position+1;
                        int total= holder.vpImage.getChildCount();
                        String text= current[0] +"/"+total;
                        holder.tvImageCount.setText(text);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });*/
            } else {
                holder.tvImageCount.setVisibility(View.GONE);
            }
        } else {
            holder.rlImage.setVisibility(View.GONE);
        }

        ///////Time
        String timeAgo = "";
        try {
            timeAgo = new HelperMethods(mContext).getDateStatus(modelPosts.get(position).getCreatedAt());
            //timeAgo=getDateStatus(modelNestComments.get(position).getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tvDate.setText(timeAgo);
        ////////
        ////////

        /*StringBuilder builder = new StringBuilder();
        if (!modelPosts.get(position).getLikesCount().equals("0")) {
            builder.append(modelPosts.get(position).getLikesCount());
            builder.append(" Likes  ");
        }
        if (!modelPosts.get(position).getCommentCount().equals("0")) {
            builder.append(modelPosts.get(position).getCommentCount());
            builder.append(" Comments");
        }
        holder.tvCount.setText(builder.toString());*/


        //////////for setting total  likes and comment count
        //////////first time visibility
        if (!modelPosts.get(position).getLikesCount().equals("0")) {
            holder.tvCountLikes.setVisibility(View.VISIBLE);
            String strLikes = modelPosts.get(position).getLikesCount() + " Likes";
            holder.tvCountLikes.setText(strLikes);
        } else {
            holder.tvCountLikes.setVisibility(View.GONE);
        }
        ////Comment
        if (!modelPosts.get(position).getCommentCount().equals("0")) {
            holder.tvCountComment.setVisibility(View.VISIBLE);
            String strComments = modelPosts.get(position).getCommentCount() + " Comments";
            holder.tvCountComment.setText(strComments);
        } else {
            holder.tvCountComment.setVisibility(View.INVISIBLE);
        }
        ////////////
        ////////////


        ////////////to check personal likes
        ////////////
        //if (modelPosts.get(position).getLikes().size() > 0 ) {
        if (modelPosts.get(position).getLikesStatus()) {
            holder.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorPrimary));
            //modelPosts.get(position).setLiked(true);
        } else {
            holder.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorText));
            holder.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorText));
            //modelPosts.get(position).setLiked(false);
        }
        ////////////
        ////////////


        /*if (firstload) {
            if (modelPosts.get(position).getLikes().size() > 0) {
                holder.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorPrimary));
                modelPosts.get(position).setLiked(true);
            } else {
                holder.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorText));
                holder.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorText));
                modelPosts.get(position).setLiked(false);
            }
        } else {
            if (modelPosts.get(position).isLiked()) {
                holder.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorPrimary));
            } else {
                holder.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorText));
                holder.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorText));
            }
        }*/
    }

    @Override
    public void onBindViewHolder(final AdapterPost.MyViewHolder holder, int position, List<Object> payloads) {
        MyViewHolder myVH = (MyViewHolder) holder;
        if (!payloads.isEmpty()) {
            if (payloads.contains("prelike")) {
                //update UI changes
                updateLike(myVH, position);
            } else if (payloads.contains("preunlike")) {
                //update UI changes
                updateUnlike(myVH, position);
            }
        } else super.onBindViewHolder(holder, position, payloads);
    }

    private void updateUnlike(MyViewHolder myVH, int position) {
        myVH.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorText));
        myVH.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorText));
        //modelPosts.get(position).setLiked(false);

        //String currentLike = myVH.tvCountLikes.getText().toString().replaceAll("[^0-9]", "");
        String currentLike = modelPosts.get(position).getLikesCount();
        int increaseLike = 0;
        if (currentLike.equals("1")) {
            increaseLike = 0;
            myVH.tvCountLikes.setVisibility(View.GONE);
        } else {
            increaseLike = Integer.parseInt(currentLike) - 1;
        }
        modelPosts.get(position).setLikesCount(String.valueOf(increaseLike));
        modelPosts.get(position).setLikesStatus(false);
        String lk = increaseLike + " Likes";
        //Toast.makeText(mContext, "" + lk, Toast.LENGTH_SHORT).show();
        myVH.tvCountLikes.setText(lk);


    }

    private void updateLike(MyViewHolder myVH, int position) {
        myVH.tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        myVH.ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorPrimary));
        //modelPosts.get(position).setLiked(true);

        myVH.tvCountLikes.setVisibility(View.VISIBLE);
        //String currentLike = myVH.tvCountLikes.getText().toString().replaceAll("[^0-9]", "");
        String currentLike = modelPosts.get(position).getLikesCount();
        int increaseLike = 0;
        if (currentLike.equals("")) {
            increaseLike = 1;
        } else {
            increaseLike = Integer.parseInt(currentLike) + 1;
        }
        modelPosts.get(position).setLikesCount(String.valueOf(increaseLike));
        modelPosts.get(position).setLikesStatus(true);
        String lk = increaseLike + " Likes";
        //Toast.makeText(mContext, "" + lk, Toast.LENGTH_SHORT).show();
        myVH.tvCountLikes.setText(lk);
    }


    @Override
    public int getItemCount() {
        return modelPosts != null ? modelPosts.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvComment, tvDate, tvIconLike;
        private CircleImageView civProfile;
        private LinearLayout llComment, llLike;
        private ImageView ivIconLike;
        private ViewPager vpImage;
        private RelativeLayout rlImage;
        private TextView tvImageCount;
        private CircleIndicator indicator;

        private TextView tvCountLikes, tvCountComment;


        private LinearLayout llOneImage, llTwoImage, llThreeImage, llFourImage;
        private ImageView llOneImage_ivOne;
        private ImageView llTwoImage_ivTwo, llTwoImage_ivOne;
        private ImageView llThreeImage_ivOne, llThreeImage_ivTwo, llThreeImage_ivThree;
        private ImageView llFourImage_ivOne, llFourImage_ivTwo, llFourImage_ivThree, llFourImage_ivFour;

        public MyViewHolder(View itemView) {
            super(itemView);

            llOneImage = itemView.findViewById(R.id.design_item_post_llOneImage);
            llTwoImage = itemView.findViewById(R.id.design_item_post_llTwoImage);
            llThreeImage = itemView.findViewById(R.id.design_item_post_llThreeImage);
            llFourImage = itemView.findViewById(R.id.design_item_post_llFourImage);

            llOneImage_ivOne = itemView.findViewById(R.id.design_item_post_llOneImage_ivOne);

            llTwoImage_ivOne = itemView.findViewById(R.id.design_item_post_llTwoImage_ivOne);
            llTwoImage_ivTwo = itemView.findViewById(R.id.design_item_post_llTwoImage_ivTwo);

            llThreeImage_ivOne = itemView.findViewById(R.id.design_item_post_llThreeImage_ivOne);
            llThreeImage_ivTwo = itemView.findViewById(R.id.design_item_post_llThreeImage_ivTwo);
            llThreeImage_ivThree = itemView.findViewById(R.id.design_item_post_llThreeImage_ivThree);

            llFourImage_ivOne = itemView.findViewById(R.id.design_item_post_llFourImage_ivOne);
            llFourImage_ivTwo = itemView.findViewById(R.id.design_item_post_llFourImage_ivTwo);
            llFourImage_ivThree = itemView.findViewById(R.id.design_item_post_llFourImage_ivThree);
            llFourImage_ivFour = itemView.findViewById(R.id.design_item_post_llFourImage_ivFour);


            tvComment = itemView.findViewById(R.id.design_item_post_tvComment);
            tvName = itemView.findViewById(R.id.design_item_post_tvName);
            tvDate = itemView.findViewById(R.id.design_item_post_tvDate);
            //tvCount = itemView.findViewById(R.id.design_item_post_tvCountDesignItemComment);
            civProfile = itemView.findViewById(R.id.design_item_post_civProfile);
            llComment = itemView.findViewById(R.id.design_item_post_llComment);
            llLike = itemView.findViewById(R.id.design_item_post_llLike);
            tvIconLike = itemView.findViewById(R.id.design_item_post_tvIconLike);
            ivIconLike = itemView.findViewById(R.id.design_item_post_ivIconLike);


            tvCountLikes = itemView.findViewById(R.id.design_item_post_tvCountLikes);
            tvCountComment = itemView.findViewById(R.id.design_item_post_tvCountComment);

            indicator = itemView.findViewById(R.id.indicator);
            vpImage = itemView.findViewById(R.id.design_item_post_vpImage);
            rlImage = itemView.findViewById(R.id.design_item_post_rlImage);
            tvImageCount = itemView.findViewById(R.id.design_item_post_tvImageCount);

            llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postSetOnItemClickListner.getPostClickPosition(modelPosts.get(getAdapterPosition()).getId());
                }
            });

            //Likes working good but need status to do
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postSetOnItemClickListner.getLikesView(getAdapterPosition(),modelPosts.get(getAdapterPosition()).getId());
                    if (!modelPosts.get(getAdapterPosition()).getLikesStatus()) {
                        //modelPosts.get(getAdapterPosition()).setLiked(true);
                        notifyItemChanged(getAdapterPosition(), "prelike");
                    } else {
                        //modelPosts.get(getAdapterPosition()).setLiked(false);
                        notifyItemChanged(getAdapterPosition(), "preunlike");
                    }
                }
            });
        }
    }

    public interface PostSetOnItemClickListner {
        void getPostClickPosition(int postId);

        void getLikesView(int position, int postId);
    }
}