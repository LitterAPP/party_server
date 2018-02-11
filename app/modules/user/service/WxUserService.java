package modules.user.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.record.ddl.MasterRecordDDL;
import modules.user.ddl.WxUserDDL;
import utils.StringUtil;

public class WxUserService {

	public static boolean add(WxUserDDL wxUser){
		WxUserDDL exist = getBySession(wxUser.getSession());
		if(exist==null){
			return Dal.insert(wxUser)>0;
		}
		exist.setId(exist.getId());
		return update(wxUser)>0;
	}
	
	public static WxUserDDL getByOpenId(String openid){
		if(StringUtil.isBlank(openid)){
			return null;
		}
		Condition condition = new Condition("WxUserDDL.openid","=",openid);
		List<WxUserDDL> result = Dal.select("WxUserDDL.*", condition, null, 0, 1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result.get(0);
	}
	
	public static WxUserDDL getBySession(String session){
		if(StringUtil.isBlank(session)){
			return null;
		}
		Condition condition = new Condition("WxUserDDL.session","=",session);
		List<WxUserDDL> result = Dal.select("WxUserDDL.*", condition, null, 0, 1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result.get(0);
	}
	
	public static int update(WxUserDDL wxUser){
		if(wxUser==null){
			return 0;
		}
		return Dal.insertUpdate(wxUser,"WxUserDDL.nickName,WxUserDDL.avatarUrl,WxUserDDL.session,"
				+ "WxUserDDL.gender,WxUserDDL.province,"
				+ "WxUserDDL.city,WxUserDDL.country");
	}
	
}
