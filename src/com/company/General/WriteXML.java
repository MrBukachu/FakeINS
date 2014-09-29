package com.company.General;

import com.company.SensorData.GNSSData;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将坐标信息写成XML文档的形式
 * 方便自己在手机上解析
 * Created by mac on 9/10/14.
 */
public class WriteXML {
    private Document document;
    private List<GNSSData> listEI = new ArrayList<GNSSData>();
    private List<GeoPoint> listGeoPoint = new ArrayList<GeoPoint>();

    public void init(List<GNSSData> listEpochInfo) {
        document = DocumentHelper.createDocument();
        this.listEI = listEpochInfo;

    }

    public void createXml(String fileName) {
        Element root = this.document.addElement("listGeoPoint");
        root.addAttribute("CoordinateSystem","WGS84");

        int size = listEI.size();
        for (int i = 0;i<size;i++)
        {
            Element info = root.addElement("info");
            info.addAttribute("No.", Integer.toString(i));

            Element epoch = info.addElement("epoch");
            epoch.setText(Double.toString(listEI.get(i).getGpst().getTOW()));
            Element longtitude = info.addElement("longtitude");
            longtitude.setText(Double.toString(listEI.get(i).getGeodeticCoordinate().getL()));
            Element latitude = info.addElement("latitude");
            latitude.setText(Double.toString(listEI.get(i).getGeodeticCoordinate().getB()));

        }
//        this.document.appendChild(root);
//        root.appendChild(info);
        try{
            OutputFormat format = OutputFormat.createPrettyPrint();//缩减型格式，换行
            //OutputFormat format = OutputFormat.createCompactFormat();//紧凑型格式
            format.setEncoding("gb2312");//设置编码
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            XMLWriter out = new XMLWriter(new FileWriter(file),format);
            out.write(document);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void parserXml(String fileName) {
//        try {
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
////            DocumentBuilder db = dbf.newDocumentBuilder();
////            Document document = db.parse(fileName);
////
////            NodeList employees = document.getChildNodes();
////            for (int i = 0; i < employees.getLength(); i++) {
////                Node employee = employees.item(i);
////                NodeList employeeInfo = employee.getChildNodes();
////                for (int j = 0; j < employeeInfo.getLength(); j++) {
////                    Node node = employeeInfo.item(j);
////                    NodeList employeeMeta = node.getChildNodes();
////                    for (int k = 0; k < employeeMeta.getLength(); k++) {
////                        System.out.println(employeeMeta.item(k).getNodeName()
////                                + ":" + employeeMeta.item(k).getTextContent());
////                    }
////                }
//            }
//            System.out.println("解析完毕");
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getMessage());
//        } catch (ParserConfigurationException e) {
//            System.out.println(e.getMessage());
//        } catch (SAXException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
    }
    public void paserXmlBySAX (String fileName) throws Exception {
        // 1.实例化SAXParserFactory对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2.创建解析器
        SAXParser parser = factory.newSAXParser();
        // 3.获取需要解析的文档，生成解析器,最后解析文档
        File f = new File(fileName);
        SaxHandler dh = new SaxHandler();
        parser.parse(f, dh);
    }
}
class GeoPoint {
    private double epoch,longtitude,latitude;
    void setEpoch(double epoch){this.epoch =  epoch;}
    void setLongtitude(double lon){this.longtitude = lon;}
    void setLatitude(double lat){this.latitude = lat;}
    double getEpoch(){return epoch;}
    double getLongtitude(){return longtitude;}
    double getLatitude(){return latitude;}
}
class SaxHandler extends DefaultHandler {
    private List<GeoPoint> listGeoPoint;

    private GeoPoint info = null;
    private String preTag = null;//作用是记录解析时的上一个节点名称
    @Override
     /* 此方法有三个参数
       arg0是传回来的字符数组，其包含元素内容
       arg1和arg2分别是数组的开始位置和结束位置 */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (preTag != null) {
            String content = new String(ch, start, length);
            if ("epoch".equals(preTag)) {
                info.setEpoch(Double.parseDouble(content));
            } else if ("longtitude".equals(preTag)) {
                info.setLongtitude(Double.parseDouble(content));
            } else if ("latitude".equals(preTag)) {
                info.setLatitude(Double.parseDouble(content));
            }
        }

    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("\n…………结束解析文档…………");
        super.endDocument();
    }

    /* arg0是名称空间
       arg1是包含名称空间的标签，如果没有名称空间，则为空
       arg2是不包含名称空间的标签 */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if("info".equals(qName)){
            listGeoPoint.add(info);
            info = null;
        }
        preTag = null;
    }

    @Override
    public void startDocument() throws SAXException {
        listGeoPoint = new ArrayList<GeoPoint>();
    }

    /*arg0是名称空间
      arg1是包含名称空间的标签，如果没有名称空间，则为空
      arg2是不包含名称空间的标签
      arg3很明显是属性的集合 */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("info".equals(qName)){
            info = new GeoPoint();

        }
        preTag = qName;
    }
    public List<GeoPoint> getListGeoPoint(){return listGeoPoint;}
}
