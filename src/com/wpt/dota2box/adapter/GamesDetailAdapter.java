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
import com.wpt.dota2box.model.GamesDetail;
import com.wpt.frame.util.MyImageLoaderUtil;

public class GamesDetailAdapter extends BaseAdapter {

	private Context mContext;

	private List<GamesDetail> mDatas;

	public GamesDetailAdapter(Context context, List<GamesDetail> list) {
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
			convertView = View.inflate(mContext,
					R.layout.games_detail_list_item, null);
			holder = new ViewHolder();
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.games_list_detail_item_progressbar);
			holder.heroImg = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_image);
			holder.userImg = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_name_image);
			holder.nameTv = (TextView) convertView
					.findViewById(R.id.games_list_detail_item_name_text);
			holder.numberTv = (TextView) convertView
					.findViewById(R.id.games_list_detail_item_nickname_text);
			holder.goldTv = (TextView) convertView
					.findViewById(R.id.games_list_detail_item_money_text);
			holder.levelTv = (TextView) convertView
					.findViewById(R.id.games_list_detail_item_level_text);
			holder.kdaTv = (TextView) convertView
					.findViewById(R.id.games_list_detail_item_kda_text);
			holder.zbImg1 = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_zb_img1);
			holder.zbImg2 = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_zb_img2);
			holder.zbImg3 = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_zb_img3);
			holder.zbImg4 = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_zb_img4);
			holder.zbImg5 = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_zb_img5);
			holder.zbImg6 = (ImageView) convertView
					.findViewById(R.id.games_list_detail_item_zb_img6);
			convertView.setTag(holder);
		}
		GamesDetail games = mDatas.get(position);
		String name = games.getName();
		if (name == null || "".equals(name)) {
			name = "匿名大神";
		}
		holder.nameTv.setText(name);
		holder.goldTv.setText(games.getGold());
		if (position < 5) {
			holder.numberTv.setText("天辉");
		} else {
			holder.numberTv.setText("夜魇");
		}
		holder.levelTv.setText(games.getLevel());
		holder.kdaTv.setText(games.getKda());
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		String heroUrl = games.getHeroUrl();
		String userUrl = games.getUserUrl();
		if (heroUrl != null && !"".equals(heroUrl.trim())) {
			util.displayImage(heroUrl, holder.heroImg,
					new MyImageLoadingListener(holder.bar));
		}
		if (userUrl != null && !"".equals(userUrl.trim())) {
			util.displayImage(userUrl, holder.userImg,
					new MyImageLoadingListener(holder.bar));
		}
		List<String> zbUrls = games.getUrlList();
		if (zbUrls != null && zbUrls.size() > 0) {
			showZbImage(holder, zbUrls);
		}
		return convertView;
	}

	private void showZbImage(ViewHolder holder,List<String> zbUrls) {
		int zbUrllen = zbUrls.size();
		String zbUrl1 = null;
		String zbUrl2 = null;
		String zbUrl3 = null;
		String zbUrl4 = null;
		String zbUrl5 = null;
		String zbUrl6 = null;
		MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
		switch (zbUrllen) {
		case 1:
			zbUrl1 = zbUrls.get(0);
			util.displayImage(zbUrl1, holder.zbImg6);
			break;
		case 2:
			zbUrl1 = zbUrls.get(0);
			util.displayImage(zbUrl1, holder.zbImg6);
			zbUrl2 = zbUrls.get(1);
			util.displayImage(zbUrl2, holder.zbImg5);
			break;
		case 3:
			zbUrl1 = zbUrls.get(0);
			util.displayImage(zbUrl1, holder.zbImg6);
			zbUrl2 = zbUrls.get(1);
			util.displayImage(zbUrl2, holder.zbImg5);
			zbUrl3 = zbUrls.get(2);
			util.displayImage(zbUrl3, holder.zbImg4);
			break;
		case 4:
			zbUrl1 = zbUrls.get(0);
			util.displayImage(zbUrl1, holder.zbImg6);
			zbUrl2 = zbUrls.get(1);
			util.displayImage(zbUrl2, holder.zbImg5);
			zbUrl3 = zbUrls.get(2);
			util.displayImage(zbUrl3, holder.zbImg4);
			zbUrl4 = zbUrls.get(3);
			util.displayImage(zbUrl4, holder.zbImg3);
			break;
		case 5:
			zbUrl1 = zbUrls.get(0);
			util.displayImage(zbUrl1, holder.zbImg6);
			zbUrl2 = zbUrls.get(1);
			util.displayImage(zbUrl2, holder.zbImg5);
			zbUrl3 = zbUrls.get(2);
			util.displayImage(zbUrl3, holder.zbImg4);
			zbUrl4 = zbUrls.get(3);
			util.displayImage(zbUrl4, holder.zbImg3);
			zbUrl5 = zbUrls.get(4);
			util.displayImage(zbUrl5, holder.zbImg2);
			break;
		case 6:
			zbUrl1 = zbUrls.get(0);
			util.displayImage(zbUrl1, holder.zbImg6);
			zbUrl2 = zbUrls.get(1);
			util.displayImage(zbUrl2, holder.zbImg5);
			zbUrl3 = zbUrls.get(2);
			util.displayImage(zbUrl3, holder.zbImg4);
			zbUrl4 = zbUrls.get(3);
			util.displayImage(zbUrl4, holder.zbImg3);
			zbUrl5 = zbUrls.get(5);
			util.displayImage(zbUrl5, holder.zbImg2);
			zbUrl6 = zbUrls.get(5);
			util.displayImage(zbUrl6, holder.zbImg1);
			break;
		}
	}
	
	private class ViewHolder {
		ImageView heroImg;
		ImageView userImg;
		TextView nameTv;
		TextView numberTv;
		ProgressBar bar;
		TextView goldTv;
		TextView levelTv;
		TextView kdaTv;
		ImageView zbImg1;
		ImageView zbImg2;
		ImageView zbImg3;
		ImageView zbImg4;
		ImageView zbImg5;
		ImageView zbImg6;
	}
	

	class MyImageLoadingListener implements ImageLoadingListener {

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
