package com.wpt.dota2box.ui.hero;

import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.HeroDetailGalleryAdapter;
import com.wpt.dota2box.adapter.HeroDetailSkillAdapter;
import com.wpt.dota2box.model.Hero;
import com.wpt.dota2box.model.HeroSkill;
import com.wpt.dota2box.net.HeroNet;
import com.wpt.dota2box.ui.news.SalesDetailActivity;
import com.wpt.dota2box.util.HeroDetailDbUtil;
import com.wpt.frame.widget.MyGallery;
import com.wpt.frame.widget.MyListView;

public class HeroDetailActivity extends Activity implements OnClickListener,OnItemClickListener {

	private Context mContext;
	
	private ImageView backImg;
	private ImageView shareImg;
	
	private ImageView avatarImg;
	private TextView quanMingTv;
	private TextView suoxieTv;
	private TextView skillTv;
	
	private TextView liLiangTv1;
	private TextView chengZhangTv1;
	private TextView zhuShuXingTv1;
	
	private TextView minJieTv2;
	private TextView chengZhangTv2;
	private TextView zhuShuXingTv2;
	
	private TextView zhiLiTv3;
	private TextView chengZhangTv3;
	private TextView zhuShuXingTv3;
	private MyListView mSkillListView;
	private ScrollView mScrollView;
	private MyGallery mGallery;
	private TextView zhuangshiTv;
	
	private TextView heroBgTv;
	
	private List<Map<String, String>> mGellaryList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hero_detail_layout);
		
		mContext = HeroDetailActivity.this;
		initView();
		setData();
	}
	
	private void initView() {
		
		backImg = (ImageView)findViewById(R.id.include_main_top_bar_left_btn);
		shareImg = (ImageView)findViewById(R.id.include_main_top_bar_right_btn);
		
		avatarImg = (ImageView)findViewById(R.id.hero_detail_avatar);
		quanMingTv = (TextView)findViewById(R.id.hero_detail_name);
		suoxieTv = (TextView)findViewById(R.id.hero_detail_suoxie_text);
		skillTv = (TextView)findViewById(R.id.hero_detail_skill_text);
		
		liLiangTv1 = (TextView)findViewById(R.id.hero_detail_basic_info_first_power);
		chengZhangTv1 = (TextView)findViewById(R.id.hero_detail_basic_info_first_grow);
		zhuShuXingTv1 = (TextView)findViewById(R.id.hero_detail_basic_info_first_attr);
		
		minJieTv2 = (TextView)findViewById(R.id.hero_detail_basic_info_second_agility);
		chengZhangTv2 = (TextView)findViewById(R.id.hero_detail_basic_info_second_grow);
		zhuShuXingTv2 = (TextView)findViewById(R.id.hero_detail_basic_info_second_attr);
		
		zhiLiTv3 = (TextView)findViewById(R.id.hero_detail_basic_info_third_wit);
		chengZhangTv3 = (TextView)findViewById(R.id.hero_detail_basic_info_third_grow);
		zhuShuXingTv3 = (TextView)findViewById(R.id.hero_detail_basic_info_third_attr);
		
		mSkillListView = (MyListView)findViewById(R.id.hero_detail_listView);
		mScrollView = (ScrollView)findViewById(R.id.hero_detail_scrollView);
		mScrollView.smoothScrollTo(0, 0);//解决listView初始化时网上滚动
//		mSkillListView.setFocusable(false);//解决listView初始化时网上滚动
		
		heroBgTv = (TextView)findViewById(R.id.hero_detail_hero_bg_text);
		mGallery = (MyGallery)findViewById(R.id.hero_detail_gallery);
		zhuangshiTv = (TextView)findViewById(R.id.hero_detail_decorate_layout);
		
		backImg.setOnClickListener(this);
		shareImg.setOnClickListener(this);
		
		mGallery.setOnItemClickListener(this);
		
	}
	
	private void setData() {
		String heroId = getIntent().getStringExtra("heroId");
		HeroDetailDbUtil util = new HeroDetailDbUtil();
		Hero hero = util.getHeroDetail(heroId);
		List<HeroSkill> list =util.getHeroSkillList(heroId);
		if (hero != null) {
			int avatarId = getResources().getIdentifier(heroId, "drawable", getApplication().getPackageName());
			avatarImg.setImageResource(avatarId);
			quanMingTv.setText(hero.getQuanMing());
			suoxieTv.setText(hero.getJianCheng());
			skillTv.setText(hero.getTishi());
			
			liLiangTv1.setText("力量:" + hero.getLiLiang() + "	每点增加19点生命");
			chengZhangTv1.setText(hero.getChengZhang() + "	每点提升0.03点生命恢复速率");
			String zhuShuXing1 = hero.getZhuShuXing();
			if ("1".equals(zhuShuXing1)) {
				zhuShuXingTv1.setVisibility(View.VISIBLE);
			} else {
				zhuShuXingTv1.setVisibility(View.GONE);
			}
			
			minJieTv2.setText(hero.getMinJie() + "	每7点增加1点护甲");
			chengZhangTv1.setText(hero.getChengZhang() + "	每点提升1点攻击速度");
			String zhuShuXing2 = hero.getZhuShuXing();
			if ("2".equals(zhuShuXing2)) {
				zhuShuXingTv2.setVisibility(View.VISIBLE);
			} else {
				zhuShuXingTv2.setVisibility(View.GONE);
			}
			
			zhiLiTv3.setText(hero.getZhiLi() + "	每点增加13点魔力值上限");
			chengZhangTv3.setText(hero.getChengZhang() + "	每点提升0.04点生命恢复速率");
			String zhuShuXing3 = hero.getZhuShuXing();
			if ("3".equals(zhuShuXing3)) {
				zhuShuXingTv3.setVisibility(View.VISIBLE);
			} else {
				zhuShuXingTv3.setVisibility(View.GONE);
			}
			heroBgTv.setText(hero.getGushi());
			
			HeroNet net = new HeroNet();
			net.getShipin(mContext, handler, hero.getQuanMing());
		}
		
		HeroDetailSkillAdapter adapter = new HeroDetailSkillAdapter(mContext, list);
		mSkillListView.setAdapter(adapter);
		
		
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			List<Map<String, String>> list = (List<Map<String, String>>) msg.obj;
			if (list != null && list.size() > 0) {
				mGellaryList = list;
				zhuangshiTv.setVisibility(View.VISIBLE);
				HeroDetailGalleryAdapter adapter = new HeroDetailGalleryAdapter(mContext, list);
				mGallery.setAdapter(adapter);
			}
		};
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(null);
	};
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.include_main_top_bar_left_btn:
			finish();
			break;
		case R.id.include_main_top_bar_right_btn:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		if (mGellaryList != null && mGellaryList.size() > 0) {
			 Map<String, String> map = mGellaryList.get(arg2);
			 String idUrl = map.get("idUrl");
			 String name = map.get("text");
			 Intent intent = new Intent(mContext,SalesDetailActivity.class);
			 intent.putExtra("id", idUrl);
			 intent.putExtra("name", name);
			 startActivity(intent);
		}
		
	}
}
