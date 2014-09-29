package com.company.TimeAndCoordinate;

import com.company.General.Const;

/**
 * Created by mac on 9/17/14.
 */
public class CommonPoint {
    /*******************
     * X,Y为XOY坐标系下的坐标
     * A,B为AO'B坐标系下的坐标
     */
    private double X, Y;
    private double A, B;

    public CommonPoint(double x, double y, double a, double b) {
        X = x;
        Y = y;
        A = a;
        B = b;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    @Override
    public String toString() {
        String str = "target coordinate: "+ Const.decimalFormat.format(X)+" "+ Const.decimalFormat.format(Y)+" "
                +"source target coordinate: "+ Const.decimalFormat.format(A)+" "+ Const.decimalFormat.format(B);
        return str;
    }

    public void setA(double a) {
        A = a;
    }

    public void setB(double b) {
        B = b;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }
}
