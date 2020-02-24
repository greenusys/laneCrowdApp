package com.mzelzoghbi.zgallery.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mzelzoghbi.zgallery.R;
import com.mzelzoghbi.zgallery.activities.VideoPlayActivity;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by mohamedzakaria on 8/11/16.
 */
public class ViewPagerAdapter extends PagerAdapter {

    Activity activity;
    LayoutInflater mLayoutInflater;
    ArrayList<String> images;
    PhotoViewAttacher mPhotoViewAttacher;
    private boolean isShowing = true;
    private Toolbar toolbar;
    private String isImage;
    private RecyclerView imagesHorizontalList;

    public ViewPagerAdapter(String isImage,Activity activity, ArrayList<String> images, Toolbar toolbar, RecyclerView imagesHorizontalList) {
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
        this.toolbar = toolbar;
        this.isImage = isImage;
        this.imagesHorizontalList = imagesHorizontalList;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.z_pager_item, container, false);
        final ImageView imageView = itemView.findViewById(R.id.iv);
        final ImageView video_icon = itemView.findViewById(R.id.video_icon);



        if(isImage.equals("true"))
            video_icon.setVisibility(View.GONE);
        else
            video_icon.setVisibility(View.VISIBLE);



     video_icon.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             activity.startActivity(new Intent(activity, VideoPlayActivity.class).putExtra("url",images.get(position)));
         }
     });


        Glide.with(activity).load(images.get(position)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mPhotoViewAttacher = new PhotoViewAttacher(imageView);




                mPhotoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        if (isShowing) {
                            isShowing = false;
                            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                            imagesHorizontalList.animate().translationY(imagesHorizontalList.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                        } else {
                            isShowing = true;
                            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                            imagesHorizontalList.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                        }
                    }

                    @Override
                    public void onOutsidePhotoTap() {

                    }
                });


                return false;
            }

        }).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
