package com.wpt.dota2box.ui.goods;

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
import com.wpt.dota2box.adapter.GoodsAdapter;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.util.GoodsDbUtil;

public class GoodsActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private Context mContext;

	private LinearLayout mTab1Layout;

	private LinearLayout mTab2Layout;

	private View mTabView1;

	private View mTabView2;

	private View mBasicLayout;

	private View mUpdateLayout;

	private GridView mBasicGridView;

	private GridView mUpdateGridView;

	private GoodsAdapter mAdapter;

	private int mTabIndex1 = 0;

	private int mTabIndex2 = 1;

	private int mCurrentTab = mTabIndex1;

	private List<List<String>> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_layout);

		mContext = GoodsActivity.this;
		initView();
		addAdView(this,R.id.goods_adview_layout);
	}
	private void initView() {

		mTab1Layout = (LinearLayout) findViewById(R.id.goods_top_tab1_layout);
		mTab2Layout = (LinearLayout) findViewById(R.id.goods_top_tab2_layout);

		mBasicLayout = (View) findViewById(R.id.goods_layout_tab_basic_layout);
		mUpdateLayout = (View) findViewById(R.id.goods_layout_tab_upate_layout);

		mTabView1 = (View) findViewById(R.id.goods_top_tab1_view);
		mTabView2 = (View) findViewById(R.id.goods_top_tab2_view);

		mBasicGridView = (GridView) findViewById(R.id.goods_basic_gridView);
		mUpdateGridView = (GridView) findViewById(R.id.goods_update_gridView);

		mTab1Layout.setOnClickListener(this);
		mTab2Layout.setOnClickListener(this);
		
		mBasicGridView.setOnItemClickListener(this);
		mUpdateGridView.setOnItemClickListener(this);

		setData(mCurrentTab);
	}

	private void setData(int tabIndex) {
		GoodsDbUtil util = new GoodsDbUtil();
		mList = util.getGoodsList(tabIndex);
		mAdapter = new GoodsAdapter(mContext, mList, tabIndex);
		if (tabIndex == mTabIndex1) {
			mBasicGridView.setAdapter(mAdapter);
		} else {
			mUpdateGridView.setAdapter(mAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goods_top_tab1_layout:
			mCurrentTab = mTabIndex1;
			setData(mCurrentTab);
			mBasicLayout.setVisibility(View.VISIBLE);
			mUpdateLayout.setVisibility(View.GONE);
			mTabView1.setVisibility(View.VISIBLE);
			mTabView2.setVisibility(View.INVISIBLE);
			break;
		case R.id.goods_top_tab2_layout:
			mCurrentTab = mTabIndex2;
			setData(mCurrentTab);
			mBasicLayout.setVisibility(View.GONE);
			mUpdateLayout.setVisibility(View.VISIBLE);
			mTabView1.setVisibility(View.INVISIBLE);
			mTabView2.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList != null && mList.size() > 0) {
			int index = 0;
			int remainder = 0;
			if (0 == mCurrentTab) {
				index = arg2 / 5;
				remainder = arg2 % 5;
			} else {
				index = arg2 / 6;
				remainder = arg2 % 6;
			}
			List<String> list = mList.get(remainder);
			if (list != null && list.size() > 0) {
				String itemsId = list.get(index);
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra("itemsId", itemsId);
				startActivity(intent);
			}
		}

	}
}
