package modules.message.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.message.ddl.MessageDDL;

public class MessageService {

	public static boolean insert(long partyId,String openid,String message){
		MessageDDL msg = new MessageDDL();
		msg.setOpenid(openid);
		msg.setMessage(message);
		msg.setPartyId(partyId);
		msg.setCreateTime(System.currentTimeMillis());
		
		return Dal.insert(msg) > 0;
	}
	public static MessageDDL get(long id){
		return Dal.select("MessageDDL.*", id);
	}
	public static boolean delete(long id){
		Condition cond = new Condition("MessageDDL.id","=",id);
		return Dal.delete(cond) > 0;
	}
	
	public static List<MessageDDL> listByPartyId(long partyId){
		if( partyId <= 0){
			return null;
		}
		Condition cond = new Condition("MessageDDL.partyId","=",partyId);
		return Dal.select("MessageDDL.*", cond, null, 0, -1);
	}
	
}
