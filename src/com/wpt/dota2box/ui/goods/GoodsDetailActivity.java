package com.wpt.dota2box.ui.goods;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.GoodsHeChengAdapter;
import com.wpt.dota2box.model.Goods;
import com.wpt.dota2box.model.GoodsHeCheng;
import com.wpt.dota2box.ui.BaseActivity;
import com.wpt.dota2box.util.GoodsDbUtil;

public class GoodsDetailActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private Context mContext;
	
	private ImageView backImg;
	private ImageView shareImg;
	
	private ImageView avatarImg;
	private TextView quanMingTv;
	private TextView shuomingTv;
	private TextView shiyongTv;
	private TextView priceTv;
	private TextView fujiaTv;
	
	private Gallery mGallery;
	
	private TextView heChengTv;
	
	private List<GoodsHeCheng> mHechengList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_detail_layout);
		
		mContext = GoodsDetailActivity.this;
		initView();
		setData();
		addAdView(this, R.id.goods_detail_adview_layout);
	}
	
	private void initView() {
		
		backImg = (ImageView)findViewById(R.id.include_main_top_bar_left_btn);
		shareImg = (ImageView)findViewById(R.id.include_main_top_bar_right_btn);
		
		avatarImg = (ImageView)findViewById(R.id.goods_detail_avatar);
		quanMingTv = (TextView)findViewById(R.id.goods_detail_name);
		shuomingTv = (TextView)findViewById(R.id.goods_detail_shuoming_text);
		shiyongTv = (TextView)findViewById(R.id.goods_detail_shiyong_text);
		priceTv = (TextView)findViewById(R.id.goods_detail_price_text);
		fujiaTv = (TextView)findViewById(R.id.goods_detail_fujia_text);
		
		mGallery = (Gallery)findViewById(R.id.goods_detail_hecheng_gallery);
		heChengTv = (TextView)findViewById(R.id.goods_detail_hecheng_text);
		
		
		backImg.setOnClickListener(this);
		shareImg.setOnClickListener(this);
		
		mGallery.setOnItemClickListener(this);
		
	}
	
	private void setData() {
		String itemsId = getIntent().getStringExtra("itemsId");
		int avatarId = getResources().getIdentifier(itemsId, "drawable", mContext.getApplicationInfo().packageName);
		avatarImg.setImageResource(avatarId);
		GoodsDbUtil util = new GoodsDbUtil();
		Goods goods = util.getGoodsInfo(itemsId);
		if (goods != null) {
			quanMingTv.setText(goods.getQuanMing());
			priceTv.setText(goods.getJiage());
			shuomingTv.setText("说明:" + goods.getShuoming());
			shiyongTv.setText("使用:" + goods.getShiyong());
			fujiaTv.setText(goods.getFujia());
			mHechengList = util.getGoodsHeCheng(goods.getItemsId());
			if (mHechengList != null && mHechengList.size() > 0) {
				heChengTv.setVisibility(View.VISIBLE);
				GoodsHeChengAdapter ghcAdapter = new GoodsHeChengAdapter(mContext, mHechengList);
				mGallery.setAdapter(ghcAdapter);
				mGallery.setSelection(mHechengList.size() - 1);
			} else {
				heChengTv.setVisibility(View.GONE);
			}
		}
		
		
	}
	
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
		if (mHechengList != null && mHechengList.size() > 0) {
			GoodsHeCheng ghc = mHechengList.get(arg2);
			String touxiang = ghc.getTouxiang();
			if (touxiang != null && !"juanzhou".equals(touxiang)) {
				Intent intent = new Intent(mContext, GoodsDetailActivity.class);
				intent.putExtra("itemsId", touxiang);
				startActivity(intent);
			}
		}
		
	}
}
