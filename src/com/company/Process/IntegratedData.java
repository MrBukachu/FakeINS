package com.company.Process;

import com.company.SensorData.ClinometerData;
import com.company.SensorData.GNSSData;

/**
 * Created by mac on 4/15/14.
 */
public class IntegratedData {
    private GNSSData priGNSSData;//车顶GNSS天线数据
    private GNSSData secGNSSData;//车尾GNSS天线数据
    private ClinometerData clinoData;//倾斜仪数据
    private double azimuth;
    IntegratedData(GNSSData priData,GNSSData secData,ClinometerData clinometerData)
    {
        priGNSSData = priData;
        secGNSSData = secData;
        this.clinoData = clinometerData;
        azimuth = calAzi(priGNSSData,secGNSSData);
    }
    public static double calAzi(GNSSData priGNSSData,GNSSData secGNSSData)
    {   //将数值角转化为方向角
        double deltaX = priGNSSData.getGaussCoordinate().getGaussX() - secGNSSData.getGaussCoordinate().getGaussX();
        double deltaY = priGNSSData.getGaussCoordinate().getGaussY() - secGNSSData.getGaussCoordinate().getGaussY();
        double azi = 0;
        if (deltaX < 0) {
            azi = Math.toDegrees(Math.atan(deltaY / deltaX) + Math.PI);
        } else {
            if (deltaX > 0) {
                if (deltaY > 0) {
                    azi = Math.toDegrees(Math.atan(deltaY / deltaX));
                } else {
                    azi = Math.toDegrees(Math.atan(deltaY / deltaX) + 2 * Math.PI);
                }
            } else {    //处理deltaX= 0的情况
                if (deltaY > 0) {
                    azi = 90;
                } else {
                    azi = 270;
                }
            }
        }
        return azi;
    }

    public ClinometerData getClinoData() { return clinoData; }

    public double getAzimuth() {
        return azimuth;
    }

    public GNSSData getPriGNSSData() {
        return priGNSSData;
    }

    public GNSSData getSecGNSSData() {
        return secGNSSData;
    }
}
