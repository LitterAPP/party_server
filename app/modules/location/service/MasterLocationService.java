package modules.location.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.location.ddl.MasterLocationDDL;

public class MasterLocationService {

	public static boolean add(MasterLocationDDL location){
		MasterLocationDDL exist = getByPartyId(location.getPartyId());
		if(exist==null){
			return Dal.insert(location)>0;
		}
		location.setId(exist.getId());
		return update(location)>0;
	}
	
	public static MasterLocationDDL getByPartyId(long partyId){
		if(partyId <=0 ){
			return null;
		}
		Condition condition = new Condition("MasterLocationDDL.partyId","=",partyId);
		List<MasterLocationDDL> result = Dal.select("MasterLocationDDL.*", condition, null, 0, 1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result.get(0);
	}
	
	public static int update(MasterLocationDDL location){
		if(location==null){
			return 0;
		}
		return Dal.insertUpdate(location,
				"MasterLocationDDL.latitude,MasterLocationDDL.longitude,MasterLocationDDL.name,MasterLocationDDL.address");
	}
	
	public static int delByPartyId(long partyId){
		if(partyId<=0){
			return 0;
		}
		Condition condition = new Condition("MasterLocationDDL.partyId","=",partyId);
		return Dal.delete(condition);
	}
}
