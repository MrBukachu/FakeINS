package com.company.Amuzith;

import com.company.SensorData.GNSSData;

import java.util.ArrayList;
import java.util.List;

/**单天线类
 * Created by mac on 5/12/14.
 */
public class SingleAntenna {
    private List<SingleAntennaInfo>  listSingleAntennaInfo = new ArrayList<SingleAntennaInfo>();
    private List<GNSSData> rawData= new ArrayList<GNSSData>();

    private static final double LIMIT_DISTANCE = 6.0;
    public SingleAntenna(List<GNSSData> dataBase)
    {
        rawData = dataBase;
        process();
    }



    public void process()
    {
        int size = rawData.size();
        for (int i = 10; i < size - 10; i++) {
            rawCublicSplineData rcsd = new rawCublicSplineData(i,rawData,LIMIT_DISTANCE);
            CubicSpline CSX = new CubicSpline(rcsd.getEpoch(), rcsd.getGaussX(), rawData.get(i).getGpst().getTOW()) ;
            CubicSpline CSY = new CubicSpline(rcsd.getEpoch(), rcsd.getGaussY(), rawData.get(i).getGpst().getTOW()) ;
            double tmpAzi = CalAzimuth.getAziValue(CSX.getDerivative()/CSY.getDerivative(),
                    rcsd.getDeltaX(),rcsd.getDeltaY());
             SingleAntennaInfo tmpSingleAntennaInfo = new SingleAntennaInfo(rawData.get(i),tmpAzi,
                     rcsd.getIsStop(),rcsd.getSurroundingPointNum(),rcsd.getRefAzi());
            listSingleAntennaInfo.add(tmpSingleAntennaInfo);


        }
    }

    public List<GNSSData> getRawData() {
        return rawData;
    }

    public List<SingleAntennaInfo> getListSingleAntennaInfo() {
        return listSingleAntennaInfo;
    }
}
