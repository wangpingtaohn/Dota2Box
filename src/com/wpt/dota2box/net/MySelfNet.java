package com.wpt.dota2box.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.content.Context;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.Friends;
import com.wpt.frame.net.Result;
import com.wpt.frame.net.https.HttpClientHelper;
import com.wpt.frame.util.DevicesUtil;
import com.wpt.frame.util.FileUtil;

public class MySelfNet {

	/**
	 * @Description: 获取用户信息
	 * @author wpt
	 * @since 2014-8-21 上午10:16:36
	 * @param context
	 * @param id
	 * @return
	 */
	public Map<String, Object> getUserInfo(final Context context, String id) {
		String doc = null;
		Map<String, Object> map = null;
		try {
			String url = context.getResources().getString(
					R.string.dotabuff_http)
					+ Constants.GET_USER_INFO_URL;
			// doc = Jsoup.connect(url + id).get();
			HttpClientHelper client = new HttpClientHelper(context);
			doc = client.getRequest(url + id);
			if (doc != null) {
				map = new HashMap<String, Object>();
				String httpStr = doc;
				int winStart = httpStr.indexOf("wins\">");
				int winEnd = httpStr.indexOf("</span>");
				if (winStart != -1 && winEnd != -1 && winEnd > winStart) {
					String win = httpStr.substring("wins\">".length()
							+ winStart, winEnd);
					map.put("win", win);
				}
				httpStr = httpStr.substring(winEnd + "</span>".length());
				int lossStart = httpStr.indexOf("losses\">");
				int lossEnd = httpStr.indexOf("</span>");
				if (lossStart != -1 && lossEnd != -1 && lossEnd > lossStart) {
					String loss = httpStr.substring("losses\">".length()
							+ lossStart, lossEnd);
					map.put("loss", loss);
				}
				httpStr = httpStr.substring(lossEnd + "</span>".length());
				int abandonStart = httpStr.indexOf("abandons\">");
				int abandonEnd = httpStr.indexOf("</span>");
				if (abandonStart != -1 && abandonEnd != -1
						&& abandonEnd > abandonStart) {
					String abandon = httpStr.substring("abandons\">".length()
							+ abandonStart, abandonEnd);
					map.put("abandon", abandon);
				}
				analysisUserInfo(doc, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @Description: 获取记录列表
	 * @author wpt
	 * @since 2014-8-21 上午10:16:46
	 * @param context
	 * @param id
	 * @return
	 */
	public List<Map<String, String>> getRecordInfo(final Context context,
			String id) {
		Document doc = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			String url = context.getResources().getString(
					R.string.dotabuff_http)
					+ Constants.GET_RECORD_URL;
			doc = Jsoup.connect(url + id + "/records").get();
			if (doc != null) {
				String httpStr = doc.toString();
				FileUtil.saveSharedPre(context, Constants.SP_NAME,
						Constants.RECORD, httpStr);
				list = analysisRecords(httpStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @Description: 解析记录信息
	 * @author wpt
	 * @since 2014-8-21 上午10:17:45
	 * @param httpStr
	 * @return
	 */
	public List<Map<String, String>> analysisRecords(String httpStr) {
		List<Map<String, String>> list = null;
		if (httpStr != null) {
			list = new ArrayList<Map<String, String>>();
			String[] httpStrs = httpStr.split("<div class=\"record\" style=\"");
			if (httpStrs != null && httpStrs.length > 0) {
				List<String> tempList = new ArrayList<String>();
				for (String str : httpStrs) {
					tempList.add(str);
				}
				if (tempList.size() > 0) {
					tempList.remove(0);
					for (String tempStr : tempList) {
						if (tempStr.contains("Most Assists")
								|| tempStr.contains("Most Gold")
								|| tempStr.contains("Most Experience")
								|| tempStr.contains("Most Hero Damage")
								|| tempStr.contains("Most Kills")
								|| tempStr.contains("Best KDA Ratio")) {
							Map<String, String> map = new HashMap<String, String>();
							int urlStar = tempStr
									.indexOf("background-image: url(/");
							tempStr = tempStr.substring(urlStar);
							int urlEnd = tempStr.indexOf(")");
							String url = tempStr.substring(
									"background-image: url(/".length(), urlEnd);
							if (url != null && !url.startsWith("http")) {
								url = "http://dotabuff.com/"
										+ url.replace("&#47;", "/");
							}

							tempStr = tempStr.substring(")".length() + urlEnd);
							int matchidStar = tempStr.indexOf("/matches/");
							tempStr = tempStr.substring(matchidStar);
							int matchidEnd = tempStr.indexOf("\">");
							String matchid = tempStr.substring(
									"/matches/".length(), matchidEnd);

							tempStr = tempStr.substring("\">".length()
									+ matchidEnd);
							int numberStar = tempStr
									.indexOf("<div class=\"value\">");
							tempStr = tempStr.substring(numberStar);
							int numberEnd = tempStr.indexOf("</div>");
							String number = tempStr
									.substring(
											"<div class=\"value\">".length(),
											numberEnd);

							map.put("matchid", matchid.trim());
							map.put("number", number.trim());
							map.put("url", url.trim());
							list.add(map);
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * @Description: 意见反馈
	 * @author wpt
	 * @since 2014-8-21 上午10:18:02
	 * @param context
	 * @param content
	 * @param userId
	 * @return
	 */
	public Result<Integer> feedback(Context context, String content,
			String userId) {

		HttpClientHelper httpClientHelper = new HttpClientHelper(context);

		String url = context.getResources().getString(R.string.dota2box_http)
				+ Constants.FEEDBACK_URL + content + "&userid=" + userId
				+ "&tokenid=111111";
		String jsonResult;
		try {
			jsonResult = httpClientHelper.getRequest(url);
			if (jsonResult != null) {
				int start = jsonResult.indexOf("<return>");
				int end = jsonResult.indexOf("</return>");
				String codeStr = jsonResult.substring("<return>".length()
						+ start, end);
				if (codeStr != null && !"".equals(codeStr)) {
					int code = Integer.parseInt(codeStr);
					return new Result<Integer>(code);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result<Integer>(-1);
	}

	/**
	 * @Description: 获取好友id列表
	 * @author wpt
	 * @since 2014-8-22 上午11:20:31
	 * @param context
	 * @param content
	 * @param userId
	 * @return
	 */
	public List<String> getFriendId(Context context, String steamid) {

		List<String> list = null;
		HttpClientHelper httpClientHelper = new HttpClientHelper(context);

		String url = context.getResources().getString(
				R.string.steampowered_http)
				+ Constants.GET_FRIEND_ID_URL
				+ Constants.KEY
				+ "&steamid="
				+ steamid + "&relationship=" + "friend";
		String jsonResult;
		try {
			jsonResult = httpClientHelper.getRequest(url);
			if (jsonResult != null) {
				JSONObject jsonObject = new JSONObject(jsonResult);
				if (jsonObject != null && jsonObject.has("friendslist")) {
					JSONObject object = jsonObject.getJSONObject("friendslist");
					if (object != null && object.has("friends")) {
						JSONArray jsonArray = object.getJSONArray("friends");
						if (jsonArray != null && jsonArray.length() > 0) {
							list = new ArrayList<String>();
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject json = jsonArray.getJSONObject(i);
								if (json != null && json.has("steamid")) {
									String steamId = json.getString("steamid");
									list.add(steamId);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @Description: 获取好友列表
	 * @author wpt
	 * @since 2014-8-22 上午11:20:31
	 * @param context
	 * @param content
	 * @param userId
	 * @return
	 */
	public List<Friends> getFriendList(Context context, List<String> steamids) {

		List<Friends> list = null;
		String steamid = "";
		if (steamids != null && steamids.size() > 0) {
			for (String str : steamids) {
				steamid += str + ",";
			}
		}
		HttpClientHelper httpClientHelper = new HttpClientHelper(context);

		String url = context.getResources().getString(
				R.string.steampowered_http)
				+ Constants.GET_FRIEND_URL
				+ Constants.KEY
				+ "&steamids="
				+ steamid;
		String jsonResult;
		try {
			jsonResult = httpClientHelper.getRequest(url);
			if (jsonResult != null) {
				JSONObject jsonObject = new JSONObject(jsonResult);
				if (jsonObject != null && jsonObject.has("response")) {
					JSONObject object = jsonObject.getJSONObject("response");
					if (object != null && object.has("players")) {
						JSONArray jsonArray = object.getJSONArray("players");
						if (jsonArray != null && jsonArray.length() > 0) {
							list = new ArrayList<Friends>();
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject json = jsonArray.getJSONObject(i);
								Friends friends = new Friends();
								if (json != null && json.has("steamid")) {
									String steamId = json.getString("steamid");
									friends.setSteamid(steamId);
								}
								if (json != null && json.has("personaname")) {
									String personaname = json
											.getString("personaname");
									friends.setPersonaname(personaname);
								}
								if (json != null && json.has("lastlogoff")) {
									int lastlogoff = (Integer) json
											.get("lastlogoff");
									friends.setLastlogoff(lastlogoff);
								}
								if (json != null && json.has("avatar")) {
									String avatar = json.getString("avatar");
									friends.setAvatar(avatar);
								}
								if (json != null && json.has("personastate")) {
									int personastate = json
											.getInt("personastate");
									friends.setPersonastate(personastate);
								}
								if (json != null && json.has("gameserverip")) {
									String gameserverip = json
											.getString("gameserverip");
									friends.setGameserverip(gameserverip);
								}
								if (json != null && json.has("steamid")) {
									String steamId = json.getString("steamid");
									friends.setSteamid(steamId);
								}
								list.add(friends);
							}
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private void analysisUserInfo(String doc, Map<String, Object> map) {

		if (doc != null) {
			String[] userInfos = doc.split("image-avatar image-player");
			int count = 0;
			if (userInfos != null && userInfos.length > 0) {
				String[] tempStrs = new String[userInfos.length - 1];
				for (int i = 0; i < userInfos.length - 1; i++) {
					tempStrs[i] = userInfos[i + 1];
				}
				count = tempStrs.length;
				for (int i = 0; i < count; i++) {
					String temp = tempStrs[i];
					int urlStart = temp.indexOf("src=\"");
					int urlEnd = temp.indexOf("\" title=");
					if (urlStart != -1 && urlEnd > urlStart) {
						String imagesPath = temp.substring("src=\"".length()
								+ urlStart, urlEnd);
						map.put("url", imagesPath);
					}

					int nameStart = temp.indexOf("title=\"");
					int nameEnd = temp.indexOf("\" /></a>");
					if (nameStart != -1 && nameEnd > nameStart) {
						String name = temp.substring("title=\"".length()
								+ nameStart, nameEnd);
						map.put("name", name);
					}

					temp = temp.substring(nameEnd);
					int timeStart = temp.indexOf("datetime=\"");
					int timeEnd = temp.indexOf(
							"\" title=");
					if (timeStart != -1 && timeEnd > timeStart) {
						String time = temp.substring("datetime=\"".length()
								+ timeStart, timeEnd);
						map.put("time", time);
					}

					int idStart = temp.indexOf("players/");
					int idEnd = temp.indexOf("/tooltip");
					if (idStart != -1 && idEnd > idStart) {
						String IdStr = temp.substring("players/".length()
								+ idStart, idEnd);

						map.put("id", IdStr);
					}
				}
			}
			GamesNet net = new GamesNet();
			map.put("gamesList", net.getMoreGameList(doc, false));
		}
	}

	public int postServer(Context context) {
		int resultCode = 0;
		String userConfigName = FileUtil.getSharedPreValue(context,
				Constants.SP_NAME, Constants.NAME);
		if (userConfigName == null || "".equals(userConfigName)) {
			userConfigName = "0";
		}
		String userConfigId = FileUtil.getSharedPreValue(context,
				Constants.SP_NAME, Constants.ID);
		if (userConfigId == null || "".equals(userConfigId)) {
			userConfigId = "0";
		}
		String tokenid = DevicesUtil.getLocalMacAddress(context);
		if("00:00:00:00:00:00".equals(tokenid)) {
			tokenid = DevicesUtil.getAndroidId(context);
		}
		String type = "1";
		String url = context.getResources().getString(R.string.dota2box_http)
				+ Constants.POST_SERVER_URL + "userConfigName="
				+ userConfigName + "&userConfigId=" + userConfigId
				+ "&tokenid=" + tokenid + "&type=" + type;
		HttpClientHelper client = new HttpClientHelper(context);
		try {
			String jsonResult = client.getRequest(url);
			if (jsonResult != null) {
				int start = jsonResult.indexOf("<return>");
				int end = jsonResult.indexOf("</return>");
				String codeStr = jsonResult.substring("<return>".length()
						+ start, end);
				if (codeStr != null && !"".equals(codeStr)) {
					int code = Integer.parseInt(codeStr);
					return code;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}
}
