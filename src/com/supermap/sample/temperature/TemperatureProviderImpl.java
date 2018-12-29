package com.supermap.sample.temperature;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import com.supermap.services.components.spi.ProviderContext;
import com.supermap.services.components.spi.ProviderContextAware;

public class TemperatureProviderImpl implements TemperatureProvider, ProviderContextAware {
    private String filePath;

    @Override
    public String GetTemperature(String cityName) throws Exception {
        String temperature = null;
        LinkedList<String> list = new LinkedList<String>();
        File f = new File(filePath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        String str = null;
        while ((str = reader.readLine()) != null) {
            list.add(str);
        }
        reader.close();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String strInfo = (String) it.next();
            if (strInfo.startsWith(cityName)) {
                int i = strInfo.indexOf("=");
                temperature = strInfo.substring(i + 1);
                break;
            }
        }
        if (temperature != null) {
            return temperature;
        } else {
            temperature = "没有 " + cityName + " 城市的天气信息";
            return temperature;
        }
    }

    public void setProviderContext(ProviderContext context) {
        FileSetting file = context.getConfig(FileSetting.class);
        this.filePath = file.getFilePath();
    }
}
