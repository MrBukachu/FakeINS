package com.company.TimeAndCoordinate;

import com.company.General.Const;

/**
 * Created by mac on 4/11/14.
 */
public class Time{
    public static class GC
    {    //格里高利历法时
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private double second;
        public GC(int y,int m,int d,int h,int min,double s)
        {
            year = y; day = d; month = m;
            hour = h; minute = min; second =s;
        }

        public int getMinute() {
            return minute;
        }

        public double getSecond() {
            return second;
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }
    }

    /*********************
     * 儒略历法计时
     */
    public static  class JD
    {
        double julianDay;
        JD(double jd){julianDay =jd;}

        public double getJulianDay() {
            return julianDay;
        }
    }

    /*****************
     * GPS时历法
     */
    public static class GPST
    {
        private int WN;
        private double TOW;
        private int WD = 0;
        public GPST(int wn,double tow){WN = wn;TOW = tow;}

        /***********
         * 获取GPS周秒
         * @return
         */
        public double getTOW() {
            return TOW;
        }

        /************
         * 获取gps周
         * @return
         */
        public int getWN() {
            return WN;
        }

        /************
         * 获取是该周的第几天
         * 举例：如果等于4，则为周四，周日为0起算
         * @return
         */
        public int getWD() {
            WD = (int) TOW/(24*60*60);//表示一周的第几天，如果等于4，则为周四，周日为0
            return WD;
        }

        /*************************
         * 返回utc时间的时分秒形式，方便数据处理时的比较
         * @return
         */
        public String displayUTC()
        {
            int day,hour,min;
            double sec;
            day = (int)Math.floor(TOW/86400);
            hour = (int)Math.floor((TOW-day*86400)/3600);
            min = (int)Math.floor((TOW- day * 86400 - 3600 * hour)/60);
            sec = TOW- day * 86400 - 3600 * hour - min * 60;
            String tmp = hour +":"+min+":"+ Const.decimalFormat.format(sec);
            return tmp;
        }
    }
}
