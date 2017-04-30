package com.wpt.dota2box.ui.myself;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.RecordAdapter;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.net.MySelfNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.ui.hero.HeroListActivity;
import com.wpt.dota2box.ui.myself.friend.FriendActivity;
import com.wpt.dota2box.ui.myself.more.MoreActivity;
import com.wpt.dota2box.ui.myself.moreGames.GamesDetailActivity;
import com.wpt.dota2box.ui.myself.moreGames.MoreGamesActivity;
import com.wpt.dota2box.ui.search.SearchActivity;
import com.wpt.frame.util.FileUtil;
import com.wpt.frame.util.MyImageLoaderUtil;
import com.wpt.frame.util.MyRoundImageLoadListener;
import com.wpt.frame.widget.MyGridView;

public class MySelfActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private Context mContext;

	private TextView mPerTv;

	private TextView mTotalTv;
	
	private RelativeLayout mTotalLayout;
	
	private RelativeLayout includeLayout;
	
	private TextView mWinTv;
	
	private TextView mLossTv;
	
	private TextView nameTv;
	
	private TextView idTv;
	
	private TextView chooseTv;
	
	private ImageView avatarImg;
	
	private MyGridView mRecordGridView;
	
	private List<Map<String, String>> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myself_layout);

		mContext = MySelfActivity.this;
		initView();

		setWinRateDatas(null);
		setRecordDatas(null);
		
		String id = getIntent().getStringExtra("id");
		getWinRate(id);
		getRecords(id);
	}

	private void initView() {
		
		RelativeLayout moreLayout = (RelativeLayout)findViewById(R.id.myself_info_more_layout);
		includeLayout = (RelativeLayout)findViewById(R.id.myself_main_info_top_bar_layout);
		ImageView includeBackImg = (ImageView)findViewById(R.id.myself_main_info_top_bar_left_btn);
		
		mRecordGridView = (MyGridView) findViewById(R.id.myself_info_record_gridview);
		
		mTotalLayout = (RelativeLayout) findViewById(R.id.myself_main_game_total_layout);
		mWinTv = (TextView) findViewById(R.id.myself_main_game_win_text);
		mLossTv = (TextView) findViewById(R.id.myself_main_game_loss_text);
		
		TextView friendLayout = (TextView) findViewById(R.id.myself_main_frient_text);
		TextView gameLayout = (TextView) findViewById(R.id.myself_main_game_text);
		TextView heroLayout = (TextView) findViewById(R.id.myself_main_hero_text);
		
		mTotalTv = (TextView) findViewById(R.id.myself_main_game_total_text);
		mPerTv = (TextView) findViewById(R.id.myself_main_game_count_per);
		nameTv = (TextView) findViewById(R.id.myself_main_info_name);
		idTv = (TextView) findViewById(R.id.myself_main_id);
		chooseTv = (TextView) findViewById(R.id.myself_main_info_choose_tv);
		avatarImg = (ImageView) findViewById(R.id.myself_main_info_avatar);
		
		friendLayout.setOnClickListener(this);
		gameLayout.setOnClickListener(this);
		heroLayout.setOnClickListener(this);
		
		chooseTv.setOnClickListener(this);
		includeBackImg.setOnClickListener(this);
		moreLayout.setOnClickListener(this);
		
		mRecordGridView.setOnItemClickListener(this);
		
		String nameStr = getIntent().getStringExtra("name");
		String idStr = getIntent().getStringExtra("id");
		String urlStr = getIntent().getStringExtra("url");
		setUserInfo(nameStr,idStr,urlStr);

	}
	
	private void setRecordDatas(List<Map<String, String>>  list) {
		mList = list;
		if (list != null) {
			RecordAdapter adapter = new RecordAdapter(mContext, list);
			mRecordGridView.setAdapter(adapter);
		} else {
			String recordStr = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME, Constants.RECORD);
			if (recordStr != null) {
				MySelfNet net = new MySelfNet();
				list = net.analysisRecords(recordStr);
				setRecordDatas(list);
			}
		}
	}
	
	private void setUserInfo(String nameStr,String idStr,String urlStr) {
		if (nameStr == null) {
			nameStr = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME,
					Constants.NAME);
		}
		if (idStr == null) {
			idStr = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME,
					Constants.ID);
		}
		if (urlStr == null) {
			urlStr = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME,
					Constants.AVATAR_URL);
		}
		if (nameStr != null) {
			FileUtil.saveSharedPre(mContext, Constants.SP_NAME, Constants.NAME,
					nameStr);
			nameTv.setText(nameStr);
			chooseTv.setText("切换>");
		} else {
			chooseTv.setText("选择>");
		}
		if (idStr != null) {
			FileUtil.saveSharedPre(mContext, Constants.SP_NAME, Constants.ID,
					idStr);
			idTv.setText(idStr);
		}
		if (urlStr != null) {
			FileUtil.saveSharedPre(mContext, Constants.SP_NAME,
					Constants.AVATAR_URL, urlStr);
			MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
			util.displayImage(urlStr, avatarImg,new MyRoundImageLoadListener(
					MyRoundImageLoadListener.ROUND));
		}
		boolean isTabPage = getIntent().getBooleanExtra("isTabPage", true);
		if (!isTabPage) {
			includeLayout.setVisibility(View.VISIBLE);
		} else {
			includeLayout.setVisibility(View.GONE);
		}
	}

	private void getWinRate(final String idStr) {
		if (idStr != null) {
			new AsyncTask<Void, Void, Map<String, Object>>() {

				@Override
				protected Map<String, Object> doInBackground(Void... params) {
					MySelfNet net = new MySelfNet();
					return net.getUserInfo(mContext, idStr);
				}

				@Override
				protected void onPostExecute(
						java.util.Map<String, Object> result) {
					setWinRateDatas(result);
				};

			}.execute();
		}
	}
	private void getRecords(final String idStr) {
		if (idStr != null) {
			new AsyncTask<Void, Void, List<Map<String, String>>>() {
				
				@Override
				protected List<Map<String, String>> doInBackground(Void... params) {
					MySelfNet net = new MySelfNet();
					List<Map<String, String>>  list = net.getRecordInfo(mContext, idStr);
					return list;
				}
				
				@Override
				protected void onPostExecute(List<Map<String, String>> result) {
					setRecordDatas(result);
				};
				
			}.execute();
		}
	}

	private void setWinRateDatas(Map<String, Object> winmap) {
		if (winmap != null) {
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
			String per = numberFormat.format((float) winInt / (float) total* 100);
			mPerTv.setText(per + "%");
			FileUtil.saveSharedPre(mContext, Constants.SP_NAME,
					Constants.TOTAL, total + "");
			FileUtil.saveSharedPre(mContext, Constants.SP_NAME,
					Constants.WIN_RATE, per + "");
			int totalWidth = 120;
			int totalInt = total;
			float winRateInt = Float.parseFloat(per);
			float winWidth = (float)(((float)totalWidth / (float)totalInt) * (totalInt * (float) winRateInt / 100));
			int winIntWidth = Math.round(winWidth);
			LayoutParams totalLp = mTotalLayout.getLayoutParams();
			totalLp.width = totalWidth;
			mTotalLayout.setLayoutParams(totalLp);
			LayoutParams winLp = mWinTv.getLayoutParams();
			winLp.width = winIntWidth;
			mWinTv.setLayoutParams(winLp);
			LayoutParams lossLp = mLossTv.getLayoutParams();
			int lossWidth = totalWidth - winIntWidth;
			lossLp.width = lossWidth;
			mLossTv.setLayoutParams(lossLp);
		} else {
			String total = FileUtil.getSharedPreValue(mContext,
					Constants.SP_NAME, Constants.TOTAL);
			String winRate = FileUtil.getSharedPreValue(mContext,
					Constants.SP_NAME, Constants.WIN_RATE);
			if (total != null && winRate != null) {
				mTotalTv.setText(total + "场");
				mPerTv.setText(winRate + "%");
				int totalWidth = 120;
				int totalInt = Integer.parseInt(total);
				float winRateInt = Float.parseFloat(winRate);
				float winWidth = (float)(((float)totalWidth / (float)totalInt) * (totalInt * (float) winRateInt / 100));
				int winIntWidth = Math.round(winWidth);
				LayoutParams totalLp = mTotalLayout.getLayoutParams();
				totalLp.width = totalWidth;
				mTotalLayout.setLayoutParams(totalLp);
				LayoutParams winLp = mWinTv.getLayoutParams();
				winLp.width = winIntWidth;
				mWinTv.setLayoutParams(winLp);
				LayoutParams lossLp = mLossTv.getLayoutParams();
				int lossWidth = totalWidth - winIntWidth;
				lossLp.width = lossWidth;
				mLossTv.setLayoutParams(lossLp);
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.myself_main_info_choose_tv:
			intent = new Intent(mContext, SearchActivity.class);
			intent.putExtra("isSwitch", true);
			startActivityForResult(intent,0);
			break;
		case R.id.myself_main_info_top_bar_left_btn:
			finish();
			break;
		case R.id.myself_info_more_layout:
			intent = new Intent(mContext, MoreActivity.class);
			startActivity(intent);
			break;
		case R.id.myself_main_frient_text:
			String id = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME, Constants.ID);
			if (id != null && !"".equals(id.trim())) {
				BigInteger bigId = new BigInteger(id);
				BigInteger bigAddId = new BigInteger(Constants.ADD_ID);
				BigInteger steamIdBig = bigAddId.add(bigId);
				intent = new Intent(mContext, FriendActivity.class);
				intent.putExtra("steamid", steamIdBig.toString());
				startActivity(intent);
			}
			break;
		case R.id.myself_main_game_text:
			String myId = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME, Constants.ID);
			if (myId != null && !"".equals(myId.trim())) {
				intent = new Intent(mContext, MoreGamesActivity.class);
				intent.putExtra("id", myId);
				startActivity(intent);
			}
			break;
		case R.id.myself_main_hero_text:
			String idStr = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME, Constants.ID);
			if (idStr != null && !"".equals(idStr.trim())) {
				intent = new Intent(mContext, HeroListActivity.class);
				intent.putExtra("id", idStr);
				startActivity(intent);
			}
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			String nameStr = data.getStringExtra("name");
			String idStr = data.getStringExtra("id");
			String urlStr = data.getStringExtra("url");
			setUserInfo(nameStr, idStr, urlStr);
			getRecords(idStr);
			getWinRate(idStr);
			new Thread(){
				public void run() {
					MySelfNet net = new MySelfNet();
					net.postServer(mContext);
				};
			}.start();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList != null && mList.size() > arg2) {
			Map<String, String> map = mList.get(arg2);
			String matchid = map.get("matchid");
			Intent intent = new Intent(mContext, GamesDetailActivity.class);
			intent.putExtra("id", matchid);
			startActivity(intent);
		}
		
	}
}
