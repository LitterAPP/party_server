package modules.game.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="game_room")
public class GameRoomDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="room_id", type=DbType.BigInt)
	private Long roomId;
	
	@Column(name="room_status", type=DbType.Int)
	private Integer roomStatus;
	
	@Column(name="limit_party_id", type=DbType.BigInt)
	private Long limitPartyId;
	
	@Column(name="create_time", type=DbType.DateTime)
	private Long creatTime;

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Integer getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(Integer roomStatus) {
		this.roomStatus = roomStatus;
	}

	public Long getLimitPartyId() {
		return limitPartyId;
	}

	public void setLimitPartyId(Long limitPartyId) {
		this.limitPartyId = limitPartyId;
	}

	public Long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Long creatTime) {
		this.creatTime = creatTime;
	} 
}
