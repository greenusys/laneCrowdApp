package com.mzelzoghbi.zgallery.activities;


import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.mzelzoghbi.zgallery.Constants;
import com.mzelzoghbi.zgallery.CustomViewPager;
import com.mzelzoghbi.zgallery.OnImgClick;
import com.mzelzoghbi.zgallery.R;
import com.mzelzoghbi.zgallery.adapters.HorizontalListAdapters;
import com.mzelzoghbi.zgallery.adapters.ViewPagerAdapter;
import com.mzelzoghbi.zgallery.entities.ZColor;

/**
 * Created by mohamedzakaria on 8/11/16.
 */
public class ZGalleryActivity extends BaseActivity {
    private RelativeLayout mainLayout;

    CustomViewPager mViewPager;
    ViewPagerAdapter adapter;
    RecyclerView imagesHorizontalList;
    LinearLayoutManager mLayoutManager;
    HorizontalListAdapters hAdapter;
    private int currentPos;
    private ZColor bgColor;
    private String isImage="";


    @Override
    protected int getResourceLayoutId() {
        return R.layout.z_activity_gallery;
    }

    @Override
    protected void afterInflation() {
        // init layouts
        mainLayout = findViewById(R.id.mainLayout);
        mViewPager = findViewById(R.id.pager);
        imagesHorizontalList = findViewById(R.id.imagesHorizontalList);

        isImage = getIntent().getStringExtra("isImage");


        // get intent data
        currentPos = getIntent().getIntExtra(Constants.IntentPassingParams.SELECTED_IMG_POS, 0);
        bgColor = (ZColor) getIntent().getSerializableExtra(Constants.IntentPassingParams.BG_COLOR);

        if (bgColor == ZColor.WHITE) {
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        }

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        // pager adapter
        adapter = new ViewPagerAdapter(isImage,this, imageURLs, mToolbar, imagesHorizontalList);
        mViewPager.setAdapter(adapter);


       if(imageURLs.size()>1) {
           // horizontal list adaapter
           hAdapter = new HorizontalListAdapters(this, imageURLs, new OnImgClick() {
               @Override
               public void onClick(int pos) {
                   mViewPager.setCurrentItem(pos, true);
               }
           });
           imagesHorizontalList.setLayoutManager(mLayoutManager);
           imagesHorizontalList.setAdapter(hAdapter);
           hAdapter.notifyDataSetChanged();
           hAdapter.setSelectedItem(currentPos);
       }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                imagesHorizontalList.smoothScrollToPosition(position);
                hAdapter.setSelectedItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mViewPager.setCurrentItem(currentPos);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
