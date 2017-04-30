package com.wpt.dota2box.adapter;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
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

/**
 * Created with IntelliJ IDEA. User: TangCan Date: 13-1-22 Time: 下午6:30
 */
public class WallpaperItemAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mList;
	private LayoutInflater mInflator;

	public WallpaperItemAdapter(Context context, List<String> wallpaper) {
		mContext = context;
		mList = wallpaper;
		mInflator = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = mInflator.inflate(R.layout.wallpaper_grid_item, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.download_wallpaper_or_head_item_imageview);
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.download_wallpaper_or_head_item_progressbar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String url = mList.get(position);

		MyImageLoaderUtil loaderUtil = new MyImageLoaderUtil(mContext);
		if (url != null && !"".equals(url)) {
			loaderUtil.displayImage(url, holder.image,new MyImageLoadingListener(holder.bar));
		}

		return convertView;
	}

	private class ViewHolder {
		ImageView image;
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
