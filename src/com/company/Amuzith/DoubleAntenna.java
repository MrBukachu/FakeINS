package com.company.Amuzith;

import com.company.SensorData.GNSSData;

/**
 * 双天线位置信息以及方向角信息
 * Created by mac on 5/13/14.
 */
public class DoubleAntenna {
    private GNSSData priGNSSData;
    private GNSSData secGNSSData;
    private double azimuth;
    public DoubleAntenna(GNSSData priGNSSData,GNSSData secGNSSData)
    {
        this.priGNSSData = priGNSSData;
        this.secGNSSData = secGNSSData;
        this.azimuth = CalAzimuth.getAziViaDoubleAntenna(priGNSSData.getGaussCoordinate().getGaussX() - secGNSSData.getGaussCoordinate().getGaussX(),
                priGNSSData.getGaussCoordinate().getGaussY() - secGNSSData.getGaussCoordinate().getGaussY());
    }
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
