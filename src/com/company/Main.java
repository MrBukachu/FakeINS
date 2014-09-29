package com.company;

import Jama.Matrix;

import apple.awt.CPanel;
import com.company.Process.SynTimeGNSSandAngle;

import com.company.Amuzith.SingleAntenna;
import com.company.Amuzith.SingleAntennaInfo;
import com.company.General.ReadRawFile;
import com.company.General.TimeMarker;
import com.company.General.WriteFile;
import com.company.Process.*;
import com.company.SensorData.AngleData;
import com.company.SensorData.GNSSData;
import com.company.TimeAndCoordinate.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        double X = -2831733.77374254;
//        double Y = 4675665.83894635;
//        double Z = 3275369.32528309;
//
//         Coordinate.GeodeticCoordinate CTG =    CoordinateTransform.xyz2blh(X, Y, Z);
//        System.out.println(CTG.getB() + " "
//                + CTG.getL() + " "
//                + CTG.getH()) ;
//        System.out.println(CoordinateTransform.getDMSFormat(CTG.getB()).getDegree() + " "
//                + CoordinateTransform.getDMSFormat(CTG.getB()).getMinute() + " "
//                + CoordinateTransform.getDMSFormat(CTG.getB()).getSecond()
//        );
//        System.out.println(CoordinateTransform.getDMSFormat(CTG.getL()).getDegree() + " "
//                + CoordinateTransform.getDMSFormat(CTG.getL()).getMinute() + " "
//                + CoordinateTransform.getDMSFormat(CTG.getL()).getSecond()
//        );
//
//        double B =  31.099641533167343;
//        double L =  121.20044728166685;
//        double H =  22.043767908588052;
//        Coordinate.GaussCoordinate GC = CoordinateTransform.bl2Gauss(B,L);
//        System.out.println(GC.getGaussX() +" "+GC.getGaussY());
//
//        Coordinate.GeodeticCoordinate geoCoor = CoordinateTransform.gauss2blh(GC.getGaussX(),GC.getGaussY(),H);
//        System.out.println(geoCoor.getB()+" "+geoCoor.getL()+" "+geoCoor.getH());
//
//        Coordinate.GaussCoordinate gaussCoor = CoordinateTransform.bl2Gauss(geoCoor.getB(),geoCoor.getL());
//        System.out.println(gaussCoor.getGaussX() +" "+ gaussCoor.getGaussY());



//
//        Coordinate.SpatialRectangularCoordinate SRC = CoordinateTransform.blh2xyz(B,L,H);
//        System.out.println(SRC.getX()+" "+SRC.gerY()+" "+SRC.getZ());
//
//        Time.GC t = new Time.GC(2013,8,15,18,8,0);
//        Time.JD juliaDay = TimeTransform.GC2JD(t);
//        System.out.println(juliaDay.getJulianDay());
//        Time.GPST gpst = TimeTransform.GC2GPST(t);
//        System.out.println(gpst.getWN()+" "+gpst.getTOW()+" "+gpst.getWD());


//        System.out.println("test over! ");
     //test file file
//        String saveIntegratedDataDir = "/Users/mac/Desktop/FakeINS/1212/integratedData.txt" ;
//        String saveTestDataDir = "/Users/mac/Desktop/FakeINS/1212/TestData.txt" ;
//
//        String ClinomoterFilePath ="/Users/mac/Desktop/FakeINS/1212/at203_as_2013121210504.txt";
//        List<ClinometerData> listClinoData = ReadRawFile.getClinometerData(ClinomoterFilePath);
//        System.out.println("read clinometerData! ");
//
//        String GNSSfileSecPath = "/Users/mac/Desktop/FakeINS/1212/1212_combined_sec_part.txt";
//        List<GNSSData> listSecGNSSData = ReadRawFile.getGNSSData(GNSSfileSecPath,2013,12,12);
//        System.out.println("read GNSS sec data! ");
//
//        String GNSSfilePriPath = "/Users/mac/Desktop/FakeINS/1212/1212_combined_dynamic_part.txt";
//        List<GNSSData> listPriGNSSData = ReadRawFile.getGNSSData(GNSSfilePriPath,2013,12,12);
//        System.out.println("read GNSS  pri data! ");
//
//        List<Double> timeMarker = new ArrayList<Double>();
//        int size = listPriGNSSData.size();
//        for(int i = 10; i <size -10;i++)
//        {
//            timeMarker.add(listPriGNSSData.get(i).getGpst().getTOW());
//        }
//        List<GNSSData> listSynPriGNSSData = TimeSynchronization.timeSynGNSSData(listPriGNSSData,timeMarker);
//        List<GNSSData> listSynSecGNSSData = TimeSynchronization.timeSynGNSSData(listSecGNSSData,timeMarker);
//        List<ClinometerData> listSynCLinoData = TimeSynchronization.timeSynClinoData(listClinoData,timeMarker);
//        if(listSynCLinoData.size() == listSynPriGNSSData.size() && listSynCLinoData.size() == listSynSecGNSSData.size())
//        {
//            System.out.println("synchronized data are in same size! ");
//        }
//        else
//        {
//            System.out.println("synchronization failed");
//        }
//        System.out.println("lalalla");
//
//        int synDataSize = listSynCLinoData.size();
//        List<IntegratedData> listIntegratedData = new ArrayList<IntegratedData>();
//        for(int j = 0;j< synDataSize;j++)
//        {
//            listIntegratedData.add(new IntegratedData(listSynPriGNSSData.get(j),
//                    listSynSecGNSSData.get(j),
//                    listSynCLinoData.get(j)));
//        }
//        WriteFile.writeListIntegratedData(listIntegratedData,saveIntegratedDataDir);
//
//
//        WriteFile.writeGNSSData(Test.process(listIntegratedData),saveTestDataDir);
     //test lagrangeInterpolation
//        double []x = {1,2,3,4,5,6,7,8};
//        double []y = {1,4,9,16,25,36,49,64};
//        System.out.println(Interpolation.LagrangeInterpolation(x,y,5.5));


        String GNSSfileSecPath = "/Users/mac/百度云同步盘/nav data/pirALL.txt";
//        List<GNSSData> listSecGNSSData = ReadRawFile.getGNSSData(GNSSfileSecPath, 2014, 1, 20, ReadRawFile.FIX);
//        WriteFile.writeGNSSData(listSecGNSSData, "/Users/mac/百度云同步盘/nav data/pirXYH_Fix.txt");
//        System.out.println("read GNSS sec data! ");

        String GNSSfilePriPath ="/Users/mac/百度云同步盘/nav data/secALL.txt";
        String AngleFilePath = "/Users/mac/Desktop/I WANT GRADUATION/AngleData/TwoAngleSensor201499154538.txt";
//        String tmpPath = "/Users/mac/Desktop/I WANT GRADUATION/AngleData/tmpStraightRoad";


        /************
         * 读GPGGA数据
         */
//        String GPGGAFilePath = "/Users/mac/Desktop/I WANT GRADUATION/expr/HiTarget2014917static_floor.txt";
        String GPGGAFilePathInCollege = "/Users/mac/Desktop/I WANT GRADUATION/AngleData/140909EXPE_School.Raw";
//        List<GNSSData> listGNSSData = ReadRawFile.getGPGGAData(GPGGAFilePath,1809,2);
//        WriteFile.writeGNSSData(listGNSSData,"/Users/mac/Desktop/I WANT GRADUATION/expr/HiTarget2014917static_floor_statistic.txt");
        List<GNSSData> listGNSSDataInCollege = ReadRawFile.getGPGGAData(GPGGAFilePathInCollege,1809,2);
        List<GNSSData> tmpGNSSData = Extraction.getListGNSS(listGNSSDataInCollege,1410,1748);
        /************
         * 读角度传感器数据
         */
        List<AngleData> listAngleData = ReadRawFile.getAngleData(AngleFilePath);
        System.out.println("read angle data !");
        List<AngleData> tmpAngleList = Extraction.getListAngleData(listAngleData,4650,5000);//big enough to do Lagrange interpolation


//        WriteFile.writeXMLFormatGNSSInfo("/Users/mac/Desktop/I WANT GRADUATION/AngleData/GNSSInCollege.XML",tmp);

//        Coordinate.GeodeticCoordinate gc = CoordinateTransform.gauss2blh(3462463.8445814606, 547564.9362703465, 0);
//        System.out.println(gc.getB() + " "+gc.getL()+ " "+gc.getH());

        /******************
         * GNSS数据和角度传感器数据进行融合
         * 先插值，得到时间同步之后的数据
         */

        SynTimeGNSSandAngle st = new SynTimeGNSSandAngle(tmpAngleList,tmpGNSSData);
        st.process();
        System.out.println("haha,data is synchronized! ");
        List<AngleData> listSynedAngleData = st.getListSynedAngleData();
        List<GNSSData> listSynedGNSSData = st.getListSynedGNSSData();
        WriteFile.writeGNSSData(listSynedGNSSData,"/Users/mac/Desktop/I WANT GRADUATION/expr/gaussCorFromGPGGA0909.txt");

        List<GNSSData> tmpIns = getPositionByAngleOnly.process(listSynedAngleData,listSynedGNSSData.get(0));
        System.out.println("data process finished !");
        WriteFile.writeGNSSData(tmpIns,"/Users/mac/Desktop/I WANT GRADUATION/expr/insResult0909.txt");

        CommonPoint p1 = new CommonPoint(listSynedGNSSData.get(1).getGaussCoordinate().getGaussX(),
                listSynedGNSSData.get(1).getGaussCoordinate().getGaussY(),
                tmpIns.get(0).getGaussCoordinate().getGaussX(), tmpIns.get(0).getGaussCoordinate().getGaussY());
//        CommonPoint p3 = new CommonPoint(listSynedGNSSData.get(2).getGaussCoordinate().getGaussX(),
//                listSynedGNSSData.get(2).getGaussCoordinate().getGaussY(),
//                tmpIns.get(1).getGaussCoordinate().getGaussX(), tmpIns.get(1).getGaussCoordinate().getGaussY());
        int synSize = listSynedGNSSData.size();
        CommonPoint p2 = new CommonPoint(listSynedGNSSData.get(synSize - 1).getGaussCoordinate().getGaussX(),
                listSynedGNSSData.get(synSize - 1).getGaussCoordinate().getGaussY(),
                tmpIns.get(synSize - 2).getGaussCoordinate().getGaussX(), tmpIns.get(synSize - 2).getGaussCoordinate().getGaussY());
        System.out.println(p1);
//        System.out.println(p3);
        System.out.println(p2);
        ArrayList<CommonPoint> listCP = new ArrayList<CommonPoint>();
        listCP.add(p1);
        listCP.add(p2);

        List<GNSSData> resultRectified = RectifyByPara4.process(tmpIns,listCP);
        WriteFile.writeGNSSData(resultRectified,"/Users/mac/Desktop/I WANT GRADUATION/expr/RectifiedSourceDataCaledByMyself1.txt");

        /******************
         * 测试矩阵类
         * http://math.nist.gov/javanumerics/jama/doc/
         * 使用Jama包：是一个非常好用的java的线性代数软件包。适用于日常编程可能碰到的各种矩阵运算问题。
         */

        /*******************
         * 测试四参数的求解
         */
        /****************
         * 测试坐标转换中的精度损失
         */
//        Coordinate.GeodeticCoordinate gc = new Coordinate.GeodeticCoordinate(31.41512,121.54632,20.24151);
//        Coordinate.GaussCoordinate gaussC = CoordinateTransform.bl2Gauss(gc.getB(),gc.getL());
//        Coordinate.GeodeticCoordinate gc1 = CoordinateTransform.gauss2blh(gaussC.getGaussX(),gaussC.getGaussY(),20);
//        System.out.println(gc);
//        System.out.println(gc1);
//        List<GNSSData> resultRectified = RectifyByPara4.process(tmpTest,para4);

//        System.out.println(para4);
//        double [][]a = {{1.1},{2.3}};
//        Matrix aM = new Matrix(a);
//        aM.print(2,2);
//        double [][]b = {{1.1,2.1}};
//        Matrix bM = new Matrix(b);
//        bM.print(2,2);
//        Matrix m =aM.times(bM);
//        m.print(2,2);



//        WriteFile.writeXMLFormatGNSSInfo("/Users/mac/Desktop/I WANT GRADUATION/AngleData/GNSS.XML",listGNSSData);
//        WriteFile.writeXMLFormatGNSSInfo("/Users/mac/Desktop/I WANT GRADUATION/AngleData/GNSSInCollege.XML",listGNSSDataInCollege);
//        WriteFile.writeGNSSData(listGNSSDataInCollege,"/Users/mac/Desktop/I WANT GRADUATION/AngleData/GNSSinfo.txt");

//        List<GNSSData> listPriGNSSData = ReadRawFile.getGNSSData(GNSSfilePriPath,2014,1,20,ReadRawFile.FIX);
//        WriteFile.writeGNSSData(listPriGNSSData,"/Users/mac/百度云同步盘/nav data/secXYH_Fix.txt");
//        System.out.println("read GNSS  pri data! ");

//        Extraction.getExtractedData(GNSSfileSecPath, "/Users/mac/百度云同步盘/nav data/secExtracted.txt",
//                108306.2, 108925.9, 2014, 1, 20);
//        Extraction.getExtractedData(GNSSfilePriPath,"/Users/mac/百度云同步盘/nav data/priExtracted.txt",
//                108306.2,108925.9,2014,1,20);
//        List<Double> timeMarker = TimeMarker.getTimeMarkerByRules(108830.2,108870.1,0.1);
//        List<GNSSData> listSecGNSSData = ReadRawFile.getGNSSData("/Users/mac/百度云同步盘/nav data/secExtracted.txt", 2014, 1, 20, ReadRawFile.FIX);
//        List<GNSSData> listPriGNSSData = ReadRawFile.getGNSSData("/Users/mac/百度云同步盘/nav data/priExtracted.txt", 2014, 1, 20, ReadRawFile.FIX);
//
//        List<GNSSData> listSynPriGNSSData = TimeSynchronization.timeSynGNSSData(listPriGNSSData,timeMarker);
//        List<GNSSData> listSynSecGNSSData = TimeSynchronization.timeSynGNSSData(listSecGNSSData,timeMarker);
//
//        SingleAntenna singleAntennaPri = new SingleAntenna(listSynPriGNSSData);
//        SingleAntenna singleAntennaSec = new SingleAntenna(listSynSecGNSSData);
//
//        WriteFile.writeSingleAntenna(singleAntennaPri, "/Users/mac/百度云同步盘/nav data/priSingleAntenna.txt");
//        WriteFile.writeSingleAntenna(singleAntennaSec, "/Users/mac/百度云同步盘/nav data/secSingleAntenna.txt");
    }
}
