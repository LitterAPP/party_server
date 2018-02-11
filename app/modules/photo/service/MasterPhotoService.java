package modules.photo.service;

import java.io.File;
import java.util.List;

import jws.Jws;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.photo.ddl.MasterPhotoDDL;

public class MasterPhotoService {

	public static boolean add(MasterPhotoDDL photo){
		MasterPhotoDDL exist = getByPartyId(photo.getPartyId());
		if(exist==null){
			return Dal.insert(photo)>0;
		}
		photo.setId(exist.getId());
		return update(photo)>0;
	}
	
	public static MasterPhotoDDL getByPartyId(long partyId){
		if(partyId <=0 ){
			return null;
		}
		Condition condition = new Condition("MasterPhotoDDL.partyId","=",partyId);
		List<MasterPhotoDDL> result = Dal.select("MasterPhotoDDL.*", condition, null, 0, 1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result.get(0);
	}
	
	public static int update(MasterPhotoDDL photo){
		if(photo==null){
			return 0;
		}
		return Dal.insertUpdate(photo,"MasterPhotoDDL.path");
	}
	
	public static int delByPartyId(long partyId){
		if(partyId<=0){
			return 0;
		}
		MasterPhotoDDL photo =  getByPartyId(partyId);
		if(null == photo){
			return 0;
		}
		String filePath = Jws.applicationPath.getParent()+photo.getPath();
		File file = new File(filePath);
		if(file.exists() && file.delete()){}
		Condition condition = new Condition("MasterPhotoDDL.partyId","=",partyId);
		return Dal.delete(condition);
	}
}
