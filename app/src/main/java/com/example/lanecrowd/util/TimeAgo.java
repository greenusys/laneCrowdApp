package com.example.lanecrowd.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static void main(String[] args) {

        String input="2020-03-16 5:00:00";



    }



    public static String getTimeAgo(String input)throws ParseException  {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        //add 5.30 hrs to orinal date to get correct date
        Calendar calendar = Calendar.getInstance();
        Calendar calenda2 = Calendar.getInstance();
        calendar.setTime(df.parse(input));
        calendar.add(Calendar.HOUR_OF_DAY, 5);
        calendar.add(Calendar.MINUTE, 30);
        Date past2=calendar.getTime();

        long time=past2.getTime();


        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }




    }

}