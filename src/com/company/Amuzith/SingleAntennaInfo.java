package com.company.Amuzith;

import com.company.SensorData.GNSSData;

/**单天线信息
 * Created by mac on 5/13/14.
 */
public class SingleAntennaInfo {

    private GNSSData gnssData;
    private double azimuth, refAzi;
    private boolean isStop;
    private int surroundPoints;

    public SingleAntennaInfo(GNSSData gnssData, double azimuth, boolean isStop, int surroundPoints, double refAzi) {
        this.gnssData = gnssData;
        this.azimuth = azimuth;
        this.isStop = isStop;
        this.surroundPoints = surroundPoints;
        this.refAzi = refAzi;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public GNSSData getGnssData() {
        return gnssData;
    }

    public boolean isStop() {
        return isStop;
    }

    public int getSurroundPoints() {
        return surroundPoints;
    }

    public double getRefAzi() {
        return refAzi;
    }
}

