package com.wpt.dota2box.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.net.MySelfNet;
import com.wpt.dota2box.util.DBManager;
import com.wpt.frame.util.FileUtil;

public class MainActivity extends Activity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = MainActivity.this;

		DBManager dbManager = new DBManager(this);
		dbManager.openDatabase();
		dbManager.closeDatabase();
		postServer();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startActivity(new Intent(mContext, MainTabActivity.class));
		finish();
	}

	private void postServer() {
		String isPostServer = FileUtil.getSharedPreValue(this,
				Constants.SP_NAME, Constants.IS_POST_SERVER);
		if (!"1".equals(isPostServer)) {
			new AsyncTask<Void, Void, Integer>() {

				@Override
				protected Integer doInBackground(Void... params) {
					MySelfNet net = new MySelfNet();
					return net.postServer(mContext);
				}

				protected void onPostExecute(Integer result) {
					FileUtil.saveSharedPre(mContext, Constants.SP_NAME,
							Constants.IS_POST_SERVER, result + "");
				};

			}.execute();
		}
	}

}
