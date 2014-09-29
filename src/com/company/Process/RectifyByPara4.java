package com.company.Process;


import com.company.SensorData.GNSSData;
import com.company.TimeAndCoordinate.CalculatePara4;
import com.company.TimeAndCoordinate.CommonPoint;
import com.company.TimeAndCoordinate.CoordinateTransform;
import com.company.TimeAndCoordinate.Para4;



import java.util.*;

/**
 * Created by mac on 9/23/14.
 * 使用四参数校正平面位置
 */
public class RectifyByPara4 {

    public static List<GNSSData> process(List<GNSSData> dataBase,List<CommonPoint> cp)
    {

        double transGaussX = 0;
        double transGaussY = 0;
        for (int i = 0;i<dataBase.size();i++) {
            transGaussX += dataBase.get(i).getGaussCoordinate().getGaussX();
            transGaussY += dataBase.get(i).getGaussCoordinate().getGaussY();
        }
        transGaussX = transGaussX/dataBase.size();
        transGaussY = transGaussY/dataBase.size();
        List<GNSSData> tmp = new ArrayList<GNSSData>();
        cp = translationCP(cp,transGaussX,transGaussY);    //公共点平移
        dataBase = translationGNSS(dataBase,transGaussX,transGaussY); //所有待算数据平移
        Para4 p =CalculatePara4.process(cp);
        System.out.println(p);
        double tmpX = p.getDeltaX();
        double tmpY = p.getDeltaY();
        double tmpµCoså = p.getµCoså();
        double tmpµSinå = p.getµSinå();
        int size = dataBase.size();
        for (int i = 0 ;i < size;i++) {
            /******************
             * para4为将AO'B坐标系下的坐标转到XOY坐标系下的四参数坐标转换参数
             * X = X0 + µ * cos å * A - µ * sin å * B
             * Y = Y0 + µ * sin å * A + µ * cos å * B
             */
            double tmpGaussX = tmpX + tmpµCoså * dataBase.get(i).getGaussCoordinate().getGaussX()
                    - tmpµSinå * dataBase.get(i).getGaussCoordinate().getGaussY() ;
            double tmpGaussY = tmpY + tmpµSinå * dataBase.get(i).getGaussCoordinate().getGaussX()
                    + tmpµCoså * dataBase.get(i).getGaussCoordinate().getGaussY()  ;
            tmp.add(new GNSSData(
                    dataBase.get(i).getGpst(),
                    CoordinateTransform.gauss2blh(tmpGaussX, tmpGaussY, 9.448),    //此时的blh是错误数据
                    "Rectified",42
            ));
//            String str = dataBase.get(i).getGpst().displayUTC() + " " +
//                    Const.decimalFormat.format(dataBase.get(i).getGaussCoordinate().getGaussX()) + " " +
//                    Const.decimalFormat.format(dataBase.get(i).getGaussCoordinate().getGaussY())  + " " +
//                    Const.decimalFormat.format(tmpGaussX)+" "+
//                    Const.decimalFormat.format(tmpGaussY);
//            WriteFile.SaveDataToFile(str,"/Users/mac/Desktop/I WANT GRADUATION/expr/Para4test.txt");
        }
        tmp = recoverGNSS(tmp,transGaussX,transGaussY);
        return tmp;
    }
    public static List<CommonPoint> translationCP(List<CommonPoint> cp,double transGaussX,double transGaussY){

        for (int i = 0; i <cp.size() ; i++) {
            cp.get(i).setA(cp.get(i).getA() - transGaussX);
            cp.get(i).setB(cp.get(i).getB() - transGaussY);
            cp.get(i).setX(cp.get(i).getX() - transGaussX);
            cp.get(i).setY(cp.get(i).getY() - transGaussY);
        }
        return cp;
    }
    public static List<GNSSData> translationGNSS(List<GNSSData> db,double transGaussX,double transGaussY){

        for (int i = 0; i <db.size() ; i++) {
            db.get(i).getGaussCoordinate().setGaussX(db.get(i).getGaussCoordinate().getGaussX()- transGaussX);
            db.get(i).getGaussCoordinate().setGaussY(db.get(i).getGaussCoordinate().getGaussY() - transGaussY);
        }
        return db;
    }
    public static List<GNSSData> recoverGNSS(List<GNSSData> db,double transGaussX,double transGaussY){

        for (int i = 0; i <db.size() ; i++) {
            db.get(i).getGaussCoordinate().setGaussX(db.get(i).getGaussCoordinate().getGaussX()+ transGaussX);
            db.get(i).getGaussCoordinate().setGaussY(db.get(i).getGaussCoordinate().getGaussY()+ transGaussY);
        }
        return db;
    }
}
