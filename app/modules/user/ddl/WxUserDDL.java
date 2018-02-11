package modules.user.ddl;

import jws.dal.annotation.Column;
import jws.dal.annotation.GeneratedValue;
import jws.dal.annotation.GenerationType;
import jws.dal.annotation.Id;
import jws.dal.annotation.Table;
import jws.dal.common.DbType;

@Table(name="wx_users")
public class WxUserDDL {
	@Id
	@GeneratedValue(generationType= GenerationType.Auto)
	@Column(name="id", type=DbType.BigInt)
	private Long id;
	
	@Column(name="open_id",type=DbType.Varchar)
	private String openid;
	
	@Column(name="nick_name",type=DbType.Varchar)
	private String nickName;
	
	@Column(name="avatar_url",type=DbType.Varchar)
	private String avatarUrl;
	
	@Column(name="gender", type=DbType.Int)
	private Integer gender;
	
	@Column(name="province",type=DbType.Varchar)
	private String province;
	
	@Column(name="city",type=DbType.Varchar)
	private String city;
	
	@Column(name="session",type=DbType.Varchar)
	private String session;
	
	@Column(name="country",type=DbType.Varchar)
	private String country;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
 
}
