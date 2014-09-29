package com.company.Amuzith;

import java.util.ArrayList;
import java.util.List;

/**
 * 三次样条插值方法及其求导数
 * Created by mac on 5/12/14.
 */
public class CubicSpline {
    private List<Double> listResult = new ArrayList<Double>();
    private List<Double> listDerivative = new ArrayList<Double>();
    private double result ;
    private double derivative;
    private double inputX;
    private double Azi;//切线处的方位角

    /*以下为三次插值所需要的参数
    * */
    double h[],λ[],µ[],d[],M[];

    double x[],y[];	//插值节点、对应的节点值
    int n;
    //----------------追赶法法中需要的参数-----------
    double b[];//系数矩阵组成b,
    //系数
    double r[],a1[],b1[];
    //求解
    double x0[],y0[];
    public CubicSpline(double[] x,double[] y,double inputX)
    {
        this.x=x;
        this.y=y;
        this.inputX = inputX;

        if(x.length == y.length)
        {
            this.n = x.length ;
        }
        else
        {
            System.out.println("x和y数量不一致！");
        }
        process();
//        derivative = getDerivative();

    }
    public CubicSpline(double []x,double []y)
    {
        this.x=x;
        this.y=y;
        if(x.length == y.length)
        {
            this.n = x.length ;
        }
        else
        {
            System.out.println("x和y数量不一致！");
        }
        process();
//        derivative =getDerivative();
    }
    //追赶法
    public double[] CatchUp(double a[],double c[],double d[])
    {
        //此追赶法只适用三次样条插值，不适用其他时候
        //Ax=d;d方程右边的矩阵,A为系数矩阵
        //a系数矩阵组成a,c系数矩阵组成c,n节点数
        b=new double[n];//系数矩阵组成b,
//        System.out.println(a.length+"   "+c.length);
        //系数
        r=new double[n];
        a1=new double[n];
        b1=new double[n];
        //求解
        x0=new double[n];
        y0=new double[n];
        //赋值a/b/c
        for(int i=0;i<n;i++)
        {
            b[i]=2;
        }

        //三角分解
        for(int i=0;i<n-2;i++)
        {
            r[i]=a[i];

        }
        a1[0]=b[0];
        b1[0]=c[0]/a1[0];
        for(int i=1;i<n-2;i++)
        {
            a1[i]=b[i]-r[i]*b1[i-1];
            b1[i]=c[i]/a1[i];
        }
        //求解方程Ax=b;
        //1.Ly=b;
        y0[0]=d[0]/a1[0];
        for(int i=1;i<n-2;i++)
        {
            y0[i]=(d[i]-r[i]*y0[i-1])/a1[i];
//            System.out.println("M的计算中出现问题了吗？?? "+y0[i]);
        }
        //2.Ux=y;
        x0[n-2]=y0[n-2];
        for (int i=n-3;i>=0;i--)
        {
            x0[i]=y0[i]-b1[i]*x0[i+1];
        }
        return x0;
    }
    //初始化h,u,q
    public void process( )
    { //x，y,n分别为节点、节点对应的函数值、节点数
        h=new double[n-1];   //相邻两个x的间隔
        λ=new double[n-2];    //一共n-2个内节点
        µ=new double[n-2];
        d=new double[n];
        //x为节点，y为对应节点x的节点值,n节点长度
        //AM=G
        for(int i=0;i<n-1;i++)
        {
            h[i]=x[i+1]-x[i];
        }
        for(int i=0;i<n-2;i++)
        {
            λ[i]=h[i]/(h[i] + h[i+1]);
        }
        for(int i=0;i<n-2;i++)
        {
            µ[i]=1-λ[i];
        }
        double y0=(y[1]-y[0])/(x[1]-x[0]);//为y[0]处的导数
        double y3=(y[n-1]-y[n-2])/(x[n-1]-x[n-2]); //为y[n-1]处的导数
        //第一类边界条件

        d[0]=6/h[0]*((y[1]-y[0])/h[0]-y0);
        for(int i=1;i<n-1;i++)
        {
            d[i]=6/(h[i-1]+h[i])*((y[i+1]-y[i])/h[i]-((y[i]-y[i-1])/h[i-1]));
        }
        d[n-1]=6/h[n-2]*(y3-(y[n-1]-y[n-2])/h[n-2]);
        M=CatchUp(λ,µ,d);//调用追赶法计算M


    }
    //通过插值函数S计算对应yy中的节点的节点值
    public List<Double> getListResult(double []yy)
    {
        for(int f=0;f<yy.length;f++)
        {
            for(int i=0;i<n-1;i++)
            {
                if(yy[f]>x[i]&&yy[f]<x[i+1])    //判断区间
                {
                    listResult.add(Math.pow((x[i+1]-yy[f]),3)*M[i]/(6*h[i])
                            +Math.pow((yy[f]-x[i]),3)*M[i+1]/(6*h[i])
                            + (x[i+1]-yy[f])*(y[i]-h[i]*h[i]*M[i]/6)/h[i]
                            + (yy[f]-x[i])*(y[i+1]-h[i]*h[i]*M[i+1]/6)/h[i]);
                    //System.out.println("S[" +f+"]="+S);
                }
                else
                if(yy[f]==x[i])
                {

                }
                else
                if(yy[f]==x[i+1])
                {
                    listResult.add(y[i+1]);

                }
            }
        }
        return listResult;
    }
    public List<Double> getListDerivative(double[] yy)
    {
        for(int f=0;f<yy.length;f++)
        {
            for(int i=0;i<n-1;i++)
            {
                if(yy[f] >= x[i] && yy[f]<x[i+1] )    //判断区间
                {
                    double Dr =  (y[i+1]-y[i])/h[i+1]
                            - h[i+1]*(M[i+1]-M[i])/6
                            + M[i+1]*Math.pow(yy[f]-x[i],2)/(2*h[i+1])
                            - M[i]*Math.pow(x[i+1]-yy[f],2)/(2*h[i+1]);
                    listDerivative.add(Math.toDegrees(Math.atan(Dr)));

                    //System.out.println("S[" +f+"]="+S);
                }

            }
        }

        return listDerivative;
    }

    public double getResult() {
        for (int i = 0; i < n -1 ; i++) {
            if (inputX >= x[i] && inputX < x[i + 1])    //判断区间
            {
                result = Math.pow((x[i + 1] - inputX), 3) * M[i] / (6 * h[i])
                        + Math.pow((inputX - x[i]), 3) * M[i + 1] / (6 * h[i])
                        + (x[i + 1] - inputX) * (y[i] - h[i] * h[i] * M[i] / 6) / h[i]
                        + (inputX - x[i]) * (y[i + 1] - h[i] * h[i] * M[i + 1] / 6) / h[i];

            }

        }
        return result;

    }
    public double getDerivative()
    {  //仅适用于x[]是单调递增的数列
        for (int i = 0; i < n-1 ; i++) {
            if (inputX >= x[i] && inputX < x[i + 1])    //判断区间
            {
                derivative = (y[i + 1] - y[i]) / h[i + 1]
                        - h[i + 1] * (M[i + 1] - M[i]) / 6
                        + M[i + 1] * Math.pow(inputX - x[i], 2) / (2 * h[i + 1])
                        - M[i] * Math.pow(x[i + 1] - inputX, 2) / (2 * h[i + 1]);

            }

        }
        return derivative;
    }
}
