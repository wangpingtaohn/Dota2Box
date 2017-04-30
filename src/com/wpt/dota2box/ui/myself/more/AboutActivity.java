package com.wpt.dota2box.ui.myself.more;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.frame.util.AppUtils;
import com.wpt.frame.widget.WebActivity;


public class AboutActivity extends Activity {

	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_about_layout);
		
		mContext = AboutActivity.this;
		initHeader();
		initView();
	}
	
	private void initView(){
		Button siteBtn = (Button)findViewById(R.id.about_dotabox_site_btn);
		TextView versionTv = (TextView)findViewById(R.id.about_version_number);
		String versionStr = AppUtils.getCurrentVersionName(mContext);
		versionTv.setText("V" + versionStr);
		
		siteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WebActivity.class);
				intent.putExtra("url", "http://www.dota2box.com/");
				intent.putExtra("title", "官网");
				startActivity(intent);
			}
		});
	}
	
	
	private void initHeader() {
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);
		
		titleTv.setText("关于");
		
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
