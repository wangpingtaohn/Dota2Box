package com.wpt.dota2box.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import android.content.Context;
import android.os.Environment;
import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.Dict;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;
import com.longevitysoft.android.xml.plist.domain.PListObjectType;
import com.wpt.dota2box.R;
import com.wpt.dota2box.common.Constants;
import com.wpt.frame.net.https.HttpClientHelper;
import com.wpt.frame.util.AppUtils;

public class UpdateNet {

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
	public boolean checkUpdate(final Context context) {

		boolean isUpdate = false;

		HttpClientHelper http = new HttpClientHelper(context);
		String url = context.getResources().getString(R.string.dota2box_http)
				+ Constants.CHECK_UPDATE_URL;
		InputStream in = http.getInputStreamRequest(url);
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
				if ("androidversion".equals(key)) {
					PListObject object = map.get(key);
					PListObjectType type = object.getType();
					if (type == PListObjectType.STRING) {
						com.longevitysoft.android.xml.plist.domain.String values = (com.longevitysoft.android.xml.plist.domain.String) object;
						if(values != null && values.getValue() != null){
							String version = values.getValue();
							version = version.replace(".", "");
							int verSionInt = Integer.parseInt(version);
							String apkVerSionStr = AppUtils.getCurrentVersionName(context);
							apkVerSionStr = apkVerSionStr.replace(".", "");
							if(null != apkVerSionStr) {
								int apkVerInt = Integer.parseInt(apkVerSionStr);
								if(apkVerInt != verSionInt) {
									isUpdate = true;
								}
							}
						}
					}
				}
			}
		}
		return isUpdate;

	}
}
