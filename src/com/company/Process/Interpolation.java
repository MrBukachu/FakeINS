package com.company.Process;

/**
 * 插值方法
 * 拉格朗日插值
 * Created by mac on 4/15/14.
 */
public class Interpolation {
    /*************************
     * 拉格朗日插值方法
     * @param x      数据源x
     * @param y      数据源y
     * @param inputX 待插值的数
     * @return
     */
    static double LagrangeInterpolation(double[] x,double[] y,double inputX)
    {
        //Define the return type:
        double result = 0;
        //Define some local variables:
        double temp = 1.0;
        double tempResult = 1.0;
        double size = 0;

        //Get the size of the resource data:
        size = x.length;
        if (size != y.length) {
            result = 0;
        }

        //Start interpolation:
        for (int i = 0; i < size; i++) {
            //Calculate one value:
            for (int j = 0; j < size; j++) {
                if (j != i) {
                    temp = (inputX - x[j]) / (x[i] - x[j]);
                    tempResult *= temp;
                }
            }
            tempResult *= y[i];
            result += tempResult;
            //Reset tempResult:
            tempResult = 1.0;
        }

        return result;
    }

    /********************
     * 三次样条插值方法
     * @param x
     * @param y
     * @param inputX
     * @return
     */
    static double  CubicSpline(double[] x,double[] y,double inputX)
    {
       return    0;
    }
}
