package com.company.Amuzith;

/**
 * Created by mac on 5/13/14.
 */
public class CalAzimuth {
    /******************
     * 根据轨迹求切线的方法求解方位角，deltaX，deltaY辅助判断方向
     * @param atanValue
     * @param deltaX
     * @param deltaY
     * @return
     */
    public static double getAziValue(double atanValue,double deltaX,double deltaY)
    {
        double aziValue = 0; //角度制
        if (deltaX < 0) {
            aziValue = Math.toDegrees(Math.atan(atanValue) + Math.PI);
        } else {
            if (deltaX > 0) {
                if (deltaY > 0) {
                    aziValue = Math.toDegrees(Math.atan(atanValue));
                } else {
                    aziValue = Math.toDegrees(Math.atan(atanValue) + 2 * Math.PI);
                }
            } else {    //处理deltaX= 0的情况
                if (deltaY > 0) {
                    aziValue = 90;
                } else {
                    aziValue = 270;
                }
            }
        }
        return aziValue;
    }

    /*************************
     * 根据双天线的坐标信息求解方位
     * 需要和直线拟合的数据相结合解算处每次试验的系统差
     * 才能得到最终的正确数值
     * @param deltaX
     * @param deltaY
     * @return
     */
    public static double getAziViaDoubleAntenna(double deltaX,double deltaY)
    {
        double azi = 0;
        if (deltaX < 0) {
            azi = Math.toDegrees(Math.atan(deltaY / deltaX) + Math.PI);
        } else {
            if (deltaX > 0) {
                if (deltaY > 0) {
                    azi = Math.toDegrees(Math.atan(deltaY / deltaX));
                } else {
                    azi = Math.toDegrees(Math.atan(deltaY / deltaX) + 2 * Math.PI);
                }
            } else {    //处理deltaX= 0的情况
                if (deltaY > 0) {
                    azi = 90;
                } else {
                    azi = 270;
                }
            }
        }
        return azi;
    }
}
