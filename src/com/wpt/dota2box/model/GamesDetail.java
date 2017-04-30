package com.wpt.dota2box.model;

import java.util.List;

public class GamesDetail {

	//heroUrl
	private String heroUrl;
	
	//userUrl
	private String userUrl;
	
	//名称
	private String name;
	
	//等级
	private String level;
	
	//金钱
	private String gold;

	//KDA
	private String kda;
	
	//id
	private String id;
	
	//装备连接
	private List<String> urlList;

	public String getHeroUrl() {
		return heroUrl;
	}

	public void setHeroUrl(String heroUrl) {
		this.heroUrl = heroUrl;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getGold() {
		return gold;
	}

	public void setGold(String gold) {
		this.gold = gold;
	}

	public String getKda() {
		return kda;
	}

	public void setKda(String kda) {
		this.kda = kda;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

}
