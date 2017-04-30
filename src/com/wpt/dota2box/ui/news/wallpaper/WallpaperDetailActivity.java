package com.wpt.dota2box.ui.news.wallpaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wpt.dota2box.R;
import com.wpt.frame.util.AppUtils;
import com.wpt.frame.util.MyImageLoaderUtil;
import com.wpt.frame.widget.CustomToast;

/**
 * @Description:手机壁纸详细页
 * @author we
 * @since 2013-6-8 下午4:34:37
 */
public class WallpaperDetailActivity extends Activity implements
		OnClickListener {

	private Context mContext;

	private ImageView imageView;

	private String[] mImageUrls;

	private int mPosition;

	private MyImageLoaderUtil mLoaderUtil;

	private final static int SET_WALL_PAPER_SUCCESS = 0;

	private final static int SET_WALL_PAPER_FAILURE = 1;

	private static final int LOAD_WALL_PAPER_SUCCESSFUL = 2;

	private static final int LOAD_WALL_PAPER_FAILED = 3;

	private TextView mPageNumberTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaper_detail_layout);

		mContext = WallpaperDetailActivity.this;
		mImageUrls = getIntent().getStringArrayExtra("imageUrls");
		mPosition = getIntent().getIntExtra("position", 0);
		initHeader();
		initView();

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SET_WALL_PAPER_SUCCESS:
				CustomToast.showToast(mContext, "设为桌面壁纸成功！");
				// mSetWallpaperProgressDialog.dismiss();
				// hidePopView();
				break;
			case SET_WALL_PAPER_FAILURE:
				CustomToast.showToast(mContext, "设为桌面壁纸失败！");
				break;
			case LOAD_WALL_PAPER_SUCCESSFUL:
				String path = AppUtils.APK_PATH + "wallpaper/";
				CustomToast.showToast(mContext, "下载壁纸成功！,保存路径为：" + path);
				break;
			case LOAD_WALL_PAPER_FAILED:
				CustomToast.showToast(mContext, "下载壁纸失败！");
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView() {

		ImageView loadBtn = (ImageView) findViewById(R.id.down_pager_itemt_btn);
		TextView setWallpaperTv = (TextView) findViewById(R.id.down_pager_itemt_page_set_wallpaper);

		ViewPager viewPager = (ViewPager) findViewById(R.id.download_image_viewpaper);
		viewPager.setAdapter(new MyPagerAdapter(mImageUrls));
		viewPager.setCurrentItem(mPosition);
		viewPager.setOnPageChangeListener(new MyOnpageListener());
		mPageNumberTv = (TextView) findViewById(R.id.down_pager_itemt_page_count);
		mPageNumberTv.setText(mPosition + 1 + "/" + mImageUrls.length);

		loadBtn.setOnClickListener(this);
		setWallpaperTv.setOnClickListener(this);

	}

	private void initHeader() {
		ImageView backBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);

		titleTv.setVisibility(View.GONE);
		rightBtn.setVisibility(View.GONE);
		backBtn.setOnClickListener(this);
	}

	class MyPagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		public MyPagerAdapter(String[] urls) {
			this.images = urls;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View imageLayout = inflater.inflate(
					R.layout.wallpaper_item_pager_image_layout, container,
					false);
			imageView = (ImageView) imageLayout
					.findViewById(R.id.down_pager_itemt_image);
			final ProgressBar pBar = (ProgressBar) imageLayout
					.findViewById(R.id.down_pager_itemt_loading);
			String url = images[position];
			mLoaderUtil = new MyImageLoaderUtil(mContext);
			if (url != null && !"".equals(url)) {
				mLoaderUtil.displayImage(url, imageView, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						if(pBar != null && pBar.getVisibility() == View.VISIBLE) {
							pBar.setVisibility(View.GONE);
						}
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if(pBar != null && pBar.getVisibility() == View.VISIBLE) {
							pBar.setVisibility(View.GONE);
						}
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						if(pBar != null && pBar.getVisibility() == View.VISIBLE) {
							pBar.setVisibility(View.GONE);
						}
					}
				});
			}

			((ViewPager) container).addView(imageLayout, 0);

			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
		}

	}

	class MyOnpageListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			mPosition = arg0;
			mPageNumberTv.setText(mPosition + 1 + "/" + mImageUrls.length);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.include_main_top_bar_left_btn:
			finish();
			break;
		case R.id.down_pager_itemt_page_set_wallpaper:
			loadBitmap(true);
			break;
		case R.id.down_pager_itemt_btn:
			loadBitmap(false);
			break;

		default:
			break;
		}
	}

	private void loadBitmap(final boolean isSetWallpaper) {
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		util.loadImage(mImageUrls[mPosition], new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {

			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				mHandler.obtainMessage(LOAD_WALL_PAPER_FAILED).sendToTarget();
			}

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				if (isSetWallpaper) {
					try {
						SetWallpaperManager.getInstance(mContext, mHandler)
						.setWallpaper(loadedImage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					loadWallpaper(loadedImage);
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});
	}

	private void loadWallpaper(Bitmap bitmap) {

		long curTime = System.currentTimeMillis();
		String path = AppUtils.APK_PATH + "wallpaper/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		File f = new File(file, curTime + "_wallpaper.png");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			mHandler.obtainMessage(LOAD_WALL_PAPER_FAILED).sendToTarget();
		}
		mHandler.obtainMessage(LOAD_WALL_PAPER_SUCCESSFUL).sendToTarget();
	}

}
