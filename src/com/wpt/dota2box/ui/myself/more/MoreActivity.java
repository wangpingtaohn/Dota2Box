package com.wpt.dota2box.ui.myself.more;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.internal.is;
import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.SearchUserAdapter;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.SearchUser;
import com.wpt.dota2box.net.SearchNet;
import com.wpt.dota2box.net.UpdateNet;
import com.wpt.frame.util.MyImageLoaderUtil;
import com.wpt.frame.widget.CustomToast;

public class MoreActivity extends Activity implements OnClickListener {

	private Context mContext;

	// private TextView mSizeTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_layout);
		mContext = MoreActivity.this;
		initHeader();
		init();
	}

	private void init() {

		RelativeLayout clearCacheLayout = (RelativeLayout) findViewById(R.id.more_listview_item_clear_cache_layout);
		RelativeLayout feedbackLayout = (RelativeLayout) findViewById(R.id.more_listview_item_feedback_layout);
		RelativeLayout updateLayout = (RelativeLayout) findViewById(R.id.more_listview_item_update_layout);
		RelativeLayout aboutLayout = (RelativeLayout) findViewById(R.id.more_listview_item_about_layout);
		// mSizeTv = (TextView)
		// findViewById(R.id.more_listview_item_clear_cache_size_text);
		// String sizeStr = new ImageLoaderUtil(mContext, 0).getCacheSize();
		// mSizeTv.setText("(" + sizeStr + ")");

		clearCacheLayout.setOnClickListener(this);
		feedbackLayout.setOnClickListener(this);
		updateLayout.setOnClickListener(this);
		aboutLayout.setOnClickListener(this);

	}

	private void update() {
		final ProgressDialog dialog = ProgressDialog.show(mContext, "提示",
				"正在检查更新...");
		dialog.show();
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				UpdateNet net = new UpdateNet();
				return net.checkUpdate(mContext);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				dialog.cancel();
				showUpdateDialog(result);
			};

		}.execute();

	}
	private void showUpdateDialog(final boolean isUpdate){
		String msg = "您目前已是最新版本";
		if(isUpdate) {
			msg = "发现新版本,是否进行更新?";
		}
		Builder alart = new AlertDialog.Builder(mContext);
		alart.setTitle("提示");
		alart.setMessage(msg);
		alart.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (isUpdate) {
					String url = getResources().getString(R.string.dota2box_http) + Constants.DOWDLOAD_URL;
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
				dialog.dismiss();
			}
		});
		final AlertDialog dialog = alart.create();
		dialog.show();
	}

	private void initHeader() {
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);

		titleTv.setText("更多");

		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Intent intent = null;
		switch (id) {
		// 清除缓存
		case R.id.more_listview_item_clear_cache_layout:
			MyImageLoaderUtil util = new MyImageLoaderUtil(mContext);
			util.clearCache();
			CustomToast.showShortToast(mContext, "清除缓存成功");
			// mSizeTv.setText("");
			break;
		// 意见反馈
		case R.id.more_listview_item_feedback_layout:
			intent = new Intent(mContext, FeedbackActivity.class);
			break;
		// 检查更新
		case R.id.more_listview_item_update_layout:
			update();
			break;
		// 关于
		case R.id.more_listview_item_about_layout:
			intent = new Intent(mContext, AboutActivity.class);
			break;
		}
		if (intent != null) {
			startActivity(intent);
			intent = null;
		}

	}
}
