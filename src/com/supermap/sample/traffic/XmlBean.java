package com.supermap.sample.traffic;

import com.alibaba.fastjson.JSONArray;

import java.sql.Timestamp;

public class XmlBean {
    /**
     * 实时路况接口返回数据
     */
    private String code = "";
    private String rticId = "";
    private String kind = "";
    private String length = "";
    private String clazz = "";
    private String flowStartlen = "";
    private String flowTraveltime = "";
    private String flowLos = "";
    private String sectionStartlen = "";
    private String sectionDegreelen = "";
    private String sectionLos = "";

    /**
     * 路况和路网关系字段
     */
    private String rticLinkKind = "";//路况接口返回ID
    private String rticLinkID = "";
    private String niLinkID = "";// 路网矢量数据ID
    private String niLinkLength = "";
    private JSONArray include = null;//路网ID和长度json数组

    /**
     * 路网数据
     */
    private String linkId = "";
    private String direction = "";//通行方向 0:未调查 1:双向 2:顺方向 3:逆方向
    private String snodeID = "";//画线方向起点号码
    private String enodeID = "";//画线方向终点号码
    private String rLengt = "";//道路 link 长度单位“km”保留到小数点后 3 位
    private Double startPointX;
    private Double startPointY;
    private Double endPointX;
    private Double endPointY;
    /**
     * 扩展
     */
    private String startNiLinkID = "";
    private String endNiLinkID = "";


    /**
     * 年报
     */
    private String nid = "";// 年报数据路段ID
    private String roadCode = "";//年报路线编码
    private String roadName = "";//年报路线名称
    private Double startzh;//起始桩号
    private Double endzh;//终点桩号
    private Double zhTemp;
    private String pointTemp;
    private String startPoint;//映射到年报底图的起始点 x,y
    private String endPoint; // 映射到年报底图的终点x,y
    private String batch;// 批次
    private Timestamp createtime;
    private String startpoint_nb;
    private String endpoint_nb;


    public String getStartpoint_nb() {
        return startpoint_nb;
    }

    public void setStartpoint_nb(String startpoint_nb) {
        this.startpoint_nb = startpoint_nb;
    }

    public String getEndpoint_nb() {
        return endpoint_nb;
    }

    public void setEndpoint_nb(String endpoint_nb) {
        this.endpoint_nb = endpoint_nb;
    }

    public String getStartNiLinkID() {
        return startNiLinkID;
    }

    public void setStartNiLinkID(String startNiLinkID) {
        this.startNiLinkID = startNiLinkID;
    }

    public String getEndNiLinkID() {
        return endNiLinkID;
    }

    public void setEndNiLinkID(String endNiLinkID) {
        this.endNiLinkID = endNiLinkID;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getRoadCode() {
        return roadCode;
    }

    public void setRoadCode(String roadCode) {
        this.roadCode = roadCode;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public Double getStartzh() {
        return startzh;
    }

    public void setStartzh(Double startzh) {
        this.startzh = startzh;
    }

    public Double getEndzh() {
        return endzh;
    }

    public void setEndzh(Double endzh) {
        this.endzh = endzh;
    }

    public Double getZhTemp() {
        return zhTemp;
    }

    public void setZhTemp(Double zhTemp) {
        this.zhTemp = zhTemp;
    }

    public String getPointTemp() {
        return pointTemp;
    }

    public void setPointTemp(String pointTemp) {
        this.pointTemp = pointTemp;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public Double getStartPointX() {
        return startPointX;
    }

    public void setStartPointX(Double startPointX) {
        this.startPointX = startPointX;
    }

    public Double getStartPointY() {
        return startPointY;
    }

    public void setStartPointY(Double startPointY) {
        this.startPointY = startPointY;
    }

    public Double getEndPointX() {
        return endPointX;
    }

    public void setEndPointX(Double endPointX) {
        this.endPointX = endPointX;
    }

    public Double getEndPointY() {
        return endPointY;
    }

    public void setEndPointY(Double endPointY) {
        this.endPointY = endPointY;
    }

    public JSONArray getInclude() {
        return include;
    }

    public void setInclude(JSONArray include) {
        this.include = include;
    }

    public String getRticLinkKind() {
        return rticLinkKind;
    }

    public void setRticLinkKind(String rticLinkKind) {
        this.rticLinkKind = rticLinkKind;
    }

    public String getRticLinkID() {
        return rticLinkID;
    }

    public void setRticLinkID(String rticLinkID) {
        this.rticLinkID = rticLinkID;
    }

    public String getNiLinkID() {
        return niLinkID;
    }

    public void setNiLinkID(String niLinkID) {
        this.niLinkID = niLinkID;
    }

    public String getNiLinkLength() {
        return niLinkLength;
    }

    public void setNiLinkLength(String niLinkLength) {
        this.niLinkLength = niLinkLength;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getrLengt() {
        return rLengt;
    }

    public void setrLengt(String rLengt) {
        this.rLengt = rLengt;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSnodeID() {
        return snodeID;
    }

    public void setSnodeID(String snodeID) {
        this.snodeID = snodeID;
    }

    public String getEnodeID() {
        return enodeID;
    }

    public void setEnodeID(String enodeID) {
        this.enodeID = enodeID;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getRticId() {
        return rticId;
    }

    public void setRticId(String rticId) {
        this.rticId = rticId;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getFlowStartlen() {
        return flowStartlen;
    }

    public void setFlowStartlen(String flowStartlen) {
        this.flowStartlen = flowStartlen;
    }

    public String getFlowTraveltime() {
        return flowTraveltime;
    }

    public void setFlowTraveltime(String flowTraveltime) {
        this.flowTraveltime = flowTraveltime;
    }

    public String getFlowLos() {
        return flowLos;
    }

    public void setFlowLos(String flowLos) {
        this.flowLos = flowLos;
    }

    public String getSectionStartlen() {
        return sectionStartlen;
    }

    public void setSectionStartlen(String sectionStartlen) {
        this.sectionStartlen = sectionStartlen;
    }

    public String getSectionDegreelen() {
        return sectionDegreelen;
    }

    public void setSectionDegreelen(String sectionDegreelen) {
        this.sectionDegreelen = sectionDegreelen;
    }

    public String getSectionLos() {
        return sectionLos;
    }

    public void setSectionLos(String sectionLos) {
        this.sectionLos = sectionLos;
    }
}
