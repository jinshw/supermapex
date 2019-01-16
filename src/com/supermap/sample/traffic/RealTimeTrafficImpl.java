package com.supermap.sample.traffic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.DatasetVector;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.services.components.ComponentContext;
import com.supermap.services.components.ComponentContextAware;
import com.supermap.services.components.commontypes.*;
import com.supermap.services.components.spi.DataProvider;
import com.supermap.services.components.spi.MapProvider;
import com.supermap.services.components.spi.SpatialAnalystProvider;
import com.supermap.services.providers.WorkspaceConnectionInfo;
import com.supermap.services.providers.WorkspaceContainer;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class RealTimeTrafficImpl implements RealTimeTraffic, ComponentContextAware {
    private MapProvider mapProvider = null;
    private DataProvider dataProvider = null;
    private SpatialAnalystProvider spatialAnalystProvider = null;
    private RealTimeTrafficProvider realTimeTrafficProvider = null;
    private MapParameter defaultMapParam = null;
    private String workspacePath = "";
    private String datasourceName = "";
    private String datasetName = "";
    private String colorFieldName = "";
    private String IDFieldName = "";
    private String mapNames = "";
    private int period = 0;
    private String trafficColors = "";
    //路况颜色值字典
    private Map<String, Integer> m_trafficColors = new HashMap<String, Integer>();

    private String areaCodes = "";
    private String rticUrl = "";
    private String roadKind = "";
    private String max = "0";
    private String mysqlDriver = "";
    private String mysqlURL = "";
    private String mysqlUser = "";
    private String mysqlPassword = "";
    private List<XmlBean> list = new ArrayList<>();
    private List<XmlBean> allList = new ArrayList<>();


    public List<TrafficInfoBean> getTrafficTop(List<TrafficInfoBean> list, int max) {
        List<TrafficInfoBean> rlist = new ArrayList<TrafficInfoBean>();
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.name = this.datasetName;
        List<Feature> fList = null;
        TrafficInfoBean trafficInfoBean = null;
        Point2D startP = null, endP = null;

        for (int i = 0; i < list.size(); i++) {
            trafficInfoBean = list.get(i);
            String key = trafficInfoBean.getTid();
            queryParameter.attributeFilter = "ID = '" + key + "'";
            fList = dataProvider.getFeature(this.datasourceName, queryParameter);
            if (fList.size() > 0 && fList.get(0).geometry.points.length > 0) {
                startP = fList.get(0).geometry.points[0];
                endP = fList.get(0).geometry.points[fList.get(0).geometry.points.length - 1];
                trafficInfoBean.setStartPointX(startP.x);
                trafficInfoBean.setStartPointY(startP.y);
                trafficInfoBean.setEndPointX(endP.x);
                trafficInfoBean.setEndPointY(endP.y);
                trafficInfoBean.setTid(key);
                rlist.add(trafficInfoBean);
            } else {
                System.out.println("not point i== " + i);
            }
            if (rlist.size() == max) {
                break;
            }
        }
        return rlist;
    }

    public List<GeometrySpatialAnalystResult> getFeaturesOfMileagePosition(Map map) {

        List<GeometrySpatialAnalystResult> rlist = new ArrayList<GeometrySpatialAnalystResult>();
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.name = this.datasetName;

        queryParameter.attributeFilter = "ID = '96579748'";
//        queryParameter.orderBy = "STARTZH ASC";
        List<Feature> fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        Feature feature;
        GeometrySpatialAnalystResult geometrySpatialAnalystResult;
        return rlist;
    }

    public GeometrySpatialAnalystResult locatePoint(Map<String, String> map) {
        GeometrySpatialAnalystResult geometrySpatialAnalystResult = null;
        String roadtype = map.get("roadtype").trim();
        String roadcode = map.get("roadcode").trim();
        String measure = map.get("measure").trim();
        String offset = map.get("offset").trim();
        String isIgnoreGapStr = map.get("isIgnoreGap").trim();
        Double measureD, offsetD;
        boolean isIgnoreGap = false;
        if ("".equals(measure)) {
            measureD = 0.0;
        } else {
            measureD = Double.valueOf(measure);
        }
        if ("".equals(offset)) {
            offsetD = 0.0;
        } else {
            offsetD = Double.valueOf(offset);
        }
        if (isIgnoreGapStr == "" || isIgnoreGapStr == null) {
            isIgnoreGap = true;
        } else {
            if (isIgnoreGapStr == "1") {
                isIgnoreGap = true;
            } else {
                isIgnoreGap = false;
            }
        }

        List<GeometrySpatialAnalystResult> rlist = new ArrayList<GeometrySpatialAnalystResult>();
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.name = this.datasetName;

        queryParameter.attributeFilter = "ROADCODE = '" + roadcode + "'";
        queryParameter.orderBy = "STARTZH ASC";
        List<Feature> fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        Feature feature;
        Double startzhD, endzhD;
//        GeometrySpatialAnalystResult geometrySpatialAnalystResult;
        for (int i = 0; i < fList.size(); i++) {
            geometrySpatialAnalystResult = null;
            feature = fList.get(i);
            startzhD = Double.valueOf(feature.fieldValues[18]) * 1000;
            endzhD = Double.valueOf(feature.fieldValues[21]) * 1000;
//            if(geometrySpatialAnalystResult.resultGeometry != null){
            if (measureD >= Math.min(startzhD, endzhD) && measureD <= Math.max(startzhD, endzhD)) {
                geometrySpatialAnalystResult = spatialAnalystProvider.locatePoint((Route) feature.geometry, measureD, offsetD, isIgnoreGap);
                return geometrySpatialAnalystResult;
            }
        }
        return null;
    }

    public Map<String, Object> getRttByAdcodeJson(String rticUrl, String areaCode, String kindLevel) {
        //String areaCode=request.getParameter("adCode");
        Map<String, Object> map = new TreeMap<String, Object>();
        try {
            //第一步：读取json文件获取路网关系表
            JSONArray rttRoadRelJsonArray = null;
            //初次加载，获取完成后存入缓存
            String path = this.getClass().getClassLoader().getResource(".").getPath();
            String urlPath = CommUtil.rttRelSel(path + "rtic", areaCode);
            String rttRoadRelJsonStr = FileUtils.readFile(urlPath, "UTF-8");

//            String urlPath = CommUtil.rttRelSel(rticDicUrl, areaCode);
//            String rttRoadRelJsonStr = CommUtil.loadJson(urlPath);

            if (!StringUtils.isEmpty(rttRoadRelJsonStr)) {
                //jsonStr转换json
                rttRoadRelJsonArray = JSONArray.parseArray(rttRoadRelJsonStr);

                //存入缓存
//                trafficRoadRel.put(areaCode, rttRoadRelJsonArray);
            } else {
                System.out.println("路网关系表获取失败：" + areaCode);
            }

            //第二步：通过接口获取实时路况数据
            String urlString = MessageFormat.format(rticUrl, areaCode, kindLevel);
            //"http://newte.sh.1251225243.clb.myqcloud.com/TEGateway/123456/RTICTraffic.xml?bizcode=xjjtysxx6bc624asdb98asdhjdf12&version=1701&datatype=14&format=1&adcode=" + areaCode+"&kind="+kindLevel;
            String rttXmlString = CommUtil.getDataByUrl(urlString);

            //匹配解析路况与空间数据对应关系
            if (!StringUtils.isEmpty(rttXmlString) && rttRoadRelJsonArray != null && rttRoadRelJsonArray.size() > 0) {
                //第三步：实时路况xml转换json
                Document doc = DocumentHelper.parseText(rttXmlString);
                JSONObject json = new JSONObject();
                XmlToJson xmlToJson = new XmlToJson();
                xmlToJson.dom4j2Json(doc.getRootElement(), json);
                //第四步：解析实时路况json和关系json，并匹配出对应地图路网的所需信息
                JSONObject jsonMsg = json.getJSONObject("result").getJSONObject("cities").getJSONObject("city");
                JSONArray jsonArr = jsonMsg.getJSONArray("mesh");
                //JSONArray jsonArr = jsonMsg.getJSONArray();
                JSONObject jsonTmp = new JSONObject();
                //定义组合信息对象
                Map<String, Object> mapFlowResult = new TreeMap<String, Object>();
                Map<String, Object> mapEventResult = new TreeMap<String, Object>();
                for (int i = 0; i < jsonArr.size(); i++) {
                    jsonTmp = jsonArr.getJSONObject(i);
                    //System.out.println(jsonTmp.toString());
                    //System.out.println(jsonTmp.get("code").toString());
                    //String adCodeTmp = jsonTmp.get("code").toString();
                    String adCodeTmp = "";
                    if (jsonTmp.get("code") != null) {
                        adCodeTmp = jsonTmp.get("code").toString();
                    }

                    //通过JSONTokener解析，JSONTokener在解析过程中可以自动转换为对应的类型
                    //JSONObject listArray = new JSONTokener(jsonTmp.getString("rtic")).nextValue();
                    //System.out.println("listArray类型是："+jsonTmp.get("rtic").getClass());
                    //System.out.println("是数组："+(jsonTmp.get("rtic") instanceof JSONArray));
                    //System.out.println("是对象："+(jsonTmp.get("rtic") instanceof JSONObject));
                    //判断类型相等
                    if (jsonTmp.get("rtic") instanceof JSONArray) {
                        JSONArray jsonRticArr = (JSONArray) jsonTmp.get("rtic");
                        for (int j = 0; j < jsonRticArr.size(); j++) {
                            JSONObject jsonObjectRtic = jsonRticArr.getJSONObject(j);
                            //跟踪出错的数据
                            //System.out.println(jsonObjectRtic);
                            //获取id
                            String idTmp = "";
                            if (jsonObjectRtic.get("@id") != null) {
                                idTmp = jsonObjectRtic.get("@id").toString();
                                //System.out.println(idTmp);
                            }
                            //获取kind
                            String kindTmp = "";
                            //int kindIntTmp = 0;
                            if (jsonObjectRtic.get("@kind") != null) {
                                kindTmp = jsonObjectRtic.get("@kind").toString();
                                //kindIntTmp = Integer.parseInt(kindTmp);
                                //（kind:0：表示高速;1:快速道路，二进制表示，需要加1）
                                //kindTmp =String.valueOf(kindIntTmp+1);
                                //System.out.println(kindTmp);
                            }
                            //组合id
                            String rttRoadId = adCodeTmp + StrUtil.leftAppend(5, idTmp);
                            //只匹配高速和快速道路
                            if (!StrUtil.isEmpty(rttRoadId) && !StrUtil.isEmpty(kindTmp)) {
                                //获取路况或者事件
                                if (jsonObjectRtic.get("flow") instanceof JSONObject && jsonObjectRtic.getJSONObject("flow") != null) {
                                    JSONObject flowTmp = jsonObjectRtic.getJSONObject("flow");

                                    //比对关系找出路网对象
                                    for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                        JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                        //id和kind同时匹配成功，才算正确匹配
                                        if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                                (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                            JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                            for (int g = 0; g < includeArr.size(); g++) {
                                                JSONObject includeObject = includeArr.getJSONObject(g);
                                                mapFlowResult.put(includeObject.getString("NILinkID"), flowTmp);
                                            }

                                        }
                                    }

                                } else if (jsonObjectRtic.get("flow") instanceof JSONArray && jsonObjectRtic.getJSONArray("flow") != null) {
                                    JSONArray flowArrTmp = jsonObjectRtic.getJSONArray("flow");
                                    for (int m = 0; m < flowArrTmp.size(); m++) {
                                        JSONObject flowTmp = flowArrTmp.getJSONObject(m);
                                        //比对关系找出路网对象
                                        for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                            JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                            //id和kind同时匹配成功，才算正确匹配
                                            if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                                    (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                                JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                                for (int g = 0; g < includeArr.size(); g++) {
                                                    JSONObject includeObject = includeArr.getJSONObject(g);
                                                    mapFlowResult.put(includeObject.getString("NILinkID"), flowTmp);
                                                }

                                            }
                                        }
                                    }
                                } else if (jsonObjectRtic.get("event") instanceof JSONObject && jsonObjectRtic.getJSONObject("event") != null) {
                                    JSONObject eventTmp = jsonObjectRtic.getJSONObject("event");

                                    //比对关系找出路网对象
                                    for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                        JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                        //id和kind同时匹配成功，才算正确匹配
                                        if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                                (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                            JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                            for (int g = 0; g < includeArr.size(); g++) {
                                                JSONObject includeObject = includeArr.getJSONObject(g);
                                                mapEventResult.put(includeObject.getString("NILinkID"), eventTmp);
                                            }

                                        }
                                    }
                                } else if (jsonObjectRtic.get("event") instanceof JSONArray && jsonObjectRtic.getJSONArray("event") != null) {
                                    JSONArray eventArrTmp = jsonObjectRtic.getJSONArray("event");
                                    for (int m = 0; m < eventArrTmp.size(); m++) {
                                        JSONObject eventTmp = eventArrTmp.getJSONObject(m);
                                        //比对关系找出路网对象
                                        for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                            JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                            //id和kind同时匹配成功，才算正确匹配
                                            if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                                    (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                                JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                                for (int g = 0; g < includeArr.size(); g++) {
                                                    JSONObject includeObject = includeArr.getJSONObject(g);
                                                    mapEventResult.put(includeObject.getString("NILinkID"), eventTmp);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (jsonTmp.get("rtic") instanceof JSONObject) {
                        JSONObject jsonObjectRtic = (JSONObject) jsonTmp.get("rtic");
                        //System.out.println(jsonObjectRtic);
                        //获取id
                        String idTmp = "";
                        if (jsonObjectRtic.get("@id") != null) {
                            idTmp = jsonObjectRtic.get("@id").toString();
                            //System.out.println(idTmp);
                        }
                        //获取kind
                        String kindTmp = "";
                        //int kindIntTmp = 0;
                        if (jsonObjectRtic.get("@kind") != null) {
                            kindTmp = jsonObjectRtic.get("@kind").toString();
                            //kindIntTmp = Integer.parseInt(kindTmp);
                            //（kind:0：表示高速;1:快速道路，二进制表示，需要加1）
                            //kindTmp =String.valueOf(kindIntTmp+1);
                            //System.out.println(kindTmp);
                        }
                        //组合id
                        String rttRoadId = adCodeTmp + StrUtil.leftAppend(5, idTmp);
                        //只匹配高速和快速道路
                        if (!StrUtil.isEmpty(rttRoadId) && !StrUtil.isEmpty(kindTmp)) {
                            //获取路况或者事件
                            if (jsonObjectRtic.get("flow") instanceof JSONObject && jsonObjectRtic.getJSONObject("flow") != null) {
                                JSONObject flowTmp = jsonObjectRtic.getJSONObject("flow");

                                //比对关系找出路网对象
                                for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                    JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                    //id和kind同时匹配成功，才算正确匹配
                                    if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                            (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                        JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                        for (int g = 0; g < includeArr.size(); g++) {
                                            JSONObject includeObject = includeArr.getJSONObject(g);
                                            mapFlowResult.put(includeObject.getString("NILinkID"), flowTmp);
                                        }

                                    }
                                }

                            } else if (jsonObjectRtic.get("flow") instanceof JSONArray && jsonObjectRtic.getJSONArray("flow") != null) {
                                JSONArray flowArrTmp = jsonObjectRtic.getJSONArray("flow");
                                for (int m = 0; m < flowArrTmp.size(); m++) {
                                    JSONObject flowTmp = flowArrTmp.getJSONObject(m);
                                    //比对关系找出路网对象
                                    for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                        JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                        //id和kind同时匹配成功，才算正确匹配
                                        if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                                (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                            JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                            for (int g = 0; g < includeArr.size(); g++) {
                                                JSONObject includeObject = includeArr.getJSONObject(g);
                                                mapFlowResult.put(includeObject.getString("NILinkID"), flowTmp);
                                            }

                                        }
                                    }
                                }
                            } else if (jsonObjectRtic.get("event") instanceof JSONObject && jsonObjectRtic.getJSONObject("event") != null) {
                                JSONObject eventTmp = jsonObjectRtic.getJSONObject("event");

                                //比对关系找出路网对象
                                for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                    JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                    //id和kind同时匹配成功，才算正确匹配
                                    if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                            (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                        JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                        for (int g = 0; g < includeArr.size(); g++) {
                                            JSONObject includeObject = includeArr.getJSONObject(g);
                                            mapEventResult.put(includeObject.getString("NILinkID"), eventTmp);
                                        }

                                    }
                                }
                            } else if (jsonObjectRtic.get("event") instanceof JSONArray && jsonObjectRtic.getJSONArray("event") != null) {
                                JSONArray eventArrTmp = jsonObjectRtic.getJSONArray("event");
                                for (int m = 0; m < eventArrTmp.size(); m++) {
                                    JSONObject eventTmp = eventArrTmp.getJSONObject(m);
                                    //比对关系找出路网对象
                                    for (int k = 0; k < rttRoadRelJsonArray.size(); k++) {
                                        JSONObject rttRoadRelJsonObject = rttRoadRelJsonArray.getJSONObject(k);
                                        //id和kind同时匹配成功，才算正确匹配
                                        if (rttRoadId.equals(rttRoadRelJsonObject.getString("RTIC_linkID")) &&
                                                (kindTmp).equals(rttRoadRelJsonObject.getString("RticLinkKind"))) {
                                            JSONArray includeArr = (JSONArray) rttRoadRelJsonObject.get("include");
                                            for (int g = 0; g < includeArr.size(); g++) {
                                                JSONObject includeObject = includeArr.getJSONObject(g);
                                                mapEventResult.put(includeObject.getString("NILinkID"), eventTmp);
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                map.put("state", "success");
                map.put("resultFlow", mapFlowResult);
                map.put("resultEvent", mapEventResult);

            } else {
                map.put("state", "failure");
                map.put("result", "无信息");
            }


        } catch (Exception e) {
            map.put("state", "failure");
            map.put("result", "后台捕获异常：" + e.getMessage());
        }

        return map;
    }

    public List<XmlBean> getRAttributes(List<XmlBean> list) {
        List<XmlBean> rlist = new ArrayList<>();
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.name = this.datasetName;
        Iterator<XmlBean> iterator = list.iterator();
        XmlBean xmlBean = new XmlBean();
        List<Feature> fList = null;
        List<Feature> fListStart = null;
        List<Feature> fListEnd = null;
        Point2D startP = null, endP = null;
        while (iterator.hasNext()) {
            xmlBean = iterator.next();
            JSONArray jsonArray = xmlBean.getInclude();
            Iterator jsonArrayIt = jsonArray.iterator();
            int size = jsonArray.size();
            JSONObject startJson = (JSONObject) jsonArray.get(0);
            JSONObject endJson = (JSONObject) jsonArray.get(size - 1);
            String startId = startJson.getString("NILinkID");
            String endId = endJson.getString("NILinkID");


            queryParameter.attributeFilter = "ID = '" + startId + "' ";
            fListStart = dataProvider.getFeature(this.datasourceName, queryParameter);

            queryParameter.attributeFilter = "ID = '" + endId + "'";
            fListEnd = dataProvider.getFeature(this.datasourceName, queryParameter);

            if (fListStart.size() > 0 && fListEnd.size() > 0) {
                String direction = fListStart.get(0).fieldValues[17];
                Feature startFeature = fListStart.get(0);
                Feature endFeature = fListEnd.get(0);
                if (startFeature != null && startFeature.geometry.points.length > 0) {
                    if (direction.equals("3")) {//逆方向：单向通行，通行方向为终点到起点方向
                        int plen = startFeature.geometry.points.length;
                        startP = startFeature.geometry.points[plen - 1];
                    } else {
                        startP = startFeature.geometry.points[0];
                    }
                    xmlBean.setStartPointX(startP.x);
                    xmlBean.setStartPointY(startP.y);
                    xmlBean.setStartNiLinkID(startId);
                } else {
                    System.out.println("路网矢量数据中没有RticLinkID=" + xmlBean.getRticLinkID() + "  RticLinkKind= " + xmlBean.getRticLinkKind() + "  NILinkID=" + startId + "的该数据");
                }
                if (endFeature != null && endFeature.geometry.points.length > 0) {
                    if (direction.equals("3")) {//逆方向：单向通行，通行方向为终点到起点方向
                        endP = endFeature.geometry.points[0];
                    } else {
                        endP = endFeature.geometry.points[endFeature.geometry.points.length - 1];
                    }
                    xmlBean.setEndPointX(endP.x);
                    xmlBean.setEndPointY(endP.y);
                    xmlBean.setEndNiLinkID(endId);
                } else {
                    System.out.println("路网矢量数据中没有RticLinkID=" + xmlBean.getRticLinkID() + "  RticLinkKind= " + xmlBean.getRticLinkKind() + "  NILinkID=" + endId + "的该数据");
                }
                rlist.add(xmlBean);
            } else {
                System.out.println("startId==" + startId + "---endId==" + endId);
                System.out.println("路网矢量数据中没有RticLinkID=" + xmlBean.getRticLinkID() + "  RticLinkKind= " + xmlBean.getRticLinkKind() + "  NILinkID=" + endId + "的该数据");
            }


            // 这种方式查询返回结果没有顺序，导致不能区分起始线段和终止线段，进而不能获取准确的起始点和终点
            //queryParameter.attributeFilter = "ID = '" + startId + "' OR ID = '" + endId + "'";
            /*fList = dataProvider.getFeature(this.datasourceName, queryParameter);
            if (fList.size() > 0) {
                int startIndex = 0, endIndex = 1;
                if (fList.size() > 1) {
                    endIndex = fList.size();
                }
                Feature startFeature = fList.get(startIndex);
                Feature endFeature = fList.get(endIndex - 1);
                if (startFeature != null && startFeature.geometry.points.length > 0) {
                    startP = startFeature.geometry.points[0];
                    xmlBean.setStartPointX(startP.x);
                    xmlBean.setStartPointY(startP.y);
                    xmlBean.setStartNiLinkID(startId);
                } else {
                    System.out.println("路网矢量数据中没有RticLinkID=" + xmlBean.getRticLinkID() + "  RticLinkKind= " + xmlBean.getRticLinkKind() + "  NILinkID=" + startId + "的改数据");
                }
                if (endFeature != null && endFeature.geometry.points.length > 0) {
                    endP = endFeature.geometry.points[endFeature.geometry.points.length - 1];
                    xmlBean.setEndPointX(endP.x);
                    xmlBean.setEndPointY(endP.y);
                    xmlBean.setEndNiLinkID(endId);
                } else {
                    System.out.println("路网矢量数据中没有RticLinkID=" + xmlBean.getRticLinkID() + "  RticLinkKind= " + xmlBean.getRticLinkKind() + "  NILinkID=" + endId + "的改数据");
                }
                rlist.add(xmlBean);
            } else {
                System.out.println("startId==" + startId + "---endId==" + endId);
            }*/
        }
        return rlist;
    }

    public List<XmlBean> getTrafficList() {
        int count = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = new Date();
//        System.out.println(df.format(date));// new Date()为获取当前系统时间

//        String areaCodes = "650100,650200,650400,650500,652300,652700,652800,652900,653000,653100,653200,654000,654200,654300";
//        String rticUrl = "http://newte.sh.1251225243.clb.myqcloud.com/TEGateway/123456/RTICTraffic.xml?bizcode=xjjtysxx6bc624asdb98asdhjdf12&version=1701&datatype=14&format=1&adcode={0}&kind={1}";
//        String roadKind = "5";
        Map<String, List<XmlBean>> map = null;
        System.out.println("areaCodes==" + areaCodes);
        list.clear();
        allList.clear();
        System.out.println("---list--size()---:" + list.size());
        for (String code : areaCodes.split(",")) {
            map = RticUtils.getLinkList(rticUrl, code, roadKind);
            list.addAll(map.get("jam"));
            allList.addAll(map.get("all"));
        }
        //--start 实时路况路网刷新
        try {
            Map<String, String> allMap = RticUtils.getAllToKeyMapByLos(allList);
            updateTrafficData(allMap);
            //清空缓存
            clearCache(this.mapProvider, this.mapNames);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //--end 实时路况路网刷新

        // list去重 根据code + rticId + kind
        list = RticUtils.distinctList(list);

        Date date2 = new Date();
        list = RticUtils.sortListBylength(list);
        //list = list.subList(0, Integer.valueOf(max) * 2);//缩小数据范围（返回值的2倍），避免后续有的数据不能匹配路网数据
        list = this.getRAttributes(list);
        // 获取排序前N条记录
        int n = 0;
        //List<XmlBean> topList = list.subList(0, Integer.valueOf(max));
//        return topList;
        return list;
    }


    private void updateTrafficData(Map<String, String> map) {
        System.out.println("updateTrafficData--workspacePath====" + workspacePath);
        Workspace workspace = WorkspaceContainer.get(WorkspaceConnectionInfo.parse(workspacePath), this);
        Datasource ds = workspace.getDatasources().get(datasourceName);
        if (ds != null && ds.getDatasets().contains(datasetName)) {
//            Map<String, Object> mapFlowResult = (Map<String, Object>) rtic.get("resultFlow");

            DatasetVector dtv = (DatasetVector) ds.getDatasets().get(datasetName);
            com.supermap.data.Recordset recordset = dtv.getRecordset(false, com.supermap.data.CursorType.DYNAMIC);
            // 获得记录集对应的批量更新对象
            com.supermap.data.Recordset.BatchEditor editor = recordset.getBatch();
            editor.setMaxRecordCount(1000);

            // 批量更新开启
            editor.begin();

            while (!recordset.isEOF()) {
                String id = recordset.getString(IDFieldName);
                if (map.containsKey(id)) {
                    String los = map.get(id);
                    if (los != null) {
                        Integer color = 0;
                        if (m_trafficColors.containsKey(los)) {
                            color = m_trafficColors.get(los);
                        } else {
                            color = m_trafficColors.get("0");
                        }
                        recordset.setInt32(colorFieldName, color);
                    }
                }else{
//                    System.out.println("接口返回的id中没有和路网数据文件IDFieldName="+id+"匹配");
                }
                recordset.moveNext();
            }

            editor.update();
            // 释放记录集
            recordset.close();
            recordset.dispose();
        }
    }

    /**
     * 清除缓存
     *
     * @param map      地图提供者
     * @param mapNames 地图名称，多个以逗号分隔
     * @return
     */
    private boolean clearCache(MapProvider map, String mapNames) {
        boolean result = false;
        String[] names = mapNames.split(",");
        for (int i = 0; i < names.length; i++) {
            String mapName = names[i];
            MapParameter para = map.getDefaultMapParameter(mapName);
            if (para != null) {
                map.clearCache(mapName, para.bounds);
                result = true;
            } else {
                LogHelper.logByIserver("地图缓存清除失败：" + mapName);
            }
        }

        return result;
    }

    @Override
    public void setComponentContext(ComponentContext context) {
        RealTimeTrafficParam param = context.getConfig(RealTimeTrafficParam.class);
        if (param == null) {
            throw new IllegalArgumentException("参数 RealTimeTrafficParam 不能为空");
        }
        List<Object> providers = context.getProviders(Object.class);
        if (providers != null) {
            for (Object provider : providers) {
                if (provider instanceof RealTimeTrafficProvider) {
                    this.realTimeTrafficProvider = (RealTimeTrafficProvider) provider;
                    break;
                }
            }
            for (Object provider : providers) {
                /*if (provider instanceof MapProvider) {
                    this.mapProvider = (MapProvider) provider;
                    this.defaultMapParam = this.mapProvider.getMapParameter(param.getMapName());
                    this.defaultMapParam.viewer = new Rectangle(new Point(0, 0), new Point(800, 600));
                }*/
                if (provider instanceof DataProvider) {
                    this.dataProvider = (DataProvider) provider;
                    this.workspacePath = param.getWorkspacePath();
                    this.datasourceName = param.getDatasourceName();
                    this.datasetName = param.getDatasetName();
                    this.colorFieldName = param.getColorFieldName();
                    this.IDFieldName = param.getIDFieldName();
                    this.mapNames = param.getMapNames();
                    this.period = param.getPeriod();
                    this.trafficColors = param.getTrafficColors();
                    //初始化路况颜色字典
                    String[] colors = trafficColors.split(",");
                    for (int i = 0; i < colors.length; i++) {
                        Integer color = Integer.parseInt(colors[i]);
                        m_trafficColors.put(i + "", color);
                    }

                    this.areaCodes = param.getAreaCodes();
                    this.rticUrl = param.getRticUrl();
                    this.roadKind = param.getRoadKind();
                    this.max = param.getMax();
                    this.mysqlDriver = param.getMysqlDriver();
                    this.mysqlURL = param.getMysqlURL();
                    this.mysqlUser = param.getMysqlUser();
                    this.mysqlPassword = param.getMysqlPassword();
                    System.out.println("workspacePath=" + this.workspacePath);
                    System.out.println("datasourceName=" + this.datasourceName + "  datasetName=" + this.datasetName);
                    System.out.println("areaCodes=" + this.areaCodes + "  rticUrl=" + this.rticUrl);
                    System.out.println("roadKind=" + this.roadKind + "  max=" + this.max);
                    System.out.println("mysqlDriver=" + this.mysqlDriver + "  mysqlURL=" + this.mysqlURL);
                    System.out.println("mysqlUser=" + this.mysqlUser + "  mysqlPassword=" + this.mysqlPassword);
                } else if (provider instanceof SpatialAnalystProvider) {
                    this.spatialAnalystProvider = (SpatialAnalystProvider) provider;
                } else if (provider instanceof MapProvider) {
                    this.mapProvider = (MapProvider) provider;
                }

            }
        }
    }


    public int[] insertDatas(List<XmlBean> list) {
        int[] results = new int[50];
        int flag = 0;
        //声明Connection对象
        Connection con = null;
        //驱动程序名
//        String driver = "com.mysql.jdbc.Driver";
//        //URL指向要访问的数据库名mydata
//        String url = "jdbc:mysql://localhost:3306/xjone";
//        //MySQL配置时的用户名
//        String user = "root";
//        //MySQL配置时的密码
//        String password = "root";

        //加载驱动程序
        PreparedStatement psql = null;
        try {
            Class.forName(mysqlDriver);

            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(mysqlURL, mysqlUser, mysqlPassword);
            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
//            Statement statement = con.createStatement();
            psql = con.prepareStatement("insert into traffic_info(id,tid,startlen,affectlen,los,traveltime,nid,roadcode,roadname,startzh,endzh,startpoint,endpoinrt,batch,createtime,include,startpoint_nb,endpoint_nb) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            con.setAutoCommit(false);// 设置为不自动提交
            java.util.Date date = new java.util.Date();
            Timestamp tt = null;
            for (XmlBean xmlBean : list) {
                if (xmlBean != null) {
                    psql.setString(1, UUIDUtil.create32UpperUUID());
                    psql.setString(2, xmlBean.getRticId());
                    psql.setInt(3, Integer.valueOf(xmlBean.getFlowStartlen()));
                    psql.setInt(4, Integer.valueOf(xmlBean.getFlowStartlen()));
                    psql.setString(5, xmlBean.getFlowLos());
                    psql.setString(6, xmlBean.getFlowTraveltime());
                    psql.setString(7, xmlBean.getNid());
                    psql.setString(8, xmlBean.getRoadCode());
                    psql.setString(9, xmlBean.getRoadName());
                    psql.setDouble(10, xmlBean.getStartzh());
                    psql.setDouble(11, xmlBean.getEndzh());
                    psql.setString(12, xmlBean.getStartPoint());
                    psql.setString(13, xmlBean.getEndPoint());
                    psql.setString(14, xmlBean.getBatch());
                    tt = new Timestamp(date.getTime());
                    psql.setTimestamp(15, tt);
                    psql.setString(16, xmlBean.getInclude().toString());
                    psql.setString(17, xmlBean.getStartpoint_nb());
                    psql.setString(18, xmlBean.getEndpoint_nb());
                    psql.addBatch();//添加对象批处理中
                }
            }
            // 将一批参数提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组。
            results = psql.executeBatch();
            con.commit();
            System.out.println("提交成功!");
            con.setAutoCommit(true);//提交完成后回复现场将Auto commit,还原为true,
        } catch (Exception e) {
            try {
                // 若出现异常，对数据库中所有已完成的操作全部撤销，则回滚到事务开始状态
                if (!con.isClosed()) {
                    con.rollback();//当异常发生执行catch中SQLException时，记得要rollback(回滚)；
                    System.out.println("插入失败，回滚！");
                    con.setAutoCommit(true);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } finally {
            try {
                psql.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
