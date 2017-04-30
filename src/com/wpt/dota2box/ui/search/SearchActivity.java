package com.wpt.dota2box.ui.search;

import java.util.List;
import com.wpt.dota2box.R;
import com.wpt.dota2box.adapter.SearchUserAdapter;
import com.wpt.dota2box.model.SearchUser;
import com.wpt.dota2box.net.SearchNet;
import com.wpt.dota2box.ui.myself.MySelfActivity;
import com.wpt.frame.widget.CustomToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SearchActivity extends Activity implements TextWatcher, OnClickListener, OnItemClickListener{

	private Context mContext;
	
	private ListView mUserListView;
	
	private SearchUserAdapter mUserAdapter;
	
	private EditText mSearchEt;
	
	private ProgressBar mBar;
	
	private TextView mBackTv;
	
	private ImageView mClearImg;
	
	private List<SearchUser> mList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		
		mContext = SearchActivity.this;
		initView();
	}
	
	private void initView() {
		mUserListView = (ListView) findViewById(R.id.search_user_listview);
		mSearchEt = (EditText) findViewById(R.id.search_layout_edit);
		mBar = (ProgressBar) findViewById(R.id.search_progressbar);
		mBackTv = (TextView) findViewById(R.id.search_layout_back_text);
		mClearImg = (ImageView) findViewById(R.id.search_layout_clear_img);
		
		mClearImg.setOnClickListener(this);
		mBackTv.setOnClickListener(this);
		mSearchEt.addTextChangedListener(this);
		
		mUserListView.setOnItemClickListener(this);
	}

	private void searchUserInfo(final String str) {
		new AsyncTask<Void, Void, List<SearchUser>>() {
			
			@Override
			protected List<SearchUser> doInBackground(Void... params) {
				SearchNet net = new SearchNet();
				return net.searchUser(mContext,str);
			}
			
			@Override
			protected void onPostExecute(List<SearchUser> result) {
				mList = result;
				if (mBar.getVisibility() == View.VISIBLE) {
					mBar.setVisibility(View.GONE);
				}
				if (result != null && result.size() > 0) {
					mUserAdapter = new SearchUserAdapter(mContext, result);
					mUserListView.setAdapter(mUserAdapter);
				} else {
					CustomToast.showShortToast(mContext, "暂时搜不到该大神的相关数据");
				}
			};
			
		}.execute();
	}
	@Override
	public void afterTextChanged(Editable s) {
		if (s != null && s.toString().length() > 0) {
			mBar.setVisibility(View.VISIBLE);
			searchUserInfo(s.toString());
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_layout_back_text:
			finish();
			break;
		case R.id.search_layout_clear_img:
			mSearchEt.setText("");
			if (mBar.getVisibility() == View.VISIBLE) {
				mBar.setVisibility(View.GONE);
			}
			mUserAdapter = new SearchUserAdapter(mContext, null);
			mUserListView.setAdapter(mUserAdapter);
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (mList != null && mList.size() > 0) {
			SearchUser user = mList.get(arg2);
			String name = user.getName();
			String id = user.getId();
			String url = user.getUrl();
			Intent intent = null;
			boolean isSwitch = getIntent().getBooleanExtra("isSwitch", false);
			if (isSwitch) {
				intent = getIntent();
				intent.putExtra("name", name);
				intent.putExtra("id", id);
				intent.putExtra("url", url);
				setResult(0,intent);
				finish();
			} else {
				intent = new Intent(mContext, MySelfActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("id", id);
				intent.putExtra("url", url);
				intent.putExtra("isTabPage", false);
				startActivity(intent);
			}
		}
		
	}
}
