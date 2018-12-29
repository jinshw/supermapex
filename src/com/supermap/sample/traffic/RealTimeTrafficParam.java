package com.supermap.sample.traffic;

public class RealTimeTrafficParam {
    private String workspacePath;
    private String mapName;
    private String datasourceName;
    private String datasetName;
    private String datasetNameGD;
    private String datasetNameSD;
    //道路数据集颜色字段
    private String colorFieldName = "";
    //道路数据集编码字段
    private String IDFieldName = "";
    //地图名称，多个以逗号分隔
    private String mapNames = "";
    //更新间隔
    private int period = 1000 * 60 * 5;

    private String areaCodes = "";
    private String rticUrl = "";
    private String trafficColors = "";

    private String roadKind = "";
    private String max = "";

    /**
     * MySql 数据库配置
     */
    private String mysqlDriver = "";
    private String mysqlURL = "";
    private String mysqlUser = "";
    private String mysqlPassword = "";

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    public String getColorFieldName() {
        return colorFieldName;
    }

    public void setColorFieldName(String colorFieldName) {
        this.colorFieldName = colorFieldName;
    }

    public String getIDFieldName() {
        return IDFieldName;
    }

    public void setIDFieldName(String IDFieldName) {
        this.IDFieldName = IDFieldName;
    }

    public String getMapNames() {
        return mapNames;
    }

    public void setMapNames(String mapNames) {
        this.mapNames = mapNames;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getTrafficColors() {
        return trafficColors;
    }

    public void setTrafficColors(String trafficColors) {
        this.trafficColors = trafficColors;
    }

    public String getMysqlDriver() {
        return mysqlDriver;
    }

    public void setMysqlDriver(String mysqlDriver) {
        this.mysqlDriver = mysqlDriver;
    }

    public String getMysqlURL() {
        return mysqlURL;
    }

    public void setMysqlURL(String mysqlURL) {
        this.mysqlURL = mysqlURL;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public void setMysqlUser(String mysqlUser) {
        this.mysqlUser = mysqlUser;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }

    public String getAreaCodes() {
        return areaCodes;
    }

    public void setAreaCodes(String areaCodes) {
        this.areaCodes = areaCodes;
    }

    public String getRticUrl() {
        return rticUrl;
    }

    public void setRticUrl(String rticUrl) {
        this.rticUrl = rticUrl;
    }

    public String getRoadKind() {
        return roadKind;
    }

    public void setRoadKind(String roadKind) {
        this.roadKind = roadKind;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasetNameGD() {
        return datasetNameGD;
    }

    public void setDatasetNameGD(String datasetNameGD) {
        this.datasetNameGD = datasetNameGD;
    }

    public String getDatasetNameSD() {
        return datasetNameSD;
    }

    public void setDatasetNameSD(String datasetNameSD) {
        this.datasetNameSD = datasetNameSD;
    }
}
