package modules.party.ddl;

import java.text.SimpleDateFormat;
import java.util.Date;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="party")
public class PartyDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.BigInt)
	private Long id;
	
	@Column(name="title",type=DbType.Varchar)
	private String title;
	
	@Column(name="banner",type=DbType.Varchar)
	private String banner;
	
	@Column(name="open_id",type=DbType.Varchar)
	private String openid;
	
	@Column(name="create_time",type=DbType.DateTime)
	private Long createTime;
	
	@Column(name="modify_time",type=DbType.DateTime)
	private Long modifyTime;
	
	@Column(name="start_time",type=DbType.DateTime)
	private Long startTime;
	
	@Column(name="notify_hours",type=DbType.Float)
	private Float notifyHours;
	
	
	
	private String showModifyTime;
	private String showStartTime;
	private String showDate;
	private String showWeekDay;
	private Integer status;//1=进行中 2=已结束

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;  
	}

	public String getShowModifyTime() {
		return showModifyTime;
	}

	public void setShowModifyTime(String showModifyTime) {
		this.showModifyTime = showModifyTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public String getShowStartTime() {
		return showStartTime;
	}

	public void setShowStartTime(String showStartTime) {
		this.showStartTime = showStartTime;
	}

	public Float getNotifyHours() {
		return notifyHours;
	}

	public void setNotifyHours(Float notifyHours) {
		this.notifyHours = notifyHours;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public String getShowWeekDay() {
		return showWeekDay;
	}

	public void setShowWeekDay(String showWeekDay) {
		this.showWeekDay = showWeekDay;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	} 
	
	
}
