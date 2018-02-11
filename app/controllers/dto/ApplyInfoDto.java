package controllers.dto;

import modules.apply.ddl.PartyApplyInfoDDL;
import modules.user.ddl.WxUserDDL;

public class ApplyInfoDto {
	
	private PartyApplyInfoDDL apply;
	private WxUserDDL user;
	
	public PartyApplyInfoDDL getApply() {
		return apply;
	}
	public void setApply(PartyApplyInfoDDL apply) {
		this.apply = apply;
	}
	public WxUserDDL getUser() {
		return user;
	}
	public void setUser(WxUserDDL user) {
		this.user = user;
	}
	
	
}
