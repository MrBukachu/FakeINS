package com.company.SensorData;

import com.company.TimeAndCoordinate.Time;

/**
 * 每次采集的倾斜仪数据结构
 * Created by mac on 4/14/14.
 */
public class ClinometerData {
    private Time.GPST gpst = null;
    private double heelAngle  = 0;
    private double trimAngle = 0;
    public ClinometerData(Time.GPST gpst,double hA,double tA)
    {  this.gpst = gpst;     heelAngle = hA;    trimAngle = tA; }

    public double getHeelAngle() {
        return heelAngle;
    }

    public double getTrimAngle() {
        return trimAngle;
    }

    public Time.GPST getGpst() {
        return gpst;
    }
}
