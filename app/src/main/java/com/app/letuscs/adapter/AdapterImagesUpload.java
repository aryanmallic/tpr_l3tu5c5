package com.app.letuscs.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.letuscs.R;
import com.app.letuscs.models.others.ImageData;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AdapterImagesUpload extends RecyclerView.Adapter<AdapterImagesUpload.MyViewHolder> {

    private ArrayList<ImageData> imageData;
    private Context mContext;
    private int width;
    private ImageUploadSetOnItemClickListner imageUploadSetOnItemClickListner;

    public AdapterImagesUpload(ArrayList<ImageData> imageData, Context mContext, int width,ImageUploadSetOnItemClickListner imageUploadSetOnItemClickListner) {
        this.imageData = imageData;
        this.mContext = mContext;
        this.width = width;
        this.imageUploadSetOnItemClickListner=imageUploadSetOnItemClickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_item_image, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int curWidth = width / 3;

        holder.ivUpload.getLayoutParams().width = curWidth-10;
        holder.ivUpload.getLayoutParams().height = curWidth-10;


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageData.get(position).getImageBitmap().compress(Bitmap.CompressFormat.PNG, 90, stream);
        Glide.with(mContext)
                .load(stream.toByteArray())
                .asBitmap()
                .into(holder.ivUpload);
        //holder.ivUpload.setImageBitmap(imageData.get(position).getImageBitmap());
    }

    @Override
    public int getItemCount() {
        return imageData != null ? imageData.size() : 0;
    }

    public void updateList(ArrayList<ImageData> imageData){
        this.imageData = imageData;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivUpload;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivUpload=itemView.findViewById(R.id.design_item_image_ivUpload);

            ivUpload.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    imageUploadSetOnItemClickListner.getLongClickPosition(getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public interface ImageUploadSetOnItemClickListner {
        void getLongClickPosition(int postId);
    }


}
