package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.letuscs.models.homeModel.ModelBanner;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class InfiniteAdapter extends PagerAdapter {
    private Context activity;
    private ArrayList<ModelBanner> image;
    private int pos = 0;

    public InfiniteAdapter(Context activity, ArrayList<ModelBanner> image){
        this.activity = activity;
        this.image = image;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView img = new ImageView(activity);
        ((ViewPager)container).addView(img);
        //img.setImageResource(image[pos]);
        Glide.with(activity)
                .load(image.get(pos).getImage())
                .centerCrop()
                .into(img);

        if(pos >= image.size() - 1)
            pos =0;
        else
            ++pos;

        Log.i("Position",pos+"");
        return img;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View)object);
    }

}
