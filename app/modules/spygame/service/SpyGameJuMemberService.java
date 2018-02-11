package modules.spygame.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jws.dal.Dal;
import jws.dal.sqlbuilder.Condition;
import modules.spygame.ddl.SpyGameJuMemberDDL;

public class SpyGameJuMemberService {

	public static boolean join(long roomId,long juId,String openId,int master){
		SpyGameJuMemberDDL old = getByJuIdAndOpenId(juId,openId);
		if(old != null){
			master = old.getMaster();
			if(!quit(old.getRoomId(),openId)){
				return false;
			}
		}
		SpyGameJuMemberDDL member = new SpyGameJuMemberDDL();
		member.setJuId(juId);
		member.setOpenId(openId);
		member.setGameRole(0); 
		member.setWord("");
		member.setRoomId(roomId);
		member.setMemberStatus(0);
		member.setMaster(master);
		return Dal.insert(member)>0;
	}
	
	public static boolean quit(long roomId,String openId){
		Condition condition = new Condition("SpyGameJuMemberDDL.roomId","=",roomId);
		condition.add(new Condition("SpyGameJuMemberDDL.openId","=",openId), "and");
		return Dal.delete(condition)>0;
	}
	
	public static boolean updateMemberStatus(long memberId,int memberStatus){
		SpyGameJuMemberDDL member = Dal.select("SpyGameJuMemberDDL.*", memberId);
		if( member == null ){
			return false;
		}
		member.setMemberStatus(memberStatus);
		return Dal.insertUpdate(member, "SpyGameJuMemberDDL.memberStatus")>0;
	}
	
	/**
	 * 是否全部发言完成
	 * @param roomId
	 * @param juId
	 * @return
	 */
	public static boolean isSpoken(long roomId,long juId){
		Condition condition = new Condition("SpyGameJuMemberDDL.juId","=",juId);
		condition.add(new Condition("SpyGameJuMemberDDL.roomId","=",roomId), "and");
		condition.add(new Condition("SpyGameJuMemberDDL.memberStatus","!=",2), "and");
		return Dal.count(condition) == 0;
	}
	
	public static List<SpyGameJuMemberDDL> listByJuId(long juId){
		Condition condition = new Condition("SpyGameJuMemberDDL.juId","=",juId);
		return Dal.select("SpyGameJuMemberDDL.*", condition, null, 0, -1);
	}
	
	public static int countByJuId(long juId){
		Condition condition = new Condition("SpyGameJuMemberDDL.juId","=",juId);
		return Dal.count(condition);
	}
	
	public static SpyGameJuMemberDDL getByJuIdAndOpenId(long juId,String openId){
		Condition condition = new Condition("SpyGameJuMemberDDL.juId","=",juId);
		condition.add(new Condition("SpyGameJuMemberDDL.openId","=",openId), "and");
		List<SpyGameJuMemberDDL> list = Dal.select("SpyGameJuMemberDDL.*", condition, null, 0, 1);
		if( null == list || list.size() == 0){
			return null;
		}
		return list.get(0);
	}
	/**
	 * 分配词汇
	 * @param gameId
	 * @return
	 */
	public static List<SpyGameJuMemberDDL> giveRole(long juId){
		Condition condition = new Condition("SpyGameJuMemberDDL.juId","=",juId);
		List<SpyGameJuMemberDDL> list = Dal.select("SpyGameJuMemberDDL.*", condition, null, 0, -1);
		if(list == null || list.size() ==0){
			return null;
		}
		int count = list.size();
		int spy = 4; 
		if(count<6){
			spy = 1;
		}else if(count>=6 && count <10){
			spy=2;
		}else if(count>=10 && count <14){
			spy=3;
		} 
		//决定用词
		java.util.Random random=new java.util.Random();
		int wordIndex = random.nextInt(47); 
		Set spySet = new HashSet();
		do{
			java.util.Random randomSpy=new java.util.Random();
			int index = randomSpy.nextInt(count);
			spySet.add(index); 
		}while(spySet.size()<spy);
		
		 
		for(int i=0;i<list.size();i++){
			SpyGameJuMemberDDL juMember = list.get(i);
			if(spySet.contains(i)){
				juMember.setGameRole(1);
				juMember.setWord(spy_words[wordIndex]);
			}else{
				juMember.setGameRole(2);
				juMember.setWord(pm_words[wordIndex]);
			} 
			Dal.insertUpdate(juMember, "SpyGameJuMemberDDL.gameRole,SpyGameJuMemberDDL.word");
		}
		return list;
	}
	
	
	 
	private static final String[] spy_words = new String[]{
			 "端午节",
			 "那英",
			 "票房大卖",
			 "包子",
			 "电动车",
			 "展昭",
			 "乌鸦",
			 "葫芦娃",
			 "蜘蛛精",
			 "冠军",
			 "增高鞋",
			 "乱弹棉花",
			 "肉夹馍",
			 "针灸",
			 "鸭脖子",
			 "肥肉",
			 "胡须",
			 "维嘉",
			 "狼牙棒",
			 "私房钱",
			 "东方不败",
			 "热水袋",
			 "唐伯虎",
			 "毛绒绒",
			 "毛裤",
			 "爹",
			 "儿孙满堂",
			 "烧烤",
			 "保镖",
			 "放大镜",
			 "空白牌",
			 "按摩",
			 "面膜",
			 "宫廷",
			 "皮蛋",
			 "呆",
			 "脚趾",
			 "林更新",
			 "文章马伊琍",
			 "回锅肉",
			 "娇滴滴",
			 "情歌",
			 "锅巴",
			 "鸡肉",
			 "吴奇隆",
			 "唐老鸭"
	};
	
	private static final String[] pm_words = new String[]{
			"中秋节",
			"王菲",
			"节节高升",
			"饺子",
			"摩托车",
			"元芳",
			"麻雀",
			"小矮人",
			"蜘蛛侠",
			"状元",
			"高跟鞋",
			"反弹琵琶",
			"汉堡包",
			"打针",
			"鸡翅膀",
			"胖子",
			"眉毛",
			"何炅",
			"铆钉靴",
			"年终奖",
			"天山童姥",
			"暖宝宝",
			"韦小宝",
			"软绵绵",
			"秋裤",
			"娘",
			"早生贵子",
			"夜宵",
			"保安",
			"眼镜",
			"孙俪",
			"挠痒",
			"面具",
			"空白牌",
			"卤蛋",
			"萌",
			"手指",
			"赵又廷",
			"张杰谢娜",
			"肥肉",
			"羞答答",
			"情书",
			"薯片",
			"肌肉",
			"四爷",
			"小黄鸭"
	};
	
}
