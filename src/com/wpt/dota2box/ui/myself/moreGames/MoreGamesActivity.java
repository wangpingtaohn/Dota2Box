package com.wpt.dota2box.ui.myself.moreGames;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.MoreGamesAdapter;
import com.wpt.dota2box.model.Games;
import com.wpt.dota2box.net.GamesNet;
import com.wpt.dota2box.ui.BaseActivity;

public class MoreGamesActivity extends BaseActivity  implements OnItemClickListener,
OnRefreshListener2<ListView> {

	private PullToRefreshListView mMoreGamesListView;
	
	private MoreGamesAdapter mAdapter;
	
	private List<Games> mList;
	
	private ProgressBar mBar;
	
	private Context mContext;
	
	private int mPage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_layout);
		
		mContext = MoreGamesActivity.this;
		initView();
		initHeader();
		mBar.setVisibility(View.VISIBLE);
		getMoreGamesList();
		addAdView(this, R.id.games_list_adview_layout);
	}
	
	private void initView() {
		mMoreGamesListView = (PullToRefreshListView) findViewById(R.id.more_games_layout_listview);
		mMoreGamesListView.setOnRefreshListener(this);
		mMoreGamesListView.setMode(Mode.BOTH);

		mBar = (ProgressBar) findViewById(R.id.more_games_progressbar);

		mMoreGamesListView.setOnItemClickListener(this);
	}

	private void getMoreGamesList() {
		final String id = getIntent().getStringExtra("id");
		new AsyncTask<Void, Void, List<Games>>() {

			@Override
			protected List<Games> doInBackground(Void... params) {
				GamesNet net = new GamesNet();
				return net.getMoreGameList(mContext, id, mPage + "");
			}

			protected void onPostExecute(List<Games> result) {
				mMoreGamesListView.onRefreshComplete();
				mBar.setVisibility(View.GONE);
				if (result != null) {
					if (mList == null) {
						mList = result;
					} else {
						mList.addAll(result);
					}
					if (mAdapter == null) {
						mAdapter = new MoreGamesAdapter(mContext, mList);
						mMoreGamesListView.setAdapter(mAdapter);
					} else {
						mAdapter.notifyDataSetChanged();
					}
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

		titleTv.setText("比赛列表");

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (mList != null) {
			mList = null;
		}
		mPage = 1;
		mAdapter = null;
		getMoreGamesList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		mPage++;
		getMoreGamesList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList != null && mList.size() > 0) {
			Games games = mList.get(arg2 - 1);
			String id = games.getMatchid();
			Intent intent = new Intent(mContext, GamesDetailActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);
		}
	}
}
