package controllers;

import java.util.HashMap;
import java.util.Map;

import jws.mvc.Controller;
import modules.game.service.GameRoomService;
import modules.spygame.ddl.SpyGameJuDDL;
import modules.spygame.service.SpyGameJuMemberService;
import modules.spygame.service.SpyGameJuService;
import modules.user.ddl.WxUserDDL;
import modules.user.service.WxUserService;
import utils.RtnUtil;
import utils.StringUtil;

public class Game extends Controller{

	public static void createRoom(String session,long limitPartyId,String factory){
		if(StringUtil.isBlank(session) || StringUtil.isBlank(factory)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		long roomId = GameRoomService.create(limitPartyId);
		
		if(roomId <=0 ){
			renderJSON(RtnUtil.returnFail("房间创建失败"));
		}
		
		//我是卧底 游戏
		if(factory.equals("WhoIsTheSpyFactory")){
			//创建局 并加入局
			SpyGameJuDDL ju = SpyGameJuService.create(roomId);
			if(ju==null){
				renderJSON(RtnUtil.returnFail("房间开局创建失败"));
			}
			//房主加入
			boolean joinResult = SpyGameJuMemberService.join(roomId,ju.getJuId(), user.getOpenid(),1);
			if(!joinResult){
				renderJSON(RtnUtil.returnFail("房间创建失败，加入失败"));
			}
			Map data = new HashMap();
			data.put("roomId", roomId);
			renderJSON(RtnUtil.returnSuccess("ok", data));
		}
		
		
	}
}
