package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.wpt.dota2box.R;

public class GoodsAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<List<String>> mDatas;
	
	private int mTabIndex;
	
	public GoodsAdapter(Context context,List<List<String>> list,int tabIndex) {
		mContext = context;
		mDatas = list;
		mTabIndex = tabIndex;
	}
	
	@Override
	public int getCount() {
		int totalCount = 0;
		if (mDatas != null && mDatas.size() > 0) {
			int count = mDatas.size();
			int maxSize = 0;
			for (int i = 0; i < count; i++) {
				int size = mDatas.get(i).size();
				if (maxSize < size) {
					maxSize = size;
				}
			}
			totalCount = maxSize * (mTabIndex == 0?5:6);
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
		setDatas(position, holder.imageView);
		return convertView;
	}
	
	private void setDatas(int position,ImageView imageView) {
		List<String> list = null;
		int index = 0;
		int remainder = 0;
		if (0 == mTabIndex) {
			index = position / 5;
			remainder = position % 5;
		} else {
			index = position / 6;
			remainder = position % 6;
		}
		list = mDatas.get(remainder);
		if (list != null && list.size() > index) {
			String heroId = list.get(index);
			if (heroId != null && !"".equals(heroId)) {
				ApplicationInfo appInfo = mContext.getApplicationInfo();
				int resID = mContext.getResources().getIdentifier(heroId, "drawable", appInfo.packageName);
				imageView.setImageResource(resID);
			}
		} else {
			imageView.setImageResource(0);
		}
	}

	private class ViewHolder {
		ImageView imageView;
	}
}
