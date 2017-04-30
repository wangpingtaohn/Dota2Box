package com.wpt.dota2box.ui.myself.friend;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.GamesAdapter;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.Games;
import com.wpt.dota2box.net.MySelfNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.ui.hero.HeroListActivity;
import com.wpt.dota2box.ui.myself.moreGames.GamesDetailActivity;
import com.wpt.dota2box.ui.myself.moreGames.MoreGamesActivity;
import com.wpt.frame.util.DateUtils;
import com.wpt.frame.util.FileUtil;
import com.wpt.frame.util.MyImageLoaderUtil;
import com.wpt.frame.util.MyRoundImageLoadListener;

public class FriendDetailActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private Context mContext;

	private ImageView mAvatarImg;
	
	private ImageView mLevelImg;

	private TextView mNameTv;

	private TextView mTimeTv;

	private TextView mPerTv;

	private TextView mTotalTv;

	private RelativeLayout mTotalLayout;

	private TextView mWinLayoutTv;

	private TextView mLossLayoutTv;
	
	private ProgressBar mBar;
	
	private ListView mGameListView;
	
	private List<Games> mGameList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_detail_layout);
		mContext = FriendDetailActivity.this;

		initHeader();
		initView();
		String idStr = getIntent().getStringExtra("id");
		getUserInfo(idStr);
		addAdView(this, R.id.friend_detail_adview_layout);
	}

	private void initView() {

		mAvatarImg = (ImageView) findViewById(R.id.friend_detail_avatar);
		mLevelImg = (ImageView) findViewById(R.id.friend_detail_level_img);
		mNameTv = (TextView) findViewById(R.id.friend_detail_name);
		mTimeTv = (TextView) findViewById(R.id.friend_detail_time);
		mPerTv = (TextView) findViewById(R.id.friend_detail_game_count_per);
		mTotalTv = (TextView) findViewById(R.id.friend_detail_game_total_text);
		mLossLayoutTv = (TextView) findViewById(R.id.friend_detail_game_loss_text);
		mWinLayoutTv = (TextView) findViewById(R.id.friend_detail_game_win_text);
		mTotalLayout = (RelativeLayout) findViewById(R.id.friend_detail_game_total_layout);
		mBar = (ProgressBar) findViewById(R.id.friend_detail_progressbar);
		TextView moreGameTv = (TextView) findViewById(R.id.friend_detail_more_game_layout);
		TextView friendLayout = (TextView) findViewById(R.id.friend_detail_frient_text);
		TextView heroLayout = (TextView) findViewById(R.id.friend_detail_hero_text);
		
		mGameListView = (ListView) findViewById(R.id.friend_detail_listview);
		mGameListView.setFocusable(false);
		mGameListView.setOnItemClickListener(this);

		moreGameTv.setOnClickListener(this);
		friendLayout.setOnClickListener(this);
		heroLayout.setOnClickListener(this);
	}
	private void initHeader() {
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.GONE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void getUserInfo(final String idStr) {
		if (idStr != null) {
			mBar.setVisibility(View.VISIBLE);
			new AsyncTask<Void, Void, Map<String, Object>>() {

				@Override
				protected Map<String, Object> doInBackground(Void... params) {
					MySelfNet net = new MySelfNet();
					return net.getUserInfo(mContext, idStr);
				}

				@Override
				protected void onPostExecute(Map<String, Object> result) {
					mBar.setVisibility(View.GONE);
					if (result != null) {
						setUserInfoDatas(result);
						List<Games> list = (List<Games>) result.get("gamesList");
						mGameList = list;
						if (list == null || list.size() == 0) {
							showEmptyGamesInfoDialog();
						}
						GamesAdapter gamesAdapter = new GamesAdapter(mContext, list);
						mGameListView.setAdapter(gamesAdapter);
						
					}
				};

			}.execute();
		}
	}
	
	private void showEmptyGamesInfoDialog(){
		Builder alart = new AlertDialog.Builder(mContext);
		alart.setTitle("提示");
		alart.setMessage("开启：游戏--设置--游戏选项--个人资料--公开比赛数据");
		alart.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		alart.show();
	}

	private void setUserInfoDatas(Map<String, Object> winmap) {
		String name = (String) winmap.get("name");
		String timeStr = (String) winmap.get("time");
		if (timeStr != null && !"".equals(timeStr)) {
			timeStr = timeStr.replace("T", " ");
			int index = timeStr.indexOf("+");
			if(-1 != index){
				timeStr = timeStr.substring(0, index);
			}
			long timeL = (System.currentTimeMillis() - DateUtils.timeStrToLong(timeStr)) / (1000*60);
			String timeS = DateUtils.getLastTime(String.valueOf(timeL));
			if (timeS == null || "".equals(timeS)) {
				timeS = DateUtils.getMsgTime(timeStr);
			}
			timeStr = timeS;
		}
		String url = (String) winmap.get("url");
		if (url != null) {
			MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
			util.displayImage(url, mAvatarImg, new MyRoundImageLoadListener(
					MyRoundImageLoadListener.ROUND));
		}
		mNameTv.setText(name);
		mTimeTv.setText(timeStr);
		String win = (String) winmap.get("win");
		String loss = (String) winmap.get("loss");
		String abandon = (String) winmap.get("abandon");
		int winInt = 0;
		int lossInt = 0;
		int abandonInt = 0;
		if (win != null) {
			winInt = Integer.parseInt(win.replace(",", ""));
		}
		if (loss != null) {
			lossInt = Integer.parseInt(loss.replace(",", ""));
		}
		if (abandon != null) {
			abandonInt = Integer.parseInt(abandon.replace(",", ""));
		}
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		int total = winInt + lossInt + abandonInt;
		mTotalTv.setText(total + "场");
		String per = numberFormat.format((float) winInt / (float) total * 100);
		mPerTv.setText(per + "%");
		int totalWidth = 120;
		int totalInt = total;
		float winRateInt = Float.parseFloat(per);
		float winWidth = (float) (((float) totalWidth / (float) totalInt) * (totalInt
				* (float) winRateInt / 100));
		int winIntWidth = Math.round(winWidth);
		LayoutParams totalLp = mTotalLayout.getLayoutParams();
		totalLp.width = totalWidth;
		mTotalLayout.setLayoutParams(totalLp);
		LayoutParams winLp = mWinLayoutTv.getLayoutParams();
		winLp.width = winIntWidth;
		mWinLayoutTv.setLayoutParams(winLp);
		LayoutParams lossLp = mLossLayoutTv.getLayoutParams();
		int lossWidth = totalWidth - winIntWidth;
		lossLp.width = lossWidth;
		mLossLayoutTv.setLayoutParams(lossLp);
		
		int level = getUserGrad(winInt, winRateInt);
		Drawable drawable = null;
		switch (level) {
		case 1:
			drawable = getResources().getDrawable(R.drawable.grad);
			break;
		case 2:
			drawable = getResources().getDrawable(R.drawable.grad2);
			break;
		case 3:
			drawable = getResources().getDrawable(R.drawable.grad3);
			break;
		case 4:
			drawable = getResources().getDrawable(R.drawable.grad4);
			break;
		case 5:
			drawable = getResources().getDrawable(R.drawable.grad5);
			break;
		}
		if (drawable != null) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			Bitmap bitmap = bd.getBitmap();
			mLevelImg.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_detail_more_game_layout:
			Intent intent = new Intent(mContext, MoreGamesActivity.class);
			String idStr = getIntent().getStringExtra("id");
			intent.putExtra("id", idStr);
			startActivity(intent);
			break;
		case R.id.friend_detail_frient_text:
			Intent friendIntent = new Intent(mContext, FriendActivity.class);
			String id = getIntent().getStringExtra("id");
			BigInteger bigId = new BigInteger(id);
			BigInteger bigAddId = new BigInteger(Constants.ADD_ID);
			BigInteger steamIdBig = bigAddId.add(bigId);
			friendIntent.putExtra("steamid", String.valueOf(steamIdBig));
			startActivity(friendIntent);
			break;
		case R.id.friend_detail_hero_text:
			Intent heroIntent = new Intent(mContext, HeroListActivity.class);
			String heroId = getIntent().getStringExtra("id");
			heroIntent.putExtra("id", heroId);
			startActivity(heroIntent);
			break;
		}

	}

	private int getUserGrad(int count, float rate) {
		if (count < 200) {
			return 1;
		} else if (count >= 200 && count < 500) {
			if (rate < 0.4) {
				return 1;
			} else if (rate < 0.5) {
				return 2;
			} else if (rate < 0.55) {
				return 3;
			}
		} else if (count >= 500) {
			if (rate < 0.4) {
				return 1;
			} else if (rate < 0.5) {
				return 2;
			} else if (rate < 0.55) {
				return 3;
			} else if (rate < 0.59) {
				return 4;
			} else
				return 5;
		}
		return 1;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mGameList != null && mGameList.size() > arg2) {
			Games games = mGameList.get(arg2);
			String matchid = games.getMatchid();
			Intent intent = new Intent(mContext, GamesDetailActivity.class);
			intent.putExtra("id", matchid);
			startActivity(intent);
		}
		
	}
}
