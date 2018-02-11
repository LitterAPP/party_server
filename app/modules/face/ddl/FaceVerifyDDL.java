package modules.face.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="face_verify")
public class FaceVerifyDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.Int)
	private Integer id;
	
	@Column(name="my_avatar", type=DbType.Varchar)
	private String myAvatar;
	
	@Column(name="ta_avatar", type=DbType.Varchar)
	private String taAvatar;
	
	@Column(name="my_name", type=DbType.Varchar)
	private String myName;
	
	@Column(name="ta_name", type=DbType.Varchar)
	private String taName;
	
	@Column(name="confidence", type=DbType.Float)
	private Float confidence;
	
	@Column(name="thresholds_1", type=DbType.Float)
	private Float thresholds1;
	
	@Column(name="thresholds_2", type=DbType.Float)
	private Float thresholds2;
	
	@Column(name="thresholds_3", type=DbType.Float)
	private Float thresholds3;
	
	@Column(name="create_time", type=DbType.DateTime)
	private Long createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMyAvatar() {
		return myAvatar;
	}

	public void setMyAvatar(String myAvatar) {
		this.myAvatar = myAvatar;
	}

	public String getTaAvatar() {
		return taAvatar;
	}

	public void setTaAvatar(String taAvatar) {
		this.taAvatar = taAvatar;
	}

	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	public String getTaName() {
		return taName;
	}

	public void setTaName(String taName) {
		this.taName = taName;
	}

	public Float getConfidence() {
		return confidence;
	}

	public void setConfidence(Float confidence) {
		this.confidence = confidence;
	}

	public Float getThresholds1() {
		return thresholds1;
	}

	public void setThresholds1(Float thresholds1) {
		this.thresholds1 = thresholds1;
	}

	public Float getThresholds2() {
		return thresholds2;
	}

	public void setThresholds2(Float thresholds2) {
		this.thresholds2 = thresholds2;
	}

	public Float getThresholds3() {
		return thresholds3;
	}

	public void setThresholds3(Float thresholds3) {
		this.thresholds3 = thresholds3;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	} 
}
