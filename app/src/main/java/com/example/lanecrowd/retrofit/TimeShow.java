package com.example.lanecrowd.retrofit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeShow
{
    public static void main(String[] args) {


        System.out.println("time"+getTime("2020-03-12 00:35:44"));
    }




   public static String getTime(String time)
    {

        try
        {



            //convert 24hrs to 12 hrs format
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = null;
            String output = null;
            try{
                date= df.parse(time);
                output = outputformat.format(date);
            }catch(ParseException pe){
                pe.printStackTrace();
            }
            //






            //add 5.30 hrs to orinal date to get correct date
            Calendar calendar = Calendar.getInstance();
            Calendar calenda2 = Calendar.getInstance();
            calendar.setTime(df.parse(output));
            calendar.add(Calendar.HOUR_OF_DAY, 5);
            calendar.add(Calendar.MINUTE, 30);
            Date past2=calendar.getTime();


            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            String inActiveDate = format1.format(past2);
            Date originDate=format1.parse(inActiveDate);
            calenda2.setTime(originDate);
            Date past3=calenda2.getTime();


            Date now = new Date();

            System.out.println("original_time"+time);
            System.out.println("inActiveDate"+inActiveDate);
            System.out.println("past3"+past3);
            System.out.println("now_kaif"+now);







            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past3.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past3.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past3.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past3.getTime());


            if(seconds<60)
            {
                System.out.println(seconds+" seconds ago");

                return seconds+" seconds ago";
            }
            else if(minutes<60)
            {
                System.out.println(minutes+" minutes ago");
                return minutes+" minutes ago";
            }
            else if(hours<24)
            {
                System.out.println(hours+" hours ago");
                return hours+" hours ago";
            }
            else
            {
                System.out.println(days+" days ago");
                return days+" days ago";
            }
        }
        catch (Exception j){
            j.printStackTrace();
            return "empty";
        }
    }
}