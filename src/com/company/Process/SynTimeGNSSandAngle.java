package com.company.Process;

import com.company.General.WriteFile;
import com.company.SensorData.AngleData;
import com.company.SensorData.GNSSData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 9/11/14.
 */
public class SynTimeGNSSandAngle {
    enum REFER{Angle,GNSS}
    private List<AngleData> listAngleData;
    private List<GNSSData> listGNSSData;
    private List<AngleData> listSynedAngleData;
    private List<GNSSData> listSynedGNSSData;
    private REFER refer;
    public SynTimeGNSSandAngle(List<AngleData> listAngleData,List<GNSSData> listGNSSData) {
        this.listAngleData = listAngleData;
        this.listGNSSData = listGNSSData;
        this.refer = REFER.GNSS;    //以GNSS为基准
    }
    public void process(){
        List<Double> timeMarker = new ArrayList<Double>();
       switch (refer)
       {
           case Angle:
           {
               timeMarker = TimeSynchronization.getTimeMarkerFromAngle(listAngleData);
               listSynedGNSSData = TimeSynchronization.timeSynGNSSData(listGNSSData,timeMarker);
               listSynedAngleData = listAngleData;
               WriteFile.writeGNSSData(listSynedGNSSData,"/Users/mac/Desktop/I WANT GRADUATION/AngleData/synedGNSS.txt");
               break;}
           case GNSS:{
               timeMarker = TimeSynchronization.getTimeMarkerFromGNSS(listGNSSData);
               listSynedGNSSData = listGNSSData;
               listSynedAngleData = TimeSynchronization.timeSynAngleData(listAngleData,timeMarker);
               WriteFile.writeAngleData(listSynedAngleData,"/Users/mac/Desktop/I WANT GRADUATION/AngleData/synedAngle.txt");
               break;}
       }

    }

    public List<AngleData> getListSynedAngleData() {
        return listSynedAngleData;
    }

    public List<GNSSData> getListSynedGNSSData() {
        return listSynedGNSSData;
    }
}

