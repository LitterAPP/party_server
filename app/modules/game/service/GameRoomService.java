package modules.game.service;

import jws.dal.Dal;
import modules.game.ddl.GameRoomDDL;

public class GameRoomService {

	public static GameRoomDDL get(long roomId){
		return Dal.select("GameRoomDDL.*", roomId);
	}
	
	public static long create(long limitPartyId){
		GameRoomDDL room = new GameRoomDDL();
		room.setCreatTime(System.currentTimeMillis());
		room.setLimitPartyId(limitPartyId);
		room.setRoomStatus(0);
		return Dal.insertSelectLastId(room);
	}
	
	public static boolean updateStatus(long roomId,int roomStatus){
		GameRoomDDL old = get(roomId);
		if(old == null){
			return false;
		}
		old.setRoomStatus(roomStatus);
		return Dal.insertUpdate(old, "GameRoomDDL.roomStatus")>0;
	}
}
