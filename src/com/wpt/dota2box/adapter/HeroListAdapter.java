package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wpt.dota2box.R;
import com.wpt.dota2box.model.Friends;
import com.wpt.dota2box.model.HeroList;
import com.wpt.frame.util.DateUtils;
import com.wpt.frame.util.MyImageLoaderUtil;

public class HeroListAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<HeroList> mDatas;
	
	public HeroListAdapter(Context context,List<HeroList> list) {
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
			convertView = View.inflate(mContext, R.layout.hero_list_item_layout,
					null);
			holder = new ViewHolder();
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.hero_list_item_progressbar);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.hero_list_item_img);
			holder.totalCountTv = (TextView) convertView
					.findViewById(R.id.hero_list_item_total_text);
			holder.winTv = (TextView) convertView
					.findViewById(R.id.hero_list_item_win_text);
			holder.rateTv = (TextView) convertView
					.findViewById(R.id.hero_list_item_count_per);
			holder.lossTv = (TextView) convertView
					.findViewById(R.id.hero_list_item_loss_text);
			holder.kdaTv = (TextView) convertView
					.findViewById(R.id.hero_list_item_kda_text);
			holder.totaLayout = (RelativeLayout) convertView
					.findViewById(R.id.hero_list_item_total_layout);
			convertView.setTag(holder);
		}
		HeroList heros = mDatas.get(position);
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		String rateStr = heros.getRate();
		String countStr = heros.getCount();
		String kdaStr = heros.getKda();
		holder.kdaTv.setText(kdaStr);
		holder.rateTv.setText(rateStr + "%");
		holder.totalCountTv.setText(countStr + "场");
		util.displayImage(heros.getUrl(), holder.imageView,new MyImageLoadingListener(holder.bar));
		
		if (countStr != null && rateStr != null) {
			int totalWidth = 120;
			int totalInt = Integer.parseInt(countStr);
			float winRateInt = Float.parseFloat(rateStr);
			float winWidth = (float)(((float)totalWidth / (float)totalInt) * (totalInt * (float) winRateInt / 100));
			int winIntWidth = Math.round(winWidth);
			LayoutParams totalLp = holder.totaLayout.getLayoutParams();
			totalLp.width = totalWidth;
			holder.totaLayout.setLayoutParams(totalLp);
			
			LayoutParams winLp = holder.winTv.getLayoutParams();
			winLp.width = winIntWidth;
			holder.winTv.setLayoutParams(winLp);
			
			LayoutParams lossLp = holder.lossTv.getLayoutParams();
			int lossWidth = totalWidth - winIntWidth;
			lossLp.width = lossWidth;
			holder.lossTv.setLayoutParams(lossLp);
		}
		
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView totalCountTv;
		TextView rateTv;
		TextView winTv;
		TextView lossTv;
		TextView kdaTv;
		ProgressBar bar;
		RelativeLayout totaLayout;
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
