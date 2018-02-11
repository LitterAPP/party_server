package modules.party.service;

import java.util.Date;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.party.ddl.PartyDDL;

public class PartyService {

	public static PartyDDL get(long id){
		if(id <= 0){
			return null;
		}
		return Dal.select("PartyDDL.*", id);
	}
	public static long add(PartyDDL party){
		if(null == party){
			return 0;
		}
		party.setModifyTime(System.currentTimeMillis());
		 
		if(party.getId()==null || party.getId()<=0){
			party.setCreateTime(System.currentTimeMillis()); 
			return Dal.insertSelectLastId(party);
		} 
		if(update(party)>0){
			return party.getId();
		}
		return 0;
	}
	
	public static int del(long partyId){
		if(partyId <= 0){
			return 0;
		}
		return Dal.delete(new Condition("PartyDDL.id","=",partyId));
	}
	
	public static int update(PartyDDL party){
		if(null == party){
			return 0;
		} 
		return Dal.insertUpdate(party, "PartyDDL.title,PartyDDL.modifyTime,PartyDDL.startTime,PartyDDL.banner"); 
	}
	
}
