package com.supermap.sample.traffic;

import java.sql.Timestamp;

/**
 * 拥堵路段信息
 */
public class TrafficInfoBean {
    private String id;
    // 实时路况路段ID
    private String tid = "";

    //起始位置距离路端终点的距离，单位米，默认整段 RTIC
    private Integer startlen;
    private Integer affectlen;
    //拥堵等级，0：未知，1：畅通，2：缓慢，3：拥堵
    private String los;
    //路段旅行时间
    private String traveltime;


    // 年报数据路段ID
    private String nid = "";
    //年报路线编码
    private String roadCode = "";
    private String roadName = "";

    private Double startzh;
    private Double endzh;
    private Double zhTemp;
    private String pointTemp;
    private String startPoint;
    private String endPoint;
    private String batch;
    private Timestamp createtime;

    private Double startPointX;
    private Double startPointY;
    private Double endPointX;
    private Double endPointY;


    public String getPointTemp() {
        return pointTemp;
    }

    public void setPointTemp(String pointTemp) {
        this.pointTemp = pointTemp;
    }

    public Double getZhTemp() {
        return zhTemp;
    }

    public void setZhTemp(Double zhTemp) {
        this.zhTemp = zhTemp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Integer getStartlen() {
        return startlen;
    }

    public void setStartlen(Integer startlen) {
        this.startlen = startlen;
    }

    public Integer getAffectlen() {
        return affectlen;
    }

    public void setAffectlen(Integer affectlen) {
        this.affectlen = affectlen;
    }

    public String getLos() {
        return los;
    }

    public void setLos(String los) {
        this.los = los;
    }

    public String getTraveltime() {
        return traveltime;
    }

    public void setTraveltime(String traveltime) {
        this.traveltime = traveltime;
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
}
