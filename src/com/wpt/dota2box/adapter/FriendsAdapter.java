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
import com.wpt.dota2box.model.Friends;
import com.wpt.dota2box.model.SearchUser;
import com.wpt.frame.util.DateUtils;
import com.wpt.frame.util.MyImageLoaderUtil;

public class FriendsAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Friends> mDatas;
	
	public FriendsAdapter(Context context,List<Friends> list) {
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
			convertView = View.inflate(mContext, R.layout.search_list_item,
					null);
			holder = new ViewHolder();
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.search_list_item_progressbar);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.search_list_item_content_text_image);
			holder.nameTv = (TextView) convertView
					.findViewById(R.id.search_list_item_content_text_name);
			holder.timeTv = (TextView) convertView
					.findViewById(R.id.search_list_item_content_text_time);
			convertView.setTag(holder);
		}
		Friends friends = mDatas.get(position);
		holder.nameTv.setText(friends.getPersonaname());
		String timeStr = DateUtils.getStandardTime(friends.getLastlogoff() * 1000);
		if (timeStr != null && !"".equals(timeStr)) {
			long timeL = (System.currentTimeMillis() - DateUtils.timeStrToLong(timeStr)) / (1000*60);
			String timeS = DateUtils.getLastTime(String.valueOf(timeL));
			if (timeS == null || "".equals(timeS)) {
				timeS = DateUtils.getMsgTime(timeStr);
			}
			timeStr = timeS;
		}
		int personastate = friends.getPersonastate();
		String gameserverip = friends.getGameserverip();
		if (gameserverip != null) {
			timeStr = "游戏中";
			holder.timeTv.setTextColor(mContext.getResources().getColor(R.color.green));
		} else if (personastate > 0) {
			timeStr = "在线";
			holder.timeTv.setTextColor(mContext.getResources().getColor(R.color.orance));
		}
		holder.timeTv.setText(timeStr);
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		util.displayImage(friends.getAvatar(), holder.imageView,new MyImageLoadingListener(holder.bar));
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView nameTv;
		TextView timeTv;
		ProgressBar bar;
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
