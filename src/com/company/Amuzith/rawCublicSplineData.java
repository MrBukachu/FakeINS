package com.company.Amuzith;

import com.company.General.List2Array;
import com.company.SensorData.GNSSData;
import com.company.Process.IntegratedData;
import java.util.ArrayList;
import java.util.List;

/**
 * 存储构成三次样条插值的原始数据以及参考方位角
 * Created by mac on 5/12/14.
 */
public class rawCublicSplineData {
    private int index;
    private List<GNSSData> listDataBase = new ArrayList<GNSSData>();
    private double limitDistance;
    private double deltaX = 0;
    private double deltaY = 0;
    //数组操作不方便
//    private int size = 9 ;
    private double[] LatArray ;
    private  double[] LonArray ;
    private double[] HeightArray ;
    private double[] epochArray;
    private double[] GaussXArray;
    private double[] GaussYArray ;

    private List<Double> listLat = new ArrayList<Double>();
    private List<Double> listLon= new ArrayList<Double>();
    private List<Double> listHeight= new ArrayList<Double>();
    private List<Double> listepoch= new ArrayList<Double>();
    private List<Double> listGaussX= new ArrayList<Double>();
    private List<Double> listGaussY= new ArrayList<Double>();


    private  int i = 0; //使用多少组数据进行插值
    private double refAzi;
    private boolean isStop = false ;
    private int ArrayIndex = 0;
    private int usedDataNumber;
    private int surroundingPointNum = 0 ;
    rawCublicSplineData(int index,List<GNSSData> listEI,double limitDistance)
    {
        this.index = index ;
        this.listDataBase = listEI;
        this.limitDistance = limitDistance;
        DataProcess();
    }
    public void DataProcess()
    {
        double deltaDistance = 0;
        int beforeNum = 0;
        int afterNum = 0;

        while ((deltaDistance < limitDistance / 2) && !(index - beforeNum < 0))  //获取前半段活动距离内有几个点
        {
            beforeNum++;
            deltaDistance = Math.sqrt(Math.pow(listDataBase.get(index - beforeNum).getGaussCoordinate().getGaussX()- listDataBase.get(index).getGaussCoordinate().getGaussX(), 2)
                            + Math.pow(listDataBase.get(index - beforeNum).getGaussCoordinate().getGaussY() - listDataBase.get(index).getGaussCoordinate().getGaussY(), 2)
                            + Math.pow(listDataBase.get(index - beforeNum).getGeodeticCoordinate().getH() - listDataBase.get(index).getGeodeticCoordinate().getH(), 2)
            );
            if(deltaDistance < 0.1)
            {
                surroundingPointNum++;
            }
        }

        double deltaDistance1 = 0;

        while ((deltaDistance1 < limitDistance / 2) && !(index + afterNum > listDataBase.size()))  //获取后半段活动距离内有几个点
        {
            afterNum++;
            deltaDistance1 = Math.sqrt(Math.pow(listDataBase.get(index + afterNum).getGaussCoordinate().getGaussX() - listDataBase.get(index).getGaussCoordinate().getGaussX(), 2)
                            + Math.pow(listDataBase.get(index + afterNum).getGaussCoordinate().getGaussY() - listDataBase.get(index).getGaussCoordinate().getGaussY(), 2)
                            + Math.pow(listDataBase.get(index + afterNum).getGeodeticCoordinate().getH() - listDataBase.get(index).getGeodeticCoordinate().getH(), 2)
            );
            if(deltaDistance1 < 0.1)
            {
                surroundingPointNum++;
            }
        }

        i = beforeNum + afterNum + 1;  //获取六米内一共多少点
//        System.out.println(i);
        if(Math.sqrt( Math.pow(listDataBase.get(index-1).getGaussCoordinate().getGaussY()-listDataBase.get(index+1).getGaussCoordinate().getGaussY(),2)
                +Math.pow(listDataBase.get(index-1).getGaussCoordinate().getGaussX()-listDataBase.get(index+1).getGaussCoordinate().getGaussX(),2)
                +Math.pow(listDataBase.get(index-1).getGeodeticCoordinate().getH() - listDataBase.get(index+1).getGeodeticCoordinate().getH(),2))<0.3)
        {  //判断测量车是否移动
            isStop = true;
        }

        refAzi = IntegratedData.calAzi(listDataBase.get(index + afterNum),
                listDataBase.get(index - beforeNum));   //方位通过一定距离(前后各三米)确定

        //使用目标时刻前后距离3米的点进行插值
        int tmpIndex = index - beforeNum; //获取起始历元数据
        int number = 0;
        for (int k = 0; k < i; k++) {
            if (Math.sqrt(Math.pow(listDataBase.get(tmpIndex + k).getGaussCoordinate().getGaussX() - listDataBase.get(index).getGaussCoordinate().getGaussX(), 2)
                    + Math.pow(listDataBase.get(tmpIndex + k).getGaussCoordinate().getGaussY() - listDataBase.get(index).getGaussCoordinate().getGaussY(), 2)) > 0.5) {
                number++;

            }

        }
        LatArray = new double[number+1];
        LonArray = new double[number+1];
        HeightArray =  new double[number+1];
        epochArray = new double[number+1];
        GaussXArray =new double[number+1];
        GaussYArray = new double[number+1];
        usedDataNumber = number+1;
        int ArrayNum = 0 ;
        for(int j = 0;j<i;j++)
        {
            if ((tmpIndex + j) != index) {
//                if (Math.sqrt(Math.pow(listDataBase.get(tmpIndex + j).getGaussCoordinate().getGaussX() - listDataBase.get(index).getGaussCoordinate().getGaussX(), 2)
//                        + Math.pow(listDataBase.get(tmpIndex + j).getGaussCoordinate().getGaussY() - listDataBase.get(index).getGaussCoordinate().getGaussY(), 2)) > 1) {
                    //使用list内存超限
                    LatArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGeodeticCoordinate().getB();
                    LonArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGeodeticCoordinate().getL();
                    HeightArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGeodeticCoordinate().getH();
                    epochArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGpst().getTOW();
                    GaussXArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGaussCoordinate().getGaussX();
                    GaussYArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGaussCoordinate().getGaussY();
                    ArrayNum++;
//                }
            } else {   //读入index本身数据
                LatArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGeodeticCoordinate().getB();
                LonArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGeodeticCoordinate().getL();
                HeightArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGeodeticCoordinate().getH();
                epochArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGpst().getTOW();
                GaussXArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGaussCoordinate().getGaussX();
                GaussYArray[ArrayNum] = listDataBase.get(tmpIndex + j).getGaussCoordinate().getGaussY();
                ArrayIndex= ArrayNum;
                ArrayNum++;
            }
            deltaX = GaussXArray[ArrayIndex+1] - GaussXArray[ArrayIndex];
            deltaY = GaussYArray[ArrayIndex+1] - GaussYArray[ArrayIndex];
            int k = 1;
            //获取deltaX和deltaY,输出辅助判断方向
            while (Math.abs(deltaX) < 0.05 && Math.abs(deltaY) < 0.05) {
                deltaX = listDataBase.get(index + k).getGaussCoordinate().getGaussX() - listDataBase.get(index ).getGaussCoordinate().getGaussX();
                deltaY = listDataBase.get(index + k).getGaussCoordinate().getGaussY() - listDataBase.get(index ).getGaussCoordinate().getGaussY();
                k++;
            }
            usedDataNumber = LatArray.length;

        }}

        public double[] getLat(){return  LatArray;}
        public double[] getlon(){return  LonArray;}
        public double[] getH(){return HeightArray;}
        public int getI(){return i;}
        public double[] getEpoch(){return  epochArray;}
        public double getRefAzi(){return refAzi;}
        public boolean getIsStop(){return isStop;}
        public double[] getGaussX(){return GaussXArray;}
        public double[] getGaussY(){return GaussYArray;}
        public int getArrayIndex(){return  ArrayIndex;}
        public int getUsedDataNumber(){return usedDataNumber;}
        public double getDeltaX(){return deltaX;}
        public double getDeltaY(){return deltaY;}
        public int getSurroundingPointNum(){return surroundingPointNum;}
}
