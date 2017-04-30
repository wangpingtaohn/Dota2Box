package com.wpt.dota2box.ui.myself.more;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.net.MySelfNet;
import com.wpt.frame.net.Result;
import com.wpt.frame.util.FileUtil;
import com.wpt.frame.widget.CustomToast;

public class FeedbackActivity extends Activity {

	private Context mContext;
	
	private EditText mContentEt;
	
	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_feedback_layout);
		
		mContext = FeedbackActivity.this;
		
		initHeader();
		initView();
	}
	
	private void initView(){
		mContentEt = (EditText)findViewById(R.id.feedback_content_edit);
		Button sendBtn = (Button)findViewById(R.id.feedback_send_btn);
		
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendFeedbackContent();
			}
		});
	}
	
	private void sendFeedbackContent() {
		if (mContentEt.getText() != null && !mContentEt.getText().toString().equals("")) {
			final String content = mContentEt.getText().toString();
			new AsyncTask<Void, Void, Result<Integer>>(){

				@Override
				protected Result<Integer> doInBackground(Void... params) {
					MySelfNet net = new MySelfNet();
					String userId = FileUtil.getSharedPreValue(mContext, Constants.SP_NAME, Constants.ID);
					if (userId == null) {
						userId = "0";
					}
					return net.feedback(mContext, content, userId);
				}
				protected void onPostExecute(Result<Integer> result) {
					if (result != null && 1 == result.getResult()) {
						CustomToast.showShortToast(mContext, "提交成功");
						mContentEt.setText("");
					} else {
						CustomToast.showShortToast(mContext, "提交失败,请重试");
					}
				};
			}.execute();
		} else {
			CustomToast.showShortToast(mContext, "内容不能为空");
		}
	}
	private void initHeader() {
		TextView titleTv = (TextView) findViewById(R.id.include_main_top_bar_title_text);
		ImageView leftBtn = (ImageView) findViewById(R.id.include_main_top_bar_left_btn);
		ImageView rightBtn = (ImageView) findViewById(R.id.include_main_top_bar_right_btn);

		titleTv.setVisibility(View.VISIBLE);
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.GONE);
		
		titleTv.setText("意见反馈");
		
		leftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
