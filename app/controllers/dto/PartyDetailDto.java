package controllers.dto;

import java.util.ArrayList;
import java.util.List;

import modules.location.ddl.MasterLocationDDL;
import modules.photo.ddl.MasterPhotoDDL;
import modules.record.ddl.MasterRecordDDL;
import modules.user.ddl.WxUserDDL;

public class PartyDetailDto {

	private MasterRecordDDL record;
	private MasterPhotoDDL photo;
	private MasterLocationDDL location;
	private PartyDto partyInfo;
	private List<ApplyInfoDto> applyInfo = new ArrayList<ApplyInfoDto>();
	private List<WxUserDDL> takeUsers = new ArrayList<WxUserDDL>();
	private List<MessageItemDto> message = new ArrayList<MessageItemDto>();
	
	public MasterRecordDDL getRecord() {
		return record;
	}
	public void setRecord(MasterRecordDDL record) {
		this.record = record;
	}
	public MasterPhotoDDL getPhoto() {
		return photo;
	}
	public void setPhoto(MasterPhotoDDL photo) {
		this.photo = photo;
	}
	public MasterLocationDDL getLocation() {
		return location;
	}
	public void setLocation(MasterLocationDDL location) {
		this.location = location;
	}
	public PartyDto getPartyInfo() {
		return partyInfo;
	}
	public void setPartyInfo(PartyDto partyInfo) {
		this.partyInfo = partyInfo;
	}
	public List<ApplyInfoDto> getApplyInfo() {
		return applyInfo;
	}
	public void setApplyInfo(List<ApplyInfoDto> applyInfo) {
		this.applyInfo = applyInfo;
	}
	public List<WxUserDDL> getTakeUsers() {
		return takeUsers;
	}
	public void setTakeUsers(List<WxUserDDL> takeUsers) {
		this.takeUsers = takeUsers;
	}
	public List<MessageItemDto> getMessage() {
		return message;
	}
	public void setMessage(List<MessageItemDto> message) {
		this.message = message;
	}
}
