package com.company.General;

import java.text.DecimalFormat;

/**
 * Created by mac on 8/7/14.
 */
public class Const {
    //define some constant here may be used in many places
    //to avoid repetitive definition
    public final  static String strSeparator = System.getProperty("line.separator");
    public final static DecimalFormat decimalFormat =new DecimalFormat("0.000");
    //汽车相关参数
    public static final double WHEEL_CIRCUMFERENCE= 1.852; //车轮直径，单位米
    public static final double DISTANCE_BETWEEN_WHEELS = 1.422; //两车轮距离，单位米
    public static final double INFINITY_RADIUS = 99999.9;
}
