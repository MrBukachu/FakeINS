package com.company.TimeAndCoordinate;

import java.util.ArrayList;

/**
 * Created by mac on 9/16/14.
 */
public class Para4 {
    /******************
     * para4为将AO'B坐标系下的坐标转到XOY坐标系下的四参数坐标转换参数
     * X = X0 + µ * cos å * A - µ * sin å * B
     * Y = Y0 + µ * sin å * A + µ * cos å * B
     */
    private double deltaX,deltaY,µCoså,µSinå;

    public Para4(double x,double y,double µcoså,double µsinå){
        deltaX = x;deltaY = y; µCoså = µcoså;µSinå= µsinå;
    }
    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getµCoså() {
        return µCoså;
    }

    public double getµSinå() {
        return µSinå;
    }

    @Override
    public String toString() {
        return  "deltaX: " + deltaX + " deltaY: "+deltaY +
               " µSinå: " + µSinå +" µCoså: " + µCoså;
    }
}
