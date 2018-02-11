package modules.spygame.service;

import java.util.List;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.spygame.ddl.SpyGameJuDDL;

public class SpyGameJuService {
	
	public static List<SpyGameJuDDL> getListByRoomAndJuStatus(long roomId,int juStatus){
		Condition con = new Condition("SpyGameJuDDL.roomId","=",roomId);
		con.add(new Condition("SpyGameJuDDL.juStatus","=",juStatus), "and");
		return Dal.select("SpyGameJuDDL.*", con, null, 0, -1);
	}
	
	public static SpyGameJuDDL create(long roomId){
		int cju = countByRoomIdAndJuStatus(roomId,2);
		SpyGameJuDDL gameJu = new SpyGameJuDDL();
		gameJu.setJu(cju+1);
		gameJu.setJuResult(0);	
		gameJu.setJuStatus(0);
		gameJu.setRoomId(roomId);
		long juId = Dal.insertSelectLastId(gameJu);
		if(juId<=0){
			return null;
		}
		gameJu.setJuId(juId);
		return gameJu;
	}
	
	public static SpyGameJuDDL get(long juId){
		return Dal.select("SpyGameJuDDL.*", juId);
	}
	
	public static int countByRoomIdAndJuStatus(long roomId,int juStatus){
		Condition con = new Condition("SpyGameJuDDL.roomId","=",roomId);
		con.add(new Condition("SpyGameJuDDL.juStatus","=",juStatus), "and");
		return Dal.count(con);
	} 
	
	public static SpyGameJuDDL findUsableJu(long roomId){
		Condition con = new Condition("SpyGameJuDDL.roomId","=",roomId);
		con.add(new Condition("SpyGameJuDDL.juStatus","=",0), "and");
		List<SpyGameJuDDL> list =  Dal.select("SpyGameJuDDL.*", con, null, 0, 1);
		if(list == null || list.size() <= 0){
			return null;
		}
		return list.get(0);
	}
	
	public static boolean updateJuStatus(long juId,int juStatus){
		SpyGameJuDDL room = get(juId);
		if(room == null){
			return false;
		}
		room.setJuStatus(juStatus);
		return Dal.insertUpdate(room, "SpyGameJuDDL.juStatus") > 0;
	}
	
	public static boolean updateJuStatus(long juId,int juStatus,int juResult){
		SpyGameJuDDL ju = get(juId);
		if(ju == null){
			return false;
		}
		ju.setJuStatus(juStatus);
		ju.setJuResult(juResult);
		return Dal.insertUpdate(ju, "SpyGameJuDDL.juStatus,SpyGameJuDDL.juResult") > 0;
	}
	
}
