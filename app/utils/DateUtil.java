package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	
	public static java.sql.Date getSqlDate(){
		return new java.sql.Date(new Date().getTime());
	}
	
	public static String format(String format,Date date){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static String format(String format,long mills){
		return format(format,new Date(mills));
	}
	
	public static int getMonth(long mills){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(mills));
		return c.get(Calendar.MONTH)+1;
	}
	
	public static int getYear(long mills){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(mills));
		return c.get(Calendar.YEAR);
	}
	
	public static int getDay(long mills){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(mills));
		return c.get(Calendar.DATE);
	}
	
	public static int getWeekDay(long mills){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(mills));
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	public static String showWeekDay(long mills){
		int dayOfWeek = getWeekDay(mills);
		switch(dayOfWeek){
		case 1: return "星期日";
		case 2: return "星期一";
		case 3: return "星期二";
		case 4: return "星期三";
		case 5: return "星期四";
		case 6: return "星期五";
		case 7: return "星期六";
		default:return "";
		} 
	} 
	public static String showDate(long mills){
		return getMonth(mills)+"月"+getDay(mills)+"日";
	}
	
	public static void main(String[] args) throws ParseException{
		System.out.println(format("HH:mm",getWeekDay(new SimpleDateFormat("yyyy-MM-dd").parse("2017-07-30").getTime())));
	}
}
