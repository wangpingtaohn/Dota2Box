package com.wpt.dota2box.ui;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;


public class WhatsnewActivity extends Activity {
	
	private ViewPager mViewPager;	
	private Button enterBtn;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        boolean showDesc = sharedPreferences.getBoolean(Constants.SHOW_DESC, true);
        if (!showDesc) {
        	Intent intent = new Intent();
    		intent.setClass(WhatsnewActivity.this,MainActivity.class);
    		startActivity(intent);
    		this.finish();
		}
        setContentView(R.layout.whatsnew_viewpager);
        mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager);        
        
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.whats1, null);
        View view2 = mLi.inflate(R.layout.whats2, null);
        View view3 = mLi.inflate(R.layout.whats3, null);
        View view4 = mLi.inflate(R.layout.whats4, null);
        View view5 = mLi.inflate(R.layout.whats5, null);
        
//        enterBtn = (Button) view4.findViewById(R.id.enter_button);
//        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
//        int height = currDisplay.getHeight();
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		layoutParams.bottomMargin = height/3;
//		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		enterBtn.setLayoutParams(layoutParams);
        
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);
        
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
    }    
    

    public void toMain(View v) {  
    	SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
    	sharedPreferences.edit().putBoolean(Constants.SHOW_DESC, false).commit();
      	Intent intent = new Intent();
		intent.setClass(WhatsnewActivity.this,MainActivity.class);
		startActivity(intent);
		this.finish();
      }  
    
}
