package com.company.TimeAndCoordinate;

/**
 * Created by mac on 4/12/14.
 */
public class TimeTransform {
    /*************************
     * 格里高利时转化成儒略历时
     * @param gc
     * @return
     */
    static Time.JD GC2JD(Time.GC gc)
    {
        int y, m ;
        double JulianDay;
        if (gc.getMonth() <= 2)
        {
            y = gc.getYear() - 1;
            m = gc.getMonth() + 12;

        }
        else
        {
            y = gc.getYear();
            m =  gc.getMonth();
        }

        //用公式计算
        JulianDay = (int)(365.25 * (y)) + (int)(30.6001 * (m + 1)) + gc.getDay() + (double)gc.getHour() / 24
                +(double)gc.getMinute()/(24*60)+ +gc.getSecond()/(3600*24)+1720981.5;

        return new Time.JD(JulianDay);
    }

    /**********************************
     * 儒略历时转换成格里高利时
     * @param jd
     * @return
     */
    static Time.GC JD2GC(Time.JD jd)
    {
        int  a, b, c, d, f;
        double JD;

        JD = jd.getJulianDay();


        a = (int)(JD + 0.5);
        b = a + 1537;
        c = (int)((b - 122.1) / 365.25);
        d = (int)(365.25 * c);
        f = (int)((b - d) / 30.6001);
        int day = b - d - (int)(30.6001 * f) ;
        int month = f - 1 - 12 * (int)(f / 14);
        int year = c - 4715 - (int)((7 + month) / 10);

        double JDs = JD+0.5 - Math.floor(JD+0.5);
        int hour = (int)(JDs / (60 * 60));
        int min = (int)(JDs / 60 - 60 * hour);
        double sec = JDs - hour * 60 * 60 - min * 60;
        return new Time.GC(year,month,day,hour,min,sec);
    }

    /**************************
     * 格里高利时转换成gps时
     * @param gc
     * @return
     */
    public static Time.GPST GC2GPST(Time.GC gc)
    {
        double JD = TimeTransform.GC2JD(gc).getJulianDay();
        int wn = (int)((JD - 2444244.5) / 7);
        double tow = (JD - 2444244.5) * 3600 * 24 - wn * 3600 * 24 * 7;
        return new Time.GPST(wn,tow);
    }

}
