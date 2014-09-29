package com.company.Process;

import com.company.General.Const;
import com.company.General.WriteFile;
import com.company.SensorData.AngleData;
import com.company.SensorData.GNSSData;
import com.company.TimeAndCoordinate.Coordinate;
import com.company.TimeAndCoordinate.CoordinateTransform;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 仅仅通过左右车轮的传感器来推算位置信息（平面）
 * Created by mac on 7/28/14.
 */



public class getPositionByAngleOnly {
    private static final String filePath = "/Users/mac/Desktop/I WANT GRADUATION/AngleData/processData.txt";
    private static final double INITIAL_LEFT_X = 0;
    private static final double INITIAL_LEFT_Y = 0;
    private static final double INITIAL_RIGHT_X = 0;
    private static final double INITIAL_RIGHT_Y = Const.DISTANCE_BETWEEN_WHEELS;
    private static double totalLeftMileage = 0;
    private static double totalRightMileage = 0;




    public static List<GNSSData> process(List<AngleData> rawAngleData,GNSSData originPoint)
    {
        //起始位置设置
       List<GNSSData> tmpListGNSS = new ArrayList<GNSSData>();

        rawAngleData.get(0).setMid_X(originPoint.getGaussCoordinate().getGaussX());
        rawAngleData.get(0).setMid_Y(originPoint.getGaussCoordinate().getGaussY());
        rawAngleData.get(0).setAzimuth(Math.toRadians(295.044));  //manual set temper
        rawAngleData.get(0).setAdvance(true);
        //迭代循环
        for (int i = 1; i < rawAngleData.size() ; i++) {
           AngleCalculatedData  acd = new AngleCalculatedData(rawAngleData.get(i-1),rawAngleData.get(i));
            tmpListGNSS.add(acd.process());

            totalLeftMileage += acd.getLeftMileage();
            totalRightMileage += acd.getRightMileage();
        }
        System.out.println("Left: " + totalLeftMileage+" Right: "+totalRightMileage);
        return  tmpListGNSS;
    }
    public static double getTotalLeftMileage() {
        return totalLeftMileage;
    }

    public static double getTotalRightMileage() {
        return totalRightMileage;
    }
}

class AngleCalculatedData
{
//    private DriveMode status;//记录汽车行驶状态
    private AngleData preAngleData;
    private AngleData currentAngleData;
    private double leftMileage;//左轮里程
    private double rightMileage;//右轮里程
    private double midMileage;
    private double centralAngle;//圆心角
    private double angleOfOsculation;//弦切角，数值上是此时刻圆心角的一半
    private double leftRadius;  //左轮半径
    private double rightRadius; //右轮半径
    private double midRadius;
    private double leftChordLength;   //左轮弦长
    private double rightChordLength;//右轮弦长
    private double midChordLength;
    private double leftDeltaX;
    private double rightDeltaX;
    private double midDeltaX;
    private double midDeltaY;
    private double leftDeltaY;
    private double rightDeltaY;
    private double actualCentralAngle;


    public  AngleCalculatedData(AngleData pre,AngleData cur)
    {
        preAngleData = pre;
        currentAngleData = cur;
  }
    public GNSSData process()
    {   //弧长必须为正值
        double tmpLeftMileage =(currentAngleData.getLeftAngle() - preAngleData.getLeftAngle()) * Const.WHEEL_CIRCUMFERENCE / 360.0;
        double tmpRightMileage = (currentAngleData.getRightAngle() - preAngleData.getRightAngle()) * Const.WHEEL_CIRCUMFERENCE / 360.0;
        leftMileage = Math.abs(tmpLeftMileage);
        rightMileage = Math.abs(tmpRightMileage);
        midMileage = (leftMileage+rightMileage)/2;

        //以下角度都为弧度制
        actualCentralAngle = (leftMileage - rightMileage)/Const.DISTANCE_BETWEEN_WHEELS ;
        centralAngle = Math.abs(actualCentralAngle) ; //正负号的问题再思考下
        //给centralAngle赋值留作下次使用
        currentAngleData.setCentralAngle(centralAngle);  //标量，无符号
        currentAngleData.setActualCentralAngle(actualCentralAngle);  //矢量，有符号，表示方向，正值顺时针
        //符号为正表示顺时针运动，符号为负为逆时针
        angleOfOsculation = actualCentralAngle / 2;

        if (leftMileage != 0 && rightMileage != 0) {
            //理论上leftRadius和rightRadius的差值的绝对值是车轮的距离
            if (centralAngle != 0.0) {
                leftRadius = leftMileage/centralAngle; //弧度制
                rightRadius = rightMileage/centralAngle;
                midRadius = (leftRadius+rightRadius)/2;
            } else {
                if(leftRadius > rightMileage)
                {
                    leftRadius = Const.INFINITY_RADIUS;
                    rightRadius =Const.INFINITY_RADIUS - Const.DISTANCE_BETWEEN_WHEELS;
                    midRadius = (leftRadius+rightRadius)/2;
                }
                else
                {
                    leftRadius = Const.INFINITY_RADIUS - Const.DISTANCE_BETWEEN_WHEELS;
                    rightRadius =Const.INFINITY_RADIUS;
                    midRadius = (leftRadius+rightRadius)/2;
                }
            }
//            System.out.println("delta radius length: "+ String.valueOf(leftRadius - rightRadius));
            leftChordLength = leftRadius * Math.sqrt(2 - 2 * Math.cos(centralAngle));
            rightChordLength = rightRadius * Math.sqrt(2 - 2 * Math.cos(centralAngle));
            midChordLength = (leftChordLength+rightMileage)/2;

            double curAzi = preAngleData.getAzimuth() + preAngleData.getActualCentralAngle();


            leftDeltaX = leftChordLength * Math.cos(preAngleData.getAzimuth() + angleOfOsculation);
//            leftDeltaY = leftChordLength * Math.sin(preAngleData.getAzimuth() + angleOfOsculation);
//            rightDeltaX = rightChordLength * Math.cos(preAngleData.getAzimuth() + angleOfOsculation);
//            rightDeltaY = rightChordLength * Math.sin(preAngleData.getAzimuth() + angleOfOsculation);
            midDeltaX = midChordLength * Math.cos(preAngleData.getAzimuth() + angleOfOsculation);
            midDeltaY = midChordLength * Math.sin(preAngleData.getAzimuth() + angleOfOsculation);


//            currentAngleData.setLeft_X(preAngleData.getLeft_X() + leftDeltaX);
//            currentAngleData.setLeft_Y(preAngleData.getLeft_Y() + leftDeltaY);
//            currentAngleData.setRight_X(preAngleData.getRight_X() + rightDeltaX);
//            currentAngleData.setRight_Y(preAngleData.getRight_Y() + rightDeltaY);
            currentAngleData.setMid_X(preAngleData.getMid_X() + midDeltaX);
            currentAngleData.setMid_Y(preAngleData.getMid_Y() + midDeltaY);
            //将计算出来的航向角赋值给原始数据作为下次解算需要的数据
            currentAngleData.setAzimuth(curAzi);
        } else {
            //车轮没转动的情况下直接赋值上一个结果
//            currentAngleData.setLeft_X(preAngleData.getLeft_X());
//            currentAngleData.setLeft_Y(preAngleData.getLeft_Y());
//            currentAngleData.setRight_X(preAngleData.getRight_X());
//            currentAngleData.setRight_Y(preAngleData.getRight_Y());
            currentAngleData.setMid_X(preAngleData.getMid_X());
            currentAngleData.setMid_Y(preAngleData.getMid_Y());

            currentAngleData.setAzimuth(preAngleData.getAzimuth());
        }
        return new GNSSData(currentAngleData.getTime(),
                CoordinateTransform.gauss2blh(currentAngleData.getMid_X(),currentAngleData.getMid_Y(),8.888),
                "fitted",42
                );

    }

    public double getLeftMileage() {
        return leftMileage;
    }

    public double getRightMileage() {
        return rightMileage;
    }

    public double getCentralAngle() {
        return centralAngle;
    }

    public double getAngleOfOsculation() {
        return angleOfOsculation;
    }

    public double getLeftDeltaX() {
        return leftDeltaX;
    }

    public double getLeftDeltaY() {
        return leftDeltaY;
    }

    public double getRightDeltaX() {
        return rightDeltaX;
    }

    public double getRightDeltaY() {
        return rightDeltaY;
    }

    public AngleData getCurrentAngleData() {
        return currentAngleData;
    }

    public AngleData getPreAngleData() {
        return preAngleData;
    }

    public double getLeftRadius() {
        return leftRadius;
    }

    public double getRightRadius() {
        return rightRadius;
    }

    public double getActualCentralAngle() {
        return actualCentralAngle;
    }
}
