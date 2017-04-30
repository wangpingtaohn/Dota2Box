package com.wpt.dota2box.ui.news;

import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.HeroDetailGalleryAdapter;
import com.wpt.dota2box.net.HeroNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.frame.util.MyImageLoaderUtil;
import com.wpt.frame.widget.CustomToast;

public class SalesDetailActivity extends BaseActivity {

	private GridView mGridView;

	private TextView mNameTv;

	private TextView mXyduTv;

	private TextView mPriceTv;

	private ImageView mAvatarImg;

	private ProgressBar proBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_detail_layout);
		initHeader();
		initView();
		String idUrl = getIntent().getStringExtra("id");
		if (idUrl == null || "".equals(idUrl)) {
			getIdUrl();
		} else {
			getSalesInfo(idUrl);
		}
	}

	private void initView() {
		mGridView = (GridView) findViewById(R.id.sales_detail_gridview);
		mAvatarImg = (ImageView) findViewById(R.id.sales_detail_avatar);
		mNameTv = (TextView) findViewById(R.id.sales_detail_name);
		mXyduTv = (TextView) findViewById(R.id.sales_detail_xiyoudu_text);
		mPriceTv = (TextView) findViewById(R.id.sales_detail_shop_price);
		proBar = (ProgressBar) findViewById(R.id.sales_detail_progressbar);
	}

	private void getIdUrl() {
		String name = getIntent().getStringExtra("name");
		HeroNet net = new HeroNet();
		net.getShipin(mContext, handler, name);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			List<Map<String, String>> list = (List<Map<String, String>>) msg.obj;
			if (list != null && list.size() > 0) {
				Map<String, String> map = list.get(0);
				String idUrl = map.get("idUrl");
				if (idUrl != null && !"".equals(idUrl)) {
					getSalesInfo(idUrl);
				}
			}
		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(null);
	};

	private void initHeader() {
		String title = getIntent().getStringExtra("name");
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);

		titleTv.setText(title);

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getSalesInfo(final String idUrl) {
		new AsyncTask<Void, Void, Map<String, Object>>() {

			@Override
			protected Map<String, Object> doInBackground(Void... params) {
				HeroNet net = new HeroNet();
				return net.getShipinDetail(mContext, idUrl);
			}

			protected void onPostExecute(Map<String, Object> result) {
				proBar.setVisibility(View.GONE);
				if (result != null) {
					setSalesInfo(result);
				} else {
					CustomToast.showShortToast(mContext, "暂时搜不到该套装的相关信息");
				}
			};

		}.execute();
	}

	@SuppressWarnings({ "unused", "unchecked" })
	private void setSalesInfo(Map<String, Object> result) {
		if (result.containsKey("goodsArray")) {
			List<Map<String, String>> list = (List<Map<String, String>>) result
					.get("goodsArray");
			HeroDetailGalleryAdapter adapter = new HeroDetailGalleryAdapter(
					mContext, list);
			mGridView.setAdapter(adapter);
		}
//		String name = (String) result.get("name");
		String url = (String) result.get("url");
		String hero = (String) result.get("hero");
		String level = (String) result.get("level");
		String price = (String) result.get("price");
		mNameTv.setText(hero);
		mPriceTv.setText(price);
		mXyduTv.setText(level);
		if (url != null) {
			MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
			util.displayImage(url, mAvatarImg);
		}
	}
}
