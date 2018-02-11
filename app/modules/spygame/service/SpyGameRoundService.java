package modules.spygame.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.spygame.ddl.SpyGameRoundDDL;

public class SpyGameRoundService {

	
	public static SpyGameRoundDDL create(long roomId,long juId){
		int roundCount = countByRoomIdJuIdStatus(roomId,juId,4);
		SpyGameRoundDDL newRound = new SpyGameRoundDDL();
		newRound.setJuId(juId);
		newRound.setRoomId(roomId);
		newRound.setRound(roundCount+1);
		newRound.setRoundStatus(1);
		if( Dal.insert(newRound) >0 ){
			return newRound;
		}
		return null;
	}
	public static SpyGameRoundDDL findByRoomIdJuIdStatus(long roomId,long juId,int rstatus){
		Condition condition = new Condition("SpyGameRoundDDL.roomId","=",roomId);
		condition.add(new Condition("SpyGameRoundDDL.juId","=",juId), "and");
		condition.add(new Condition("SpyGameRoundDDL.roundStatus","=",rstatus), "and");
		List<SpyGameRoundDDL> list = Dal.select("SpyGameRoundDDL.*", condition, null, 0, 1);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	
	public static int countByRoomIdJuIdStatus(long roomId,long juId,int rstatus){
		Condition condition = new Condition("SpyGameRoundDDL.roomId","=",roomId);
		condition.add(new Condition("SpyGameRoundDDL.juId","=",juId), "and");
		condition.add(new Condition("SpyGameRoundDDL.roundStatus","=",rstatus), "and");
		return Dal.count(condition);
	}
	
	public static boolean updateStatus(long roundId,int status){
		SpyGameRoundDDL round = Dal.select("SpyGameRoundDDL.*", roundId);
		if(round == null){
			return false;
		}
		round.setRoundStatus(status);
		return Dal.insertUpdate(round, "SpyGameRoundDDL.roundStatus") > 0;
	}
}
