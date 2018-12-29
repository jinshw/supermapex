package com.supermap.sample.traffic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CommUtil {
    /**
     * 获取数据
     *
     * @throws IOException
     */
    public static String getDataByUrl(String urlString) {
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }
            in.close();
        } catch (Exception e) {
            //logger.error("error in wapaction,and e is " + e.getMessage());
        }
        return res;
    }
    /**
     * 关系数据选择
     * @param
     * @return:json文件的内容
     */
    public static String rttRelSel(String url, String areacode) {
        String urlPath = "";
        //"650100,650200,650400,650500,652300,652700,652800,652900,653000,653100,653200,654000,654200,654300"
        if(!StrUtil.isEmpty(areacode)){
            if(("650100").equals(areacode)){//乌鲁木齐市	650100
                urlPath = url+"/rtt-rel-wulumuqi.json";
            }else if(("650200").equals(areacode)){//克拉玛依市	650200
                urlPath = url+"/rtt-rel-kelamayi.json";
            }else if(("650400").equals(areacode)){//吐鲁番地区	 650400
                urlPath = url+"/rtt-rel-tulufan.json";
            }else if(("650500").equals(areacode)){//哈密地区	650500
                urlPath = url+"/rtt-rel-hami.json";
            }else if(("652300").equals(areacode)){//昌吉回族自治州	652300
                urlPath = url+"/rtt-rel-changji.json";
            }else if(("652700").equals(areacode)){//博尔塔拉蒙古自治州	652700
                urlPath = url+"/rtt-rel-boertala.json";
            }else if(("652800").equals(areacode)){//巴音郭楞蒙古自治州	652800
                urlPath = url+"/rtt-rel-bayinguoleng.json";
            }else if(("652900").equals(areacode)){//阿克苏地区	652900
                urlPath = url+"/rtt-rel-akesu.json";
            }else if(("653000").equals(areacode)){//克孜勒苏柯尔克孜自治州	653000
                urlPath = url+"/rtt-rel-kezilesukeerkezi.json";
            }else if(("653100").equals(areacode)){//喀什地区	653100
                urlPath = url+"/rtt-rel-kashi.json";
            }else if(("653200").equals(areacode)){//和田地区	653200
                urlPath = url+"/rtt-rel-hetian.json";
            }else if(("654000").equals(areacode)){//伊犁哈萨克自治州	654000
                urlPath = url+"/rtt-rel-yili.json";
            }else if(("654200").equals(areacode)){//塔城地区	654200
                urlPath = url+"/rtt-rel-tacheng.json";
            }else if(("654300").equals(areacode)){//阿勒泰地区	654300
                urlPath = url+"/rtt-rel-aletai.json";
            }
        }

        return urlPath;
    }
    /**
     * 通过网络访问json并读取文件
     * @param
     * @return:json文件的内容
     */
    public static String loadJson (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"UTF-8"));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
