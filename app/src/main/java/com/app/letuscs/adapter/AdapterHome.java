package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.letuscs.R;
import com.app.letuscs.models.homeModel.ModelHomeGrids;

import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.MyViewHolder> {
    List<ModelHomeGrids> itemData;
    Context mContext;

    MyOnItemClickListner myOnItemClickListner;

    public AdapterHome(List<ModelHomeGrids> itemData, Context mContext, MyOnItemClickListner myOnItemClickListner) {
        this.itemData = itemData;
        this.mContext = mContext;
        this.myOnItemClickListner = myOnItemClickListner;
    }

    @Override
    public AdapterHome.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.design_item_home, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterHome.MyViewHolder holder, int position) {
        holder.ivIcon.setImageResource(itemData.get(position).getImageUrl());
        holder.tvName.setText(itemData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivIcon;
        private CardView cvItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName_designItemHome);
            ivIcon = itemView.findViewById(R.id.ivIcon_designItemHome);
            cvItem = itemView.findViewById(R.id.cvItem_designItemHome);

            cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myOnItemClickListner.myViews(getAdapterPosition());
                }
            });
        }
    }

    public interface MyOnItemClickListner {
        void myViews(int position);
    }
}
