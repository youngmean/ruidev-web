package com.ruidev.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ruidev.framework.constant.BaseConstants;

/**
 * 日期函数工具类
 */
public class DateTimeUtil {

	/**
	 * 获得当前的系统时间
	 * 
	 * @return 当前的系统时间
	 */
	public static Date getCurrentTime() {
		return new Date();
	}

	/**
	 * 获得当前的系统日期，0分0秒
	 * 
	 * @return 当前的系统日期
	 */
	public static Date getCurrentDate() {

		Date date = getCurrentTime();
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);

		date = (Date) c.getTime();
		return date;
	}
	
	public static Date getTimeCustomizedDate(Date date, int hour, int minutes, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, second);
		cal.clear(Calendar.MILLISECOND);
		return cal.getTime();
	}

	/**
	 * 得到当前系统日期,格式："yyyy-MM-dd"
	 * 
	 * @return String
	 */
	public static String getCurrentDateStr() {
		return getFormatDate(getCurrentDate());
	}

	/**
	 * 输出字符串类型的格式化日期 "yyyy-MM-dd"
	 * 
	 * @param dt
	 *            Date
	 * @return sDate
	 */
	public static String getFormatDate(Date dt) {
		return getFormatDate(dt, BaseConstants.DATE_PATTERN);
	}

	/**
	 * 字符串格式化为日期 "yyyy-MM-dd"
	 * 
	 * @param dt
	 *            Date
	 * @return sDate
	 */
	public static Date getFormatDate(String dateStr) {
		return getFormatDate(dateStr, BaseConstants.DATE_PATTERN);
	}

	/**
	 * 输出字符串类型的格式化时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dt
	 *            Date
	 * @return sDate
	 */
	public static String getFormatTime(Date dt) {
		return getFormatDate(dt, BaseConstants.TIME_PATTERN);
	}

	/**
	 * 字符串格式化为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dt
	 *            Date
	 * @return sDate
	 */
	public static Date getFormatTime(String timeStr) {
		return getFormatDate(timeStr, BaseConstants.TIME_PATTERN);
	}
	
	private static Map<String, SimpleDateFormat> SIMPLE_DATE_FORMATS = new HashMap<String, SimpleDateFormat>();
	
	private static SimpleDateFormat getSimpleDateFormat(String pattern) {
		SimpleDateFormat format = SIMPLE_DATE_FORMATS.get(pattern);
		if(format == null) {
			format = new SimpleDateFormat(pattern);
			SIMPLE_DATE_FORMATS.put(pattern, format);
		}
		return format;
	}

	/**
	 * 输出字符串类型的格式化日期
	 * 
	 * @param dt
	 *            Date
	 * @param pattern
	 *            时间格式
	 * @return sDate
	 */
	public static String getFormatDate(Date dt, String pattern) {
		SimpleDateFormat formatter = getSimpleDateFormat(pattern);
		String sDate = formatter.format(dt);
		return sDate;
	}

	/**
	 * 输出字符串类型的格式化日期
	 * 
	 * @param dt
	 *            Date
	 * @param pattern
	 *            时间格式
	 * @return sDate
	 */
	public static Date getFormatDate(String dateStr, String pattern) {
		SimpleDateFormat formatter = getSimpleDateFormat(pattern);
		Date date = null;
		try {
			date = formatter.parse(dateStr);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * 得到某一天的开始时间，精确到毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 某一天的0时0分0秒0毫秒的那个Date
	 */
	public static Date beginOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 根据传入的参数得到定制的年份
	 * 
	 * @param date
	 *            日期
	 * @param mark
	 *            年份参数
	 * @return 定制好的年份
	 */
	public static Date getCustomYear(Date date, int mark) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, mark);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 得到某一天的最后时间，精确到毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 某一天的下一天的0时0分0秒0毫秒的那个Date减去1毫秒所得到的Date
	 */
	public static Date endOfDay(Date date) {
		date = beginOfDay(date);
		return endOfDayByBeginOfDate(date);
	}

	/**
	 * 根据某一天的开始时间，得到某一天的最后时间，精确到毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 某一天的下一天的0时0分0秒0毫秒的那个Date减去1毫秒所得到的Date
	 */
	public static Date endOfDayByBeginOfDate(Date date) {
		date = nextDay(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MILLISECOND, -1);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 得到指定日期后若干天的日期
	 * 
	 * @param date
	 *            指定日期
	 * @param days
	 *            天数
	 * @return 自指定日期后的若干天的日期
	 */
	public static Date afterDaysSinceDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 判断两个Date是否在同一天
	 * 
	 * @param date1
	 *            date1
	 * @param date2
	 *            date2
	 * @return 如果两个Date在同一天，则返回true，否则false
	 */
	public static boolean isTwoDatesInSameDay(Date date1, Date date2) {
		Date preDate1 = preDay(date1);
		Date nextDate1 = nextDay(date1);
		if (date2.after(preDate1) && date2.before(nextDate1)) {
			return true;
		}
		return false;
	}

	/**
	 * 得到指定日期的下一天的开始时间
	 * 
	 * @param date
	 *            指定Date
	 * @return 下一天的开始时间
	 */
	public static Date beginOfNextDay(Date date) {
		date = nextDay(date);
		return beginOfDay(date);
	}

	/**
	 * 得到指定日期的下一天
	 * 
	 * @param date
	 *            日期
	 * @return 传入日期的下一天
	 */
	public static Date nextDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 得到指定日期的前一天
	 * 
	 * @param date
	 *            日期
	 * @return 传入日期的前一天
	 */
	public static Date preDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 得到当前日期推后1个月的日期
	 * 
	 * @return String
	 */
	public static Date addMonth(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		date = (Date) c.getTime();
		return date;
	}

	/**
	 * 得到当前月的最后一天
	 * 
	 * @return String
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DATE, -1);
		date = c.getTime();
		return date;
	}

	/**
	 * 得到当前月的第一天
	 * 
	 * @return String
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		date = c.getTime();
		return date;
	}

	public static Date trimTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 得到当前周的第一天
	 * 
	 * @return String
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0) {
			dayofweek = 7;
		}
		c.add(Calendar.DATE, -dayofweek + 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	/**
	 * 得到当前周的最后一天
	 * 
	 * @return String
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0) {
			dayofweek = 7;
		}
		c.add(Calendar.DATE, -dayofweek + 8);
		c.add(Calendar.SECOND, -1);
		return c.getTime();
	}

	/**
	 * 得到当前年的第一天
	 * 
	 * @return String
	 */
	public static Date getFirstDayOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_YEAR, 1);
		date = c.getTime();
		return date;
	}

	/**
	 * 判断一个日期是否在指定的时间段内
	 * 
	 * @return String
	 */
	public static boolean inTimeSegment(Date start, Date end, Date date) {
		Date s = beginOfDay(start);
		Date e = beginOfNextDay(end);
		if ((s.getTime() >= s.getTime()) && date.before(e)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取两个时间的分钟差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getAbsMinutesBetweenDates(Date d1, Date d2) {
		return Math.abs((d1.getTime() - d2.getTime()) / 1000 / 60);
	}
	
	/**
	 * 获取两个时间的分钟差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getAbsHoursBetweenDates(Date d1, Date d2) {
		return getAbsMinutesBetweenDates(d1, d2) / 60;
	}
	
	/**
	 * 获取两个时间的分钟差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getMinutesBetweenDates(Date d1, Date d2) {
		return (d2.getTime() - d1.getTime()) / 1000 / 60;
	}

	/**
	 * 获取两个时间的秒钟差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getAbsSecondsBetweenDates(Date d1, Date d2) {
		return Math.abs((d1.getTime() - d2.getTime()) / 1000);
	}
	
	/**
	 * 获取两个时间的秒钟差
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getSecondsBetweenDates(Date d1, Date d2) {
		return (d2.getTime() - d1.getTime()) / 1000;
	}

	/**
	 * 判断当前日期是否在指定的时间段内
	 * 
	 * @param start
	 *            时间段开始时间
	 * @param end
	 *            时间段结束时间
	 * @return 如果当前日期在指定时间段内，则为true，否则为false
	 */
	public static boolean isCurrentDateInTimeSegment(Date start, Date end) {
		Date date = getCurrentDate();
		if (inTimeSegment(start, end, date)) {
			return true;
		}
		return false;
	}

	/**
	 * @author 得到两个日期的间隔天数
	 * @param start
	 * @param end
	 * @param date
	 * @return -1说明开始日期大于结束日期
	 */
	public static int getBetweenDays(Date start, Date end) {
		if (start.after(end)) {
			return -1;
		}
		Calendar startC = Calendar.getInstance();
		startC.setTime(start);
		Calendar endC = Calendar.getInstance();
		endC.setTime(end);
		int days = 0;
		while (startC.before(endC)) {
			startC.add(Calendar.DAY_OF_YEAR, 1);
			days++;
		}
		return days;
	}

	/**
	 * 判断两个时间段是否存在重叠
	 * 
	 * @param start1
	 *            第一个时间段的开始时间
	 * @param end1
	 *            第一个时间段的结束时间
	 * @param start2
	 *            第二个时间段的开始时间
	 * @param end2
	 *            第二个时间段的结束时间
	 * @return 如果存在重叠返回true，否则false
	 */
	public static boolean isTimeOverlap(Date start1, Date end1, Date start2,
			Date end2) {
		if (inTimeSegment(start1, start2, end2)
				|| inTimeSegment(end1, start2, end2)) {
			return true;
		}
		return false;
	}

	/**
	 * SQL语句小于时间的转换 比如小于1月1日，sql语句中其实是小于1月2日
	 * 
	 * @param resourceDate
	 * @return
	 */
	public static Date getSqlLessDate(Date resourceDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(resourceDate);
		c.add(Calendar.DATE, 1);
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		return c.getTime();
	}

	/**
	 * 在一个日期推后 days 天 的日期，不包含星期六和星期天
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDateIgnoreSaturdaySunday(Date date, long days) {
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			for (int i = 0; i < days;) {
				c.add(Calendar.DATE, 1);
				if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
						&& c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
					i++;
				}
			}
			date = c.getTime();
		}
		return date;

	}

	public static Date addDays(Date date, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, i);
		return c.getTime();
	}

	public static Date addMinutes(Date date, int i) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, i);
		return c.getTime();
	}

	public static Date addSeconds(Date date, int seconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, seconds);
		return c.getTime();
	}

	public static Date addMillSeconds(Date date, int millseconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MILLISECOND, millseconds);
		return c.getTime();
	}
}
