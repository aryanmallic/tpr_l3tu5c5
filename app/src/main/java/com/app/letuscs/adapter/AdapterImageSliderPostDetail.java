package com.app.letuscs.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.letuscs.R;
import com.app.letuscs.models.postDetailModel.ModelPostDetailImage;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterImageSliderPostDetail extends PagerAdapter implements View.OnClickListener{
    private Context mContext;
    private List<ModelPostDetailImage> modelPostsImages;
    private LayoutInflater layoutInflater;
    private View view;

    public AdapterImageSliderPostDetail(Context mContext, List<ModelPostDetailImage> modelPostsImages) {
        this.mContext = mContext;
        this.modelPostsImages = modelPostsImages;
    }

    @Override
    public int getCount() {
        return modelPostsImages!=null?modelPostsImages.size():0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.design_item_image_slider, (ViewGroup) container, false);
        this.view = view;
        ImageView ivImage =view.findViewById(R.id.design_item_image_slider_ivImage);

        Glide.with(mContext)
                .load(modelPostsImages.get(position).getImagePath())
                .dontAnimate()
                .centerCrop()
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
