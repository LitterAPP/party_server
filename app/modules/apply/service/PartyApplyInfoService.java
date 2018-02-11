package modules.apply.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import modules.apply.ddl.PartyApplyInfoDDL;
import utils.StringUtil;

public class PartyApplyInfoService {

	public static boolean add(PartyApplyInfoDDL apply){
		PartyApplyInfoDDL exist = getByOpenIdAndPartyId(apply.getOpenid(),apply.getPartyId());
		if(exist==null){ 
			apply.setMsgSent(0);
			return Dal.insert(apply)>0;
		} 
		apply.setId(exist.getId());
		return update(apply)>0;
	}
	
	public static PartyApplyInfoDDL getByOpenIdAndPartyId(String openid,long partyId){
		if(StringUtil.isBlank(openid) || partyId<=0){
			return null;
		}
		Condition condition = new Condition("PartyApplyInfoDDL.openid","=",openid);
		condition.add(new Condition("PartyApplyInfoDDL.partyId","=",partyId), "and");
		List<PartyApplyInfoDDL> result = Dal.select("PartyApplyInfoDDL.*", condition, null, 0, 1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result.get(0);
	}
	
	public static List<PartyApplyInfoDDL> getByOpenIdAndPartyId(long partyId){
		if(partyId<=0){
			return null;
		}
		Condition condition = new Condition("PartyApplyInfoDDL.partyId","=",partyId); 
		List<PartyApplyInfoDDL> result = Dal.select("PartyApplyInfoDDL.*", condition, null, 0, -1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result;
	}
	
	public static int update(PartyApplyInfoDDL apply){
		if(apply==null){
			return 0;
		}
		return Dal.insertUpdate(apply,"PartyApplyInfoDDL.status,PartyApplyInfoDDL.reason,PartyApplyInfoDDL.mobile,PartyApplyInfoDDL.formId,PartyApplyInfoDDL.msgSent");
	}
	
	public static int delByPartyId(long partyId){
		if(partyId<=0){
			return 0;
		}
		Condition condition = new Condition("PartyApplyInfoDDL.partyId","=",partyId);
		return Dal.delete(condition);
	}
	
	public static List<PartyApplyInfoDDL> listMyPartys(String openid,int page,int pageSize){
		if(page==0){
			page = 1;
		}
		if(pageSize == 0){
			pageSize = 10;
		}
		int offset = (page-1)*pageSize;
		Sort sort = new Sort("PartyApplyInfoDDL.id",false);
		Condition condition = new Condition("PartyApplyInfoDDL.openid","=",openid);
		return Dal.select("PartyApplyInfoDDL.*", condition, sort, offset, pageSize);
	}
	
	public static int countMyPartys(String openid){
		Condition condition = new Condition("PartyApplyInfoDDL.openid","=",openid);
		return Dal.count(condition);
	}
	
	public static  List<PartyApplyInfoDDL> listNotifyUsers(int page,int pageSize){
		if(page==0){
			page = 1;
		}
		if(pageSize == 0){
			pageSize = 10;
		}
		int offset = (page-1)*pageSize;
		Sort sort = new Sort("PartyApplyInfoDDL.id",false);
		Condition condition = new Condition("PartyApplyInfoDDL.formId","!=","");
		condition.add(new Condition("PartyApplyInfoDDL.msgSent","=",0) , "and");
		return Dal.select("PartyApplyInfoDDL.*", condition, sort, offset, pageSize);
	}
	
	/**
	 * 活动参加人数
	 * @param partyId
	 * @return
	 */
	public static int countApply(long partyId){
		if(partyId<=0) return 0;
		Condition condition = new Condition("PartyApplyInfoDDL.partyId","=",partyId);
		condition.add(new Condition("PartyApplyInfoDDL.status","=",2) , "and");
		return Dal.count(condition);
	}
	
}
