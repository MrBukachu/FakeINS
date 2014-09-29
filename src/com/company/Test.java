package com.company;

import com.company.SensorData.GNSSData;
import com.company.Process.IntegratedData;
import com.company.TimeAndCoordinate.Coordinate;
import com.company.TimeAndCoordinate.CoordinateTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/16/14.
 */
public class Test {
    /************
     * 使用传感器数据模拟推算方位
     * @param dataBase
     * @return
     */
    static List<GNSSData> process(List<IntegratedData> dataBase)
    {
       int size = dataBase.size();
       int beginIndex =905;
       int endIndex = 1105;
        List<GNSSData> listTest = new ArrayList<GNSSData>();
        IntegratedData preData = dataBase.get(beginIndex - 1);
        double preX = preData.getSecGNSSData().getGaussCoordinate().getGaussX();
        double preY = preData.getSecGNSSData().getGaussCoordinate().getGaussY();
        double preH = preData.getSecGNSSData().getGeodeticCoordinate().getH();

       for(int i = beginIndex;i<endIndex;i++)
       {
           double deltaDistance = Math.sqrt(
                   Math.pow(dataBase.get(i).getSecGNSSData().getGaussCoordinate().getGaussX()
                           - dataBase.get(i - 1).getSecGNSSData().getGaussCoordinate().getGaussX(), 2)
                 + Math.pow(dataBase.get(i).getSecGNSSData().getGaussCoordinate().getGaussY()
                           - dataBase.get(i - 1).getSecGNSSData().getGaussCoordinate().getGaussY(), 2)
                 + Math.pow(dataBase.get(i).getSecGNSSData().getGeodeticCoordinate().getH()
                           - dataBase.get(i - 1).getSecGNSSData().getGeodeticCoordinate().getH(), 2)
           );
           double pitchAngle = Math.toRadians(-dataBase.get(i-1).getClinoData().getHeelAngle());
           double headAngle  = Math.toRadians(dataBase.get(i-1).getAzimuth());
           double x = preX + deltaDistance *
                   Math.cos(pitchAngle) * Math.cos(headAngle);
           double y = preY + deltaDistance *
                   Math.cos(pitchAngle) * Math.sin(headAngle);
           double h = preH + deltaDistance * Math.sin(pitchAngle);
//           System.out.println(deltaDistance);
           Coordinate.GeodeticCoordinate geoCoor = CoordinateTransform.gauss2blh(x, y, h);
           listTest.add(new GNSSData(dataBase.get(i).getSecGNSSData().getGpst(),geoCoor,"test",42));
           preX = x;
           preY = y;
           preH = h;
       }
       return  listTest;

    }
}
