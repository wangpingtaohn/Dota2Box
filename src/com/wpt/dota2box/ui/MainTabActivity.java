package com.wpt.dota2box.ui;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.wpt.dota2box.R;
import com.wpt.dota2box.ui.goods.GoodsActivity;
import com.wpt.dota2box.ui.hero.HeroActivity;
import com.wpt.dota2box.ui.myself.MySelfActivity;
import com.wpt.dota2box.ui.news.NewsActivity;
import com.wpt.frame.widget.CustomToast;

/**
 * @author wpt
 */
public class MainTabActivity extends ActivityGroup implements OnClickListener {

	// private RadioGroup radioGroup;

	private TabHost tabHost;

	public static final String TAB_NEWS = "tabNews";

	public static final String TAB_HERO = "tabHero";

	public static final String TAB_GOODS = "tabGoods";
	
	public static final String TAB_MYSELF = "tabMysefl";

	private Context mContext;

	private static long exitTime;

	private ImageView mTab1;
	

	private ImageView mTab2;

	private ImageView mTab3;
	
	private ImageView mTab4;

	private TextView mTabTv1;
	
	private TextView mTabTv2;
	
	private TextView mTabTv3;
	
	private TextView mTabTv4;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintab);
		
		mContext = MainTabActivity.this;
		initView();
		
	}

	private void initView() {

		tabHost = (TabHost) findViewById(R.id.host);
		tabHost.setup(getLocalActivityManager());

		tabHost.addTab(tabHost.newTabSpec(TAB_NEWS)
				.setIndicator(TAB_NEWS).setContent(new Intent(this, NewsActivity.class)));

		tabHost.addTab(tabHost.newTabSpec(TAB_HERO)
				.setIndicator(TAB_HERO).setContent(new Intent(this, HeroActivity.class)));

		tabHost.addTab(tabHost.newTabSpec(TAB_GOODS).setIndicator(TAB_GOODS)
				.setContent(new Intent(this, GoodsActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(TAB_MYSELF).setIndicator(TAB_MYSELF)
				.setContent(new Intent(this, MySelfActivity.class)));

		tabHost.setCurrentTabByTag(TAB_NEWS);

		mTab1 = (ImageView) findViewById(R.id.tab1);
		mTab2 = (ImageView) findViewById(R.id.tab2);
		mTab3 = (ImageView) findViewById(R.id.tab3);
		mTab4 = (ImageView) findViewById(R.id.tab4);
		
		mTabTv1 = (TextView) findViewById(R.id.tab1_text);
		mTabTv2 = (TextView) findViewById(R.id.tab2_text);
		mTabTv3 = (TextView) findViewById(R.id.tab3_text);
		mTabTv4 = (TextView) findViewById(R.id.tab4_text);

		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);
		mTab3.setOnClickListener(this);
		mTab4.setOnClickListener(this);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - exitTime > 5000) {
				CustomToast.showToast(mContext, R.string.str_eixt_tip);
				exitTime = System.currentTimeMillis();
			} else {
				BaseActivity.isAddFullAd = false;
				finish();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onClick(View v) {
		switchTab(v.getId());
	}
	
	private void switchTab(int tabId) {
		switch (tabId) {
		case R.id.tab1:
			tabHost.setCurrentTabByTag(TAB_NEWS);
			mTab1.setImageResource(R.drawable.tab1_pressed);
			mTab2.setImageResource(R.drawable.tab2);
			mTab3.setImageResource(R.drawable.tab3);
			mTab4.setImageResource(R.drawable.tab4);
			mTabTv1.setTextColor(getResources().getColor(R.color.green));
			mTabTv2.setTextColor(getResources().getColor(R.color.gray));
			mTabTv3.setTextColor(getResources().getColor(R.color.gray));
			mTabTv4.setTextColor(getResources().getColor(R.color.gray));
			break;
		case R.id.tab2:
			tabHost.setCurrentTabByTag(TAB_HERO);
			mTab1.setImageResource(R.drawable.tab1);
			mTab2.setImageResource(R.drawable.tab2_pressed);
			mTab3.setImageResource(R.drawable.tab3);
			mTab4.setImageResource(R.drawable.tab4);
			mTabTv1.setTextColor(getResources().getColor(R.color.gray));
			mTabTv2.setTextColor(getResources().getColor(R.color.green));
			mTabTv3.setTextColor(getResources().getColor(R.color.gray));
			mTabTv4.setTextColor(getResources().getColor(R.color.gray));
			break;
		case R.id.tab3:
			tabHost.setCurrentTabByTag(TAB_GOODS);
			mTab1.setImageResource(R.drawable.tab1);
			mTab2.setImageResource(R.drawable.tab2);
			mTab3.setImageResource(R.drawable.tab3_pressed);
			mTab4.setImageResource(R.drawable.tab4);
			mTabTv1.setTextColor(getResources().getColor(R.color.gray));
			mTabTv2.setTextColor(getResources().getColor(R.color.gray));
			mTabTv3.setTextColor(getResources().getColor(R.color.green));
			mTabTv4.setTextColor(getResources().getColor(R.color.gray));
			break;
		case R.id.tab4:
			tabHost.setCurrentTabByTag(TAB_MYSELF);
			mTab1.setImageResource(R.drawable.tab1);
			mTab2.setImageResource(R.drawable.tab2);
			mTab3.setImageResource(R.drawable.tab3);
			mTab4.setImageResource(R.drawable.tab4_pressed);
			mTabTv1.setTextColor(getResources().getColor(R.color.gray));
			mTabTv2.setTextColor(getResources().getColor(R.color.gray));
			mTabTv3.setTextColor(getResources().getColor(R.color.gray));
			mTabTv4.setTextColor(getResources().getColor(R.color.green));
			break;
		default:
			break;
		}
	}

}