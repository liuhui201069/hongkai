package util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author huiliu
 * @since 2017/10/14
 */
public class DateUtil {

    private static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 获取当天日期YYYY-MM-DD
     */
    public static String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        return dateFormat.get().format(calendar.getTime());
    }

    /**
     * 获取当天日期YYYY-MM-DD
     */
    public static String getTodayTime() {
        Calendar calendar = Calendar.getInstance();
        return timeFormat.get().format(calendar.getTime());
    }

    /**
     * 日期格式化
     *
     * @return
     */
    public static String format(Timestamp ts) {
        return timeFormat.get().format(ts);
    }
}
