package com.wpt.dota2box.ui.hero;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.HeroAdapter;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.util.HeroDbUtil;

public class HeroActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private Context mContext;
	
	private LinearLayout mTab1Layout;
	
	private LinearLayout mTab2Layout;
	
	private LinearLayout mTab3Layout;
	
	private View mTabView1;
	
	private View mTabView2;
	
	private View mTabView3;
	
	private GridView mGridView;
	
	private HeroAdapter mAdapter;
	
	private int mTabIndex1 = 1; 
	
	private int mTabIndex2 = 2; 
	
	private int mTabIndex3 = 3; 
	
	private List<String> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hero_layout);
		
		mContext = HeroActivity.this;
		initView();
		addAdView(this,R.id.hero_adview_layout);
	}
	
	private void initView() {
		
		mTab1Layout = (LinearLayout)findViewById(R.id.hero_top_tab1_layout);
		mTab2Layout = (LinearLayout)findViewById(R.id.hero_top_tab2_layout);
		mTab3Layout = (LinearLayout)findViewById(R.id.hero_top_tab3_layout);
		
		mGridView = (GridView)findViewById(R.id.hero_gridView);
		
		mTabView1 = (View)findViewById(R.id.hero_top_tab1_view);
		mTabView2 = (View)findViewById(R.id.hero_top_tab2_view);
		mTabView3 = (View)findViewById(R.id.hero_top_tab3_view);
		
		mTab1Layout.setOnClickListener(this);
		mTab2Layout.setOnClickListener(this);
		mTab3Layout.setOnClickListener(this);
		
		mGridView.setOnItemClickListener(this);
		
		setData(mTabIndex1);
	}
	
	private void setData(int tabIndex) {
		HeroDbUtil util = new HeroDbUtil();
		mList = util.getHeroList(tabIndex);
		mAdapter = new HeroAdapter(mContext, mList);
		mGridView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hero_top_tab1_layout:
			setData(mTabIndex1);
			mTabView1.setVisibility(View.VISIBLE);
			mTabView2.setVisibility(View.GONE);
			mTabView3.setVisibility(View.GONE);
			break;
		case R.id.hero_top_tab2_layout:
			setData(mTabIndex2);
			mTabView1.setVisibility(View.GONE);
			mTabView2.setVisibility(View.VISIBLE);
			mTabView3.setVisibility(View.GONE);
			break;
		case R.id.hero_top_tab3_layout:
			setData(mTabIndex3);
			mTabView1.setVisibility(View.GONE);
			mTabView2.setVisibility(View.GONE);
			mTabView3.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList != null &&  mList.size() > 0) {
			String heroId = mList.get(arg2);
			Intent intent = new Intent(mContext, HeroDetailActivity.class);
			intent.putExtra("heroId", heroId);
			startActivity(intent);
		}
		
	}
}
