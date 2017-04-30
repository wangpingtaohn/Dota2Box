package com.wpt.dota2box.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.Rank;
import com.wpt.dota2box.model.SearchUser;
import com.wpt.dota2box.ui.news.NewsActivity;
import com.wpt.frame.net.https.HttpClientHelper;

public class SearchNet {

	/**
	 * @Description: 搜索用户列表
	 * @author wpt
	 * @since 2014-8-21 上午10:20:12
	 * @param str
	 * @return
	 */
	public List<SearchUser> searchUser(Context context, String str) {
		List<SearchUser> list = null;
		Document doc = null;
		try {
			String url = context.getResources().getString(
					R.string.dotabuff_http)
					+ Constants.SEARCH_USER_URL;
			str = URLEncoder.encode(str, "UTF-8");
			doc = Jsoup.connect(url + str).get();
			String[] htmls = doc.toString().split("image-avatar image-player");
			int count = 0;
			if (htmls != null && htmls.length > 0) {
				// htmls.remove(0);
				String[] tempStrs = new String[htmls.length - 1];
				for (int i = 0; i < htmls.length - 1; i++) {
					tempStrs[i] = htmls[i + 1];
				}
				list = new ArrayList<SearchUser>();
				count = tempStrs.length;
				for (int i = 0; i < count; i++) {
					SearchUser user = new SearchUser();
					String temp = tempStrs[i];
					int urlStart = temp.indexOf("src=\"");
					int urlEnd = temp.indexOf("\" title=");
					if (-1 != urlStart && urlEnd > urlStart) {
						String imagesPath = temp.substring("src=\"".length()
								+ urlStart, urlEnd);
						user.setUrl(imagesPath);
					}

					int nameStart = temp.indexOf("title=\"");
					int nameEnd = temp.indexOf("\" /></a>");
					if (-1 != nameStart && nameEnd > nameStart) {
						String name = temp.substring("title=\"".length()
								+ nameStart, nameEnd);
						user.setName(name);
					}

					int timeStart = temp.indexOf("datetime=\"");
					int timeEnd = temp.replaceFirst("\" title=", "").indexOf(
							"\" title=");
					if (-1 != timeStart && timeEnd > timeStart) {
						String time = temp.substring("datetime=\"".length()
								+ timeStart, timeEnd);
						user.setTime(time);
					}

					int idStart = temp.indexOf("players/");
					int idEnd = temp.indexOf("/tooltip");
					if (-1 != idStart && idEnd > idStart) {
						String IdStr = temp.substring(
								"players/".length() + idStart, idEnd);
						user.setId(IdStr);
					}

					list.add(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @Description: 获取在线人数
	 * @author wpt
	 * @since 2014-8-21 上午10:21:04
	 * @param context
	 * @param handler
	 */
	public void getOnlineNumber(final Context context, final Handler handler) {
		new Thread() {
			public void run() {
				int count = 0;
				HttpClientHelper client = new HttpClientHelper(context);
				try {
					String url = context.getResources().getString(
							R.string.steampowered_http)
							+ Constants.GET_ONLINE_NUMBER_URL
							+ "?appid="
							+ Constants.ADD_ID + "&" + "key=" + Constants.KEY;
					String resultStr = client.getRequest(url);
					if (resultStr != null) {
						JSONObject json = new JSONObject(resultStr);
						JSONObject object = json.getJSONObject("response");
						if (object.has("player_count")) {
							count = object.getInt("player_count");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = NewsActivity.GET_ONLINE_WHAT;
				msg.obj = count;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * @Description: 获取排行榜列表
	 * @author wpt
	 * @since 2014-8-21 上午10:22:20
	 * @param context
	 * @return
	 */
	public Map<String, List<Rank>> getRankList(final Context context) {
		Map<String, List<Rank>> map = null;
		HttpClientHelper client = new HttpClientHelper(context);
		try {
			String url = context.getResources().getString(
					R.string.dota2box_http)
					+ Constants.GET_RANK_URL;
			String resultStr = client.getRequest(url);
			if (resultStr != null) {
				map = new HashMap<String, List<Rank>>();
				JSONObject json = new JSONObject(resultStr);
				if (json.has("china")) {
					JSONArray chArray = json.getJSONArray("china");
					List<Rank> chList = new ArrayList<Rank>();
					if (chArray != null && chArray.length() > 0) {
						for (int i = 0; i < chArray.length(); i++) {
							JSONObject object = chArray.getJSONObject(i);
							chList.add(setRankInfo(object));
						}
					}
					map.put("ch", chList);
				}
				if (json.has("se_asia")) {
					JSONArray seArray = json.getJSONArray("se_asia");
					List<Rank> seList = new ArrayList<Rank>();
					if (seArray != null && seArray.length() > 0) {
						for (int i = 0; i < seArray.length(); i++) {
							JSONObject object = seArray.getJSONObject(i);
							seList.add(setRankInfo(object));
						}
					}
					map.put("se", seList);
				}
				if (json.has("europe")) {
					JSONArray europeArray = json.getJSONArray("europe");
					List<Rank> euList = new ArrayList<Rank>();
					if (europeArray != null && europeArray.length() > 0) {
						for (int i = 0; i < europeArray.length(); i++) {
							JSONObject object = europeArray.getJSONObject(i);
							euList.add(setRankInfo(object));
						}
					}
					map.put("eu", euList);
				}
				if (json.has("americas")) {
					JSONArray usaArray = json.getJSONArray("americas");
					List<Rank> usaList = new ArrayList<Rank>();
					if (usaArray != null && usaArray.length() > 0) {
						for (int i = 0; i < usaArray.length(); i++) {
							JSONObject object = usaArray.getJSONObject(i);
							usaList.add(setRankInfo(object));
						}
					}
					map.put("use", usaList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @Description: 解析排行榜信息
	 * @author wpt
	 * @since 2014-8-21 上午10:23:18
	 * @param object
	 * @return
	 * @throws Exception
	 */
	private Rank setRankInfo(JSONObject object) throws Exception {
		Rank rank = new Rank();
		if (object.has("rank")) {
			rank.setRank(object.getString("rank"));
		}
		if (object.has("solo_mmr")) {
			rank.setSoloMmr(object.getString("solo_mmr"));
		}
		if (object.has("name")) {
			rank.setName(object.getString("name"));
		}
		if (object.has("team_tag")) {
			rank.setTeamTag(object.getString("team_tag"));
		}
		if (object.has("country")) {
			rank.setCountry(object.getString("country"));
		}
		if (object.has("account_id")) {
			rank.setAccountId(object.getString("account_id"));
		}
		return rank;
	}
}
