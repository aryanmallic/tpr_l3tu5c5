package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.letuscs.R;
import com.app.letuscs.models.homeModel.ModelBanner;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterBanner extends PagerAdapter implements View.OnClickListener{
    private Context mContext;
    private ArrayList<ModelBanner> list;
    private LayoutInflater layoutInflater;
    private View view;

    public AdapterBanner(Context mContext, ArrayList<ModelBanner> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.design_item_image_slider, (ViewGroup) container, false);
        this.view = view;
        ImageView ivImage =view.findViewById(R.id.design_item_image_slider_ivImage);

        Glide.with(mContext)
                .load(list.get(position).getImage())
                .dontAnimate()
                .placeholder(R.drawable.ic_place)
                .into(ivImage);
        ivImage.setOnClickListener(this);
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void onClick(View view) {

    }
}
