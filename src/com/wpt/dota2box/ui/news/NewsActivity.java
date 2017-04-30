package com.wpt.dota2box.ui.news;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.NewsAdapter;
import com.wpt.dota2box.adapter.WallpaperItemAdapter;
import com.wpt.dota2box.model.News;
import com.wpt.dota2box.net.NewsNet;
import com.wpt.dota2box.net.SearchNet;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.ui.news.wallpaper.WallpaperActivity;
import com.wpt.dota2box.ui.search.SearchActivity;
import com.wpt.frame.util.BitmapUtil;
import com.wpt.frame.util.DateUtils;
import com.wpt.frame.util.MyImageLoaderUtil;
import com.wpt.frame.widget.WebActivity;

public class NewsActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private Context mContext;

	private ListView mNewsListView;

	private NewsAdapter mNewsAdapter;

	private ImageView mSearchImg;

	private LinearLayout searchLayout;

	private ImageView mHeroAvatarImg;

	private ImageView starTv3;
	private ImageView starTv4;
	private ImageView starTv5;

	private ProgressBar mProgressBar;

	private TextView onlineNumberTv;

	public static final int GET_NEWS_WHAT = 0;

	public static final int GET_ONLINE_WHAT = 1;

	public static final int SET_TODAY_PRICE_TIME = 2;

	private List<News> mNewsList;

	private ScrollView scrollView;

	private ImageView mPriceImg;

	private TextView mPriceTv;

	private TextView mPriceNameTv;

	private TextView mPriceTimeTv1;
	private TextView mPriceTimeTv2;
	private TextView mPriceTimeTv3;
	private TextView mPriceTimeTv4;
	private TextView mPriceTimeTv5;
	private TextView mPriceTimeTv6;

	private TextView mPricePerTv;

	private int mHours;

	private int mMinutes;

	private int mSecondes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_layout);

		mContext = NewsActivity.this;
		initView();
		addAdView(this, R.id.new_adview_layout);

	}

	@Override
	protected void onResume() {
		super.onResume();
		scrollView.smoothScrollTo(0, 0);
	}

	private void initView() {

		mNewsListView = (ListView) findViewById(R.id.news_listview);
		mProgressBar = (ProgressBar) findViewById(R.id.news_progressbar);

		starTv3 = (ImageView) findViewById(R.id.news_chat_star_img3);
		starTv4 = (ImageView) findViewById(R.id.news_chat_star_img4);
		starTv5 = (ImageView) findViewById(R.id.news_chat_star_img5);
		TextView searchEt = (TextView) findViewById(R.id.search_user_et);
		TextView rankTv = (TextView) findViewById(R.id.news_rank_text);
		TextView wallpaperMoreTv = (TextView) findViewById(R.id.news_wallpaper_more_text);
		mPriceImg = (ImageView) findViewById(R.id.news_price_img);
		mPriceTv = (TextView) findViewById(R.id.news_price_layout_price);
		mPriceNameTv = (TextView) findViewById(R.id.news_price_layout_name);
		LinearLayout salesPriceLayout = (LinearLayout) findViewById(R.id.news_sales_price_layout);
		mPriceTimeTv1 = (TextView) findViewById(R.id.news_price_layout_time1);
		mPriceTimeTv2 = (TextView) findViewById(R.id.news_price_layout_time2);
		mPriceTimeTv3 = (TextView) findViewById(R.id.news_price_layout_time3);
		mPriceTimeTv4 = (TextView) findViewById(R.id.news_price_layout_time4);
		mPriceTimeTv5 = (TextView) findViewById(R.id.news_price_layout_time5);
		mPriceTimeTv6 = (TextView) findViewById(R.id.news_price_layout_time6);
		mPricePerTv = (TextView) findViewById(R.id.news_price_layout_per);

		onlineNumberTv = (TextView) findViewById(R.id.news_online_counter_text);

		mSearchImg = (ImageView) findViewById(R.id.news_search_img);
		searchLayout = (LinearLayout) findViewById(R.id.news_search_layout);
		mHeroAvatarImg = (ImageView) findViewById(R.id.news_hero_avatar_img);

		scrollView = (ScrollView) findViewById(R.id.search_layout_scrollView);

		mSearchImg.setOnClickListener(this);
		searchLayout.setOnClickListener(this);
		searchEt.setOnClickListener(this);
		rankTv.setOnClickListener(this);
		wallpaperMoreTv.setOnClickListener(this);
		salesPriceLayout.setOnClickListener(this);

		SearchNet net = new SearchNet();
		net.getOnlineNumber(mContext, handler);

		NewsNet newsNet = new NewsNet();
		newsNet.getPlist(mContext, handler);
		getTodayPrice();

		mNewsListView.setOnItemClickListener(this);

		showStar();
	}

	private void getTodayPrice() {
		new AsyncTask<Void, Void, Map<String, String>>() {

			@Override
			protected Map<String, String> doInBackground(Void... params) {
				NewsNet net = new NewsNet();
				return net.getTodayPrice(mContext);
			}

			protected void onPostExecute(Map<String, String> result) {
				setTodayPrice(result);
			};

		}.execute();
	}

	private void setTodayPrice(Map<String, String> result) {
		if (result != null) {
			String name = result.get("itemNameBase");
			mPriceNameTv.setText(name);
			String per = result.get("discountTagBase");
			mPricePerTv.setText(per);
			String price = result.get("discountedPrice");
			mPriceTv.setText(price);
			String url = result.get("itemImageDropShadow");
			if (null != url) {
				MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
				util.displayImage(url, mPriceImg);
			}
			String remainingTimeStr = result.get("remainingTime");
			long remainingTime = -1;
			if (null != remainingTimeStr) {
				remainingTime = Long.parseLong(remainingTimeStr);
			}
			long updateTime = -1;
			String updateTimeStr = result.get("updateTime");
			if (null != updateTimeStr) {
				updateTime = Long.parseLong(updateTimeStr);
			}
			long curTime = System.currentTimeMillis();
			long time = (remainingTime - (curTime - updateTime) / 1000) * 1000;
			String hours = DateUtils.mSecondsToHour(time);
			String minutes = DateUtils.mSecondsToMinute(time);
			String seconds = DateUtils.mSecondsToseconds(time);

			setTodayTimeView(hours, minutes, seconds);
			handler.sendEmptyMessageDelayed(SET_TODAY_PRICE_TIME, 1000);
		}
	}

	private void showStar() {
		// long time = -1;
		// long curTime = System.currentTimeMillis();
		// String luckHeroTime = FileUtil.getSharedPreValue(mContext,
		// Constants.SP_NAME, Constants.LUCK_HERO_TIME);
		// if (null != luckHeroTime && !"".equals(luckHeroTime)) {
		// long timeL = Long.parseLong(luckHeroTime);
		// time = curTime - timeL;
		// }
		// String timeVal = DateUtils.mSecondsToDay(time);
		// int timeInt = -1;
		// if(null != timeVal){
		//
		// }
		Random random = new Random();
		int startRandom = random.nextInt(5);
		switch (startRandom) {
		case 0:
		case 1:
			starTv3.setVisibility(View.GONE);
			starTv4.setVisibility(View.GONE);
			starTv5.setVisibility(View.GONE);
			break;
		case 2:
			starTv4.setVisibility(View.GONE);
			starTv5.setVisibility(View.GONE);
			break;
		case 3:
			starTv5.setVisibility(View.GONE);
			break;
		}
		String[] avatarArray = getResources().getStringArray(
				R.array.search_layout_hero_avatar_array);
		if (avatarArray != null) {
			int avatarRandom = random.nextInt(avatarArray.length);
			ApplicationInfo appInfo = mContext.getApplicationInfo();
			int resID = mContext.getResources().getIdentifier(
					avatarArray[avatarRandom], "drawable", appInfo.packageName);
			if (0 != resID && -1 != resID) {
				Drawable drawable = getResources().getDrawable(resID);
				BitmapDrawable bd = (BitmapDrawable) drawable;
				Bitmap bitmap = bd.getBitmap();
				bitmap = BitmapUtil.toRound(bitmap);
				mHeroAvatarImg.setImageBitmap(bitmap);
			}
		}
	}

	private void setData(List<News> list) {
		mNewsListView.setVisibility(View.VISIBLE);
		mNewsAdapter = new NewsAdapter(mContext, list);
		mNewsListView.setAdapter(mNewsAdapter);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_NEWS_WHAT:
				Bundle bundle = msg.getData();
				String[] wallpaperUrls = bundle.getStringArray("wallpaperUrls");
				setWallpaper(wallpaperUrls);
				List<News> list = (List<News>) msg.obj;
				mNewsList = list;
				setData(list);
				mProgressBar.setVisibility(View.GONE);
				break;
			case GET_ONLINE_WHAT:
				int count = (Integer) msg.obj;
				onlineNumberTv.setText(count + "");
				break;
			case SET_TODAY_PRICE_TIME:
				String hourStr = "00";
				String minuteStr = "00";
				String secondStr = "00";
				if (0 < mSecondes) {
					mSecondes--;
					handler.sendEmptyMessageDelayed(SET_TODAY_PRICE_TIME, 1000);
				} else {
					if (0 < mMinutes) {
						mMinutes--;
						mSecondes = 59;
						handler.sendEmptyMessageDelayed(SET_TODAY_PRICE_TIME,
								1000);
					} else {
						if (0 < mHours) {
							mHours--;
							mMinutes = 59;
							mSecondes = 59;
							handler.sendEmptyMessageDelayed(
									SET_TODAY_PRICE_TIME, 1000);
						} else {
							getTodayPrice();
						}
					}

				}
				hourStr = String.valueOf(mHours);
				minuteStr = String.valueOf(mMinutes);
				secondStr = String.valueOf(mSecondes);
				if (hourStr.length() == 1) {
					hourStr = "0" + hourStr;
				}
				if (minuteStr.length() == 1) {
					minuteStr = "0" + minuteStr;
				}
				if (secondStr.length() == 1) {
					secondStr = "0" + secondStr;
				}
				setTodayTimeView(hourStr, minuteStr, secondStr);
				break;
			}
		};
	};

	private void setTodayTimeView(String hours, String minutes, String seconds) {
		char[] hoursChars = null;
		if (null != hours) {
			mHours = Integer.parseInt(hours);
			hoursChars = hours.toCharArray();
		}
		char[] minutesChars = null;
		if (null != minutes) {
			mMinutes = Integer.parseInt(minutes);
			minutesChars = minutes.toCharArray();
		}
		char[] secondsChars = null;
		if (null != seconds) {
			mSecondes = Integer.parseInt(seconds);
			secondsChars = seconds.toCharArray();
		}
		if (hoursChars != null) {
			for (int i = 0; i < hoursChars.length; i++) {
				if (0 == i) {
					mPriceTimeTv1.setText(hoursChars[i] + "");
				} else if (1 == i) {
					mPriceTimeTv2.setText(hoursChars[1] + "");
				}
			}
		}
		if (minutesChars != null) {
			for (int i = 0; i < minutesChars.length; i++) {
				if (0 == i) {
					mPriceTimeTv3.setText(minutesChars[i] + "");
				} else if (1 == i) {
					mPriceTimeTv4.setText(minutesChars[1] + "");
				}
			}
		}
		if (secondsChars != null) {
			for (int i = 0; i < secondsChars.length; i++) {
				if (0 == i) {
					mPriceTimeTv5.setText(secondsChars[i] + "");
				} else if (1 == i) {
					mPriceTimeTv6.setText(secondsChars[1] + "");
				}
			}
		}
	}

	private void setWallpaper(String[] wallpaperUrls) {
		List<String> list = new ArrayList<String>();
		if (wallpaperUrls != null && wallpaperUrls.length > 0) {
			for (String url : wallpaperUrls) {
				list.add(url);
			}
			GridView wallpaperGridview = (GridView) findViewById(R.id.news_wallpaper_img_gridview);
			WallpaperItemAdapter adapter = new WallpaperItemAdapter(mContext,
					list);
			wallpaperGridview.setAdapter(adapter);
			wallpaperGridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					startActivity(new Intent(mContext, WallpaperActivity.class));
				}
			});

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(null);
		handler.removeMessages(GET_NEWS_WHAT);
		handler.removeMessages(GET_ONLINE_WHAT);
		handler.removeMessages(SET_TODAY_PRICE_TIME);
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.news_search_layout:
		case R.id.news_search_img:
		case R.id.search_user_et:
			startActivity(new Intent(mContext, SearchActivity.class));
			break;
		case R.id.news_rank_text:
			startActivity(new Intent(mContext, RankActivity.class));
			break;
		case R.id.news_wallpaper_more_text:
			startActivity(new Intent(mContext, WallpaperActivity.class));
			break;
		case R.id.news_sales_price_layout:
			Intent intent = new Intent(mContext, SalesDetailActivity.class);
			String name = mPriceNameTv.getText().toString();
			if (name != null) {
				intent.putExtra("name", name);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mNewsList != null && mNewsList.size() > 0) {
			News news = mNewsList.get(arg2);
			String url = news.getUrl();
			String title = news.getTitle();
			Intent intent = new Intent(mContext, WebActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("title", title);
			startActivity(intent);
		}

	}

}
