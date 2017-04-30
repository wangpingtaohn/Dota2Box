package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.wpt.dota2box.R;

public class HeroAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<String> datas;
	
	public HeroAdapter(Context context,List<String> list) {
		mContext = context;
		datas = list;
	}
	
	@Override
	public int getCount() {
		if (datas != null && datas.size() > 0) {
			return datas.size();
		}
		return 0;
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
		String heroId = datas.get(position);
		if (heroId != null && !"".equals(heroId)) {
			ApplicationInfo appInfo = mContext.getApplicationInfo();
			int resID = mContext.getResources().getIdentifier(heroId, "drawable", appInfo.packageName);
			holder.imageView.setImageResource(resID);
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
	}
}
