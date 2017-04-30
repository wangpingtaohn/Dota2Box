package com.wpt.dota2box.ui;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.wpt.frame.util.AppUtils;

public class BaseActivity extends Activity {

	private AdView adView;

	private InterstitialAd interfaceAddress;

	private static final String AD_NORMAL_ID = "ca-app-pub-3852102900701495/1048091560";

	private static final String AD_FULL_ID = "ca-app-pub-3852102900701495/5947465962";

	private int fullAdTime = 200 * 1000;

	public static boolean isAddFullAd = false;

	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mContext == null) {
			mContext = getApplicationContext();
		}
		if (!isAddFullAd) {
			showFullAdview(this);
			isAddFullAd = true;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (interfaceAddress.isLoaded()) {
				if (isTopApp(mContext, mContext.getPackageName())) {
					interfaceAddress.show();
				} else {
					isAddFullAd = false;
				}
			}
			handler.removeMessages(0);
		};
	};

	// 判断应用是否在前台
	public static boolean isTopApp(Context context, String packageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskList = am.getRunningTasks(1);
		if (taskList != null && taskList.size() > 0) {
			if (packageName
					.equals(taskList.get(0).topActivity.getPackageName())) {
				return true;
			}
		}

		return false;
	}

	protected void addAdView(Activity activity, int layoutId) {
		final LinearLayout layout = (LinearLayout) findViewById(layoutId);
		// 创建adView。
		adView = new AdView(activity);
		adView.setAdUnitId(AD_NORMAL_ID);
		adView.setAdSize(AdSize.FULL_BANNER);
		layout.addView(adView);

		// 启动一般性请求。
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				super.onAdLoaded();
				layout.setVisibility(View.VISIBLE);
			}
		});
		// 在adView中加载广告请求。
		adView.loadAd(adRequest);
	}

	protected void showFullAdview(Context activity) {
		interfaceAddress = new InterstitialAd(activity);
		interfaceAddress.setAdUnitId(AD_FULL_ID);
		//  创建广告请求
		AdRequest adRequest = new AdRequest.Builder().build();
		//  开始加载插页式广告。
		interfaceAddress.loadAd(adRequest);
		handler.sendEmptyMessageDelayed(0, fullAdTime);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (adView != null) {
			adView.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}
		handler.removeCallbacks(null);
	}
}
