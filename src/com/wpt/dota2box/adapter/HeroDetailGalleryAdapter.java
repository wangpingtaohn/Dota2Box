package com.wpt.dota2box.adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.wpt.dota2box.R;
import com.wpt.frame.util.MyImageLoaderUtil;

public class HeroDetailGalleryAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Map<String, String>> mDatas;
	
	public HeroDetailGalleryAdapter(Context context,List<Map<String, String>> list) {
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
			convertView = View.inflate(mContext, R.layout.hero_detail_gallery_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.hero_detail_gallery_item_avatar);
			holder.textView = (TextView) convertView
					.findViewById(R.id.hero_detail_gallery_item_text);
			convertView.setTag(holder);
		}
		Map<String, String> map = mDatas.get(position);
		String textStr = map.get("text");
		String priceStr = map.get("price");
		String imgUrl = map.get("url");
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		util.displayImage(imgUrl, holder.imageView);
		if (null != priceStr && !"".equals(priceStr.trim())) {
			textStr = textStr + "  " + priceStr + "金币";
		}
		holder.textView.setText(textStr);
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageView;
		TextView textView;
	}
}
