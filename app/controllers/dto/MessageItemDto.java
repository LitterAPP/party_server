package controllers.dto;

import modules.message.ddl.MessageDDL;
import modules.user.ddl.WxUserDDL;

public class MessageItemDto {

	private WxUserDDL user;
	private MessageDDL message;
	private boolean isMySelf;
	
	public WxUserDDL getUser() {
		return user;
	}
	public void setUser(WxUserDDL user) {
		this.user = user;
	}
	public MessageDDL getMessage() {
		return message;
	}
	public void setMessage(MessageDDL message) {
		this.message = message;
	}
	public boolean isMySelf() {
		return isMySelf;
	}
	public void setMySelf(boolean isMySelf) {
		this.isMySelf = isMySelf;
	} 
	
}
