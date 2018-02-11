package utils;

public class StringUtil {

	public static boolean isBlank(String src){
		return src==null||src.trim().length()==0;
	}
	
	public static String filterEmoji(String source,String slipStr) {
		return source;
        /*if(!isBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }*/
    }
	
}
