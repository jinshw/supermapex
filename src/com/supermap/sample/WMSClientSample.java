package com.supermap.sample;

import com.supermap.services.components.commontypes.Rectangle2D;
import com.supermap.services.components.spi.ogc.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WMSClientSample {


    // WMS 服务地址
    private static String strURL = "http://localhost:8099/iserver/services/map-china400/wms111/China";

    // 访问 WMS 服务的用户名
    private static String userName = null;

    // 访问的 WMS 服务密码
    private static String password = null;

    // 根据 WMS 地址、用户名和密码构建一个 WMSClient 实例化对象
    private WMS wmsClient = new WMSClient(strURL, userName, password);

    // 获取 WMS 服务级元数据信息
    public WMSCapabilities getCapabilities() {

        WMSCapabilities wmsCapabilities = null;
        String wmsVersion = "1.1.1";
        try {
            wmsCapabilities = wmsClient.getCapabilities(wmsVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wmsCapabilities;
    }

    // 获取地图
    public byte[] getMap() {
        byte[] mapBytes = null;
        // 构建 WMS 地图参数对象
        WMSMapParameter wmsMapParameter = new WMSMapParameter();
        // WMS 服务的版本号
        String wmsVersion = "1.1.1";
        // 给 WMS 地图参数对象赋值
        wmsMapParameter.mapName = "China";
        wmsMapParameter.entireBounds = new Rectangle2D(-180, -90, 180, 90);
        wmsMapParameter.bounds = new Rectangle2D(-180, -90, 180, 90);
        wmsMapParameter.layers = new String[1];
        wmsMapParameter.layers[0] = "China_Boundary_ln@China#1";
        wmsMapParameter.srs = "EPSG:4326";
        wmsMapParameter.width = 800;
        wmsMapParameter.height = 400;
        wmsMapParameter.styles = new String[]{""};
        try {
            mapBytes = wmsClient.getMap(wmsVersion, wmsMapParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapBytes;
    }


    // 获取地图要素信息
    public WMSFeatureInfo[] getFeatureInfo() {
        WMSFeatureInfo[] wmsFeatureInfos = null;
        // 构建 WMS 查询参数对象
        WMSQueryParameter wmsQueryParameter = new WMSQueryParameter();

        // WMS 服务的版本号
        String wmsVersion = "1.1.1";
        wmsQueryParameter.mapName = "China";
        wmsQueryParameter.entireBounds = new Rectangle2D(-180, -90, 180, 90);
        wmsQueryParameter.bounds = new Rectangle2D(-180, -90, 180, 90);
        wmsQueryParameter.layers = new String[1];
        wmsQueryParameter.layers[0] = "World_Division_pl@China";
        wmsQueryParameter.srs = "EPSG:4326";
        wmsQueryParameter.width = 400;
        wmsQueryParameter.height = 400;
        wmsQueryParameter.queryLayers = new String[1];
        wmsQueryParameter.queryLayers[0] = "World_Division_pl@China";
        wmsQueryParameter.x = 150;
        wmsQueryParameter.y = 150;
        try {
            wmsFeatureInfos = wmsClient.getFeatureInfo(wmsVersion, wmsQueryParameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wmsFeatureInfos;
    }


    public static void main(String[] args) {
        WMSClientSample wmsClientSample = new WMSClientSample();
        // 输出服务标题和地图标题
        WMSCapabilities capabilities = wmsClientSample.getCapabilities();

        String wmsTitle = capabilities.serviceDescription.title;
        String mapTitle = capabilities.mapTitle;
        System.out.println("WMS Title is " + wmsTitle);
        System.out.println("MapTitle is " + mapTitle);

        // 输出地图
        byte[] bytes = wmsClientSample.getMap();
        String fileName = "d:/map.png";
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请在 d:/ map.png 文件中查看地图");

        // 输出地物要素信息
        WMSFeatureInfo[] wmsFeatureInfo = wmsClientSample.getFeatureInfo();
        int length = wmsFeatureInfo.length;
        System.out.println("地物要素信息：");
        for (int i = 0; i < length; i++) {
            String[] fieldNames = wmsFeatureInfo[i].fieldNames;
            String[] fieldValues = wmsFeatureInfo[i].fieldValues;
            int nameLength = fieldNames.length;
            for (int j = 0; j < nameLength; j++) {
                String name = fieldNames[j];
                String value = fieldValues[j];
                System.out.println(name + " = " + value);
            }
        }



    }
}
