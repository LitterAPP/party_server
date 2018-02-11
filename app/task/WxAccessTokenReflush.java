package task;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import jws.Jws;
import jws.Logger;
import jws.http.Request;
import jws.http.Response;
import jws.http.sf.ConnectException;
import jws.http.sf.HTTP;
import jws.http.sf.UnusableException;
import modules.token.ddl.WXAccessTokenDDL;
import modules.token.service.WXAccessTokenService;

public class WxAccessTokenReflush implements Runnable {

	@Override
	public void run() {
		try{
			WXAccessTokenDDL accessToken = WXAccessTokenService.get();
			//5分钟快过期了，赶紧更新一下
			if(accessToken==null || accessToken.getExpiresIn()-System.currentTimeMillis() < 10*60*1000){
				JsonObject tokenJson = requestAccessToken();
				if(tokenJson==null){
					return ;
				}
				if(tokenJson.get("errcode")!=null){
					Logger.error("请求微信获取accessToken错误,%s", tokenJson.toString());
					return ;
				}
				String accessTokenStr = tokenJson.get("access_token").getAsString();
				long expireIn = tokenJson.get("expires_in").getAsLong() * 1000;
				WXAccessTokenService.updateExpireIn(accessTokenStr, expireIn);
				return ;
			}
		}catch(Exception e){
			Logger.error(e, e.getMessage());
		}
	}
	
	private JsonObject requestAccessToken(){
		try{
			String queryString = String.format("?grant_type=client_credential&appid=%s&secret=%s",
					Jws.configuration.getProperty("wx.api.appid"),
					Jws.configuration.getProperty("wx.api.secret")
				);
			
			Request request = new Request("wx","token",queryString);
			Response response = HTTP.GET(request);
			if(response.getStatusCode()!=200){
				return null;
			}
			String respStr = response.getContent();
			Logger.info("请求微信获取accessToken响应内容，%s",respStr);
			return new JsonParser().parse(respStr).getAsJsonObject();
		} catch (Exception e) {
			Logger.error(e, e.getMessage());
			return null;
		} 
	}

}
