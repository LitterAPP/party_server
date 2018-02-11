package utils;

import java.util.HashMap;
import java.util.Map;

public class RtnUtil {
	public static final String CODE="code";
	public static final String MSG="msg";
	public static final String DATA="data";
	public static final int OK = 1;
	public static final int FAIL = -1;
	
	public static Map<String,Object> returnFail(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CODE, FAIL);
		map.put(MSG, "fail");
		return map;
	}
	
	public static Map<String,Object> returnFail(String msg){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CODE, FAIL);
		map.put(MSG, msg);
		return map;
	}
	
	public static Map<String,Object> returnFail(String msg,Object o){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CODE, FAIL);
		map.put(MSG, msg);
		map.put(DATA, o);
		return map;
	}
	
	public static Map<String,Object> returnSuccess(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CODE, OK);
		map.put(MSG, "ok");
		return map;
	}
	
	public static Map<String,Object> returnSuccess(String msg){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CODE, OK);
		map.put(MSG, msg);
		return map;
	}
	
	public static Map<String,Object> returnSuccess(String msg,Object o){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(CODE, OK);
		map.put(MSG, msg);
		map.put(DATA, o);
		return map;
	}
}
