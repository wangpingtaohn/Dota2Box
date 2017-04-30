package com.wpt.dota2box.ui.news.wallpaper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.WallpaperItemAdapter;
import com.wpt.dota2box.net.NewsNet;

/**
 * @Description:手机壁纸页
 * @author wpt
 * @since 2013-6-8 下午4:34:37
 */
public class WallpaperActivity extends Activity implements OnClickListener{

	private GridView mGridView;

	private View mLoadLayout;

	private List<String> mWallpaperList;
	
	private WallpaperItemAdapter mAdapter;

	private Context mContext;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_layout);

		mContext = WallpaperActivity.this;
		initView();
		initHeader();
	}

	@Override
	protected void onResume() {
		super.onResume();
		new GetWallpaperTask().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView() {

		mGridView = (GridView) findViewById(R.id.wallpaper_gridview);
		mLoadLayout = findViewById(R.id.download_wallpaper_loading_layout);
		
		setOnItemOclick();
	}

	private void setOnItemOclick() {
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				Intent intent = new Intent(mContext,
						WallpaperDetailActivity.class);
				Bundle bundle = new Bundle();
				String[] imageS = new String[mWallpaperList.size()];
				for (int i = 0; i < imageS.length; i++) {
					imageS[i] = mWallpaperList.get(i);
				}
				intent.putExtra("imageUrls", imageS);
				intent.putExtra("position", position);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
	}

	private void initHeader() {
		ImageView backBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);

		titleTv.setVisibility(View.GONE);
		rightBtn.setVisibility(View.GONE);
		backBtn.setOnClickListener(this);
	}

	private class GetWallpaperTask extends
			AsyncTask<Integer, Object, List<String>> {

		@Override
		protected List<String> doInBackground(Integer... arg0) {
			NewsNet net = new NewsNet();
			return net.getWallpaper(mContext);
		}
		
		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);
			hideLoading();
			if(result != null){
				mWallpaperList = result;
				setAdapter(result);
			}
		}
	}

	private void setAdapter(List<String> list) {
		if (mAdapter == null) {
			mAdapter = new WallpaperItemAdapter(mContext,
					mWallpaperList);
			mGridView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void hideLoading() {
		mLoadLayout.setVisibility(View.GONE);
		mGridView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.include_main_top_bar_left_btn:
			finish();
			break;
		default:
			break;
		}
	}

}
