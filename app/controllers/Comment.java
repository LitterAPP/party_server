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
import modules.user.ddl.WxUserDDL;
import modules.user.service.WxUserService;
import utils.RtnUtil;
import utils.StringUtil;

public class Comment extends Controller {
	
	public static void commitComment(String session,int id,String comment){
		if(StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("未完成微信授权"));
		} 
		if(id==0 || StringUtil.isBlank(comment)){
			renderJSON(RtnUtil.returnFail("信息不完整"));
		} 
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		} 
		try {
			CommentDDL commentDDL = new CommentDDL();
			commentDDL.setAvatar(loginUser.getAvatarUrl());
			commentDDL.setNickName(loginUser.getNickName());
			commentDDL.setComment(comment);
			commentDDL.setCommentId(id);
			commentDDL.setCreateTime(System.currentTimeMillis());
			renderJSON(RtnUtil.returnSuccess());
		} catch (Exception e) {
			Logger.error("", e);
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	}
	
	public static void listComments(String session,int id){
		if(StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("未完成微信授权"));
		} 
		if(id==0){
			renderJSON(RtnUtil.returnFail("信息不完整"));
		} 
		WxUserDDL loginUser = WxUserService.getBySession(session);
		if(loginUser==null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		} 
		try {
 			Condition  cond = new Condition("CommentDDL.commentId","=",id);
			Sort sort = new Sort("CommentDDL.createTime",false);
			List<CommentDDL> comments = Dal.select("CommentDDL.*", cond, sort, 0, 100);
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("comments", comments);
			renderJSON(RtnUtil.returnSuccess("OK",result));
		} catch (Exception e) {
			Logger.error("", e);
			renderJSON(RtnUtil.returnFail("服务器异常"));
		}
	}
}
