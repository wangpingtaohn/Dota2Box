package com.wpt.dota2box.net;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.Games;
import com.wpt.dota2box.model.GamesDetail;
import com.wpt.frame.net.https.HttpClientHelper;

public class GamesNet {

	/**
	 * @Description: 获取更多的比赛
	 * @author wpt
	 * @since 2014-8-28 下午4:13:17
	 * @param context
	 * @param id
	 * @param page
	 * @return
	 */
	public List<Games> getMoreGameList(Context context, String id, String page) {
		List<Games> list = null;
		HttpClientHelper client = new HttpClientHelper(context);
		String url = context.getResources().getString(R.string.dotabuff_http)
				+ Constants.GET_MORE_GAMES_URL + id + "/matches?page=" + page;
		try {
			String resultStr = client.getRequest(url);
			list = getMoreGameList(resultStr, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Games> getMoreGameList(String httpStr, boolean isMore) {
		List<Games> gamesList = null;
		int count = 0;
		String subStr = "<section><header>Latest";
		int index = httpStr.indexOf(subStr);
		String tempStr = httpStr.substring(subStr.length() + index);
		String[] gameInfos = tempStr
				.split("<div class=\"image-container image-container-icon image-container-hero\">");
		if (gameInfos != null && gameInfos.length > 0) {
			count = gameInfos.length;
			gamesList = new ArrayList<Games>();
			for (int i = 1; i < count; i++) {
				String temp = gameInfos[i];
				Games games = new Games();

				int nameStart = temp.indexOf("alt=\"");
				int nameEnd = temp.indexOf("\" class=");
				if (nameStart != -1 && nameEnd > nameStart) {
					String name = temp.substring("alt=\"".length() + nameStart,
							nameEnd);
					games.setName(name);
				}

				temp = temp.substring(nameEnd);
				int urlStart = temp.indexOf("src=\"");
				int urlEnd = temp.indexOf("\" title");
				if (urlStart != -1 && urlEnd > urlStart) {
					String url = temp.substring("src=\"".length() + urlStart,
							urlEnd);
					url = url.startsWith("http") ? url : "http://dotabuff.com/"
							+ url.replace("&#47;", "/");
					games.setUrl(url);
				}

				temp = temp.substring(urlEnd);
				if (isMore) {
					temp = temp.replaceFirst("\">", "");
				}
				int matchidStart = temp.indexOf("<a href=\"/matches/");
				int matchidEnd = temp.indexOf("\">");
				if (matchidStart != -1 && matchidEnd > matchidStart) {
					String matchid = temp.substring(
							"<a href=\"/matches/".length() + matchidStart,
							matchidEnd);
					games.setMatchid(matchid);
				}

				temp = temp.substring(matchidEnd);
				boolean rang = temp.contains("\"won\"");
				if (rang) {
					games.setRang("胜利");
				} else {
					games.setRang("失败");
				}

				int timeStart = temp.indexOf("datetime=\"");
				int timeEnd = temp.indexOf("\" title=");
				if (timeStart != -1 && timeEnd > timeStart) {
					String time = temp.substring("datetime=\"".length()
							+ timeStart, timeEnd);
					games.setTime(time);
				}

				temp = temp.substring(timeEnd);
				int matchTypeStart = temp.indexOf("</time></div></td><td>");
				int matchTypeEnd = temp.indexOf("<div class=");
				if (matchTypeStart != -1 && matchTypeEnd > matchTypeStart) {
					String matchType = temp.substring(
							"</time></div></td><td>".length() + matchTypeStart,
							matchTypeEnd);
					if (matchType != null) {
						String matchTypeStr = "";
						if (matchType.startsWith("Ranked")) {
							matchTypeStr = "天梯比赛";
						} else {
							matchTypeStr = "普通比赛";
						}
						games.setMatchType(matchTypeStr);
					}
				}

				temp = temp.substring(matchTypeEnd);
				int pickStart = temp.indexOf("<div class=\"subtext\">");
				int pickEnd = temp.indexOf("</div></td><td>");
				if (pickStart != -1 && pickEnd > pickStart) {
					String pick = temp.substring(
							"<div class=\"subtext\">".length() + pickStart,
							pickEnd);
					if (pick != null) {
						String pickStr = "";
						if (pick.startsWith("All")) {
							pickStr = "全阵营";
						} else if (pick.startsWith("Captains")) {
							pickStr = "队长模式";
						} else {
							pickStr = "其他模式";
						}
						games.setPick(pickStr);
					}
				}

				temp = temp.substring(pickEnd);
				int lastStart = temp.indexOf("</div></td><td>");
				int lastEnd = temp.indexOf("<div class=");
				if (lastStart != -1 && lastEnd > lastStart) {
					String last = temp.substring("</div></td><td>".length()
							+ lastStart, lastEnd);
					games.setLast(last);
				}

				temp = temp.substring(lastEnd);
				int kdaStart = temp
						.indexOf("<span class=\"kda-record\"><span class=\"value\">");
				int kdaEnd = temp.indexOf("</span></span><div class=");
				if (kdaStart != -1 && kdaEnd > kdaStart) {
					String kdaStr = temp.substring(
							"<span class=\"kda-record\"><span class=\"value\">"
									.length() + kdaStart, kdaEnd);
					if (kdaStr != null) {
						String[] kdas = kdaStr
								.split("</span>/<span class=\"value\">");
						if (kdas != null && kdas.length > 0) {
							String kda = "";
							for (int j = 0; j < kdas.length; j++) {
								if (j != kdas.length - 1) {
									kda += kdas[j] + "/";
								} else {
									kda += kdas[j];
								}
							}
							games.setKda(kda);
						}
					}
				}
				temp = temp.substring(kdaEnd);
				if (isMore) {
					String[] zbStrs = temp.split("src=\"");
					List<String> urlList = null;
					if (zbStrs != null && zbStrs.length > 2) {
						urlList = new ArrayList<String>();
						int len = zbStrs.length < 8 ? zbStrs.length : 8;
						for (int j = 2; j < len; j++) {
							String zbUrl = zbStrs[j];
							int start = 0;
							int end = zbUrl.indexOf(" title=\"");
							if (start != -1 && end > start) {
								zbUrl = zbUrl.substring(start, end).replace(
										"\"", "");
							}
							zbUrl = zbUrl.startsWith("http") ? zbUrl
									: "http://dotabuff.com"
											+ zbUrl.replace("&#47;", "/");
							urlList.add(zbUrl);
						}
					}
					games.setUrlList(urlList);
				}
				gamesList.add(games);
			}
		}
		return gamesList;
	}

	public List<GamesDetail> getGamesDetailInfo(Context context, String id) {
		List<GamesDetail> list = null;
		HttpClientHelper client = new HttpClientHelper(context);
		String url = context.getResources().getString(R.string.dotabuff_http)
				+ "/matches/" + id;
		try {
			String resultStr = client.getRequest(url);
			if (resultStr != null) {
				String[] infoS = resultStr
						.split("<div class=\"image-container image-container-icon image-container-hero\">");
				if (infoS != null && infoS.length > 0) {
					list = new ArrayList<GamesDetail>();
					int count = infoS.length <= 11?infoS.length:11;
					for (int i = 1; i < count; i++) {
						GamesDetail games = new GamesDetail();
						String tempStr = infoS[i];

						int herUrlStart = tempStr.indexOf("src=\"");
						int herUrlEnd = tempStr.indexOf("\" title");
						if (herUrlStart != -1 && herUrlEnd > herUrlStart) {
							String heroUrl = tempStr.substring(
									"src=\"".length() + herUrlStart, herUrlEnd);
							heroUrl = heroUrl.startsWith("http") ? heroUrl
									: "http://dotabuff.com"
											+ heroUrl.replace("&#47;", "/");
							games.setHeroUrl(heroUrl);
						}

						tempStr = tempStr.substring("\" title".length() + herUrlEnd);
						int accidStart = tempStr
								.indexOf("data-tooltip-url=\"/players/");
						int accidEnd = tempStr.indexOf("/tooltip");
						String accid = null;
						if (accidStart != -1 && accidEnd > accidStart) {
							accid = tempStr.substring(
									"data-tooltip-url=\"/players/".length()
											+ accidStart, accidEnd);
							games.setId(accid);
						}

						int userUrlEnd = 0;
						if (accid != null && !"".equals(accid)) {
							int userUrlStart = tempStr
									.indexOf(accid
											+ "/tooltip\" rel=\"tooltip-remote\" src=\"");
							userUrlEnd = tempStr.indexOf("\" title");
							if (userUrlStart != -1 && userUrlEnd > userUrlStart) {
								String userUrl = tempStr
										.substring(
												accid.length()
														+ "/tooltip\" rel=\"tooltip-remote\" src=\""
																.length()
														+ userUrlStart,
												userUrlEnd);
								userUrl = userUrl.startsWith("http") ? userUrl
										: "http://dotabuff.com"
												+ userUrl.replace("&#47;", "/");
								games.setUserUrl(userUrl);
							}
						}

						tempStr = tempStr.substring(userUrlEnd);
						int nameStart = tempStr.indexOf(" title=\"");
						int nameEnd = tempStr.indexOf("\" /></a>");
						if (nameStart != -1 && nameEnd > nameStart) {
							String name = tempStr.substring(
									" title=\"".length() + nameStart,
									nameEnd);
							games.setName(name);
						}

						tempStr = tempStr.substring(nameEnd);
						int numberStart = tempStr
								.indexOf("class=\"cell-centered\">");
						int numberEnd = tempStr.indexOf("</td><td><div class");
						if (numberStart != -1 && numberEnd > numberStart) {
							String number = tempStr.substring(
									"class=\"cell-centered\">".length() + numberStart,
									numberEnd);
							String[] levelS = number
									.split("</td><td class=\"cell-centered\">");
							if (levelS != null) {
								if (levelS.length > 0) {
									games.setLevel(levelS[0]);
								}
								if (levelS.length > 3) {
									games.setKda(levelS[1] + "/" + levelS[2]
											+ "/" + levelS[3]);
								}
								if (levelS.length > 4) {
									games.setGold(levelS[4]);
								}
							}
						}

						String[] zbStrs = tempStr.split("src=\"");
						List<String> urlList = null;
						if (zbStrs != null && zbStrs.length > 2) {
							urlList = new ArrayList<String>();
							int len = zbStrs.length < 8 ? zbStrs.length : 8;
							for (int j = 2; j < len; j++) {
								String zbUrl = zbStrs[j];
								int start = 0;
								int end = zbUrl.indexOf(" title=\"");
								if (start != -1 && end > start) {
									zbUrl = zbUrl.substring(start, end)
											.replace("\"", "");
								}
								zbUrl = zbUrl.startsWith("http") ? zbUrl
										: "http://dotabuff.com"
												+ zbUrl.replace("&#47;", "/");
								urlList.add(zbUrl);
							}
							games.setUrlList(urlList);
						}
						list.add(games);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
