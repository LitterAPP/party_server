package modules.message.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="message")
public class MessageDDL {

	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.BigInt)
	private Long id;
	
	@Column(name="open_id",type=DbType.Varchar)
	private String openid;
	
	@Column(name="party_id",type=DbType.BigInt)
	private Long partyId;
	
	@Column(name="message",type=DbType.Varchar)
	private String message;
	
	@Column(name="create_time",type=DbType.DateTime)
	private Long createTime;

	private String showCreateTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	 

	public Long getPartyId() {
		return partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getShowCreateTime() {
		return showCreateTime;
	}

	public void setShowCreateTime(String showCreateTime) {
		this.showCreateTime = showCreateTime;
	} 
	
}
