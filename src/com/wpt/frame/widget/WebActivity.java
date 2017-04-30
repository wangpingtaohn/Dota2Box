package com.wpt.frame.widget;

import java.lang.reflect.Field;

import com.wpt.dota2box.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

public class WebActivity extends Activity {
	
	private WebView mWebView;
	
	private ProgressBar pBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_layout);
		initView();
		initHeader();
	}

	private void initView() {

		Intent intent = getIntent();
		final String url = intent.getStringExtra("url");
		mWebView = (WebView) findViewById(R.id.common_webview);
		pBar = (ProgressBar) findViewById(R.id.common_webview_bar);
		// 设置web的属性，能够执行javaScript脚本
		mWebView.getSettings().setJavaScriptEnabled(true);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSupportZoom(true);
		mWebView.setBackgroundColor(0);
		setZoomControlGone(mWebView);
		mWebView.loadUrl(url);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		mWebView.setWebViewClient(new MyWebViewClient());
		
	}

	private void setZoomControlGone(View view) {
		Class classType;
		Field field;
		try {
			classType = WebView.class;
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);
			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
					view);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
			try {
				field.set(view, mZoomButtonsController);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	// ------------响应点击事件执行超链接---------
	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (pBar != null && pBar.getVisibility() == View.VISIBLE) {
				pBar.setVisibility(View.GONE);
			}
		}
	}

	// ---------响应单击返回键返回上一个页面而不是上一个Activity---
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initHeader() {
		
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);
		
		String title = getIntent().getStringExtra("title");
		rightBtn.setVisibility(View.GONE);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText(title);
		

		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
