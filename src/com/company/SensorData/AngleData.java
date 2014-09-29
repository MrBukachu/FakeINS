package com.company.SensorData;

import com.company.TimeAndCoordinate.Time;

/**
 * 存贮左右两个角度传感器的采集数据，包括时间
 * Created by mac on 7/28/14.
 */
public class AngleData {
    private Time.GPST time = null;
    private double leftAngle = 0;
    private double rightAngle = 0;
    private double azimuth = 0; //弧度制
    private double left_X;
    private double left_Y;
    private double right_X;
    private double right_Y;
    private double mid_X;
    private double mid_Y;  //车轮中心坐标
    private double centralAngle;
    private double actualCentralAngle;
    private boolean isAdvance = true;

    public double getLeftAngle() {
        return leftAngle;
    }

    public double getRightAngle() {
        return rightAngle;
    }

    public Time.GPST getTime() {
        return time;
    }
    public AngleData(Time.GPST t,double leftAngle,double rightAngle)
    {
        time = t; this.leftAngle = leftAngle;this.rightAngle = rightAngle;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getLeft_X() {
        return left_X;
    }

    public double getLeft_Y() {
        return left_Y;
    }

    public void setLeft_X(double left_X) {
        this.left_X = left_X;
    }

    public void setLeft_Y(double left_Y) {
        this.left_Y = left_Y;
    }

    public double getRight_X() {
        return right_X;
    }

    public double getRight_Y() {
        return right_Y;
    }

    public void setRight_X(double right_X) {
        this.right_X = right_X;
    }

    public void setRight_Y(double right_Y) {
        this.right_Y = right_Y;
    }

    public void setCentralAngle(double centralAngle) {
        this.centralAngle = centralAngle;
    }

    public double getCentralAngle() {
        return centralAngle;
    }

    public boolean getAdvance(){return isAdvance;}

    public void setAdvance(boolean isAdvance) {
        this.isAdvance = isAdvance;
    }

    public double getActualCentralAngle() {
        return actualCentralAngle;
    }

    public void setActualCentralAngle(double actualCentralAngle) {
        this.actualCentralAngle = actualCentralAngle;
    }

    public double getMid_X() {
        return mid_X;
    }

    public double getMid_Y() {
        return mid_Y;
    }

    public void setMid_X(double mid_X) {
        this.mid_X = mid_X;
    }

    public void setMid_Y(double mid_Y) {
        this.mid_Y = mid_Y;
    }
}
