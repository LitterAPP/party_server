package utils;

public class NumberUtil {

	/**
	 * 大于等于start && 小于等于end
	 * @param src
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean between(float src,float start,float end){
		return src>=start && src <= end;
	}
}
