package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wpt.dota2box.R;
import com.wpt.dota2box.model.Games;
import com.wpt.frame.util.DateUtils;
import com.wpt.frame.util.MyImageLoaderUtil;

public class GamesAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Games> mDatas;
	
	public GamesAdapter(Context context,List<Games> list) {
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
			convertView = View.inflate(mContext, R.layout.friend_detail_list_item,
					null);
			holder = new ViewHolder();
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.friend_detail_list_item_progressbar);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.friend_detail_list_item_image);
			holder.killTv = (TextView) convertView
					.findViewById(R.id.friend_detail_list_item_kill_text);
			holder.timeTv = (TextView) convertView
					.findViewById(R.id.friend_detail_list_item_times_text);
			holder.gamesTv = (TextView) convertView
					.findViewById(R.id.friend_detail_list_item_tianti_game_text);
			holder.zhenyingTv = (TextView) convertView
					.findViewById(R.id.friend_detail_list_item_quanzhenying_text);
			holder.resultTv = (TextView) convertView
					.findViewById(R.id.friend_detail_list_item_result_text);
			holder.lastTv = (TextView) convertView
					.findViewById(R.id.friend_detail_list_item_tianti_times_count_text);
			convertView.setTag(holder);
		}
		Games games = mDatas.get(position);
		holder.killTv.setText(games.getKda());
		String timeStr = games.getTime();
		if (timeStr != null && !"".equals(timeStr)) {
			timeStr = timeStr.replace("T", " ");
			int index = timeStr.indexOf("+");
			if(-1 != index){
				timeStr = timeStr.substring(0, index);
			}
			long timeL = (System.currentTimeMillis() - DateUtils.timeStrToLong(timeStr)) / (1000*60);
			String timeS = DateUtils.getLastTime(String.valueOf(timeL));
			if (timeS == null || "".equals(timeS)) {
				timeS = DateUtils.getMsgTime(timeStr);
			}
			holder.timeTv.setText(timeS);
		}
		holder.gamesTv.setText(games.getMatchType());
		holder.zhenyingTv.setText(games.getPick());
		holder.lastTv.setText(games.getLast());
		String result = games.getRang();
		if (result.contains("胜利")) {
			holder.resultTv.setTextColor(mContext.getResources().getColor(R.color.dark_green));
		} else {
			holder.resultTv.setTextColor(mContext.getResources().getColor(R.color.gray));
		}
		holder.resultTv.setText(result);
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		util.displayImage(games.getUrl(), holder.imageView,new MyImageLoadingListener(holder.bar));
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView killTv;
		TextView timeTv;
		ProgressBar bar;
		TextView gamesTv;
		TextView zhenyingTv;
		TextView resultTv;
		TextView lastTv;
	}
	
	class MyImageLoadingListener implements ImageLoadingListener{

		ProgressBar myBar;
		
		public MyImageLoadingListener(ProgressBar bar) {
			myBar = bar;
		}
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			myBar.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
		}
		
	}
}
