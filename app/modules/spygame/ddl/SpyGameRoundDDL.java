package modules.spygame.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="spy_game_round")
public class SpyGameRoundDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="round_id", type=DbType.BigInt)
	private Long roundId;
	
	@Column(name="room_id", type=DbType.BigInt)
	private Long roomId;
	
	@Column(name="ju_id", type=DbType.BigInt)
	private Long juId;
	
	
	@Column(name="round", type=DbType.Int)
	private Integer round;
	
	@Column(name="round_status", type=DbType.Int)
	private Integer roundStatus;//0=无状态 1=发言中 2=发言结束 3=投票中 4=投票结束

	public Long getRoundId() {
		return roundId;
	}

	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getJuId() {
		return juId;
	}

	public void setJuId(Long juId) {
		this.juId = juId;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Integer getRoundStatus() {
		return roundStatus;
	}

	public void setRoundStatus(Integer roundStatus) {
		this.roundStatus = roundStatus;
	} 
}
