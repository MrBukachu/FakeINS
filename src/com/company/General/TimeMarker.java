package com.company.General;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 5/13/14.
 */
public class TimeMarker {
    /*************************
     * 自动生成时间标志，测试用
     * @param startTime
     * @param endTime
     * @param interval
     * @return  listTimeMarker
     */
    public static List<Double> getTimeMarkerByRules(double startTime,double endTime,double interval)
    {
        List<Double> listTimeMarker = new ArrayList<Double>();
        double tmpTimeMarker = startTime;
        while(tmpTimeMarker <= endTime &&tmpTimeMarker>=startTime)
        {
            listTimeMarker.add(tmpTimeMarker);
            tmpTimeMarker =+ tmpTimeMarker+interval;
        }
        return listTimeMarker;
    }
//    public  static List<Double> getTimeMarkerByFile(String timeMarkerDir)
//    {
//        return
//    }

}
