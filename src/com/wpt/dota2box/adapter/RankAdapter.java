package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.wpt.dota2box.R;
import com.wpt.dota2box.model.Rank;
import com.wpt.frame.util.MyImageLoaderUtil;

public class RankAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Rank> mDatas;
	
	public RankAdapter(Context context,List<Rank> list) {
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
			convertView = View.inflate(mContext, R.layout.rank_list_item,
					null);
			holder = new ViewHolder();
			holder.rankTv = (TextView) convertView
					.findViewById(R.id.rank_list_item_rank_text);
			holder.nameTv = (TextView) convertView
					.findViewById(R.id.rank_list_item_name_text);
			holder.soloTv = (TextView) convertView
					.findViewById(R.id.rank_list_item_solo_text);
			holder.countryImg = (ImageView) convertView
					.findViewById(R.id.rank_list_item_country_img);
			convertView.setTag(holder);
		}
		Rank rank = mDatas.get(position);
		String name = rank.getTeamTag() == null?rank.getName():rank.getTeamTag() + "." + rank.getName();
		holder.rankTv.setText(rank.getRank());
		holder.nameTv.setText(name);
		holder.soloTv.setText(rank.getSoloMmr());
		if (rank.getCountry() != null) {
			String url = "http://cdn.steamcommunity.com/public/images/countryflags/" + rank.getCountry() +".gif";
			MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
			util.displayImage(url, holder.countryImg);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView rankTv;
		TextView nameTv;
		TextView soloTv;
		ImageView countryImg;
	}
}
