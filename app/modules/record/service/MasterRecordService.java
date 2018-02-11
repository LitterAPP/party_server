package modules.record.service;

import java.io.File;
import java.util.List;

import jws.Jws;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.photo.ddl.MasterPhotoDDL;
import modules.record.ddl.MasterRecordDDL;

public class MasterRecordService {

	public static boolean add(MasterRecordDDL record){
		MasterRecordDDL exist = getByPartyId(record.getPartyId());
		if(exist==null){
			return Dal.insert(record)>0;
		}
		record.setId(exist.getId());
		return update(record)>0;
	}
	
	public static MasterRecordDDL getByPartyId(long partyId){
		if(partyId <=0 ){
			return null;
		}
		Condition condition = new Condition("MasterRecordDDL.partyId","=",partyId);
		List<MasterRecordDDL> result = Dal.select("MasterRecordDDL.*", condition, null, 0, 1);
		if(result==null || result.size() ==0 ){
			return null;
		}
		return result.get(0);
	}
	
	public static int update(MasterRecordDDL record){
		if(record==null){
			return 0;
		}
		return Dal.insertUpdate(record,"MasterRecordDDL.path");
	}
	
	public static int delByPartyId(long partyId){
		if(partyId<=0){
			return 0;
		}
		MasterRecordDDL record = getByPartyId(partyId);
		if(null == record){
			return 0;
		}
		String filePath = Jws.applicationPath.getParent()+record.getPath();
		File file = new File(filePath);
		if(file.exists() && file.delete()){}
		Condition condition = new Condition("MasterRecordDDL.partyId","=",partyId);
		return Dal.delete(condition);
	}
	
}
