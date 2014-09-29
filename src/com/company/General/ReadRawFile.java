package com.company.General;

import com.company.SensorData.AngleData;
import com.company.SensorData.ClinometerData;
import com.company.SensorData.GNSSData;
import com.company.TimeAndCoordinate.Coordinate;
import com.company.TimeAndCoordinate.Time;
import com.company.TimeAndCoordinate.TimeTransform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/14/14.
 */
public class ReadRawFile {
    //使用枚举会更加方便
    public static final int ALL = 0;
    public static final int FIX = 1;
    /**************************
     * 读取倾斜仪数据
     * @param ClinometerFilePath clinometer file path
     * @return  clinometer data picked up
     */
    public static List<ClinometerData> getClinometerData(String ClinometerFilePath)
    {
        List<ClinometerData> listClinoData = new ArrayList<ClinometerData>();
        try {
        FileInputStream fis = new FileInputStream(ClinometerFilePath);
        InputStreamReader isw = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isw);

        String str;
        while ((str = br.readLine()) != null) {
            listClinoData.add( readClinoDataOneLine(str));
        }

    } catch (FileNotFoundException f) {
    } catch (IOException e) {
        e.printStackTrace();
    }
        return   listClinoData ;
    }
    public static ClinometerData readClinoDataOneLine(String oneLineStr)
    {
        String[] temp = oneLineStr.split(" +");
        int tempYear = Integer.parseInt(temp[0]);
        int tempMonth = Integer.parseInt(temp[1]);
        int tempDay = Integer.parseInt(temp[2]);
        int tempHour = Integer.parseInt(temp[3]) - 8;
        int tempMin = Integer.parseInt(temp[4]);
        double tempSec = Integer.parseInt(temp[5]) + Double.parseDouble(temp[6]) / 1000;
        double tempHeelAngle = Double.parseDouble(temp[7]);
        double tempTrimAngle = Double.parseDouble(temp[8]);

        ClinometerData tempClinodata =  new ClinometerData(
                TimeTransform.GC2GPST(new Time.GC(tempYear, tempMonth, tempDay, tempHour, tempMin, tempSec)),  //小时要减八
                tempHeelAngle,tempTrimAngle);
        return  tempClinodata;
    }
     /********************************
     * 下面读取司南仪器输出的定位结果文件
     * 其中文件不提供年份，月份，天的信息，获取解的类型
     * 需要人为在起始处作为参数传入
     * */
    public static List<GNSSData> getGNSSData(String GNSSFilePath,int year,int month,int day,int TYPE)
    {
        List<GNSSData> listGNSSData = new ArrayList<GNSSData>();
        try {
            FileInputStream fis = new FileInputStream(GNSSFilePath);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);

            String str;
            while ((str = br.readLine()) != null) {
                if (TYPE == ALL) {
                    listGNSSData.add(readGNSSDataOneLine(str,year,month,day));
                }
                else
                {
                    if(TYPE == FIX)
                    {
                        GNSSData tmpGNSSData = readGNSSDataOneLine(str,year,month,day);
                        if(tmpGNSSData.getSolveStyle().equals("Fix"))
                        {
                            listGNSSData.add(tmpGNSSData);
                        }
                    }
                }

            }

        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   listGNSSData ;
    }
    public static GNSSData readGNSSDataOneLine(String oneLineStr,int y,int m,int d)
    {
        String[] temp = oneLineStr.split("\t+");
        String tmpTime = temp[1];
        String tmpLat = temp[7];
        String tmpLon = temp[8];
        String tmpHeight = temp[9];
        String tmpSolStyle = temp[2];
        int tmpSatNum = Integer.parseInt(temp[4]);

        String[] tmpTimeSplit = tmpTime.split(":");
        int hour =  Integer.parseInt(tmpTimeSplit[0].trim());
        int min =   Integer.parseInt(tmpTimeSplit[1]);

        double sec = Double.parseDouble(tmpTimeSplit[2]);
        Time.GC tmpGC = new Time.GC(y,m,d,hour,min ,sec);
        //读取每一行的lat度分秒
        String[] tmpLatSplit = tmpLat.split(":");
        String tmpLatDeg = tmpLatSplit[0];
        String tmpLatMin = tmpLatSplit[1];
        String tmpLatSec = tmpLatSplit[2].substring(0, tmpLatSplit[2].length() - 1);
        double Lat = Double.parseDouble(tmpLatDeg) + Double.parseDouble(tmpLatMin) / 60 + Double.parseDouble(tmpLatSec) / 3600;
        //读取每一个历元得lon度分秒
        String[] tmpLonSplit0 = tmpLon.split(":");
        String tmpLonDeg = tmpLonSplit0[0];
        String tmpLonMin = tmpLonSplit0[1];
        String tmpLonSec = tmpLonSplit0[2].substring(0, tmpLonSplit0[2].length() - 1);
        double Lon = Double.parseDouble(tmpLonDeg) + Double.parseDouble(tmpLonMin) / 60 + Double.parseDouble(tmpLonSec) / 3600;
        double height =  Double.parseDouble(tmpHeight);
        Coordinate.GeodeticCoordinate tempGeoCoor = new Coordinate.GeodeticCoordinate(Lat,Lon,height);
        return new GNSSData(
                TimeTransform.GC2GPST(tmpGC),
                tempGeoCoor,tmpSolStyle,tmpSatNum);
    }

    /**************************
     * 从文本获取角度传感器数据
     * @param AngleFilePath
     * @return
     */
    public static List<AngleData> getAngleData(String AngleFilePath)
    {
        String tmpSaveDir = "/Users/mac/Desktop/I WANT GRADUATION/AngleData/tmpAngleFile.txt";
        List<AngleData> listAngleData = new ArrayList<AngleData>();
        try {
            FileInputStream fis = new FileInputStream(AngleFilePath);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);

            String str;
            while ((str = br.readLine()) != null) {
                AngleData tmpAngleData =  readAngleDataOneLine(str);
                listAngleData.add(tmpAngleData);
                String temp =  String.valueOf(tmpAngleData.getTime().getWN()) +" "
                        +  String.valueOf(Const.decimalFormat.format(tmpAngleData.getTime().getTOW()))+" "+
                        tmpAngleData.getTime().displayUTC()+" "+
                        tmpAngleData.getLeftAngle()+" "
                        +tmpAngleData.getRightAngle() ;
                WriteFile.SaveDataToFile(temp,tmpSaveDir);
            }

        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return   listAngleData ;
    }

    public static AngleData readAngleDataOneLine(String str)
    {
        String[] temp = str.split(" +");
        int tempYear = Integer.parseInt(temp[0]);
        int tempMonth = Integer.parseInt(temp[1]);
        int tempDay = Integer.parseInt(temp[2]);
        int tempHour = Integer.parseInt(temp[3]) - 8;
        int tempMin = Integer.parseInt(temp[4]);
        double tempSec = Integer.parseInt(temp[5]) + Double.parseDouble(temp[6]) / 1000;
        double tempLeftAngle = Double.parseDouble(temp[7]);
        double tempRightAngle = Double.parseDouble(temp[8]);

        AngleData tempAngledata =  new AngleData(
                TimeTransform.GC2GPST(new Time.GC(tempYear, tempMonth, tempDay, tempHour, tempMin, tempSec)),  //小时要减八
                tempLeftAngle,tempRightAngle);
        return  tempAngledata;
    }
    /********************
     * read GPGGA format
     * 语句标识头，世界时间，纬度，纬度半球，经度，经度半球，
     * 定位质量指示，使用卫星数量，水平精确度，海拔高度，高度单位，
     * 大地水准面高度，高度单位，差分GPS数据期限，差分参考基站标号，
     * 校验和结束标记(用回车符<CR>和换行符<LF>)，分别用14个逗号进行分隔。
     * 这里只提取固定解
     */
    public static List<GNSSData> getGPGGAData(String GPGGAFilePath,int weekNum,int weekday)
    {
        List<GNSSData> listGNSSData = new ArrayList<GNSSData>();
        try {
            FileInputStream fis = new FileInputStream(GPGGAFilePath);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);

            String str;
            GNSSData tmpGNSSData = null;
            while ((str = br.readLine()) != null) {
                    tmpGNSSData = readGPGGADataOneLine(str,weekNum,weekday);
                if (tmpGNSSData!=null && tmpGNSSData.getSolveStyle().equals("Fix")) {
                    listGNSSData.add(tmpGNSSData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return   listGNSSData ;
    }
    public static GNSSData readGPGGADataOneLine(String oneLineStr,int weekNum,int weekday)
    {
        String[] temp = oneLineStr.split(",");

        if (temp[0].equals("$GPGGA")) {
            String tmpTime = temp[1];//utc时间 hhmmss.ss的形式
            double tmpLat = Double.parseDouble(temp[2]);
            double tmpLon = Double.parseDouble(temp[4]);
            double tmpHeight = Double.parseDouble(temp[9]);
            int tmpSolStyle = Integer.parseInt(temp[6]);
            int tmpSatNum = Integer.parseInt(temp[7]);
            String solStyle;
            if (tmpSolStyle == 4)
                solStyle = "Fix";
            else
                solStyle = "Others";
            //解码UTC时间
            double hour = Double.valueOf(tmpTime.substring(0, 2).trim());
            double min = Double.valueOf(tmpTime.substring(2, 4).trim());
            double sec = Double.valueOf(tmpTime.substring(4).trim());
            double tow = weekday * 86400 + hour * 3600 + min * 60 + sec;
            //解码经纬度
            double latDeg, latMin;
            double lonDeg, lonMin;

            latDeg = Math.floor(tmpLat / 100);
            latMin = tmpLat - latDeg * 100;

            lonDeg = Math.floor(tmpLon / 100);
            lonMin = tmpLon - lonDeg * 100;

            double Lat = latDeg + latMin / 60;
            double Lon = lonDeg + lonMin / 60;

            Coordinate.GeodeticCoordinate tempGeoCoor = new Coordinate.GeodeticCoordinate(Lat, Lon, tmpHeight);
            return new GNSSData(new Time.GPST(weekNum, tow),
                    tempGeoCoor, solStyle, tmpSatNum);
        } else {
            return null;
        }
    }
    public static double readGPHDTDataOneLine(String oneLineStr)
    {
        String[] temp = oneLineStr.split(",");


        if (temp[0].equals("$GPHDT")) {

            double tmpAzi = Double.parseDouble(temp[1]);
             return tmpAzi;
        } else {
            return 0;
        }
    }
 }
