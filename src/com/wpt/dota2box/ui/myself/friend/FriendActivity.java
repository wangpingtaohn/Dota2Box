package com.wpt.dota2box.ui.myself.friend;

import java.math.BigInteger;
import java.util.ArrayList;
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
import com.wpt.dota2box.adapter.FriendsAdapter;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.Friends;
import com.wpt.dota2box.net.MySelfNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.frame.widget.CustomToast;

public class FriendActivity extends BaseActivity implements OnItemClickListener,
		OnRefreshListener2<ListView> {

	private Context mContext;

	private PullToRefreshListView mFriendListView;

	private FriendsAdapter mfriendAdapter;

	private ProgressBar mBar;

	private List<Friends> mList;

	private int count = 10;

	private int startCount = 0;

	private List<String> mIdList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_layout);

		mContext = FriendActivity.this;
		initView();
		initHeader();
		getFrienIdList();
		addAdView(this, R.id.friend_list_adview_layout);
	}

	private void initView() {
		mFriendListView = (PullToRefreshListView) findViewById(R.id.friend_user_listview);
		mFriendListView.setOnRefreshListener(this);
		mFriendListView.setMode(Mode.BOTH);

		mBar = (ProgressBar) findViewById(R.id.friend_progressbar);

		mFriendListView.setOnItemClickListener(this);
	}

	private void initHeader() {
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);

		titleTv.setText("好友列表");

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getFrienIdList() {
		mBar.setVisibility(View.VISIBLE);
		new AsyncTask<Void, Void, List<String>>() {

			@Override
			protected List<String> doInBackground(Void... params) {
				MySelfNet net = new MySelfNet();
				String steamid = getIntent().getStringExtra("steamid");
				List<String> list = net.getFriendId(mContext, steamid);
				return list;
			}

			protected void onPostExecute(java.util.List<String> result) {
				if (result != null) {
					mIdList = result;
					getFrienListInfo();
				} else {
					CustomToast.showShortToast(mContext, "该用户暂时没有好友");
					mBar.setVisibility(View.GONE);
				}
			};

		}.execute();
	}

	private void getFrienListInfo() {
		new AsyncTask<Void, Void, List<Friends>>() {

			@Override
			protected List<Friends> doInBackground(Void... params) {
				MySelfNet net = new MySelfNet();
				if (mIdList != null) {
					if (mIdList.size() < 10) {
						return net.getFriendList(mContext, mIdList);
					} else {
						if (count - 9 <= mIdList.size()) {
							List<String> tempList = new ArrayList<String>();
							int len = mIdList.size() / count > 0?count:mIdList.size() % count;
							for (int i = startCount; i < len; i++) {
								tempList.add(mIdList.get(i));
							}
							return net.getFriendList(mContext, tempList);
						}
					}
				}
				return null;
			}

			protected void onPostExecute(java.util.List<Friends> result) {
				mFriendListView.onRefreshComplete();
				if (mBar.getVisibility() == View.VISIBLE) {
					mBar.setVisibility(View.GONE);
				}
				if (result != null) {
					if (mList == null) {
						mList = result;
					} else {
						mList.addAll(result);
					}
					if (mfriendAdapter == null) {
						mfriendAdapter = new FriendsAdapter(mContext, result);
						mFriendListView.setAdapter(mfriendAdapter);
					} else {
						mfriendAdapter.notifyDataSetChanged();
					}
				}
			};

		}.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList != null && mList.size() > 0) {
			Friends friends = mList.get(arg2 - 1);
			String id = friends.getSteamid();
			if (id != null && !"".equals(id)) {
				BigInteger bigId = new BigInteger(id);
				BigInteger addId = new BigInteger(Constants.ADD_ID);
				BigInteger ids = bigId.subtract(addId);
				Intent intent = new Intent(mContext, FriendDetailActivity.class);
				intent.putExtra("id", String.valueOf(ids));
				startActivity(intent);
			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		mFriendListView.onRefreshComplete();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (mIdList != null && mIdList.size() > count) {
			startCount += 10;
			count += 10;
			getFrienListInfo();
		}
	}
}
