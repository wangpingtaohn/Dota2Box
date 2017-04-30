package com.wpt.dota2box.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;
import com.longevitysoft.android.xml.plist.domain.PListObjectType;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.News;
import com.wpt.dota2box.ui.news.NewsActivity;
import com.wpt.frame.net.https.HttpClientHelper;

public class NewsNet {

	public static final String PLIST_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ "/dota2Box/newSPlist.plist";

	/**
	 * @Description: 获取新闻列表
	 * @author wpt
	 * @since 2014-8-21 上午10:19:09
	 * @param context
	 * @param handler
	 */
	public void getPlist(final Context context, final Handler handler) {
		new Thread() {
			public void run() {

				List<News> list = new ArrayList<News>();
				HttpClientHelper http = new HttpClientHelper(context);
				String url = context.getResources().getString(
						R.string.dota2box_http)
						+ Constants.GET_NEWS_URL;
				// InputStream in = FileUtil.getFileFromSdcard(PLIST_PATH);
				// if(in == null){
				InputStream in = http.getInputStreamRequest(url);
				// FileUtil.saveFileToSdcard(PLIST_PATH, in);
				// }
				PListXMLParser parser = new PListXMLParser();
				PListXMLHandler plistHandler = new PListXMLHandler();
				parser.setHandler(plistHandler);
				try {
					parser.parse(in);
				} catch (IOException e) {
					e.printStackTrace();
				}
				PList pList = ((PListXMLHandler) parser.getHandler())
						.getPlist();
				if (pList != null) {
					Dict rootDict = (Dict) pList.getRootElement();
					Map<String, PListObject> map = rootDict.getConfigMap();

					for (String key : map.keySet()) {
						String[] wallpaperUrls = null;
						if ("News".equals(key)) {
							PListObject object = map.get(key);
							PListObjectType type = object.getType();
							// if (type == PListObjectType.STRING) {
							// com.longevitysoft.android.xml.plist.domain.String
							// values =
							// (com.longevitysoft.android.xml.plist.domain.String)
							// object;
							// }
							if (type == PListObjectType.ARRAY) {
								com.longevitysoft.android.xml.plist.domain.Array arrays = (com.longevitysoft.android.xml.plist.domain.Array) object;
								if (arrays != null) {
									for (int i = 0; i < arrays.toArray().length; i++) {
										News news = new News();
										Dict dicts = (Dict) arrays.get(i);
										Map<String, PListObject> arrayMap = dicts
												.getConfigMap();
										for (String arrayKey : arrayMap
												.keySet()) {
											PListObject arrayObject = arrayMap
													.get(arrayKey);
											if (PListObjectType.STRING == arrayObject
													.getType()) {
												com.longevitysoft.android.xml.plist.domain.String arrarValues = (com.longevitysoft.android.xml.plist.domain.String) arrayObject;
												String arrayValue = arrarValues
														.getValue();
												if ("url".equals(arrayKey)) {
													news.setUrl(arrayValue);
												} else if ("title"
														.equals(arrayKey)) {
													news.setTitle(arrayValue);
												} else if ("content"
														.equals(arrayKey)) {
													news.setContent(arrayValue);
												}
											}
										}
										list.add(news);
									}
								}
							}
						} else if ("Images".equals(key)) {
							PListObject object = map.get(key);
							PListObjectType type = object.getType();
							if (type == PListObjectType.ARRAY) {
								com.longevitysoft.android.xml.plist.domain.Array arrays = (com.longevitysoft.android.xml.plist.domain.Array) object;
								if (arrays != null) {
									wallpaperUrls = new String[3];
									for (int i = 0; i < arrays.toArray().length; i++) {
										com.longevitysoft.android.xml.plist.domain.String values = (com.longevitysoft.android.xml.plist.domain.String) arrays
												.get(i);
										wallpaperUrls[i] = values.getValue();
									}
								}
							}
						}
						Message msg = new Message();
						msg.what = NewsActivity.GET_NEWS_WHAT;
						msg.obj = list;
						Bundle bundle = new Bundle();
						bundle.putStringArray("wallpaperUrls", wallpaperUrls);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
				}

			};
		}.start();

	}

	public List<String> getWallpaper(Context context) {
		List<String> list = null;
		HttpClientHelper http = new HttpClientHelper(context);
		String url = context.getResources().getString(R.string.dota2box_http)
				+ Constants.GET_WALLPAPER_URL;
		// InputStream in = FileUtil.getFileFromSdcard(PLIST_PATH);
		// if(in == null){
		InputStream in = http.getInputStreamRequest(url);
		// FileUtil.saveFileToSdcard(PLIST_PATH, in);
		// }
		PListXMLParser parser = new PListXMLParser();
		PListXMLHandler plistHandler = new PListXMLHandler();
		parser.setHandler(plistHandler);
		try {
			parser.parse(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PList pList = ((PListXMLHandler) parser.getHandler()).getPlist();
		if (pList != null) {
			Dict rootDict = (Dict) pList.getRootElement();
			Map<String, PListObject> map = rootDict.getConfigMap();

			for (String key : map.keySet()) {
				if ("Images".equals(key)) {
					PListObject object = map.get(key);
					PListObjectType type = object.getType();
					if (type == PListObjectType.ARRAY) {
						com.longevitysoft.android.xml.plist.domain.Array arrays = (com.longevitysoft.android.xml.plist.domain.Array) object;
						if (arrays != null) {
							list = new ArrayList<String>();
							for (int i = 0; i < arrays.toArray().length; i++) {
								com.longevitysoft.android.xml.plist.domain.String values = (com.longevitysoft.android.xml.plist.domain.String) arrays
										.get(i);
								String wallpaperUrl = values.getValue();
								list.add(wallpaperUrl);
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * @dec 获取今日特价
	 * @param context
	 * @return
	 */
	public Map<String,String> getTodayPrice(Context context) {
		Map<String,String> map = null;
		HttpClientHelper http = new HttpClientHelper(context);
		String url = context.getResources().getString(R.string.members_http);
		try {
			String resultJson = http.getRequest(url);
			if(resultJson != null) {
				map = new HashMap<String, String>();
				resultJson = resultJson.replace("data(", "").replace("(", "");
				JSONObject json = new JSONObject(resultJson);
				if(json.has("discountTagBase")){
					map.put("discountTagBase", json.getString("discountTagBase"));
				}
				if(json.has("discountedPrice")){
					map.put("discountedPrice", json.getString("discountedPrice"));
				}
				if(json.has("itemImageDropShadow")){
					map.put("itemImageDropShadow", json.getString("itemImageDropShadow"));
				}
				if(json.has("itemNameBase")){
					map.put("itemNameBase", json.getString("itemNameBase"));
				}
				if(json.has("remainingTime")){
					map.put("remainingTime", json.getString("remainingTime"));
				}
				if(json.has("updateTime")){
					map.put("updateTime", json.getString("updateTime"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
