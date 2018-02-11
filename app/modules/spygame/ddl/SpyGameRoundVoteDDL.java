package modules.spygame.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="spy_game_round_vote")
public class SpyGameRoundVoteDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.BigInt)
	private Long id;
	
	@Column(name="room_id", type=DbType.BigInt)
	private Long roomId;
	
	@Column(name="ju_id", type=DbType.BigInt)
	private Long juId;
	
	@Column(name="round_id", type=DbType.BigInt)
	private Long roundId;
	
	@Column(name="member_id", type=DbType.BigInt)
	private Long memberId;
	
	
	@Column(name="voter_member_id", type=DbType.BigInt)
	private Long voterMemberId;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public Long getRoundId() {
		return roundId;
	}


	public void setRoundId(Long roundId) {
		this.roundId = roundId;
	}


	public Long getMemberId() {
		return memberId;
	}


	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}


	public Long getVoterMemberId() {
		return voterMemberId;
	}


	public void setVoterMemberId(Long voterMemberId) {
		this.voterMemberId = voterMemberId;
	}
	
	
	
}
