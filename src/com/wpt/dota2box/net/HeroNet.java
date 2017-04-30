package com.wpt.dota2box.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.dota2box.model.HeroList;
import com.wpt.frame.net.https.HttpClientHelper;
import com.wpt.frame.util.AESUtil;

public class HeroNet {

	/**
	 * @Description: 获取饰品列表
	 * @author wpt
	 * @since 2014-8-21 上午10:14:58
	 * @param context
	 * @param handler
	 * @param name
	 */
	public void getShipin(final Context context, final Handler handler,
			final String name) {
		new Thread() {
			public void run() {

				HttpClientHelper http = new HttpClientHelper(context);
				String result = null;
				try {
					String url = context.getResources().getString(
							R.string.store_http)
							+ Constants.SHIPIN_URL;
					result = http.getRequest(url + name);
					List<Map<String, String>> list = getInfo(result);
					if (handler != null) {
						Message msg = new Message();
						msg.obj = list;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	/**
	 * @Description: 解析饰品信息
	 * @author wpt
	 * @since 2014-8-21 上午10:15:14
	 * @param resultStr
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, String>> getInfo(String resultStr)
			throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] array = resultStr.split("ItemImageDropShadow");
		if (array != null && array.length > 0) {
			for (int i = 1; i < array.length; i++) {
				String temp = array[i];
				Map<String, String> map = new HashMap<String, String>();
				int starImgIndex = temp.indexOf("src='");
				int endImgIndex = temp.indexOf("'\\/>\\r\\n\\r\\n\\t\\t<\\/a>");
				if (starImgIndex != -1 && -1 != endImgIndex) {
					String imageUrl = temp.substring(
							starImgIndex + "src='".length(), endImgIndex);
					if (imageUrl != null) {
						imageUrl = imageUrl.replace("\\", "");
					}
					map.put("url", imageUrl);
				}
				int starNameIndex = temp.indexOf("'ItemName_Base Name'>");
				int endNameIndex = temp
						.indexOf("<\\/div>\\r\\n\\r\\n\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t\\t");
				if (starNameIndex != -1 && -1 != endNameIndex) {
					String name = temp.substring(starNameIndex
							+ "'ItemName_Base Name'>".length(), endNameIndex);
					if (name != null) {
						name = AESUtil.ascii2native(name);
					}
					map.put("text", name);
				}
				int starPriceIndex = temp.indexOf("'Price'>");
				int endPriceIndex = temp.indexOf(" &#20992;&#24065;");
				if (starPriceIndex != -1 && -1 != endPriceIndex) {
					String price = temp
							.substring(starPriceIndex + "'Price'>".length(),
									endPriceIndex);
					map.put("price", price);
				}

				int starIdIndex = temp.indexOf("item_details_url\\\":\\\"");
				temp = temp.substring(starIdIndex);
				starIdIndex = temp.indexOf("item_details_url\\\":\\\"");
				int endIdIndex = temp.indexOf("\\\",");
				if (starIdIndex != -1 && -1 != endIdIndex) {
					String idUrl = temp.substring(starIdIndex
							+ "item_details_url\\\":\\\"".length(), endIdIndex);
					if (idUrl != null && idUrl.contains("\\\\\\")) {
						idUrl = idUrl.replace("\\\\\\", "");
					}
					map.put("idUrl", idUrl);
				}

				list.add(map);
			}
		}
		return list;
	}

	/**
	 * @Description: 根据id获取英雄列表
	 * @author wpt
	 * @since 2014-8-28 下午4:17:01
	 * @param context
	 * @param id
	 * @return
	 */
	public List<HeroList> getHeroList(Context context, String id) {
		List<HeroList> list = null;
		HttpClientHelper client = new HttpClientHelper(context);
		String url = context.getResources().getString(R.string.dotabuff_http)
				+ "/players/" + id + Constants.GET_HERO_LIST_URL;
		try {
			String resultStr = client.getRequest(url);
			if (resultStr != null) {
				String[] resultStrs = resultStr
						.split("<div class=\"image-container image-container-icon image-container-hero\">");
				if (resultStrs != null && resultStrs.length > 0) {
					list = new ArrayList<HeroList>();
					for (int i = 1; i < resultStrs.length; i++) {
						String tempStr = resultStrs[i];
						HeroList heros = new HeroList();

						int nameStart = tempStr.indexOf("title=\"");
						int nameEnd = tempStr.indexOf("\" /></a></div></td>");
						if (nameStart != -1 && nameEnd > nameStart) {
							String name = tempStr.substring("title=\"".length()
									+ nameStart, nameEnd);
							heros.setName(name);
						}

						int urlStart = tempStr.indexOf("src=\"");
						int urlEnd = tempStr.indexOf("\" title");
						if (urlStart != -1 && urlEnd > urlStart) {
							String urlStr = tempStr.substring("src=\"".length()
									+ urlStart, urlEnd);
							urlStr = urlStr.startsWith("http") ? urlStr
									: "http://dotabuff.com"
											+ urlStr.replace("&#47;", "/");
							heros.setUrl(urlStr);
						}

						int countStart = tempStr.indexOf("</a></td><td>");
						int countEnd = tempStr.indexOf("<div class=");
						if (countStart != -1 && countEnd > countStart) {
							String count = tempStr.substring(
									"</a></td><td>".length() + countStart,
									countEnd);
							heros.setCount(count);
						}

						int rateStart = tempStr
								.indexOf("></div></div></td><td>");
						int rateEnd = tempStr.indexOf("%<div class=");
						if (rateStart != -1 && rateEnd > rateStart) {
							String rate = tempStr.substring(
									"></div></div></td><td>".length()
											+ rateStart, rateEnd);
							heros.setRate(rate);
						}

						tempStr = tempStr.substring(rateEnd);
						int kdaStart = tempStr
								.indexOf("></div></div></td><td>");
						tempStr = tempStr.substring(kdaStart);
						kdaStart = tempStr.indexOf("></div></div></td><td>");
						int kdaEnd = tempStr.indexOf("<div class=");
						if (kdaStart != -1 && kdaEnd > kdaStart) {
							String kda = tempStr.substring(
									"></div></div></td><td>".length()
											+ kdaStart, kdaEnd);
							heros.setKda(kda);
						}
						list.add(heros);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @dec获取饰品详细
	 * @param context
	 * @param handler
	 * @param url
	 */
	public Map<String, Object> getShipinDetail(Context context, String url) {

		Map<String, Object> map = null;
		
		HttpClientHelper http = new HttpClientHelper(context);
		String result = null;
		try {
			result = http.getRequest(url + "?l=schinese");
			map = anolysisShipinInfo(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}

	/**
	 * @解析饰品详细信息
	 * @param resultStr
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> anolysisShipinInfo(String resultStr) throws Exception {
		int removeIndex = resultStr.indexOf("<div class=\"PreviewContainer\">");
		String temp = resultStr.substring(removeIndex
				+ "<div class=\"PreviewContainer\">".length());
		Map<String, Object> map = new HashMap<String, Object>();

		String[] goodsArray = temp.split("class=\"SmallPreviewImage\"");
		if (goodsArray != null && goodsArray.length > 1) {
			List<Map<String, String>> goodsList = new ArrayList<Map<String, String>>();
			for (int i = 1; i < goodsArray.length; i++) {
				Map<String, String> goodsMap = new HashMap<String, String>();
				String temps = goodsArray[i];
				int starAvatarIndex = temps.indexOf("src=\"");
				int endAvatarIndex = temps.indexOf("\" title=");
				if (starAvatarIndex != -1 && -1 != endAvatarIndex) {
					String avatar = temps
							.substring(starAvatarIndex + "src=\"".length(),
									endAvatarIndex);
					goodsMap.put("url", avatar);
				}
				int starNameIndex = temps.indexOf("title=\"");
				int endNameIndex = temps.indexOf("\" /></a>");
				if (starNameIndex != -1 && -1 != endNameIndex) {
					String name = temps.substring(
							starNameIndex + "title=\"".length(), endNameIndex);
					goodsMap.put("text", name);
				}
				goodsList.add(goodsMap);
			}
			map.put("goodsArray", goodsList);
			
		}
		int starNameIndex = temp.indexOf("Title\">");
		int endNameIndex = temp.indexOf("</h2>");
		if (starNameIndex != -1 && -1 != endNameIndex) {
			String name = temp.substring(starNameIndex + "Title\">".length(),
					endNameIndex);
			map.put("name", name);
		}

		int starImgIndex = temp.indexOf("LargePreviewImage\" src=\"");
		int endImgIndex = temp.indexOf("\"/>");
		if (starImgIndex != -1 && -1 != endImgIndex) {
			String imageUrl = temp.substring(starImgIndex
					+ "LargePreviewImage\" src=\"".length(), endImgIndex);
			map.put("url", imageUrl);
		}

		int starheroIndex = temp.indexOf("<span class=\"AttribValue\">");
		int endheroIndex = temp.indexOf("</span><br />");
		if (starheroIndex != -1 && -1 != endheroIndex) {
			String hero = temp.substring(starheroIndex
					+ "<span class=\"AttribValue\">".length(), endheroIndex);
			map.put("hero", hero);
		}

		temp = temp.substring(endheroIndex + "</span><br />".length());
		int starlevelIndex = temp.indexOf("style=\"color: ");
		int endlevelIndex = temp.indexOf("</span><br />");
		if (starlevelIndex != -1 && -1 != endlevelIndex) {
			String level = temp.substring(
					starlevelIndex + "style=\"color: ".length(), endlevelIndex);
			if (null != level) {
				int tempIndex = level.indexOf("\">");
				level = level.substring(tempIndex + "\">".length());
			}
			map.put("level", level);
		}

		int starPriceIndex = temp
				.indexOf("<div class=\"Price contentBox\">\r\n\t\t");
		int endPriceIndex = temp.indexOf(" &#20992;&#24065;");
		if (starPriceIndex != -1 && -1 != endPriceIndex) {
			String price = temp.substring(starPriceIndex
					+ "<div class=\"Price contentBox\">\r\n\t\t".length(),
					endPriceIndex);
			if (price != null) {
				price = price.trim();
				if (price.contains("<span")) {
					int tempIndex = price.indexOf("<span");
					price = price.substring("<span".length() + tempIndex);
					tempIndex = price.indexOf("\n");
					price = price.substring("<span".length() + tempIndex);
				}
			}
			map.put("price", price.trim());
		}

		return map;
	}
}
