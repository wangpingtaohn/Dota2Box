package com.wpt.dota2box.model;

public class Rank {

	private String rank;
	
	private String accountId;
	
	private String name;
	
	private String country;
	
	private String soloMmr;
	
	private String teamTag;

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSoloMmr() {
		return soloMmr;
	}

	public void setSoloMmr(String soloMmr) {
		this.soloMmr = soloMmr;
	}
	
	public void setTeamTag(String teamTag) {
		this.teamTag = teamTag;
	}
	
	public String getTeamTag() {
		return teamTag;
	}

}
