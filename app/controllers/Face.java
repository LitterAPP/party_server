package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jws.Logger;
import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import jws.dal.sqlbuilder.Sort;
import jws.mvc.Controller;
import modules.comments.ddl.CommentDDL;
import modules.face.ddl.FaceVerifyDDL;
import modules.user.ddl.WxUserDDL;
import modules.user.service.WxUserService;
import utils.AliAPIUtil;
import utils.AliAPIUtil.FaceVerfiyResponse;
import utils.RtnUtil;
import utils.StringUtil;

public class Face  extends Controller{
	
	public static void verify(String session,String taName,String myAvatar,String taAvatar){
		if(StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("未完成微信授权"));
		} 
		if(StringUtil.isBlank(taName) || StringUtil.isBlank(myAvatar) || StringUtil.isBlank(taAvatar)){
			renderJSON(RtnUtil.returnFail("信息不完整"));
		} 
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		 String accessKey = "HW77gOwWnQiwQIuB"; //用户ak
	     String secretKey = "0N36kSmuIapg7352cX23fOGxUyXMoq"; // 用户ak_secret
	     String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";
 	     String body = "{\"type\": \"0\", \"image_url_1\":\""+myAvatar+"\",\"image_url_2\":\""+taAvatar+"\"}";
		try {
			FaceVerfiyResponse verifyRsp = AliAPIUtil.sendPost(url, body, accessKey, secretKey,FaceVerfiyResponse.class);
			if(verifyRsp==null || verifyRsp.getErrno() != 0){
				renderJSON(RtnUtil.returnFail("识别不了外星人脸~"));
			}
			FaceVerifyDDL faceVerifyDDL = new FaceVerifyDDL();
			faceVerifyDDL.setConfidence(verifyRsp.getConfidence());
			faceVerifyDDL.setCreateTime(System.currentTimeMillis());
			faceVerifyDDL.setMyAvatar(myAvatar);
			faceVerifyDDL.setMyName(loginUser.getNickName());
			faceVerifyDDL.setTaAvatar(taAvatar);
			faceVerifyDDL.setTaName(taName);
			faceVerifyDDL.setThresholds1(verifyRsp.getThresholds()[0]);
			faceVerifyDDL.setThresholds2(verifyRsp.getThresholds()[1]);
			faceVerifyDDL.setThresholds3(verifyRsp.getThresholds()[2]);
			long id = Dal.insertSelectLastId(faceVerifyDDL);
			renderJSON(RtnUtil.returnSuccess("OK",id));
		} catch (Exception e) {
			Logger.error(e,e.getMessage());
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	}
	
	
	public static void result(int id){
		if(id==0){
			renderJSON(RtnUtil.returnFail("信息不完整"));
		}		
		try {
			String desc = "";
			Map<String,Object> data = new HashMap<String,Object>();
			FaceVerifyDDL faceVerifyDDL = Dal.select("FaceVerifyDDL.*", id);
			
			if( null == faceVerifyDDL){
				renderJSON(RtnUtil.returnFail("信息不存在"));
			}
			
			if( faceVerifyDDL.getConfidence() > faceVerifyDDL.getThresholds1() && 
					faceVerifyDDL.getConfidence() > faceVerifyDDL.getThresholds2() &&
					faceVerifyDDL.getConfidence() > faceVerifyDDL.getThresholds3()
			){
				desc =String.format("竟然相似度达到%s，你不要骗我，这明明就是你自己！",faceVerifyDDL.getConfidence());
			}else {
				desc =String.format("@"+faceVerifyDDL.getMyName()+"和@%s的相似度：%s",faceVerifyDDL.getTaName(),faceVerifyDDL.getConfidence());
			}
			
			//获取评论列表
			Condition cond = new Condition("CommentDDL.commentId","=",id);
			Sort sort = new Sort("CommentDDL.createTime",false);
			
			List<CommentDDL> comments = Dal.select("CommentDDL.*", cond, sort, 0, 150);
			data.put("comments", comments);
			data.put("result", desc);
			data.put("verifyData", faceVerifyDDL);
			renderJSON(RtnUtil.returnSuccess("OK",data));
		} catch (Exception e) {
			Logger.error(e,e.getMessage());
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	} 
	
	
	public static void comment(int id,String comment,String session){
		if(id==0){
			renderJSON(RtnUtil.returnFail("信息不完整"));
		}		
		
		if(StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("未完成微信授权"));
		} 
		if(StringUtil.isBlank(comment)){
			renderJSON(RtnUtil.returnFail("评论不允许空"));
		} 
		
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		 
		try {
			CommentDDL commentDDL = new CommentDDL();
			commentDDL.setCommentId(id);
			commentDDL.setAvatar(loginUser.getAvatarUrl());
			commentDDL.setComment(comment);
			commentDDL.setCreateTime(System.currentTimeMillis());
			commentDDL.setNickName(loginUser.getNickName());
			
			if(Dal.insert(commentDDL)>0){
				renderJSON(RtnUtil.returnSuccess("OK"));
			}
			renderJSON(RtnUtil.returnFail("评论失败，稍后再试"));
		} catch (Exception e) {
			Logger.error(e,e.getMessage());
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	} 
}
