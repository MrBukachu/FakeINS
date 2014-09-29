package com.company.General;

import com.company.Amuzith.SingleAntenna;
import com.company.SensorData.AngleData;
import com.company.SensorData.GNSSData;
import com.company.Process.IntegratedData;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 4/15/14.
 */
public class WriteFile {

    /**************************
     * 整合后的数据（2个GNSS天线数据和倾斜仪数据）写到指定文本
     * @param dataBase 整合后的数据
     * @param saveDir  指定文本路径
     */
    static void writeListIntegratedData(List<IntegratedData> dataBase,String saveDir)
    {
         try {
            FileOutputStream fos = new FileOutputStream(saveDir);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            int size = dataBase.size();
            for(int i = 0; i< size;i++)
            {
                //输出形式 ：年 月 日 时 分 秒 GPS周 GPS周秒 B L H X Y 方向角
                bw.write("priAntenna " + Const.decimalFormat.format(dataBase.get(i).getPriGNSSData().getGpst().getTOW()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getPriGNSSData().getGaussCoordinate().getGaussX()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getPriGNSSData().getGaussCoordinate().getGaussY()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getPriGNSSData().getGeodeticCoordinate().getH()) + " " +
                        "secAntenna " + Const.decimalFormat.format(dataBase.get(i).getSecGNSSData().getGpst().getTOW()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getSecGNSSData().getGaussCoordinate().getGaussX()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getSecGNSSData().getGaussCoordinate().getGaussY()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getSecGNSSData().getGeodeticCoordinate().getH()) + " " +
                        "clinometer " +Const.decimalFormat.format(dataBase.get(i).getClinoData().getGpst().getTOW())+" "
                        + Const.decimalFormat.format(dataBase.get(i).getClinoData().getHeelAngle()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getClinoData().getTrimAngle()) + " "
                        + "Azimuth " + Const.decimalFormat.format(dataBase.get(i).getAzimuth()));
                bw.write(Const.strSeparator);
                bw.flush();
            }
            bw.close();
        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***************************
     * GNSS数据写到指定文本
     * @param dataBase  GNSS数据
     * @param saveDir   指定文本路径
     */
    public static void writeGNSSData(List<GNSSData> dataBase,String saveDir)
    {
        try {
            FileOutputStream fos = new FileOutputStream(saveDir);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            int size = dataBase.size();
            for(int i = 0; i< size;i++)
            {
                //输出形式 ：年 月 日 时 分 秒 GPS周 GPS周秒 B L H X Y 方向角
                bw.write("GNSS " + Const.decimalFormat.format(dataBase.get(i).getGpst().getTOW()) + " "
                        + dataBase.get(i).getSolveStyle()+" "
                        + Const.decimalFormat.format(dataBase.get(i).getGaussCoordinate().getGaussX()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getGaussCoordinate().getGaussY()) + " "
                        + Const.decimalFormat.format(dataBase.get(i).getGeodeticCoordinate().getH()));
                bw.write(Const.strSeparator);
                bw.flush();
            }
            bw.close();
        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeSingleAntenna(SingleAntenna singleAntenna,String saveDir)
    {
        try {
            FileOutputStream fos = new FileOutputStream(saveDir);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            int size = singleAntenna.getListSingleAntennaInfo().size();
            for(int i = 0; i< size;i++)
            {
                //输出形式 ：GNSS gps周秒 GaussX GaussY H isStop 周围点数 方向角 参考方向角
                bw.write("GNSS " +
                                Const.decimalFormat.format(singleAntenna.getListSingleAntennaInfo().get(i).getGnssData().getGpst().getTOW()) + " "
                        + Const.decimalFormat.format(singleAntenna.getListSingleAntennaInfo().get(i).getGnssData().getGaussCoordinate().getGaussX()) + " "
                        + Const.decimalFormat.format(singleAntenna.getListSingleAntennaInfo().get(i).getGnssData().getGaussCoordinate().getGaussY()) + " "
                        + Const.decimalFormat.format(singleAntenna.getListSingleAntennaInfo().get(i).getGnssData().getGeodeticCoordinate().getH())
                        + singleAntenna.getListSingleAntennaInfo().get(i).isStop()+" "
                        +singleAntenna.getListSingleAntennaInfo().get(i).getSurroundPoints()+" "
                        +singleAntenna.getListSingleAntennaInfo().get(i).getAzimuth()+" "
                        +singleAntenna.getListSingleAntennaInfo().get(i).getRefAzi()
                );

                bw.write(Const.strSeparator);
                bw.flush();
            }
            bw.close();
        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeAngleData(List<AngleData> listAngleData,String saveDir)
    {
        try {
            FileOutputStream fos = new FileOutputStream(saveDir);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            int size = listAngleData.size();
            for(int i = 0; i< size;i++)
            {
                //输出形式 ：GNSS gps周秒 左轮x 左轮y 右轮x 右轮y
                bw.write(Const.decimalFormat.format(listAngleData.get(i).getTime().getTOW()) + " " +
                                listAngleData.get(i).getTime().displayUTC()+" "+
                                Const.decimalFormat.format(listAngleData.get(i).getLeftAngle()) + " " +
                                Const.decimalFormat.format(listAngleData.get(i).getRightAngle())
                );
                bw.write(Const.strSeparator);
                bw.flush();
            }
            bw.close();
        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  writeXMLFormatGNSSInfo(String saveDir,List<GNSSData> list)
    {
        WriteXML dd=new WriteXML();
        dd.init(list);
        dd.createXml(saveDir);
    }
    public static void SaveDataToFile(String strData, String fileName)
    {
        //Create a stream and save the file:
        //FileStream fs = new FileStream(fileName, FileMode.Create);
        //Create one file if not exist, append if exist:
        BufferedWriter bw;
        File writefile;
        try {
            writefile = new File(fileName);

            // 如果文本文件不存在则创建它
            if (writefile.exists() == false) {
                writefile.createNewFile();
                writefile = new File(fileName); // 重新实例化
            }
            FileWriter fw = new FileWriter(writefile, true);
            bw = new BufferedWriter(fw);
            bw.write(strData);
            bw.write(Const.strSeparator);
            bw.flush();
            bw.close();
        } catch (Exception d) {
            System.out.println(d.getMessage());
        }

    }

}
