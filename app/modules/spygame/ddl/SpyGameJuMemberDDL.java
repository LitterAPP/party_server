package modules.spygame.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="spy_game_ju_member")
public class SpyGameJuMemberDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="member_id", type=DbType.BigInt)
	private Long memberId;
	
	
	@Column(name="ju_id", type=DbType.BigInt)
	private Long juId;
	
	
	@Column(name="room_id", type=DbType.BigInt)
	private Long roomId;

	
	@Column(name="open_id", type=DbType.Varchar)
	private String openId;
	
	
	@Column(name="master", type=DbType.Int)
	private Integer master;
	
	
	@Column(name="game_role", type=DbType.Int)
	private Integer gameRole;
	
	
	@Column(name="member_status", type=DbType.Int)
	private Integer memberStatus;
	
	@Column(name="word", type=DbType.Varchar)
	private String word; 


	public Long getMemberId() {
		return memberId;
	}


	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}


	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	} 

	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}
	
	
	public Long getRoomId() {
		return roomId;
	}


	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}


	public Integer getMaster() {
		return master;
	}


	public void setMaster(Integer master) {
		this.master = master;
	}


	public Long getJuId() {
		return juId;
	}


	public void setJuId(Long juId) {
		this.juId = juId;
	}

	public Integer getGameRole() {
		return gameRole;
	} 

	public void setGameRole(Integer gameRole) {
		this.gameRole = gameRole;
	}


	public Integer getMemberStatus() {
		return memberStatus;
	}


	public void setMemberStatus(Integer memberStatus) {
		this.memberStatus = memberStatus;
	}   
}
