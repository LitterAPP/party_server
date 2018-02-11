package modules.spygame.service;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.spygame.ddl.SpyGameRoundVoteDDL;

public class SpyGameRoundVoteService {

	public static boolean create(long roomId,long juId,
			long roundId,long memberId,long voterMemberId){
		
		if(countByUnique(roundId,memberId) > 0){
			return false;
		}
		SpyGameRoundVoteDDL vote = new SpyGameRoundVoteDDL();
		vote.setRoomId(roomId);
		vote.setJuId(juId);
		vote.setRoundId(roundId);
		vote.setMemberId(memberId);
		vote.setVoterMemberId(voterMemberId);
		return Dal.insert(vote) > 0;
	}
	
	public static int countByUnique(long roundId,long memberId){
		Condition cond = new Condition("SpyGameRoundVoteDDL.roundId","=",roundId);
		cond.add(new Condition("SpyGameRoundVoteDDL.memberId","=",memberId), "and");
		return Dal.count(cond);
	}
}