package modules.token.service;

import java.util.List;

import jws.dal.Dal;
import modules.token.ddl.WXAccessTokenDDL;

public class WXAccessTokenService {

	public static void add(WXAccessTokenDDL wxAccessTokenDDL){
		WXAccessTokenDDL result = get();
		if(result!=null)return ;
		Dal.insert(wxAccessTokenDDL);
	}
	
	public static WXAccessTokenDDL get(){
		List<WXAccessTokenDDL> results =  Dal.select("WXAccessTokenDDL.*", null, null, 0, 1);
		if(results==null || results.size() == 0) return null;
		return results.get(0);
	}
	
	public static void updateExpireIn(String accessToken,long expireIn){
		WXAccessTokenDDL result = get();
		if(result == null){
			WXAccessTokenDDL newWXAccessTokenDDL = new WXAccessTokenDDL();
			newWXAccessTokenDDL.setAccessToken(accessToken);
			newWXAccessTokenDDL.setExpiresIn(System.currentTimeMillis()+expireIn);
			add(newWXAccessTokenDDL);
			return ;
		}
		
		result.setAccessToken(accessToken);
		result.setExpiresIn(System.currentTimeMillis()+expireIn);
		Dal.insertUpdate(result, "WXAccessTokenDDL.accessToken,WXAccessTokenDDL.expiresIn");
	}
}
