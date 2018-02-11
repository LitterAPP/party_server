package task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jws.Logger;
import jws.http.Request;
import jws.http.Response;
import jws.http.sf.HTTP;
import modules.apply.ddl.PartyApplyInfoDDL;
import modules.apply.service.PartyApplyInfoService;
import modules.location.ddl.MasterLocationDDL;
import modules.location.service.MasterLocationService;
import modules.party.ddl.PartyDDL;
import modules.party.service.PartyService;
import modules.token.ddl.WXAccessTokenDDL;
import modules.token.service.WXAccessTokenService;
import modules.user.ddl.WxUserDDL;
import modules.user.service.WxUserService;
import utils.DateUtil;

public class NotifyUser implements Runnable{
	
	private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	@Override
	public void run() {
		try{
			WXAccessTokenDDL token = WXAccessTokenService.get();
			if(token == null){
				return ;
			}
			
			List<PartyApplyInfoDDL> applys = PartyApplyInfoService.listNotifyUsers(0, 10);
			if(applys==null || applys.size() == 0)return ;
			for(PartyApplyInfoDDL apply : applys){
				long partyId = apply.getPartyId();
				PartyDDL party = PartyService.get(partyId);
				//默认提前一小时提醒
				float notifyHours = party.getNotifyHours()==null?1.0f:party.getNotifyHours();
				if(party == null 
						|| party.getStartTime() == null 
						|| party.getStartTime() <=0 
						|| party.getStartTime() - System.currentTimeMillis() > notifyHours*60*60*1000 //提前半小时通知
				){
					continue;
				}
				
				WxUserDDL user = WxUserService.getByOpenId(party.getOpenid());
				if( null == user ) return ;
				
				MasterLocationDDL location = MasterLocationService.getByPartyId(partyId);
				
				int countApply = PartyApplyInfoService.countApply(partyId);
				
				Map<String,Map> dataMap = new HashMap<String,Map>();
				
				Map<String,String> k1 = new HashMap<String,String>();
				k1.put("value", party.getTitle());
				k1.put("color", "#000033");
				
				Map<String,String> k2 = new HashMap<String,String>();
				k2.put("value", user.getNickName());
				k2.put("color", "#000033");
				
				Map<String,String> k3 = new HashMap<String,String>();
				k3.put("value", String.valueOf(countApply));
				k3.put("color", "#3300cc");
				
				Map<String,String> k4 = new HashMap<String,String>();
				k4.put("value", location==null?"待定":location.getName());
				k4.put("color", "#3300cc");
				
				Map<String,String> k5 = new HashMap<String,String>();
				k5.put("value", "请准时出席");
				k5.put("color", "#636363");
				
				Map<String,String> k6 = new HashMap<String,String>();
				k6.put("value", DateUtil.format("yyyy-MM-dd HH:mm", party.getStartTime()));
				k6.put("color", "#636363");
				
				dataMap.put("keyword1", k1);
				dataMap.put("keyword2", k2);
				dataMap.put("keyword3", k3);
				dataMap.put("keyword4", k4);
				dataMap.put("keyword5", k5);
				dataMap.put("keyword6", k6);
			
			/*String dataMapJson = new Gson().toJson(dataMap);*/
			
			
				//发送模板消息
				String queryString = String.format("?access_token=%s",token.getAccessToken());
				Request request = new Request("wx","msgSend",queryString);
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("touser", apply.getOpenid());
				params.put("template_id","xJHMUoILG5NYcF8aURA2YPREk9o9bseIgTmk49BpIJs");
				params.put("page", "pages/partydetail/detail?partyId="+partyId);
				params.put("form_id", apply.getFormId());
				params.put("data", dataMap); 
				
				String bodyStr = gson.toJson(params);
				
				Logger.info("请求微信发送模板消息body=%s", bodyStr);
				request.setBody(bodyStr.getBytes("UTF-8"));
				
				Response response = HTTP.POST(request);
				if(response.getStatusCode()==200){
					String respStr = response.getContent();
					Logger.info("请求微信发送模版消息响应内容，%s",respStr);
					JsonObject result =  new JsonParser().parse(respStr).getAsJsonObject();
					//成功和formid错误的都设置为已发，不再重试
					if(result.get("errcode").getAsInt() == 0 ){
						apply.setMsgSent(1);
						PartyApplyInfoService.update(apply);
					}else if(result.get("errcode").getAsInt()==41028){
						apply.setMsgSent(-1); 
						PartyApplyInfoService.update(apply); 
					}
					
				} 
			}
		}catch(Exception e){
			Logger.error(e, e.getMessage());  
		}
	}
	
	public static void main(String[] args){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("page", "/pages/partydetail/detail?partyId="+112);
		System.out.println(gson.toJson(params));
	}
}
