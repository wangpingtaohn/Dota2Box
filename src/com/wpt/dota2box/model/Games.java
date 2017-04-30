package com.wpt.dota2box.model;

import java.util.List;

public class Games {

	//url
	private String url;
	
	//名称
	private String name;
	
	//比赛类型
	private String matchType;
	
	//阵营
	private String pick;
	
	//胜利or失败
	private String rang;
	
	//持续时长
	private String last;
	
	//时间
	private String time;
	
	 //KDA
	private String kda;
	
	//id
	private String matchid;
	
	//装备连接
	private List<String> urlList;

	
	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
	
	public List<String> getUrlList() {
		return urlList;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getPick() {
		return pick;
	}

	public void setPick(String pick) {
		this.pick = pick;
	}

	public String getRang() {
		return rang;
	}

	public void setRang(String rang) {
		this.rang = rang;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getKda() {
		return kda;
	}

	public void setKda(String kda) {
		this.kda = kda;
	}

	public String getMatchid() {
		return matchid;
	}

	public void setMatchid(String matchid) {
		this.matchid = matchid;
	}

}
