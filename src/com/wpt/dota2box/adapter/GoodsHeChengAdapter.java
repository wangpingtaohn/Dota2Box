package com.wpt.dota2box.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wpt.dota2box.R;
import com.wpt.dota2box.model.GoodsHeCheng;

public class GoodsHeChengAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<GoodsHeCheng> mDatas;
	
	public GoodsHeChengAdapter(Context context,List<GoodsHeCheng> list) {
		mContext = context;
		mDatas = list;
	}
	
	@Override
	public int getCount() {
		int totalCount = 0;
		if (mDatas != null && mDatas.size() > 0) {
			totalCount = mDatas.size();
		}
		return totalCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(mContext, R.layout.hero_grid_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.hero_grid_item_imageView);
			convertView.setTag(holder);
		}
		GoodsHeCheng ghc = mDatas.get(position); 
		String itemsId = ghc.getTouxiang();
		if (itemsId != null && !"".equals(itemsId)) {
			ApplicationInfo appInfo = mContext.getApplicationInfo();
			int resID = mContext.getResources().getIdentifier(itemsId, "drawable", appInfo.packageName);
			holder.imageView.setImageResource(resID);
		}
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageView;
	}
}
