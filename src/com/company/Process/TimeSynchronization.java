package com.company.Process;

import com.company.Process.Interpolation;
import com.company.SensorData.AngleData;
import com.company.SensorData.ClinometerData;
import com.company.SensorData.GNSSData;
import com.company.TimeAndCoordinate.Coordinate;
import com.company.TimeAndCoordinate.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/15/14.
 */
public class TimeSynchronization {


    /******************
     * 得到时间同步后的GNSS数据
     * @param rawGNSSData 原始数据
     * @param timeMarker 需要同步的时间目标列表
     * @return
     */
    public static List<GNSSData> timeSynGNSSData(List<GNSSData> rawGNSSData,List<Double> timeMarker)
    {
        //只处理了数据完美配套的情况
        if (rawGNSSData == null || timeMarker == null) {
            System.out.println("GNSS Data or time marker is null !");
            return null;
        }
        int GNSSSize = rawGNSSData.size();
        int timeMarkerSize = timeMarker.size();
        if (timeMarker.get(timeMarkerSize-1) < rawGNSSData.get(0).getGpst().getTOW() ||
                timeMarker.get(0) > rawGNSSData.get(GNSSSize).getGpst().getTOW()) {
            System.out.println("GNSS Data is not matched with time marker! ");
            return null;
        }
        List<GNSSData> synGNSSData = new ArrayList<GNSSData>();
        int size = timeMarker.size();
        for(int i = 0;i<size;i++)
        {
            int index = getNearestGNSSDataIndex(rawGNSSData, timeMarker.get(i));
            synGNSSData.add(getInterpolatedGNSSData(rawGNSSData, 5, index, timeMarker.get(i)));
        }
        return synGNSSData;
    }

    /******************
     * 获取插值后的GNSS数据
     * @param dataBase 原始数据
     * @param n      表示做n阶插值
     * @return       插值后的结果
     */
    public static GNSSData getInterpolatedGNSSData(List<GNSSData> dataBase,int n,int index,double target)
    {
        int gpsWeek = dataBase.get(0).getGpst().getWN();
        int offset = 0;
        double []towArray  = new double[n];
        double []latArray  = new double[n];
        double []lonArray = new double [n];
        double []heightArray = new double[n];
        if( n%2 == 0)
        {
             offset = n/2;
        }
        else
        {
            offset = (n-1)/2;//actually it doesn't make any sense
        }
        int beginIndex = index -offset;
        int endIndex = index + n - offset;
        int size = dataBase.size();
        if (beginIndex>=0 && endIndex <=size) {
            for(int i = 0;i<n;i++)
            {
                towArray[i] = dataBase.get(beginIndex+i).getGpst().getTOW();
                latArray[i]= dataBase.get(beginIndex+i).getGeodeticCoordinate().getB();
                lonArray[i]= dataBase.get(beginIndex+i).getGeodeticCoordinate().getL();
                heightArray[i]= dataBase.get(beginIndex+i).getGeodeticCoordinate().getH();
            }
        } else {
            if (beginIndex < 0) {
                for (int i = 0; i < n; i++) {   //处理target历元前面没有数据的情况
                    towArray[i] = dataBase.get(i).getGpst().getTOW();
                    latArray[i] = dataBase.get(i).getGeodeticCoordinate().getB();
                    lonArray[i] = dataBase.get(i).getGeodeticCoordinate().getL();
                    heightArray[i] = dataBase.get(i).getGeodeticCoordinate().getH();
                }
                if (beginIndex > size) {
                    for (int i = 0; i < n; i++) {   //处理target历元前面没有数据的情况
                        towArray[i] = dataBase.get(size - n + i).getGpst().getTOW();
                        latArray[i] = dataBase.get(size - n + i).getGeodeticCoordinate().getB();
                        lonArray[i] = dataBase.get(size - n + i).getGeodeticCoordinate().getL();
                        heightArray[i] = dataBase.get(size - n + i).getGeodeticCoordinate().getH();
                    }
                }
            }

        }
        double tmpLat = Interpolation.LagrangeInterpolation(towArray, latArray, target);
        double tmpLon = Interpolation.LagrangeInterpolation(towArray,lonArray,target);
        double tmpHeight = Interpolation.LagrangeInterpolation(towArray,heightArray,target);
        return new GNSSData(new Time.GPST(gpsWeek,target),
                new Coordinate.GeodeticCoordinate(tmpLat,tmpLon,tmpHeight),
                "Interpolated",42);
    }

    /******************
     * 得到目标时刻在原始数据中最靠近时刻的index
     * @param dataBase   原始数据
     * @param target     目标时刻
     * @return   index
     */
    public static int getNearestGNSSDataIndex(List<GNSSData> dataBase,double target)
    {
        //需要确保target之前有数据
        int index = 0;//源数据中最近时刻的个数下标
        boolean isfind = false;
        int size = dataBase.size();
        double deltaEpoch1 = 10000;
        int i = 0 ;
        while(true != isfind)
        {
            double deltaEpoch2 = Math.abs(target - dataBase.get(i).getGpst().getTOW());
            if (0 == deltaEpoch2) {
                isfind = true;
                index = i;

            } else {
                if (deltaEpoch2 < deltaEpoch1) {
                    i++;
                    deltaEpoch1 = deltaEpoch2;
                } else {
                    isfind = true;
                    index = i - 1;
                }
            }

        }
        return index;
    }

    /******************
     * 得到时间同步后的倾斜仪数据
     * @param rawClinoData   原始数据
     * @param timeMarker     同步目标时刻列表
     * @return               同步后的倾斜仪数据
     */
    static List<ClinometerData> timeSynClinoData(List<ClinometerData> rawClinoData,List<Double> timeMarker)
    {
        List<ClinometerData> synClinoData = new ArrayList<ClinometerData>();
        int size = timeMarker.size();
        for(int i = 0;i<size;i++)
        {
            int index = getNearestClinometerDataIndex(rawClinoData, timeMarker.get(i));
            synClinoData.add(getInterpolatedClinometerData(rawClinoData, 5, index, timeMarker.get(i)));
        }
        return synClinoData;
    }
    /******************
     * 得到目标时刻在倾斜仪原始数据中最靠近时刻的index
     * @param dataBase   原始数据
     * @param target     目标时刻
     * @return   index
     */
    public static int getNearestClinometerDataIndex(List<ClinometerData> dataBase,double target)
    {
        int index = 0;//源数据中最近时刻的个数小标
        boolean isfind = false;
        int size = dataBase.size();
        double deltaEpoch1 = 10000;
        int i = 0 ;
        while(true != isfind)
        {
            double deltaEpoch2 = Math.abs(target - dataBase.get(i).getGpst().getTOW());
            if (0 == deltaEpoch2) {
                isfind = true;
                index = i;

            } else {
                if (deltaEpoch2 < deltaEpoch1) {
                    i++;
                    deltaEpoch1 = deltaEpoch2;
                } else {
                    isfind = true;
                    index = i - 1;
                }
            }

        }
        return index;
    }
    /******************
     * 获取插值后的倾斜仪数据
     * @param dataBase 原始数据
     * @param n      表示做n阶插值
     * @param target 目标时刻
     * @return       插值后的结果
     */
    public static ClinometerData getInterpolatedClinometerData(List<ClinometerData> dataBase,int n,int index,double target)
    {
        int gpsWeek = dataBase.get(0).getGpst().getWN();
        int offset = 0;
        double []heelAngleArray  = new double[n];
        double []trimAngleArray  = new double[n];
        double []towArray = new double [n];
        if( n%2 == 0)
        {
            offset = n/2;
        }
        else
        {
            offset = (n-1)/2;//actually it doesn't make any sense
        }
        int beginIndex = index -offset;
        for(int i = 0;i<n;i++)
        {
            towArray[i] = dataBase.get(beginIndex+i).getGpst().getTOW();
            heelAngleArray[i]= dataBase.get(beginIndex+i).getHeelAngle();
            trimAngleArray[i]= dataBase.get(beginIndex+i).getTrimAngle();
        }
        double tmpHeelAngle = Interpolation.LagrangeInterpolation(towArray,heelAngleArray,target);
        double tmpTrimAngle = Interpolation.LagrangeInterpolation(towArray,trimAngleArray,target);
        return new ClinometerData(new Time.GPST(gpsWeek,target),
                tmpHeelAngle,tmpTrimAngle);
    }


    public static List<AngleData> timeSynAngleData(List<AngleData> rawAngleData,List<Double> timeMarker)
    {
        //只处理了数据完美配套的情况
        //rawAngleData的时间覆盖范围得大于timeMarker的时间范围
        if (rawAngleData == null || timeMarker == null) {
            System.out.println("GNSS Data or time marker is null !");
            return null;
        }
        int angleSize = rawAngleData.size();
        int timeMarkerSize = timeMarker.size();
        if (timeMarker.get(timeMarkerSize-1) < rawAngleData.get(0).getTime().getTOW() ||
                timeMarker.get(0) > rawAngleData.get(angleSize - 1).getTime().getTOW()) {
            System.out.println("angle Data is not matched with time marker! ");
            return null;
        }
        List<AngleData> synAngleData = new ArrayList<AngleData>();
//        int size = timeMarker.size();
        for(int i = 0;i<timeMarkerSize;i++)
        {
            int index = getNearestAngleDataIndex(rawAngleData, timeMarker.get(i));
            synAngleData.add(getInterpolatedAngleData(rawAngleData, 5, index, timeMarker.get(i)));
        }
        return synAngleData;
    }

    /******************
     * 获取插值后的角度数据
     * @param dataBase 原始数据
     * @param n      表示做n阶插值
     * @return       插值后的结果
     */
    public static AngleData getInterpolatedAngleData(List<AngleData> dataBase,int n,int index,double target)
    {
        int gpsWeek = dataBase.get(0).getTime().getWN();
        int offset = 0;
        double []towArray = new double[n];
        double []leftAngleArray  = new double[n];
        double []rightAngleArray  = new double[n];

        if( n%2 == 0)
        {
            offset = n/2;
        }
        else
        {
            offset = (n-1)/2;//actually it doesn't make any sense
        }
        int beginIndex = index -offset;
        int endIndex = index + n - offset;
        int size = dataBase.size();
        if (beginIndex>=0 && endIndex <=size) {
            for(int i = 0;i<n;i++)
            {
                towArray[i] = dataBase.get(beginIndex+i).getTime().getTOW();
                leftAngleArray[i]= dataBase.get(beginIndex+i).getLeftAngle();
                rightAngleArray[i]= dataBase.get(beginIndex+i).getRightAngle();  }
        } else {
            if (beginIndex < 0) {
                for (int i = 0; i < n; i++) {   //处理target历元前面没有数据的情况
                    towArray[i] = dataBase.get(i).getTime().getTOW();
                    leftAngleArray[i]= dataBase.get(i).getLeftAngle();
                    rightAngleArray[i]= dataBase.get(i).getRightAngle();  }
                }
                if (beginIndex > size) {
                    for (int i = 0; i < n; i++) {   //处理target历元后面没有数据的情况
                        towArray[i] = dataBase.get(size - n + i).getTime().getTOW();
                        leftAngleArray[i]= dataBase.get(size - n + i).getLeftAngle();
                        rightAngleArray[i]= dataBase.get(size - n + i).getRightAngle();
                    }
                }
            }


        double tmpLeftAngle = Interpolation.LagrangeInterpolation(towArray,leftAngleArray, target);
        double tmpRightAngle = Interpolation.LagrangeInterpolation(towArray,rightAngleArray,target);

        return new AngleData(new Time.GPST(gpsWeek,target),tmpLeftAngle,tmpRightAngle);
    }

    /******************
     * 得到目标时刻在原始数据中最靠近时刻的index
     * @param dataBase   原始数据
     * @param target     目标时刻
     * @return   index
     */
    public static int getNearestAngleDataIndex(List<AngleData> dataBase,double target)
    {
        //需要确保target之前有数据
        int index = 0;//源数据中最近时刻的个数下标
        boolean isfind = false;
        int size = dataBase.size();
        double deltaEpoch1 = 10000;  //set big enough
        int i = 0 ;
        while(true != isfind)
        {
            double deltaEpoch2 = Math.abs(target - dataBase.get(i).getTime().getTOW());
            if (0 == deltaEpoch2) {
                isfind = true;
                index = i;

            } else {
                if (deltaEpoch2 < deltaEpoch1) {
                    i++;
                    deltaEpoch1 = deltaEpoch2;
                } else {
                    isfind = true;
                    index = i - 1;
                }
            }

        }
        return index;
    }

    /*************************
     * 从角度数据获取timeMaker基准
     * @param rawAngleData
     * @return
     */
    public static List<Double> getTimeMarkerFromAngle(List<AngleData> rawAngleData)
    {
        List<Double> timeMarker = new ArrayList<Double>();
        int size = rawAngleData.size();
        for(int i = 0;i<size;i++)
        {
            timeMarker.add(rawAngleData.get(i).getTime().getTOW());
        }
        return timeMarker;
    }

    /***************************
     * 从GNSS数据获取timeMarker基准
     * @param rawGNSSData
     * @return
     */
    public static List<Double> getTimeMarkerFromGNSS(List<GNSSData> rawGNSSData)
    {
        List<Double> timeMarker = new ArrayList<Double>();
        int size = rawGNSSData.size();
        for(int i = 0;i<size;i++)
        {
            timeMarker.add(rawGNSSData.get(i).getGpst().getTOW());
        }
        return timeMarker;
    }
}
