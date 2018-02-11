package controllers;
 

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jws.mvc.Controller;
import modules.apply.ddl.PartyApplyInfoDDL;
import modules.apply.service.PartyApplyInfoService;
import modules.game.ddl.GameRoomDDL;
import modules.game.service.GameRoomService;
import modules.party.ddl.PartyDDL;
import modules.party.service.PartyService;
import modules.spygame.ddl.SpyGameJuDDL;
import modules.spygame.ddl.SpyGameJuMemberDDL;
import modules.spygame.ddl.SpyGameRoundDDL;
import modules.spygame.service.SpyGameJuMemberService;
import modules.spygame.service.SpyGameJuService;
import modules.spygame.service.SpyGameRoundService;
import modules.spygame.service.SpyGameRoundVoteService;
import modules.user.ddl.WxUserDDL;
import modules.user.service.WxUserService;
import utils.RtnUtil;
import utils.StringUtil;

public class SpyGame extends Controller{

	public static void getUserInfo(String session){
		if(StringUtil.isBlank(session)){
			renderJSON(RtnUtil.returnFail("非法参数"));
		}
		WxUserDDL user = WxUserService.getBySession(session);
		renderJSON(RtnUtil.returnSuccess("ok", user));
	}
	
	public static void joinRoom(long roomId,String session){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		GameRoomDDL room = GameRoomService.get(roomId);
		if(room == null){
			renderJSON(RtnUtil.returnFail("房间不存在"));
		}
		
		if(room.getLimitPartyId()>0){
			PartyApplyInfoDDL apply = PartyApplyInfoService.getByOpenIdAndPartyId(user.getOpenid(), room.getLimitPartyId());
			if(apply==null || apply.getStatus()!=2){
				renderJSON(RtnUtil.returnFail("非指定活动人员不允许参与游戏"));
			}
		}
		
		if(room.getRoomStatus() == 1){
			renderJSON(RtnUtil.returnFail("游戏中,不能加入"));
		}
		
		//此房间还存进行的局
		int gaming= SpyGameJuService.countByRoomIdAndJuStatus(room.getRoomId(), 1);
		if( gaming > 0 ){
			renderJSON(RtnUtil.returnFail("游戏中,不能加入"));
		}
		
		//查找未开始的局
		SpyGameJuDDL joinJu = SpyGameJuService.findUsableJu(room.getRoomId());
		if(joinJu == null){
			renderJSON(RtnUtil.returnFail("房主还未开局"));
		}
		
		//非房主入局
		if(!SpyGameJuMemberService.join(joinJu.getRoomId(),joinJu.getJuId() ,user.getOpenid(),0)){
			renderJSON(RtnUtil.returnFail("加入游戏失败,请重试"));
		}
		
		Map data = new HashMap();
		data.put("nickName", user.getNickName());
		data.put("juId",joinJu.getJuId());
		renderJSON(RtnUtil.returnSuccess("ok",data));
	}
	
	public static void quitRoom(long roomId,String session){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		
		GameRoomDDL room = GameRoomService.get(roomId);
		if(room == null){
			renderJSON(RtnUtil.returnFail("房间不存在"));
		}
		
		if( room.getRoomStatus() == 1 ){
			renderJSON(RtnUtil.returnFail("游戏中，不能退出"));
		} 
		 
		if(!SpyGameJuMemberService.quit(room.getRoomId(),  user.getOpenid())){
			renderJSON(RtnUtil.returnFail("退出失败,请稍后再试"));
		}
		
		Map data = new HashMap();
		data.put("nickName", user.getNickName());
		data.put("roomId",room.getRoomId());
		renderJSON(RtnUtil.returnSuccess("ok",data));
	}
	
	//房主开局
	public static void startJu(long roomId,String session){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		} 
		
		//查找未开始的局
		SpyGameJuDDL usableJu = SpyGameJuService.findUsableJu(roomId);
		if(usableJu == null){
			renderJSON(RtnUtil.returnFail("还未开局"));
		}  
		long juId = usableJu.getJuId();
		
		int memberCount = SpyGameJuMemberService.countByJuId(juId);
		if(memberCount<4){
			renderJSON(RtnUtil.returnFail("至少4人才能开局"));
		} 
		
		if(usableJu.getJuStatus() == 1){
			renderJSON(RtnUtil.returnFail("游戏当局进行中"));
		}
		
		if(usableJu.getJuStatus() == 2){
			renderJSON(RtnUtil.returnFail("游戏当局已经结束，请重新开局"));
		}
		
		boolean startRoom = GameRoomService.updateStatus(roomId, 1);
		if(!startRoom){
			renderJSON(RtnUtil.returnFail("游戏开始失败，请重试"));
		}
		 
		SpyGameJuMemberDDL member = SpyGameJuMemberService.getByJuIdAndOpenId(juId, user.getOpenid());
		if(member == null ){
			renderJSON(RtnUtil.returnFail("您未加入游戏"));
		}
		if(member.getMaster() == 0 ){
			renderJSON(RtnUtil.returnFail("您不是房主，不能开始游戏"));
		}
		
		//开始游戏，分配角色
		List<SpyGameJuMemberDDL> members =	SpyGameJuMemberService.giveRole(juId);
		//第一轮
		SpyGameRoundDDL round = SpyGameRoundService.findByRoomIdJuIdStatus(roomId, juId, 0);
		if(round == null ){
			round = SpyGameRoundService.create(roomId, juId);
		}
		if( round == null ){
			renderJSON(RtnUtil.returnFail("开始失败，请重试"));
		}
		
		boolean startJu = SpyGameJuService.updateJuStatus(juId, 1);//开始局
		if(!startJu){
			renderJSON(RtnUtil.returnFail("游戏开局失败，请重试"));
		}
		
		Map data = new HashMap();
		data.put("ju", usableJu.getJu());
		data.put("round", round.getRound());
		data.put("members", members);
		
		renderJSON(RtnUtil.returnSuccess("ok", data));
	}
	
	public static void speaking(long juId,String session){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		SpyGameJuMemberDDL member = SpyGameJuMemberService.getByJuIdAndOpenId(juId, user.getOpenid());
		if(member == null ){
			renderJSON(RtnUtil.returnFail("您未加入游戏"));
		}
		if(member.getMemberStatus()!=0){
			renderJSON(RtnUtil.returnFail("状态非待发言"));
		}
		
		SpyGameRoundDDL round = SpyGameRoundService.findByRoomIdJuIdStatus(member.getRoomId(), member.getJuId(), 1);
		if(round == null){
			renderJSON(RtnUtil.returnFail("Round为空"));
		}
		if(round.getRoundStatus() != 1){
			renderJSON(RtnUtil.returnFail("Round非发言中状态"));
		}
		
		boolean speaking = SpyGameJuMemberService.updateMemberStatus(member.getMemberId(), 1);
		if(speaking){
			renderJSON(RtnUtil.returnSuccess());
		}
		renderJSON(RtnUtil.returnFail("发言状态设置失败"));
	}
	
	public static void spoken(long juId,String session){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		SpyGameJuMemberDDL member = SpyGameJuMemberService.getByJuIdAndOpenId(juId, user.getOpenid());
		if(member == null ){
			renderJSON(RtnUtil.returnFail("您未加入游戏"));
		}
		if(member.getMemberStatus()!=1){
			renderJSON(RtnUtil.returnFail("状态非发言中"));
		}
		
		SpyGameRoundDDL round = SpyGameRoundService.findByRoomIdJuIdStatus(member.getRoomId(), member.getJuId(), 1);
		if(round == null){
			renderJSON(RtnUtil.returnFail("Round为空 or Round非发言中状态"));
		} 
		
		boolean speaking = SpyGameJuMemberService.updateMemberStatus(member.getMemberId(), 2);
		boolean isSpoken = SpyGameJuMemberService.isSpoken(member.getRoomId(), member.getJuId());
		
		if(isSpoken){
			if(!SpyGameRoundService.updateStatus(round.getRoundId(), 3)){
				renderJSON(RtnUtil.returnFail("Round发言完成状态设置失败"));
			}
		}
		
		if(speaking){
			Map data = new HashMap();
			data.put("isSpoken", isSpoken);
			renderJSON(RtnUtil.returnSuccess("ok",data));
		} 
		renderJSON(RtnUtil.returnFail("发言完成状态设置失败"));
	}
	
	public static void vote(String session,long juId,long roundId,long voterMemberId){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		SpyGameJuMemberDDL ju = SpyGameJuMemberService.getByJuIdAndOpenId(juId, user.getOpenid());
		if( ju == null || ju.getMemberStatus() != 2 ){
			renderJSON(RtnUtil.returnFail("未参加游戏 or 状态非发言结束，不允许投票"));
		}
		
		SpyGameRoundDDL round = SpyGameRoundService.findByRoomIdJuIdStatus(ju.getRoomId(), ju.getJuId(), 3);
		if(round == null){
			renderJSON(RtnUtil.returnFail("Round为空 or Round非投票中状态"));
		}
		
		int count = SpyGameRoundVoteService.countByUnique(roundId, ju.getMemberId());
		
		if(count > 0){
			renderJSON(RtnUtil.returnFail("已经投过票了"));
		}
		
		boolean voted = SpyGameRoundVoteService.create(ju.getRoomId(), juId, roundId, ju.getMemberId(), voterMemberId);
		
		if( !voted ){
			renderJSON(RtnUtil.returnFail("投票失败，请重试"));
		}
		
		renderJSON(RtnUtil.returnSuccess());
	}
	
	public static void nextRound(long juId,String session){/*
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		 
		SpyGameJuDDL ju = SpyGameJuService.get(juId);
		if(ju == null){
			renderJSON(RtnUtil.returnFail("游戏局不存在"));
		}
		
		SpyGameRoundService.create(ju.getRoomId(), juId);
		if(game.getGameStatus() !=2 ){
			renderJSON(RtnUtil.returnFail("游戏还未结束"));
		}
		
		int round = SpyGameJuService.addRound(partyId);
		if(round <= 0){
			renderJSON(RtnUtil.returnFail("开局失败"));
		}
		
		List<SpyGameJuMemberDDL> gameDatas = SpyGameJuMemberService.giveRole(game.getId());
		
		if(gameDatas==null || gameDatas.size()<3){
			renderJSON(RtnUtil.returnFail("至少3人才能开局"));
		}
		Map data = new HashMap();
		data.put("ju", game.getJuNum());
		data.put("round", round);
		data.put("game_datas", gameDatas);
		
		renderJSON(RtnUtil.returnSuccess("ok", data));
	*/}
	
	//机器人使用房主session操作
	public static void endJu(long juId,String session,int juResult){
		WxUserDDL user = WxUserService.getBySession(session);
		if(user == null){
			renderJSON(RtnUtil.returnFail("登录失效"));
		}
		SpyGameJuDDL ju = SpyGameJuService.get(juId);
		if(ju == null){
			renderJSON(RtnUtil.returnFail("不存在局"));
		}
		boolean endRoom = GameRoomService.updateStatus(ju.getRoomId(), 2);
		if(!endRoom){
			renderJSON(RtnUtil.returnFail("游戏结束失败，请重试"));
		}
		boolean endJu = SpyGameJuService.updateJuStatus(juId, 2,juResult);//结束局
		if(!endJu){
			renderJSON(RtnUtil.returnFail("游戏开局失败，请重试"));
		}
		renderJSON(RtnUtil.returnSuccess("ok"));
	} 
}
