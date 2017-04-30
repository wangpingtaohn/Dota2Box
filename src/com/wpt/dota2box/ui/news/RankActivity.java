package com.wpt.dota2box.ui.news;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.RankAdapter;
import com.wpt.dota2box.model.Rank;
import com.wpt.dota2box.net.SearchNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.ui.myself.friend.FriendDetailActivity;

public class RankActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private Context mContext;
	
	private LinearLayout mTab1Layout;
	
	private LinearLayout mTab2Layout;
	
	private LinearLayout mTab3Layout;
	
	private LinearLayout mTab4Layout;
	
	private View mTabView1;
	
	private View mTabView2;
	
	private View mTabView3;
	
	private View mTabView4;
	
	private ListView mListView;
	
	private RankAdapter mAdapter;
	
	private final int mTabIndex1 = 1; 
	
	private final int mTabIndex2 = 2; 
	
	private final int mTabIndex3 = 3; 
	
	private final int mTabIndex4 = 4;
	
	private int mCurrentTab = mTabIndex1;
	
	private List<Rank> mChList;
	
	private List<Rank> mAseSiaList;
	
	private List<Rank> mEuropeList;
	
	private List<Rank> mUSAList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rank_layout);
		
		mContext = RankActivity.this;
		initHeader();
		initView();
		addAdView(this,R.id.rank_adview_layout);
	}
	
	private void initView() {
		
		mTab1Layout = (LinearLayout)findViewById(R.id.rank_top_tab1_layout);
		mTab2Layout = (LinearLayout)findViewById(R.id.rank_top_tab2_layout);
		mTab3Layout = (LinearLayout)findViewById(R.id.rank_top_tab3_layout);
		mTab4Layout = (LinearLayout)findViewById(R.id.rank_top_tab4_layout);
		
		mListView = (ListView)findViewById(R.id.rank_listview);
		
		mTabView1 = (View)findViewById(R.id.rank_top_tab1_view);
		mTabView2 = (View)findViewById(R.id.rank_top_tab2_view);
		mTabView3 = (View)findViewById(R.id.rank_top_tab3_view);
		mTabView4 = (View)findViewById(R.id.rank_top_tab4_view);
		
		mTab1Layout.setOnClickListener(this);
		mTab2Layout.setOnClickListener(this);
		mTab3Layout.setOnClickListener(this);
		mTab4Layout.setOnClickListener(this);
		
		mListView.setOnItemClickListener(this);
		
		getRankList();
	}
	private void initHeader() {
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);
		
		titleTv.setText("关于");
		
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void getRankList() {
		new AsyncTask<Void, Void, Map<String, List<Rank>>>(){

			@Override
			protected Map<String, List<Rank>> doInBackground(Void... params) {
				SearchNet util = new SearchNet();
				return util.getRankList(mContext);
			}
			
			protected void onPostExecute(Map<String, List<Rank>> result) {
				if (result != null) {
					mChList = result.get("ch");
					mAseSiaList = result.get("se");
					mEuropeList = result.get("eu");
					mUSAList = result.get("use");
					setData(mTabIndex1);
				}
			};
			
		}.execute();
		
	}
	
	private void setData(int tabIndex) {
		List<Rank> list = null;
		switch (tabIndex) {
		case mTabIndex1:
			list = mChList;
			break;
		case mTabIndex2:
			list = mAseSiaList;
			break;
		case mTabIndex3:
			list = mEuropeList;
			break;
		case mTabIndex4:
			list = mUSAList;
			break;
		}
		mAdapter = new RankAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rank_top_tab1_layout:
			setData(mTabIndex1);
			mTabView1.setVisibility(View.VISIBLE);
			mTabView2.setVisibility(View.GONE);
			mTabView3.setVisibility(View.GONE);
			mTabView4.setVisibility(View.GONE);
			mCurrentTab = mTabIndex1;
			break;
		case R.id.rank_top_tab2_layout:
			setData(mTabIndex2);
			mTabView1.setVisibility(View.GONE);
			mTabView2.setVisibility(View.VISIBLE);
			mTabView3.setVisibility(View.GONE);
			mTabView4.setVisibility(View.GONE);
			mCurrentTab = mTabIndex2;
			break;
		case R.id.rank_top_tab3_layout:
			setData(mTabIndex3);
			mTabView1.setVisibility(View.GONE);
			mTabView2.setVisibility(View.GONE);
			mTabView3.setVisibility(View.VISIBLE);
			mTabView4.setVisibility(View.GONE);
			mCurrentTab = mTabIndex3;
			break;
		case R.id.rank_top_tab4_layout:
			setData(mTabIndex4);
			mTabView1.setVisibility(View.GONE);
			mTabView2.setVisibility(View.GONE);
			mTabView3.setVisibility(View.GONE);
			mTabView4.setVisibility(View.VISIBLE);
			mCurrentTab = mTabIndex4;
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		List<Rank> list = null;
		switch (mCurrentTab) {
		case mTabIndex1:
			list = mChList;
			break;
		case mTabIndex2:
			list = mAseSiaList;
			break;
		case mTabIndex3:
			list = mEuropeList;
			break;
		case mTabIndex4:
			list = mUSAList;
			break;
		}
		if (list != null &&list.size() > 0) {
			Rank rank = list.get(arg2);
			String id = rank.getAccountId();
			Intent intent = new Intent(mContext, FriendDetailActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}
		
	}
}
