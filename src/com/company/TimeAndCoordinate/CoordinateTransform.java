package com.company.TimeAndCoordinate;

import com.company.TimeAndCoordinate.Coordinate;

/**
 * Created by mac on 4/11/14.
 */
public class CoordinateTransform {
    //此处常量该使用大写，但是常量太多，为保持和书本一致，按照书本的写法来写
    private final static double a = 6378137.000;//WGS84
    private final static double b = 6356752.3142;
    private final static double eSqr = 1 - Math.pow(b, 2) / (Math.pow(a, 2));
    private final static double e = Math.sqrt(eSqr);
    private final static double e2Sqr = (Math.pow(a, 2)-Math.pow(b,2))/Math.pow(b, 2);
    private final static double DeltaY = 500000;
    private final static double L0 = 121.0 ;//角度制

    /*******************
     * 将空间直角坐标转换成大地坐标（WGS84）
     * @param X
     * @param Y
     * @param Z
     * @return 大地坐标对象
     */
    public static Coordinate.GeodeticCoordinate xyz2blh(double X,double Y,double Z)
    {

        double B1 = 0;
        double B1tmp =Z/Math.sqrt(Math.pow(X,2)+Math.pow(Y,2));
        if(B1tmp >= 0 )
        {
            B1 = Math.atan(B1tmp);
        }
        else
        {
            B1 = Math.atan(B1tmp)+Math.PI;
        }
        double B0 = 0;
        double W =  Math.sqrt(1 - eSqr * Math.pow(Math.sin(B1), 2));
        double N0 = a / W;

        while (Math.abs(B0 - B1) > 0.0000000001) {
            B0 = B1;
            double tmp = (Z + N0 * eSqr * Math.sin(B0)) / Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
            B1 = Math.atan(tmp);
            if (B1 < 0) {
                B1 += Math.PI;
            }
            W = Math.sqrt(1 - eSqr * Math.pow(Math.sin(B1), 2));
            N0 = a / W;
        }
        B1 = B1 * 180 / Math.PI;   //B1即为精度


        double L = Math.atan(Y / X);
        if (L < 0) {
            L += Math.PI;
        }
        L *= 180 / Math.PI;
        double H = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2)) / Math.cos(Math.toRadians(B1)) - N0;

        return new Coordinate.GeodeticCoordinate(B1,L,H);
    }

    /********************
     * 将大地坐标转换成空间直角坐标
     * @param B
     * @param L
     * @param H
     * @return  空间直角坐标对象
     */
    public static Coordinate.SpatialRectangularCoordinate blh2xyz(double B,double L,double H)
    {
        double W = Math.sqrt(1 - eSqr * Math.pow(Math.sin(Math.toRadians(B)), 2));
        double N = a / W;

        double X = (N+H) * Math.cos(Math.toRadians(B)) * Math.cos(Math.toRadians(L));
        double Y = (N+H) * Math.cos(Math.toRadians(B)) * Math.sin(Math.toRadians(L));
        double Z = (N*(1-eSqr)+H)*Math.sin(Math.toRadians(B));

        return new Coordinate.SpatialRectangularCoordinate(X,Y,Z);
    }

    /**********************
     * 将大地坐标转换成高斯平面坐标
     * 即高斯投影正算
     * @param B
     * @param L
     * @return   高斯平面坐标对象
     */
    public static Coordinate.GaussCoordinate bl2Gauss(double B,double L)
    {
        double W = Math.sqrt(1 - eSqr * Math.pow(Math.sin(Math.toRadians(B)), 2));
        double N = a / W;
        // double l = (L - L0)*Math.PI/180;//l以弧度为单位
        double l = Math.toRadians(L - L0);
        double t = Math.tan(Math.toRadians(B));
        double η = Math.sqrt(e2Sqr*Math.pow(Math.cos(Math.toRadians(B)), 2));
        double cosB = Math.cos(Math.toRadians(B));

        //X是赤道沿子午线弧长，不是xyz的x坐标
        //double m = N * Math.cos(Math.toRadians(B)) * Math.cos(Math.toRadians(L));

        double A0 = 1 + 3 * eSqr / 4 + 45 * Math.pow(eSqr, 2) / 64 + 350 * Math.pow(eSqr, 3) / 512 + 11025 * Math.pow(eSqr, 4) / 16384;
        double A2 = -(3 *eSqr / 4 + 60 * Math.pow(eSqr, 2) / 64 + 525 * Math.pow(eSqr, 3) / 512 + 17640 * Math.pow(eSqr, 4) / 16384) / 2;
        double A4 = (15 * Math.pow(eSqr, 2) / 64 + 210 * Math.pow(eSqr, 3) / 512 + 8820 * Math.pow(eSqr, 4) / 16384) / 4;
        double A6 = -(35 * Math.pow(eSqr, 3) / 512 + 2520 * Math.pow(eSqr, 4) / 16384) / 6;
        double A8 = (315 * Math.pow(eSqr, 4) / 16384) / 8;
        //m是赤道沿子午线弧长
        double m = a * (1 - eSqr) * (A0 * Math.toRadians(B) + A2 * Math.sin( Math.toRadians(2*B)) + A4 * Math.sin( Math.toRadians(4*B)) + A6 * Math.sin( Math.toRadians(6*B)) + A8 * Math.sin( Math.toRadians(8*B)));

        double GaussX = m + N*t*cosB*cosB*l*l*(
                0.5 + (5-t*t+ 9*η*η + 4*η*η*η*η)*l*l*cosB*cosB/24
                        +  cosB*cosB*cosB*cosB*l*l*l*l*(61 -58 * t*t + t*t* t*t)/720) ;
        double GaussY = N * cosB * l * (1+
                Math.pow(cosB,2) * Math.pow(l, 2)*(1 - Math.pow(t, 2)+Math.pow(η, 2))/6
                +Math.pow(cosB,4)*Math.pow(l, 4)*(5 - 18*t*t + Math.pow(t,4) +14*η*η -58*t*t*η*η)/120)
                +DeltaY;
        return  new Coordinate.GaussCoordinate(GaussX,GaussY);
    }

    /***********************
     * 将高斯平面坐标转换成大地坐标
     * 即高斯投影反算
     * @param X
     * @param Y
     * @param H
     * @return 大地坐标对象
     */
    public static Coordinate.GeodeticCoordinate gauss2blh(double X,double Y,double H)
     {
        double Y1 = Y -DeltaY;
        double m0 = a * (1 - eSqr);
        double m2 = (double) 3 / 2 * eSqr * m0;
        double m4 = (double) 5 / 4 * eSqr * m2;
        double m6 = (double) 7 / 6 * eSqr * m4;
        double m8 = (double) 9 / 8 * eSqr * m6;
        double a0, a2, a4, a6, a8;
        a0 = m0 + 1.0 / 2.0 * m2 + 3.0 / 8.0 * m4 + 5.0 / 16.0 * m6 + 35.0
                / 128.0 * m8;
        a2 = 1.0 / 2.0 * m2 + 1.0 / 2.0 * m4 + 15.0 / 32.0 * m6 + 7.0 / 16.0
                * m8;
        a4 = 1.0 / 8.0 * m4 + 3.0 / 16.0 * m6 + 7.0 / 32.0 * m8;
        a6 = 1.0 / 32.0 * m6 + 1.0 / 16.0 * m8;
        a8 = 1.0 / 128.0 * m8;
        double B1;
        double Bf = X / a0;
        do {
            B1 = Bf;
            Bf = (X + 1.0 / 2.0 * a2 * Math.sin(2 * B1) - 1.0 / 4.0 * a4
                    * Math.sin(4 * B1) + 1.0 / 6.0 * a6 * Math.sin(6 * B1))
                    / a0;

        } while (Math.abs(B1 - Bf) > 0.00000000001);
        double tf = Math.tan(Bf);
        double it2 = e2Sqr * Math.cos(Bf);
        double Nf = a / Math.sqrt(1 - eSqr * Math.sin(Bf)
                * Math.sin(Bf));
        double Mf = Nf
                / (1 + e2Sqr * Math.cos(Bf) * Math.cos(Bf));

         double B1f = Bf - tf * Math.pow(Y1, 2) / (2 * Mf * Nf) *
                 (1 - (5 + 3 * tf * tf + it2 - 9 * it2 * tf * tf) * Y1 * Y1 / (12 * Nf * Nf) +
                         (61 + 90 * tf * tf + 45 * tf * tf * tf * tf) * Math.pow(Y1, 4) / (Math.pow(Nf, 4) * 360));
         double l = Y1
                 / (Nf * Math.cos(Bf))
                 - (1.0 / 6.0)
                * (1 + 2 * tf * tf + it2)
                * Math.pow(Y1, 3)
                / (Math.pow(Nf, 3) * Math.cos(Bf))
                + (1.0 / 120.0)
                * (5 + 28 * tf * tf + 24 * tf * tf * tf * tf + 6 * it2 + 8
                * it2 * tf * tf) * Math.pow(Y1, 5)
                / (Math.pow(Nf, 5) * Math.cos(Bf));
        l = l * 180 / Math.PI;
        double L = l + L0;
        double B = B1f * 180 / Math.PI;

         return new Coordinate.GeodeticCoordinate(B,L,H);
    }

    /***********************
     * 将角度从度分秒形式转换成°形式
     * @param degree
     * @param minute
     * @param second
     * @return
     */
    public static DegreeFormat getDegreeFormat(int degree,int minute,double second){return new DegreeFormat(degree, minute, second);}

    /*************************
     * 将角度从°形式转换成度分秒形式
     * @param input
     * @return
     */
    static  DMSFormat getDMSFormat(double input){return new DMSFormat(input);}


    public static class DMSFormat
    {
        private int degree,minute;
        private double second;
        DMSFormat(double input)
        {
            degree = (int) Math.floor(input);
            minute = (int) Math.floor((input - degree) * 60);
            second = (input - (double) degree - (double) minute / 60) * 3600;
        }
        public double getSecond(){return second;}
        public int getDegree(){return degree;}
        public int getMinute(){return minute;}
    }

    public static class DegreeFormat
    {

        private double out;
        DegreeFormat(int degree,int minute,double second)
        {
            out =  degree +(double) minute / 60+ (double) second / 3600;
        }
        public double getOut(){return out;}

    }

}
