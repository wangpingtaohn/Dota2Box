package com.wpt.dota2box.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.wpt.dota2box.R;
import com.wpt.dota2box.model.News;

public class NewsAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<News> mDatas;
	
	public NewsAdapter(Context context,List<News> list) {
		mContext = context;
		mDatas = list;
	}
	
	@Override
	public int getCount() {
		if (mDatas != null && mDatas.size() > 0) {
			return mDatas.size();
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
			convertView = View.inflate(mContext, R.layout.news_list_item,
					null);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView
					.findViewById(R.id.news_list_item_title_text);
			holder.contentTv = (TextView) convertView
					.findViewById(R.id.news_list_item_content_text);
			convertView.setTag(holder);
		}
		News news = mDatas.get(position);
		holder.titleTv.setText(news.getTitle());
		holder.contentTv.setText(news.getContent());
		return convertView;
	}

	private class ViewHolder {
		TextView titleTv;
		TextView contentTv;
	}
}
