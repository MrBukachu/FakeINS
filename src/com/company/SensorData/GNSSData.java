package com.company.SensorData;

/**
 * Created by mac on 4/14/14.
 */

import com.company.TimeAndCoordinate.Coordinate;
import com.company.TimeAndCoordinate.CoordinateTransform;
import com.company.TimeAndCoordinate.Time;

/***************************
 * GNSS每个历元结算数据类
 */
public class GNSSData {
    private Time.GPST gpst = null;
    private Coordinate.GeodeticCoordinate geodeticCoordinate = null;
    private Coordinate.GaussCoordinate gaussCoordinate = null;
    private String solveStyle = null; //定位结果的类型（Fix/Float/Diff）
    private int satNum;//接收卫星数
    private double azimuth = 0;
    public GNSSData(Time.GPST gpst,Coordinate.GeodeticCoordinate geoCoor,String solStyle,int satNum)
    {
        this.gpst = gpst;  geodeticCoordinate = geoCoor;
        gaussCoordinate = CoordinateTransform.bl2Gauss(geoCoor.getB(), geoCoor.getL());
        solveStyle = solStyle;  this.satNum = satNum;
    }

    public Time.GPST getGpst() {
        return gpst;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public Coordinate.GaussCoordinate getGaussCoordinate() {
        return gaussCoordinate;
    }

    public Coordinate.GeodeticCoordinate getGeodeticCoordinate() {
        return geodeticCoordinate;
    }

    public String getSolveStyle() {
        return solveStyle;
    }

    public int getSatNum() {
        return satNum;
    }
}
