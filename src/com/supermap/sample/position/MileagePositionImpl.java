package com.supermap.sample.position;

import com.supermap.analyst.spatialanalyst.ComputeDistanceResult;
import com.supermap.analyst.spatialanalyst.ProximityAnalyst;
import com.supermap.data.*;
import com.supermap.data.Geometry;
import com.supermap.sample.traffic.TrafficInfoBean;
import com.supermap.sample.traffic.XmlBean;
import com.supermap.services.components.ComponentContext;
import com.supermap.services.components.ComponentContextAware;
import com.supermap.services.components.commontypes.*;
import com.supermap.services.components.commontypes.Feature;
import com.supermap.services.components.commontypes.GeometryType;
import com.supermap.services.components.commontypes.Point2D;
import com.supermap.services.components.commontypes.QueryParameter;
import com.supermap.services.components.spi.DataProvider;
import com.supermap.services.components.spi.MapProvider;
import com.supermap.services.components.spi.SpatialAnalystProvider;
import com.supermap.services.providers.UGCSpatialAnalystProvider;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class MileagePositionImpl implements MileagePosition, ComponentContextAware {
    private final static double MAX_DISTANCE = 0.001;
    private final static double MIN_DISTANCE = 0;

    private MapProvider mapProvider = null;
    private DataProvider dataProvider = null;
    private SpatialAnalystProvider spatialAnalystProvider = null;
    private MileagePositionProvider mileagePositionProvider = null;
    private MapParameter defaultMapParam = null;
    private String datasourceName = "";
    private String datasetName = "";
    private String datasetNameGD = "";
    private String datasetNameSD = "";

    /**
     * 组件查询配置信息
     */
    private String filePath = "";
    private String datasetGSObject = "";
    private String datasetGDObject = "";
    private String datasetSDObject = "";
    private String datasetRouteGSObject = "";
    private String datasetRouteGDObject = "";
    private String datasetRouteSDObject = "";

    private String orcldriver = "";
    private String orcljdbcurl = "";
    private String orcluser = "";
    private String orclpassword = "";


    /**
     * 根据坐标点获取桩号（组件实现）
     *
     * @param geometries 坐标点数组
     * @return list 里程桩号数据组
     */
    public List<XmlBean> getNearPointByiObjects(Geometry[] geometries) {
        //最近距离计算
        List computeDistanceResultList = getComputeDistanceResult(Arrays.copyOf(geometries, 20));
//        ComputeDistanceResult[] computeDistanceResult = getComputeDistanceResult(Arrays.copyOf(geometries, 20));

        List<XmlBean> list = new ArrayList<>();
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo workspaceConnectionInfo = new
                WorkspaceConnectionInfo();
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
//        String filePath = "C:\\Users\\Administrator\\Desktop\\Temp\\综合地图\\zhdt.smwu";
        workspaceConnectionInfo.setServer(filePath);
        workspace.open(workspaceConnectionInfo);
        Datasource datasource = workspace.getDatasources().get(7);
        DatasetVector dataset = (DatasetVector) datasource.getDatasets().get(datasetGSObject);
        DatasetVector datasetGS = (DatasetVector) datasource.getDatasets().get(datasetGSObject);
        DatasetVector datasetGD = (DatasetVector) datasource.getDatasets().get(datasetGDObject);
        DatasetVector datasetSD = (DatasetVector) datasource.getDatasets().get(datasetSDObject);
        DatasetVector datasetRoute = (DatasetVector) datasource.getDatasets().get(datasetRouteGSObject);
        DatasetVector datasetRouteGS = (DatasetVector) datasource.getDatasets().get(datasetRouteGSObject);
        DatasetVector datasetRouteGD = (DatasetVector) datasource.getDatasets().get(datasetRouteGDObject);
        DatasetVector datasetRouteSD = (DatasetVector) datasource.getDatasets().get(datasetRouteSDObject);
//        DatasetVector dataset = (DatasetVector) datasource.getDatasets().get("高速公路路段");
//        DatasetVector datasetRoute = (DatasetVector) datasource.getDatasets().get("Routes_GS_M");
        com.supermap.data.Recordset recordsetRoute = null;
        com.supermap.data.Feature featureRoute = null;
        com.supermap.data.GeoLineM geoLineM = null;
        Geometry geometry = null;

        com.supermap.data.Recordset recordset = null;
//        com.supermap.data.Recordset recordset = dataset.query("ROADCODE='G30'", com.supermap.data.CursorType.STATIC);
//        double x = 88.505;
//        double y = 43.1334326413204;
        //最新距离计算
//        Geometry[] geometries = {new GeoPoint(x, y)};0.025
//        ComputeDistanceResult[] computeDistanceResult = ProximityAnalyst.computeMinDistance(geometries, recordset, 0, 0.001);
//        System.out.println("geometries.length==" + geometries.length + "----computeDistanceResult.lenght===" + computeDistanceResult.length);
        XmlBean xmlBean = null;
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = tempDate.format(new java.util.Date());

        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        Iterator iterator = computeDistanceResultList.iterator();
        int i = -1;
        while (iterator.hasNext()) {
            i = i + 1;
            Map map = (Map) iterator.next();
            if (map == null || map.size() == 0) {
                continue;
            }
            xmlBean = new XmlBean();
            String type = (String) map.get("type");
            ComputeDistanceResult r = (ComputeDistanceResult) map.get("result");
            if (type.equals("gs")) {
                recordset = datasetGS.getRecordset(false, com.supermap.data.CursorType.STATIC);
                datasetRoute = datasetRouteGS;
            } else if (type.equals("gd")) {
                recordset = datasetGD.getRecordset(false, com.supermap.data.CursorType.STATIC);
                datasetRoute = datasetRouteGD;
            } else {
                recordset = datasetSD.getRecordset(false, com.supermap.data.CursorType.STATIC);
                datasetRoute = datasetRouteSD;
            }
            // 最近距离计算结果，线数据主键字段名称
            int idL = r.getReferenceGeometryIDs()[0];
            recordset.seekID(idL);
            geometry = recordset.getGeometry();
            // 获得线上最近点（垂足点）
//            com.supermap.data.Point2D point2D = new com.supermap.data.Point2D(x, y);
            com.supermap.data.Point2D point2D = geometries[i].getInnerPoint();
            com.supermap.data.Point2D tarPoint2D = Geometrist.nearestPointToVertex(point2D, geometry);

            // 查询路由数据集
            String id = (String) recordset.getFeature().getValue("ID");
            recordsetRoute = datasetRoute.query("ID='" + id + "'", com.supermap.data.CursorType.STATIC);
            featureRoute = recordsetRoute.getFeature();
            geoLineM = (com.supermap.data.GeoLineM) recordsetRoute.getGeometry();
            Double zh = geoLineM.getMAtPoint(tarPoint2D, 0.5, false);

            xmlBean.setNid((String) recordset.getFeature().getValue("ID"));
            xmlBean.setRoadCode((String) recordset.getFeature().getValue("ROADCODE"));
            xmlBean.setRoadName((String) recordset.getFeature().getValue("ROADNAME"));
            xmlBean.setBatch(datetime);
            xmlBean.setCreatetime(timestamp);
            xmlBean.setPointTemp(String.valueOf(point2D.getX()) + "," + String.valueOf(point2D.getY()));
            xmlBean.setZhTemp(zh);//
            list.add(xmlBean);
        }


        /*for (int i = 0; i < computeDistanceResult.length; i++) {
            xmlBean = new XmlBean();
            ComputeDistanceResult r = computeDistanceResult[i];
            // 最近距离计算结果，线数据主键字段名称
            int idL = r.getReferenceGeometryIDs()[0];
            recordset.seekID(idL);
            geometry = recordset.getGeometry();
            // 获得线上最近点（垂足点）
//            com.supermap.data.Point2D point2D = new com.supermap.data.Point2D(x, y);
            com.supermap.data.Point2D point2D = geometries[i].getInnerPoint();
            com.supermap.data.Point2D tarPoint2D = Geometrist.nearestPointToVertex(point2D, geometry);

            // 查询路由数据集
            String id = (String) recordset.getFeature().getValue("ID");
            recordsetRoute = datasetRoute.query("ID='" + id + "'", com.supermap.data.CursorType.STATIC);
            featureRoute = recordsetRoute.getFeature();
            geoLineM = (com.supermap.data.GeoLineM) recordsetRoute.getGeometry();
            Double zh = geoLineM.getMAtPoint(tarPoint2D, 0.5, false);
            System.out.println("zh===" + zh);

            xmlBean.setNid((String) recordset.getFeature().getValue("ID"));
            xmlBean.setRoadCode((String) recordset.getFeature().getValue("ROADCODE"));
            xmlBean.setRoadName((String) recordset.getFeature().getValue("ROADNAME"));
            xmlBean.setBatch(datetime);
            xmlBean.setCreatetime(timestamp);
            xmlBean.setPointTemp(String.valueOf(point2D.getX()) + "," + String.valueOf(point2D.getY()));
            xmlBean.setZhTemp(zh);//
            list.add(xmlBean);
        }*/

        // 关闭记录集，释放几何对象、特征要素和记录集。
        recordset.close();
        recordsetRoute.close();
        geometry.dispose();
        featureRoute.dispose();
        recordset.dispose();
        recordsetRoute.dispose();
        dataset.close();
        datasource.close();
        return list;
    }

    public Point getNearPointByiServer() {
        this.datasourceName = "THEMEDATASOURCE";
        this.datasetName = "高速公路路段";
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.name = this.datasetName;
        queryParameter.attributeFilter = "SmID = '2'";
        queryParameter.orderBy = "STARTZH ASC";
        List<Feature> fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        Feature feature;
        com.supermap.data.Point2D point2D = new com.supermap.data.Point2D(88.52095, 43.0975425243378);
        for (int i = 0; i < fList.size(); i++) {
            feature = fList.get(i);

        }

        ComputeMinDistanceParameterForGeometriesInput inputParam = new ComputeMinDistanceParameterForGeometriesInput();
        com.supermap.services.components.commontypes.Geometry geometry =
                new com.supermap.services.components.commontypes.Geometry();

        Point2D p = new Point2D(88.52095, 43.0975425243378);
        Point2D[] parrays = {p};
        geometry.type = GeometryType.POINT;
        geometry.points = parrays;
        com.supermap.services.components.commontypes.Geometry[] geometries = {geometry};
        inputParam.inputGeometries = geometries;
        com.supermap.services.components.commontypes.ComputeDistanceResult computeDistanceResult =
                spatialAnalystProvider.computeMinDistance(inputParam);
        return null;
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
        if (roadtype.equals("gs")) {
            queryParameter.name = this.datasetName;
        } else if (roadtype.equals("gd")) {
            queryParameter.name = this.datasetNameGD;
        } else if (roadtype.equals("sd")) {
            queryParameter.name = this.datasetNameSD;
        }

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

    public List<GeometrySpatialAnalystResult> getFeaturesOfMileagePosition(Map<String, String> map) {
        String roadtype = map.get("roadtype").trim();
        String roadcode = map.get("roadcode").trim();
        String startzh = map.get("startzh").trim();
        String endzh = map.get("endzh").trim();
        Double startzhD, endzhD;
        if ("".equals(startzh)) {
            startzhD = 0.0;
        } else {
            startzhD = Double.valueOf(startzh);
        }
        if ("".equals(endzh)) {
            endzhD = 0.0;
        } else {
            endzhD = Double.valueOf(endzh);
        }

        List<GeometrySpatialAnalystResult> rlist = new ArrayList<GeometrySpatialAnalystResult>();
        QueryParameter queryParameter = new QueryParameter();
        if (roadtype.equals("gs")) {
            queryParameter.name = this.datasetName;
        } else if (roadtype.equals("gd")) {
            queryParameter.name = this.datasetNameGD;
        } else if (roadtype.equals("sd")) {
            queryParameter.name = this.datasetNameSD;
        }

        queryParameter.attributeFilter = "ROADCODE = '" + roadcode + "'";
        queryParameter.orderBy = "STARTZH ASC";
        List<Feature> fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        Feature feature;
        GeometrySpatialAnalystResult geometrySpatialAnalystResult;
        for (int i = 0; i < fList.size(); i++) {
            feature = fList.get(i);
            geometrySpatialAnalystResult = spatialAnalystProvider.locateLine((Route) feature.geometry, startzhD, endzhD);
            if (geometrySpatialAnalystResult.resultGeometry != null) {
                rlist.add(geometrySpatialAnalystResult);
            }
        }

        return rlist;
    }

    @Override
    public void setComponentContext(ComponentContext context) {
        MileagePositionParam param = context.getConfig(MileagePositionParam.class);
        if (param == null) {
            throw new IllegalArgumentException("参数 MileagePositionParam 不能为空");
        }
        List<Object> providers = context.getProviders(Object.class);
        if (providers != null) {
            for (Object provider : providers) {
                if (provider instanceof MileagePositionProvider) {
                    this.mileagePositionProvider = (MileagePositionProvider) provider;
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
                    this.datasourceName = param.getDatasourceName();
                    this.datasetName = param.getDatasetName();
                    this.datasetNameGD = param.getDatasetNameGD();
                    this.datasetNameSD = param.getDatasetNameSD();
                    this.filePath = param.getFilePath();
                    this.datasetGSObject = param.getDatasetGSObject();
                    this.datasetGDObject = param.getDatasetGDObject();
                    this.datasetSDObject = param.getDatasetSDObject();
                    this.datasetRouteGSObject = param.getDatasetRouteGSObject();
                    this.datasetRouteGDObject = param.getDatasetRouteGDObject();
                    this.datasetRouteSDObject = param.getDatasetRouteSDObject();
                    this.orcldriver = param.getOrcldriver();
                    this.orcljdbcurl = param.getOrcljdbcurl();
                    this.orcluser = param.getOrcluser();
                    this.orclpassword = param.getOrclpassword();
                }
                if (provider instanceof SpatialAnalystProvider) {
                    this.spatialAnalystProvider = (SpatialAnalystProvider) provider;
                }
            }
        }
    }

    /**
     * 获取桩号对象列表
     *
     * @param list
     * @return
     */
    public List getZHObjectList(List<XmlBean> list) {
        Geometry[] geometries = this.toGeometries(list);
        List<XmlBean> zhList = this.getNearPointByiObjects(geometries);
        list = this.setZHList(zhList, list);
        return list;
    }

    /**
     * 获取TrafficInfoBean列表中的Point集合
     *
     * @param list
     * @return
     */
    public Geometry[] toGeometries(List<XmlBean> list) {
        Geometry[] geometries = new Geometry[4000];
        XmlBean xmlBean = null;
        Geometry geometry = null;
        int j = 0;
        for (int i = 0; i < list.size(); i++) {
            xmlBean = list.get(i);
            geometries[j] = new GeoPoint(xmlBean.getStartPointX(), xmlBean.getStartPointY());
            geometries[j + 1] = new GeoPoint(xmlBean.getEndPointX(), xmlBean.getEndPointY());
            j = j + 2;
        }
        return geometries;
    }

    /**
     * 设置桩号
     *
     * @param zhList 桩号列表
     * @param list   排序对象列表
     * @return 设置桩号的排序列表
     */
    public List<XmlBean> setZHList(List<XmlBean> zhList, List<XmlBean> list) {
        XmlBean xmlBean = null;
        int listSize = list.size();
        int zhListSize = zhList.size();
        try {
            if (listSize < zhListSize) {
                XmlBean startObj = null, endObj = null;
                for (int i = 0; i < list.size(); i++) {
                    xmlBean = list.get(i);
                    startObj = zhList.get(i * 2);
                    endObj = zhList.get(i * 2 + 1);

                    xmlBean.setStartzh(startObj.getZhTemp());
                    xmlBean.setEndzh(endObj.getZhTemp());
                    xmlBean.setNid(startObj.getNid());
                    xmlBean.setRoadCode(startObj.getRoadCode());
                    xmlBean.setRoadName(startObj.getRoadName());
                    xmlBean.setStartPoint(String.valueOf(startObj.getStartPointX()) + "," + String.valueOf(startObj.getStartPointY()));
                    xmlBean.setEndPoint(String.valueOf(startObj.getEndPointX()) + "," + String.valueOf(startObj.getEndPointY()));
                    xmlBean.setBatch(startObj.getBatch());
                    xmlBean.setCreatetime(startObj.getCreatetime());
                    xmlBean.setStartPoint(startObj.getPointTemp());
                    xmlBean.setEndPoint(endObj.getPointTemp());
//                trafficInfoBean.setStartzh(zhList.get(i * 2));
//                trafficInfoBean.setEndzh(zhList.get(i * 2 + 1));
                    list.set(i, xmlBean);
                }
            } else {
                list = null;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            list = null;
        } finally {
        }
        return list;
    }

    public List<Map> getComputeDistanceResult(Geometry[] geometries) {
        List<Map> list = new ArrayList<>();
        ComputeDistanceResult[] computeDistanceResult = new ComputeDistanceResult[20];

        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo workspaceConnectionInfo = new
                WorkspaceConnectionInfo();
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);
//        String file = "C:\\Users\\Administrator\\Desktop\\Temp\\综合地图\\zhdt.smwu";
        workspaceConnectionInfo.setServer(filePath);
        workspace.open(workspaceConnectionInfo);
        Datasource datasource = workspace.getDatasources().get(7);
        DatasetVector datasetGS = (DatasetVector) datasource.getDatasets().get(datasetGSObject);
        DatasetVector datasetGD = (DatasetVector) datasource.getDatasets().get(datasetGDObject);
        DatasetVector datasetSD = (DatasetVector) datasource.getDatasets().get(datasetSDObject);
//        DatasetVector datasetGS = (DatasetVector) datasource.getDatasets().get("高速公路路段");
//        DatasetVector datasetGD = (DatasetVector) datasource.getDatasets().get("国道路段");
//        DatasetVector datasetSD = (DatasetVector) datasource.getDatasets().get("省道路段");
        com.supermap.data.Recordset recordsetGS = datasetGS.getRecordset(false, com.supermap.data.CursorType.STATIC);
        com.supermap.data.Recordset recordsetGD = datasetGD.getRecordset(false, com.supermap.data.CursorType.STATIC);
        com.supermap.data.Recordset recordsetSD = datasetSD.getRecordset(false, com.supermap.data.CursorType.STATIC);
        ComputeDistanceResult[] results = null;
        Geometry geometry = null;
        for (int i = 0; i < geometries.length && geometries[i] != null; i++) {
            geometry = geometries[i];
            Geometry[] geometrys = {geometry};

            Map map = new HashMap();
            results = ProximityAnalyst.computeMinDistance(geometrys, recordsetGS, MIN_DISTANCE, MAX_DISTANCE);
            map.put("type", "gs");
            if (results != null && results.length > 0) {
                map.put("result", results[0]);
            } else {
                map.put("result", null);
            }

            if (results == null || results.length == 0) {
                results = ProximityAnalyst.computeMinDistance(geometrys, recordsetGD, MIN_DISTANCE, MAX_DISTANCE);
                map.put("type", "gd");
                if (results != null && results.length > 0) {
                    map.put("result", results[0]);
                } else {
                    map.put("result", null);
                }
            }
            /*else {//GS
                Map map = new HashMap();
                map.put("type", "gs");
                map.put("result", results[0]);
                list.add(map);
            }*/
            if (results == null || results.length == 0) {
                results = ProximityAnalyst.computeMinDistance(geometrys, recordsetSD, MIN_DISTANCE, MAX_DISTANCE);
                map.put("type", "sd");
                if (results != null && results.length > 0) {
                    map.put("result", results[0]);
                } else {
                    map.put("result", null);
                }
            }
            /*else {//gd
                Map map = new HashMap();
                map.put("type", "gd");
                map.put("result", results[0]);
                list.add(map);
            }*/
            if (results == null) {//gs\gd\sd 都没有去匹配上，设置为null
//                computeDistanceResult[i] = null;
                list.add(null);
            } else {//sd
//                Map map = new HashMap();
//                map.put("type", "sd");
//                map.put("result", results[0]);
                list.add(map);
//                computeDistanceResult[i] = results[0];
            }

//            geometry.dispose();
        }

        recordsetGS.close();
        recordsetGD.close();
        recordsetSD.close();
//        geometry.dispose();
        datasetGS.close();
        datasetGD.close();
        datasetSD.close();
        datasource.close();
        workspace.close();

        return list;
    }

    /**
     * 获取阻断信息列表
     *
     * @return
     */
    public List<RoadBlockedBean> getZDXXList() {
        List<RoadBlockedBean> list = getRoadBlockedDatas();
        list = getPointListByZH(list);
        return list;
    }

    /**
     * 获取一包数据中心阻断信息数据
     *
     * @return
     */
    public List<RoadBlockedBean> getRoadBlockedDatas() {
        List<RoadBlockedBean> list = new ArrayList<>();
        Connection conn = OrclDBUtil.getConn(this.orcldriver, this.orcljdbcurl, this.orcluser, this.orclpassword);
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String sysDateTime = DateUtils.date2Str(time_sdf);
        String sql = " select * from XJCX.ROADBLOCKED WHERE ENDTIME > ? and ?  > STARTTIME ORDER BY STARTTIME DESC";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sysDateTime);
            pstmt.setString(2, sysDateTime);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                RoadBlockedBean roadBlockedBean = new RoadBlockedBean();
                roadBlockedBean.setId(resultSet.getString("ID"));
                roadBlockedBean.setGldw(resultSet.getString("GLDW"));
                roadBlockedBean.setReportperson(resultSet.getString("REPORTPERSON"));
                roadBlockedBean.setConnphone(resultSet.getString("CONNPHONE"));
                roadBlockedBean.setFax(resultSet.getString("FAX"));
                roadBlockedBean.setRoadcode(resultSet.getString("ROADCODE"));
                roadBlockedBean.setRoadname(resultSet.getString("ROADNAME"));
                roadBlockedBean.setRoadstart(resultSet.getString("ROADSTART"));
                roadBlockedBean.setRoadend(resultSet.getString("ROADEND"));
                roadBlockedBean.setInfoname(resultSet.getString("INFONAME"));
                roadBlockedBean.setBlockedstart(resultSet.getString("BLOCKEDSTART"));
                roadBlockedBean.setBlockedend(resultSet.getString("BLOCKEDEND"));
                roadBlockedBean.setEnumcode(resultSet.getString("ENUMCODE"));
                roadBlockedBean.setEnumcontent(resultSet.getString("ENUMCONTENT"));
                roadBlockedBean.setReasoncode(resultSet.getString("REASONCODE"));
                roadBlockedBean.setReasoncontent(resultSet.getString("REASONCONTENT"));
                roadBlockedBean.setDirectioncode(resultSet.getString("DIRECTIONCODE"));
                roadBlockedBean.setDirection(resultSet.getString("DIRECTION"));
                roadBlockedBean.setTypecode(resultSet.getString("TYPECODE"));
                roadBlockedBean.setType(resultSet.getString("TYPE"));
                roadBlockedBean.setStarttime(resultSet.getString("STARTTIME"));
                roadBlockedBean.setEndtime(resultSet.getString("ENDTIME"));
                roadBlockedBean.setPubtime(resultSet.getString("PUBTIME"));
                roadBlockedBean.setUpdatetime(resultSet.getString("UPDATETIME"));
                roadBlockedBean.setDescription(resultSet.getString("DESCRIPTION"));
                roadBlockedBean.setIschecked(resultSet.getString("ISCHECKED"));
                roadBlockedBean.setEnumdetail(resultSet.getString("ENUMDETAIL"));
                roadBlockedBean.setYx(resultSet.getString("YX"));
                roadBlockedBean.setCitycode(resultSet.getString("CITYCODE"));
                roadBlockedBean.setCityname(resultSet.getString("CITYNAME"));
                roadBlockedBean.setYxcode(resultSet.getString("YXCODE"));
                roadBlockedBean.setF001(resultSet.getString("F001"));
                roadBlockedBean.setF002(resultSet.getString("F002"));
                roadBlockedBean.setF003(resultSet.getString("F003"));
                roadBlockedBean.setF004(resultSet.getString("F004"));
                roadBlockedBean.setF005(resultSet.getString("F005"));
                roadBlockedBean.setF006(resultSet.getString("F006"));
                roadBlockedBean.setF007(resultSet.getString("F007"));
                roadBlockedBean.setF008(resultSet.getString("F008"));
                roadBlockedBean.setF009(resultSet.getString("F009"));
                roadBlockedBean.setF010(resultSet.getString("F010"));
                roadBlockedBean.setIshebing(resultSet.getString("ISHEBING"));
                roadBlockedBean.setIsreportjtb(resultSet.getString("ISREPORTJTB"));
                roadBlockedBean.setIsreportyjxt(resultSet.getString("ISREPORTYJXT"));
                roadBlockedBean.setIscancel(resultSet.getString("ISCANCEL"));
                list.add(roadBlockedBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OrclDBUtil.close(resultSet);
            OrclDBUtil.close(pstmt);
            OrclDBUtil.close(conn);
        }
        System.out.println("oracle list size=" + list.size());
        return list;
    }

    public List<RoadBlockedBean> getPointListByZH(List<RoadBlockedBean> list) {
        List<RoadBlockedBean> rlist = new ArrayList<>();
        RoadBlockedBean roadBlockedBean = null;
        GeometrySpatialAnalystResult geometrySpatialAnalystResult = null;
        for (int i = 0; i < list.size(); i++) {
            roadBlockedBean = list.get(i);
            String roadCode = roadBlockedBean.getRoadcode();
            int roadCodeLen = roadCode.toString().length();
            roadCode = roadCode.substring(0, roadCodeLen - 6);
            Double blockedstart = Double.valueOf(roadBlockedBean.getBlockedstart());
            Double blockedend = Double.valueOf(roadBlockedBean.getBlockedend());
            Double roadZhPoint = (blockedstart + blockedend) / 2;

            // 根据roadcode 和 中心桩号roadZhPoint 获取终点坐标
            geometrySpatialAnalystResult = locatePointByZH(roadCode, roadZhPoint * 1000);
            if (geometrySpatialAnalystResult != null) {
                double x = geometrySpatialAnalystResult.resultGeometry.points[0].x;
                double y = geometrySpatialAnalystResult.resultGeometry.points[0].y;
                roadBlockedBean.setX(x);
                roadBlockedBean.setY(y);
            }
            list.set(i, roadBlockedBean);
        }
        return list;
    }

    /**
     * 根据桩号和线路编号查询点对象
     *
     * @param roadcode
     * @param zh
     * @return 点对象
     */
    public GeometrySpatialAnalystResult locatePointByZH(String roadcode, Double zh) {
        GeometrySpatialAnalystResult geometrySpatialAnalystResult = null;
        List<Feature> fList = null;
        Double measureD;
        measureD = Double.valueOf(zh);
//        if ("".equals(zh)) {
//            measureD = 0.0;
//        } else {
//            measureD = Double.valueOf(zh);
//        }
        List<GeometrySpatialAnalystResult> rlist = new ArrayList<GeometrySpatialAnalystResult>();
        QueryParameter queryParameter = new QueryParameter();
        queryParameter.attributeFilter = "ROADCODE = '" + roadcode + "' and STARTZH_M <= " + zh + " and ENDZH_M >= " + zh;
        queryParameter.orderBy = "STARTZH ASC";

        queryParameter.name = this.datasetName;//gs
        fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        if (fList == null || fList.size() == 0) {
            queryParameter.name = this.datasetNameGD;//gd
            fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        }
        if (fList == null || fList.size() == 0) {
            queryParameter.name = this.datasetNameSD;//sd
            fList = dataProvider.getFeature(this.datasourceName, queryParameter);
        }
        Feature feature;
        Double startzhD, endzhD;
//        for (int i = 0; i < fList.size(); i++) {
        if (fList != null && fList.size() > 0) {
//            geometrySpatialAnalystResult = null;
            feature = fList.get(0);
//            startzhD = Double.valueOf(feature.fieldValues[18]) * 1000;
//            endzhD = Double.valueOf(feature.fieldValues[21]) * 1000;
//            if (measureD >= Math.min(startzhD, endzhD) && measureD <= Math.max(startzhD, endzhD)) {
            geometrySpatialAnalystResult = spatialAnalystProvider.locatePoint((Route) feature.geometry, measureD, 0.0, true);
            return geometrySpatialAnalystResult;
//            }
        }
        return null;
    }
}
