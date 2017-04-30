package com.wpt.dota2box.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.test.AndroidTestCase;
import android.util.Log;

import com.wpt.dota2box.net.SearchNet;
import com.wpt.frame.net.https.HttpClientHelper;


public class NetTest extends AndroidTestCase {

	public void testNet() throws Exception {
//		getInfo();
//		net();
		SearchNet net = new SearchNet();
//		net.getOnlineNumber(getContext());
		
//		net.getUserInfo(getContext(), "141421197");
//		net();
		
	}
	
    public static String ascii2native(String ascii) {  
        int n = ascii.length() / 6;  
        StringBuilder sb = new StringBuilder(n);  
        for (int i = 0, j = 2; i < n; i++, j += 6) {  
            String code = ascii.substring(j, j + 4);  
            char ch = (char) Integer.parseInt(code, 16);  
            sb.append(ch);  
        }  
        return sb.toString();  
    }  
	
	private void net() {
		
		HttpClientHelper clientHelper = new HttpClientHelper(getContext());
		try {
			String result = clientHelper.getRequest("http://www.dotabuff.com/players/141421197");
			Log.i("wpt", "tempStr=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getInfo() {
		Document doc = null;
		try {
			doc = (Document) Jsoup
					.connect(
							"http://store.dota2.com.cn/searchdata?ajax=1&l=schinese&c=RMB&appid=570&v=_M1400279931&query=斧王")
					.data("query", "Java").userAgent("Mozilla")
					.cookie("auth", "token").timeout(3000).ignoreContentType(true).post();
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
//		Elements srcLinks = doc.select("img[src$=.png]");
//		Elements times = doc.getElementsByTag("time");
//		int timeCount = 0;
//		int imgCount = 0;
//		int hrefCount = 0;
//		for (Element element : times) {
//			timeCount++;
//		}
//		for (Element element : srcLinks) {
//			imgCount++;
//			String imagesPath = element.attr("src");
//			String IdStr = element.attr("data-tooltip-url");
//			int startIndex = "/players/".length();
//			int endIndex = IdStr.indexOf("/tooltip");
//			IdStr = IdStr.substring(startIndex, endIndex);
//			System.out.println("imagesPath=" + imagesPath);
//			System.out.println("IdStr=" + IdStr);
//		}
		Log.i("wpt", "doc" + doc.toString());
	}
}
