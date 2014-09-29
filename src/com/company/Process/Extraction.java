package com.company.Process;

import com.company.General.Const;
import com.company.General.ReadRawFile;
import com.company.SensorData.AngleData;
import com.company.SensorData.GNSSData;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 5/12/14.
 */
public class Extraction {

    /**************************
     * 针对思南处理文本提取连续数据
     * 需要人为输入年月日
     * @param rawFilePath  NavCom file path
     * @param saveDir      the directory to save extracted data
     * @param startEpoch   the first epoch
     * @param endEpoch     the last epoch
     * @param year         year
     * @param month        month
     * @param day          day
     */
    public static void getExtractedData(String rawFilePath,String saveDir,double startEpoch,double endEpoch,int year,int month,int day)
    {
        try {
            FileInputStream fis = new FileInputStream(rawFilePath);
            InputStreamReader isw = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isw);

            FileOutputStream fos = new FileOutputStream(saveDir);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            String str;
            while ((str = br.readLine()) != null) {
                double epoch = ReadRawFile.readGNSSDataOneLine(str, year, month, day).getGpst().getTOW();
                if ((epoch >= startEpoch)&&(epoch <= endEpoch)) {
                    bw.write(str);
                    bw.write(Const.strSeparator);
                    bw.flush();
                }
            }

        } catch (FileNotFoundException f) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<GNSSData> getListGNSS(List<GNSSData> dataBase,int startIndex,int endIndex){
        List<GNSSData> tmp = new ArrayList<GNSSData>();
        for(int i = startIndex;i<endIndex;i++)
        {
            tmp.add(dataBase.get(i));
        }
        return tmp;
    }
    //可以使用模板来和上面的代码实现重用
    public static List<AngleData> getListAngleData(List<AngleData> dataBase,int startIndex,int endIndex){
        List<AngleData> tmp = new ArrayList<AngleData>();
        for(int i = startIndex;i<endIndex;i++)
        {
            tmp.add(dataBase.get(i));
        }
        return tmp;
    }
}
