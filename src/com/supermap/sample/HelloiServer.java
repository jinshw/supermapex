package com.supermap.sample;

import com.supermap.services.components.spi.ogc.WMS;
import com.supermap.services.components.spi.ogc.WMSCapabilities;
import com.supermap.services.components.spi.ogc.WMSClient;

public class HelloiServer {
    // WMS 服务地址
    private static String strURL = "http://localhost:8099/iserver/services/map-china400/wms111/China";
    // 访问 WMS 服务的用户名
    private static String userName = null;
    // 访问的 WMS 服务密码
    private static String password = null;
    // 根据 WMS 地址、用户名和密码构建一个 WMSClient 实例化对象
    private WMS wmsClient = new WMSClient(strURL, userName, password);

    // 获取 WMS 服务名
    public String getServiceName() {
        WMSCapabilities wmsCapabilities = null;
        String wmsVersion = "1.1.1";
        try {
            // 获取 WMS 服务级元数据信息
            wmsCapabilities = wmsClient.getCapabilities(wmsVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 获取 WMS 服务的名称
        String serviceName = wmsCapabilities.serviceDescription.name;
        return serviceName;
    }

    public static void main(String[] args) {
        HelloiServer helloiServer = new HelloiServer();
        // 输出 Hello SuperMap iServer
        System.out.println("Hello SuperMap iServer!");
        String serviceName = helloiServer.getServiceName();
        // 输出 WMS 服务名称
        System.out.println("The WMS Service name is: " + serviceName);
    }

}
