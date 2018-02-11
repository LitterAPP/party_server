package controllers.dto;

import modules.party.ddl.PartyDDL;
import modules.user.ddl.WxUserDDL;

public class PartyDto {
	
	private PartyDDL party;
	private WxUserDDL user;
	
	public PartyDDL getParty() {
		return party;
	}
	public void setParty(PartyDDL party) {
		this.party = party;
	}
	public WxUserDDL getUser() {
		return user;
	}
	public void setUser(WxUserDDL user) {
		this.user = user;
	}
	
}
