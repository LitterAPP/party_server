package controllers.dto;

import java.util.ArrayList;
import java.util.List;

import modules.apply.ddl.PartyApplyInfoDDL;
import modules.location.ddl.MasterLocationDDL;
import modules.party.ddl.PartyDDL;
import modules.user.ddl.WxUserDDL;

public class ApplyInfoItemDto {
	
	private PartyApplyInfoDDL apply;
	private WxUserDDL master;
	private PartyDDL party;
	private MasterLocationDDL location;
	private List<WxUserDDL> takeUsers = new ArrayList<WxUserDDL>();
	
	public PartyApplyInfoDDL getApply() {
		return apply;
	}
	public void setApply(PartyApplyInfoDDL apply) {
		this.apply = apply;
	}
	
	public WxUserDDL getMaster() {
		return master;
	}
	public void setMaster(WxUserDDL master) {
		this.master = master;
	}
	public PartyDDL getParty() {
		return party;
	}
	public void setParty(PartyDDL party) {
		this.party = party;
	}
	public MasterLocationDDL getLocation() {
		return location;
	}
	public void setLocation(MasterLocationDDL location) {
		this.location = location;
	}
	public List<WxUserDDL> getTakeUsers() {
		return takeUsers;
	}
	public void setTakeUsers(List<WxUserDDL> takeUsers) {
		this.takeUsers = takeUsers;
	}
	
	
}
