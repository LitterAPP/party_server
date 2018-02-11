package controllers;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import controllers.dto.ApplyInfoDto;
import controllers.dto.ApplyInfoItemDto;
import controllers.dto.MessageItemDto;
import controllers.dto.MyPartyListDto;
import controllers.dto.PartyDetailDto;
import controllers.dto.PartyDto;
import jws.Jws;
import jws.Logger;
import jws.http.Request;
import jws.http.Response;
import jws.http.sf.HTTP;
import jws.mvc.Controller;
import modules.apply.ddl.PartyApplyInfoDDL;
import modules.apply.service.PartyApplyInfoService;
import modules.location.ddl.MasterLocationDDL;
import modules.location.service.MasterLocationService;
import modules.message.ddl.MessageDDL;
import modules.message.service.MessageService;
import modules.party.ddl.PartyDDL;
import modules.party.service.PartyService;
import modules.photo.ddl.MasterPhotoDDL;
import modules.photo.service.MasterPhotoService;
import modules.record.ddl.MasterRecordDDL;
import modules.record.service.MasterRecordService;
import modules.user.ddl.WxUserDDL;
import modules.user.service.WxUserService;
import utils.AES;
import utils.DateUtil;
import utils.FileUtil;
import utils.NumberUtil;
import utils.RtnUtil;
import utils.StringUtil;
import utils.WXDecriptUtil;

public class Party extends Controller{
	/**
	 * 新建/编辑
	 * @param partyId 可空
	 * @param title 必填
	 * @param openid 必填
	 * @param name
	 * @param address
	 * @param latitude
	 * @param longitude
	 * @throws ParseException 
	 */
	public static void create(long partyId,String title,String session,String name,
			String address,Float latitude,Float longitude,String recordPath,
			String photoPath,String mobile,String selectDate,String selectTime,
			String formId,String banner) throws ParseException{
		if(StringUtil.isBlank(session) || StringUtil.isBlank(title)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		//save party 
		PartyDDL party = PartyService.get(partyId); 
		if(party!=null && !party.getOpenid().equals(loginUser.getOpenid())){//编辑权限判断
			renderJSON(RtnUtil.returnFail("非法操作"));
		}
		if(party == null){
			party = new PartyDDL(); 
			party.setOpenid(loginUser.getOpenid());
		}
		party.setTitle(StringUtil.filterEmoji(title, ""));
		party.setBanner(StringUtil.isBlank(banner)?null:banner);
		
		if( !StringUtil.isBlank(selectDate) 
				&& !StringUtil.isBlank(selectTime) ){
			Date startTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(selectDate+" "+selectTime);
			party.setStartTime(startTimeDate.getTime());
		}
		
		long id = PartyService.add(party);
		
		if(id<=0){
			renderJSON(RtnUtil.returnFail("开趴失败，稍后再试"));
		} 
		
		if(!StringUtil.isBlank(recordPath)){
			MasterRecordDDL record = new MasterRecordDDL();
			record.setPartyId(id);
			record.setPath(recordPath);
			MasterRecordService.add(record);
		}
		
		if(!StringUtil.isBlank(photoPath)){
			MasterPhotoDDL photo = new MasterPhotoDDL();
			photo.setPath(photoPath);
			photo.setPartyId(id);
			MasterPhotoService.add(photo);
		}
		//save location
		if(!StringUtil.isBlank(name) 
				&& NumberUtil.between(latitude, -90, 90) 
				&& NumberUtil.between(latitude, -180, 180)){
			MasterLocationDDL location = new MasterLocationDDL();
			location.setPartyId(id);
			location.setName(name);
			location.setAddress(address);
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			MasterLocationService.add(location);
		}
		 
		
		PartyApplyInfoDDL apply = new PartyApplyInfoDDL();
		apply.setOpenid(loginUser.getOpenid());
		apply.setPartyId(id);
		apply.setStatus(2);
		apply.setReason("我一定准时先到场~");
		apply.setMobile(mobile);
		
		//用户发送模板消息
		if(!StringUtil.isBlank(formId)){
			apply.setFormId(formId);	
		}
		
		PartyApplyInfoService.add(apply);
		
		renderJSON(RtnUtil.returnSuccess("ok", id));
	}
	
	public static void del(long partyId,String session){
		if(partyId<=0 || StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		 
		PartyDDL party = PartyService.get(partyId);
		if(party==null){
			renderJSON(RtnUtil.returnSuccess());
		}
		
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		if(!party.getOpenid().equals(loginUser.getOpenid())){
			renderJSON(RtnUtil.returnFail("非法操作"));
		}
		
		int i = PartyService.del(party.getId());
		if(i>0){
			PartyApplyInfoService.delByPartyId(partyId);
			MasterLocationService.delByPartyId(partyId);
			MasterPhotoService.delByPartyId(partyId);
			MasterRecordService.delByPartyId(partyId);
		}
		renderJSON(RtnUtil.returnSuccess());
	}
	/**
	 * 详情
	 * @param partyId
	 * @param openid
	 */
	public static void get(long partyId,String session){
		if(partyId<=0 || StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		PartyDetailDto dto = new PartyDetailDto();
		
		PartyDDL party = PartyService.get(partyId);
		if(party == null){
			renderJSON(RtnUtil.returnFail("活动不存在"));
		} 
		
		long startTime = party.getStartTime();
		party.setShowWeekDay(DateUtil.showWeekDay(startTime));
		party.setShowDate(DateUtil.showDate(startTime));
		party.setShowStartTime(DateUtil.format("HH:mm", startTime));
		party.setStatus(System.currentTimeMillis()<startTime?1:2);
		
		
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		//是否apply过了
		PartyApplyInfoDDL applyinfo = PartyApplyInfoService.getByOpenIdAndPartyId(loginUser.getOpenid(), partyId);
		if(applyinfo==null){//给个默认未表态的状态
			PartyApplyInfoDDL apply = new PartyApplyInfoDDL();
			apply.setOpenid(loginUser.getOpenid());
			apply.setPartyId(partyId);
			apply.setStatus(1);
			apply.setReason("已阅");
			apply.setFormId(""); 
			if(!PartyApplyInfoService.add(apply)){
				renderJSON(RtnUtil.returnFail());
			}
		}
		
		WxUserDDL master = WxUserService.getByOpenId(party.getOpenid());
		PartyDto pdto = new PartyDto();
		pdto.setParty(party);
		pdto.setUser(master);
		dto.setPartyInfo(pdto);
		
		MasterLocationDDL location = MasterLocationService.getByPartyId(partyId);
		dto.setLocation(location);
		
		MasterRecordDDL record = MasterRecordService.getByPartyId(partyId);
		dto.setRecord(record);
		
		MasterPhotoDDL photo = MasterPhotoService.getByPartyId(partyId);
		dto.setPhoto(photo);
		
		List<PartyApplyInfoDDL> applys = PartyApplyInfoService.getByOpenIdAndPartyId(partyId);
		if(applys!=null && applys.size()>0){
			for(PartyApplyInfoDDL apply : applys){
				ApplyInfoDto adto = new ApplyInfoDto();
				adto.setApply(apply);
				WxUserDDL itemUser = WxUserService.getByOpenId(apply.getOpenid());
				adto.setUser(itemUser);
				dto.getApplyInfo().add(adto);
				if(apply.getStatus() == 2){
					dto.getTakeUsers().add(itemUser);
				}
			}
		}
		
		List<MessageDDL> msgList =   MessageService.listByPartyId(partyId);
		if(msgList != null && msgList.size() >0 ){
			for(MessageDDL msg : msgList){
				WxUserDDL user = WxUserService.getByOpenId(msg.getOpenid());
				MessageItemDto msgDto = new MessageItemDto();
				msg.setShowCreateTime(DateUtil.format("MM-dd HH:mm:ss", msg.getCreateTime()));
				msgDto.setMessage(msg);
				msgDto.setUser(user); 
				msgDto.setMySelf(msg.getOpenid().equals(loginUser.getOpenid()));
				dto.getMessage().add(msgDto);
			}
		}
		
		renderJSON(RtnUtil.returnSuccess("ok", dto));
	}
	
	/**
	 * 报名
	 * @param partyId
	 * @param openid
	 * @param status
	 * @param reason
	 */
	public static void apply(long partyId,String session,int status,String reason,String formId){
		
		//兼容客户端旧版本发起的未表态申请
		if(status==1){
			renderJSON(RtnUtil.returnSuccess("ok"));
		}
				
		if(partyId<=0 || StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		PartyDDL party = PartyService.get(partyId);
		if(party == null){
			renderJSON(RtnUtil.returnFail("趴不存在"));
		}
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		if(party.getOpenid().equals(loginUser.getOpenid())){
			renderJSON(RtnUtil.returnFail("房主不能不参加"));
		}
		
		
		if(status==2){
			reason = "非常荣幸，感谢邀请~";
		}
		
		PartyApplyInfoDDL apply = new PartyApplyInfoDDL();
		apply.setOpenid(loginUser.getOpenid());
		apply.setPartyId(partyId);
		apply.setStatus(status);
		apply.setReason(reason);
		apply.setFormId(formId);
		
		if(!PartyApplyInfoService.add(apply)){
			renderJSON(RtnUtil.returnFail());
		}else{
			renderJSON(RtnUtil.returnSuccess("ok"));
		}
	}
	
	/**
	 * 列表
	 * @param openid
	 * @param page
	 * @param pageSize
	 */
	public static void listItems(String session,int page , int pageSize){
		if(StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		MyPartyListDto dto = new MyPartyListDto();
		
		List<PartyApplyInfoDDL>  applys = PartyApplyInfoService.listMyPartys(loginUser.getOpenid(), page, pageSize);
		if(applys==null || applys.size() ==0){
			renderJSON(RtnUtil.returnSuccess("ok",dto));
		}
		
		if(applys==null || applys.size() ==0){
			renderJSON(RtnUtil.returnSuccess("ok",dto));
		}
		
		int count = PartyApplyInfoService.countMyPartys(loginUser.getOpenid());
		dto.setCount(count);
		
		for(PartyApplyInfoDDL apply : applys){
			ApplyInfoItemDto item = new ApplyInfoItemDto();
			
			PartyDDL party = PartyService.get(apply.getPartyId());
			WxUserDDL master = WxUserService.getByOpenId(party.getOpenid());
			
			party.setShowModifyTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(party.getModifyTime())));
			long startTime = party.getStartTime();
			party.setShowWeekDay(DateUtil.showWeekDay(startTime));
			party.setShowDate(DateUtil.showDate(startTime));
			party.setShowStartTime(DateUtil.format("HH:mm", startTime));
			party.setStatus(System.currentTimeMillis()<startTime?1:2);
			item.setMaster(master);
			item.setParty(party);
			item.setApply(apply);
			
			MasterLocationDDL location = MasterLocationService.getByPartyId(apply.getPartyId());
			item.setLocation(location);
			
			List<PartyApplyInfoDDL>  takeApplys = PartyApplyInfoService.getByOpenIdAndPartyId(apply.getPartyId());
			if(takeApplys != null && takeApplys.size() > 0){
				for(PartyApplyInfoDDL take:takeApplys){
					if(take.getStatus() != 2) continue;
					WxUserDDL user = WxUserService.getByOpenId(take.getOpenid());
					item.getTakeUsers().add(user);
				}
			}
			
			dto.getItems().add(item);
		}
		renderJSON(RtnUtil.returnSuccess("ok",dto));
	}
	
	public static void login(String code,String rawData,String encryptedData,String signature,String iv){ 
		if(StringUtil.isBlank(code) || StringUtil.isBlank(rawData) || StringUtil.isBlank(iv)
				|| StringUtil.isBlank(encryptedData)|| StringUtil.isBlank(signature)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		
		try{
 			String queryString = String.format("?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
 					Jws.configuration.getProperty("wx.api.appid"),
 					Jws.configuration.getProperty("wx.api.secret"),
 					code);
			Request request = new Request("wx","session",queryString);
			Response response = HTTP.GET(request);
			if(response.getStatusCode()!=200){
				renderJSON(RtnUtil.returnFail("请求微信获取sessionKey失败，http状态错误"));
			} 
			Logger.info("response form wx %s", response.getContent());
			JsonObject obj = new JsonParser().parse(response.getContent()).getAsJsonObject();
			String sessionKey = obj.get("session_key").getAsString();
			String signStr = rawData+sessionKey;
			String mySignature =WXDecriptUtil.SHA1(signStr);
			if(!mySignature.equals(signature)){
				Logger.error("SHA1 签名串 %s,mySignature %s ,youSignature %s",signStr, mySignature,signature);
				renderJSON(RtnUtil.returnFail("请求微信获取sessionKey失败，数据不完整"));
			}
			byte[] encryptedDataBase64Decoder = Base64.decodeBase64(encryptedData);
			byte[] sessionKeyBase64Decoder = Base64.decodeBase64(sessionKey);
			byte[] ivBase64Decoder = Base64.decodeBase64(iv);
			AES aes = new AES();
			byte[] aseBytes = aes.decrypt(encryptedDataBase64Decoder, sessionKeyBase64Decoder, ivBase64Decoder);
			String decryptedData = new String(WXDecriptUtil.decode(aseBytes));
			Logger.info("after ecrypt4Base64 %s",decryptedData);
			JsonObject userDataJson = new JsonParser().parse(decryptedData).getAsJsonObject();
			
			WxUserDDL wxUser = new WxUserDDL();
			wxUser.setAvatarUrl(userDataJson.get("avatarUrl").getAsString());
			wxUser.setOpenid(userDataJson.get("openId").getAsString());
			wxUser.setSession(userDataJson.get("openId").getAsString()+"$"+UUID.randomUUID().toString());
			wxUser.setGender(userDataJson.get("gender").getAsInt());
			wxUser.setNickName(StringUtil.filterEmoji(userDataJson.get("nickName").getAsString(), ""));
			wxUser.setCity(userDataJson.get("city").getAsString());
			wxUser.setProvince(userDataJson.get("province").getAsString());
			wxUser.setCountry(userDataJson.get("country").getAsString());
			if(WxUserService.add(wxUser)){
				renderJSON(RtnUtil.returnSuccess("ok",wxUser));
			}
			renderJSON(RtnUtil.returnFail("用户信息入库失败"));
		}catch(Exception  e){
			Logger.error(e, "");
			renderJSON(RtnUtil.returnFail("请求微信获取sessionKey失败"));
		} 
	}
	/**
	 * 文件上传
	 * @param file 
	 * @param type
	 */
	public static void upload(File file,int type,String session){
		try {
			WxUserDDL loginUser = WxUserService.getBySession(session);
			if(loginUser==null){
				renderJSON(RtnUtil.returnFail("登录失效"));
			} 
			String filePath = FileUtil.storeFile(file, type);
			/*boolean result = false;
			if(type==1){
				MasterRecordDDL record = new MasterRecordDDL();
				record.setPartyId(partyId);
				record.setPath(filePath);
				result = MasterRecordService.add(record);
			}else if(type == 2){
				MasterPhotoDDL photo = new MasterPhotoDDL();
				photo.setPath(filePath);
				photo.setPartyId(partyId);
				result = MasterPhotoService.add(photo);
			}
			if(!result){
				renderJSON(RtnUtil.returnFail("上传文件失败，稍后再试"));
			}*/
			renderJSON(RtnUtil.returnSuccess("ok",filePath));
		} catch (Exception e) {
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail());
		} 
	}
	public static void delfile(long partyId,int type,String session){
		if(partyId<=0 || type <=0){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		PartyDDL party = PartyService.get(partyId);
		
		if(!party.getOpenid().equals(loginUser.getOpenid())){
			renderJSON(RtnUtil.returnFail("非法操作"));
		}
		 
		if(type == 1){
			MasterRecordService.delByPartyId(partyId);
		}else if(type ==2){
			MasterPhotoService.delByPartyId(partyId);
		}
		 
		renderJSON(RtnUtil.returnSuccess("ok"));
	}
	/**
	 * 文件下载
	 * @param filePath
	 */
	public static void download(String filePath){
		try {
			 File file = new File(Jws.applicationPath.getPath()+filePath);
			 if(!file.exists()){
				 throw new Exception("文件不存在["+filePath+"]");
			 }
			 response.setHeader("content-disposition", "attachment;filename=" + file.getName());
			 renderBinary(file);
		} catch (Exception e) {
			Logger.error(e, e.getMessage());
			renderJSON(RtnUtil.returnFail());
		} 
	}
	
	/**
	 * 收集邀请日志
	 * @param openid
	 * @param partId
	 */
	public static void invitation(String openid,long partId){
		Logger.info("openid %s invitation %s", openid,partId);
	}
	/**
	 * 用户建议
	 * @param openid
	 * @param suggestion
	 */
	public static void suggest(String openid,String suggestion,String imagePath){
		Logger.info("openid %s suggest %s imagePath %s", openid,suggestion,imagePath);
	}
	
	/**
	 * 用户留言
	 * @param partyId
	 * @param session
	 * @param message
	 */
	public static void sendMessage(long partyId,String session,String message){
		if(StringUtil.isBlank(session) || StringUtil.isBlank(message)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		PartyDDL party = PartyService.get(partyId);
		if(party == null){
			renderJSON(RtnUtil.returnFail("活动不存在"));
		}
		String openid = loginUser.getOpenid();
		PartyApplyInfoDDL apply = PartyApplyInfoService.getByOpenIdAndPartyId(openid, partyId);
		if(apply == null){
			renderJSON(RtnUtil.returnFail("你没权限留言"));
		}
		boolean result = MessageService.insert(partyId, openid, message);
		
		if(!result){
			renderJSON(RtnUtil.returnFail("留言失败"));
		}
		renderJSON(RtnUtil.returnSuccess("ok"));
	}
	
	/**
	 * 删除留言
	 * @param session
	 * @param id
	 */
	public static void deleteMessage(String session,long id){
		if(StringUtil.isBlank(session) || id <= 0 ){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		MessageDDL msg = MessageService.get(id);
		if(msg == null){
			renderJSON(RtnUtil.returnFail("留言已删除"));
		}
		
		if( !msg.getOpenid().equals(loginUser.getOpenid()) ){
			renderJSON(RtnUtil.returnFail("无权限删除别人留言"));
		}
		
		boolean result = MessageService.delete(id);
		
		if(!result){
			renderJSON(RtnUtil.returnFail("留言删除失败"));
		}
		renderJSON(RtnUtil.returnSuccess("ok"));
	}
	
	public static void wxmessage(){
		Map<String,String> params = request.params.allSimple();
		//开通微信客服消息服务
		if(params.containsKey("nonce") && params.containsKey("echostr") && params.containsKey("timestamp")){
			//String mySignature =WXDecriptUtil.SHA1(signStr);
		}
	}
	
	public static void listActivity(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("id", 1);
		obj.put("title", "我俩真像");
		obj.put("subtitle", "也许是走散多年的兄弟、也行是个明星脸呢");
		obj.put("actBgUrl", "https://party.91loving.cn/public/images/activity/act_bg_1.png");
		obj.put("path", "/pages/face/verify");
		obj.put("local", 1);
		
		list.add(obj);
		
		renderJSON(RtnUtil.returnSuccess("ok",list));
	}
}
