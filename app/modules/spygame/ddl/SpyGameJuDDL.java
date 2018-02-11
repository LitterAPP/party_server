package modules.spygame.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="spy_game_ju")
public class SpyGameJuDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="ju_id", type=DbType.BigInt)
	private Long juId;
	
	@Column(name="room_id", type=DbType.BigInt)
	private Long roomId;
	
	@Column(name="ju", type=DbType.Int)
	private Integer ju;
	
	@Column(name="ju_result", type=DbType.Int)
	private Integer juResult;
	
	@Column(name="ju_status", type=DbType.Int)
	private Integer juStatus;
	 
	public Long getJuId() {
		return juId;
	}

	public void setJuId(Long juId) {
		this.juId = juId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Integer getJu() {
		return ju;
	}

	public void setJu(Integer ju) {
		this.ju = ju;
	}

	public Integer getJuResult() {
		return juResult;
	}

	public void setJuResult(Integer juResult) {
		this.juResult = juResult;
	}

	public Integer getJuStatus() {
		return juStatus;
	}

	public void setJuStatus(Integer juStatus) {
		this.juStatus = juStatus;
	}

	 
}
