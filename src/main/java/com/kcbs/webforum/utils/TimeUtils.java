package com.kcbs.webforum.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {

    //时间戳转时间
    public static String stampToDate(long time){
        if (time==0){
            return null;
        }
        String s = String.valueOf(time).substring(0,13);
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    //往后多少S的日期
    public static Date getEndDate(Integer s){
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        c.setTime(date);//设置参数时间
        c.add(Calendar.SECOND,+s);
        date=c.getTime(); //这个时间就是日期往后推一天的结果
        return date;
    }
}
