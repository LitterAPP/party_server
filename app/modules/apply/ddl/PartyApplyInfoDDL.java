package modules.apply.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="party_apply_info")
public class PartyApplyInfoDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.BigInt)
	private Long id;
	
	@Column(name="party_id", type=DbType.BigInt)
	private Long partyId;
	
	@Column(name="open_id",type=DbType.Varchar)
	private String openid;
	
	@Column(name="status", type=DbType.Int)
	private Integer status;
	
	@Column(name="reason",type=DbType.Varchar)
	private String reason;
	
	@Column(name="mobile",type=DbType.Varchar)
	private String mobile;
	
	@Column(name="form_id",type=DbType.Varchar)
	private String formId;
	
	
	@Column(name="msg_sent",type=DbType.Int)
	private Integer msgSent;
	

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	} 
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public Integer getMsgSent() {
		return msgSent;
	}

	public void setMsgSent(Integer msgSent) {
		this.msgSent = msgSent;
	} 
}
