package com.wpt.dota2box.adapter;

import java.util.List;
import java.util.Map;

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
import com.wpt.frame.util.MyImageLoaderUtil;

public class RecordAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Map<String, String>> mDatas;
	
	public RecordAdapter(Context context,List<Map<String, String>> list) {
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
			convertView = View.inflate(mContext, R.layout.myself_record_gridview_item,
					null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.myself_record_gridview_item_image);
			holder.titleTv = (TextView) convertView
					.findViewById(R.id.myself_record_gridview_item_shuoming_text);
			holder.priceTv = (TextView) convertView
					.findViewById(R.id.myself_record_gridview_item_price_text);
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.myself_rocord_gridview_item_progressbar);
			convertView.setTag(holder);
		}
		Map<String, String> map = mDatas.get(position);
		String url = map.get("url");
		String price = map.get("number");
		if (url != null) {
			MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
			util.displayImage(url, holder.imageView,new MyImageLoadingListener(holder.bar));
		}
		holder.priceTv.setText(price);
		setTitle(position,holder.titleTv,holder.priceTv);
		return convertView;
	}
	
	private void setTitle(int index,TextView textView,TextView priceTv) {
		String titleStr = "";
		int textColor = 0;
		switch (index) {
		case 0:
			titleStr = "最多击杀";
			textColor = mContext.getResources().getColor(R.color.orance);
			break;
		case 1:
			titleStr = "最多助攻";
			textColor = mContext.getResources().getColor(R.color.orance);
			break;
		case 2:
			titleStr = "最多金钱";
			textColor = mContext.getResources().getColor(R.color.pink);
			break;
		case 3:
			titleStr = "最多经验";
			textColor = mContext.getResources().getColor(R.color.pink);
			break;
		case 4:
			titleStr = "最多伤害";
			textColor = mContext.getResources().getColor(R.color.dark_green);
			break;
		case 5:
			titleStr = "最多KDA";
			textColor = mContext.getResources().getColor(R.color.dark_green);
			break;
		}
		textView.setText(titleStr);
		priceTv.setTextColor(textColor);
	}

	private class ViewHolder {
		ImageView imageView;
		TextView titleTv;
		TextView priceTv;
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
