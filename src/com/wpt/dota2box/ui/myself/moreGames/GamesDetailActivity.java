package com.wpt.dota2box.ui.myself.moreGames;

import java.math.BigInteger;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.GamesDetailAdapter;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.GamesDetail;
import com.wpt.dota2box.net.GamesNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.ui.myself.friend.FriendDetailActivity;
import com.wpt.frame.widget.CustomToast;

public class GamesDetailActivity extends BaseActivity implements OnItemClickListener {

	private Context mContext;

	private ListView mListView;

	private ProgressBar mBar;
	
	private List<GamesDetail> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_detail_list_layout);
		mContext = GamesDetailActivity.this;
		initView();
		initHeader();
		getGamesDetailList();
		addAdView(this, R.id.games_detail_adview_layout);
	}
	
	private void initView() {
		mListView = (ListView) findViewById(R.id.games_detail_list_layout_listview);
		mBar = (ProgressBar) findViewById(R.id.games_detail_list_progressbar);
		mListView.setOnItemClickListener(this);
	}

	private void getGamesDetailList() {
		mBar.setVisibility(View.VISIBLE);
		final String id = getIntent().getStringExtra("id");
		new AsyncTask<Void, Void, List<GamesDetail>>() {

			@Override
			protected List<GamesDetail> doInBackground(Void... params) {
				GamesNet net = new GamesNet();
				return net.getGamesDetailInfo(mContext, id);
			}

			protected void onPostExecute(List<GamesDetail> result) {
				mBar.setVisibility(View.GONE);
				if (result != null) {
					mList = result;
					GamesDetailAdapter adapter = new GamesDetailAdapter(mContext, result);
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

		titleTv.setText("比赛详情");

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		if(mList != null && mList.size() > 0){
			GamesDetail games = mList.get(arg2);
			String id = games.getId();
			String name = games.getName();
			if (name != null && id != null && !"".equals(id)) {
				Intent intent = new Intent(mContext, FriendDetailActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		}
	}
}
