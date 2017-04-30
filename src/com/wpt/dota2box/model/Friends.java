package com.wpt.dota2box.model;

public class Friends {

	private String steamid;
	
	private String personaname;
	
	private String avatar;
	
	private long lastlogoff;
	
	private int personastate;
	
	private String gameserverip;

	public String getSteamid() {
		return steamid;
	}

	public void setSteamid(String steamid) {
		this.steamid = steamid;
	}

	public String getPersonaname() {
		return personaname;
	}

	public void setPersonaname(String personaname) {
		this.personaname = personaname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public long getLastlogoff() {
		return lastlogoff;
	}

	public void setLastlogoff(long lastlogoff) {
		this.lastlogoff = lastlogoff;
	}

	public int getPersonastate() {
		return personastate;
	}

	public void setPersonastate(int personastate) {
		this.personastate = personastate;
	}

	public String getGameserverip() {
		return gameserverip;
	}

	public void setGameserverip(String gameserverip) {
		this.gameserverip = gameserverip;
	}
	
}
