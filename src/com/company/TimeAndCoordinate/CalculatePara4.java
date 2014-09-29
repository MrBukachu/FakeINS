package com.company.TimeAndCoordinate;

import Jama.Matrix;

import java.util.List;

/**
 * Created by mac on 9/17/14.
 */
public class CalculatePara4 {

    public static Para4 process(List<CommonPoint> listCP)
    {
        int size = listCP.size();
        for(int i = 0;i < size;i++){
            listCP.get(i).setA(listCP.get(i).getA() );
            listCP.get(i).setB(listCP.get(i).getB() );
            listCP.get(i).setX(listCP.get(i).getX() );
            listCP.get(i).setY(listCP.get(i).getY() );
        }
         if(size < 2)
         {
             System.out.println("Given data is not enough to calculate 4 para！ ");
             return null;
         }
        else
         {   // error Equation Coefficient construction
             Matrix errorEquationCoefficient = new Matrix( 2 * size , 4 );
             setMartrixZeros(errorEquationCoefficient);
             for (int i = 0; i < size; i++) {
                 errorEquationCoefficient.set(2 * i, 0, 1.0);
                 errorEquationCoefficient.set(2 * i, 2, listCP.get(i).getA());
                 errorEquationCoefficient.set(2 * i, 3, listCP.get(i).getB() * (-1));
                 errorEquationCoefficient.set(2 * i + 1, 1, 1.0);
                 errorEquationCoefficient.set(2 * i + 1, 2, listCP.get(i).getB());
                 errorEquationCoefficient.set(2 * i + 1, 3, listCP.get(i).getA());
             }
             errorEquationCoefficient.print(2,3);
             //constant Coefficient construction
             Matrix constantCoefficient = new Matrix( 2 * size, 1 );
             setMartrixZeros(constantCoefficient);
             for (int i = 0; i < size; i++) {
                 constantCoefficient.set(2 * i, 0, listCP.get(i).getX());
                 constantCoefficient.set(2 * i + 1, 0, listCP.get(i).getY());
             }
            constantCoefficient.print(2,3);
             //power matrix construction
             Matrix power = new Matrix(2 * size,2 * size);
             setMartrixZeros(power);
             for (int i = 0; i < 2 * size; i++) {
                  power.set(i,i,1.0);
             }
             power.print(2,3);
//             Matrix result = new Matrix(4,1);
             Matrix result = (errorEquationCoefficient.transpose(). times(power).times(errorEquationCoefficient)).inverse()
                     .times(errorEquationCoefficient.transpose()). times(power).times(constantCoefficient);
             result.print(2,3);
             return new Para4(result.get(0,0),result.get(1,0),result.get(2,0),result.get(3,0));
         }

    }
    public static void setMartrixZeros(Matrix m){
        int row = m.getRowDimension();
        int col = m.getColumnDimension();
        for (int i = 0; i <row ; i++) {
            for (int j = 0; j < col; j++) {
                    m.set(i,j,0.0);
            }

        }
    }
}
