package com.company.General;

import java.util.List;

/**
 * Created by mac on 4/15/14.
 */
public class List2Array {
    private static double[] Array;

    /*********************
     * 将list类型转换为数组类型
     * @param list
     * @return
     */
    public static double[] trans(List<Double> list)
    {
        int size = list.size();
        Array = new double[size];
        for (int i = 0; i < size; i++) {
            Array[i] = list.get(i);
        }
        return Array;
    }
}
