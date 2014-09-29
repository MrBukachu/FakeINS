package com.company.TimeAndCoordinate;

import com.company.General.Const;

/**
 * Created by mac on 4/12/14.
 */
public class Coordinate {
    /*******************************
     * 空间直角坐标（X，Y，Z）
     */
    public static class SpatialRectangularCoordinate {
        private double X, Y, Z;
        public SpatialRectangularCoordinate(double x, double y, double z) {
            X = x;
            Y = y;
            Z = z;
        }

        public double getX() {
            return X;
        }

        public double gerY() {
            return Y;
        }

        public double getZ() {
            return Z;
        }
    }

    /***************************
     * 大地坐标（B，L，H）
     */
    public static class GeodeticCoordinate {
        private double B, L, H;

        public GeodeticCoordinate(double b, double l, double h) {
            B = b;
            L = l;
            H = h;
        }

        public double getB() {
            return B;
        }

        public double getL() {
            return L;
        }

        public double getH() {
            return H;
        }

        @Override
        public String toString() {
            String str ="B: "+ Const.decimalFormat.format(B)+" "
                    +"L: "+Const.decimalFormat.format(L)+" "
                    +"H: "+Const.decimalFormat.format(H);
            return str;
        }
    }

    /*****************************
     * 高斯平面坐标（gaussX，gaussY）
     */
    public static class GaussCoordinate {
        private double GaussX, GaussY;

        public GaussCoordinate(double x, double y) {
            GaussX = x;
            GaussY = y;
        }

        public double getGaussX() {
            return GaussX;
        }

        public double getGaussY() {
            return GaussY;
        }

        public void setGaussX(double gaussX) {
            GaussX = gaussX;
        }

        public void setGaussY(double gaussY) {
            GaussY = gaussY;
        }

        @Override
        public String toString() {
            String str ="GaussX: "+ Const.decimalFormat.format(GaussX)+" "
                    +"GaussY: "+Const.decimalFormat.format(GaussY);

            return str;
        }
    }

}
