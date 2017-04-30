package com.wpt.dota2box.ui.hero;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.HeroListAdapter;
import com.wpt.dota2box.model.HeroList;
import com.wpt.dota2box.net.HeroNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.frame.widget.CustomToast;

public class HeroListActivity extends BaseActivity {

	private Context mContext;

	private ListView mListView;

	private ProgressBar mBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hero_list_layout);
		mContext = HeroListActivity.this;
		initView();
		initHeader();
		getHeroList();
		addAdView(this, R.id.hero_list_adview_layout);
	}
	
	private void initView() {
		mListView = (ListView) findViewById(R.id.hero_list_layout_listview);
		mBar = (ProgressBar) findViewById(R.id.hero_list_progressbar);
	}

	private void getHeroList() {
		mBar.setVisibility(View.VISIBLE);
		final String id = getIntent().getStringExtra("id");
		new AsyncTask<Void, Void, List<HeroList>>() {

			@Override
			protected List<HeroList> doInBackground(Void... params) {
				HeroNet net = new HeroNet();
				return net.getHeroList(mContext, id);
			}

			protected void onPostExecute(List<HeroList> result) {
				mBar.setVisibility(View.GONE);
				if (result != null) {
					HeroListAdapter adapter = new HeroListAdapter(mContext, result);
					mListView.setAdapter(adapter);
				} else {
					CustomToast.showShortToast(mContext, "暂时搜不到跟该用户相关的英雄信息");
				}
			};

		}.execute();
	}
	private void initHeader() {
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);

		titleTv.setText("英雄战绩");

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
