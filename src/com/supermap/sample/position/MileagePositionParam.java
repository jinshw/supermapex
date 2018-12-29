package com.supermap.sample.position;

public class MileagePositionParam {
    private String mapName;

    private String datasourceName;
    private String datasetName;
    private String datasetNameGD;
    private String datasetNameSD;

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

    public String getOrcldriver() {
        return orcldriver;
    }

    public void setOrcldriver(String orcldriver) {
        this.orcldriver = orcldriver;
    }

    public String getOrcljdbcurl() {
        return orcljdbcurl;
    }

    public void setOrcljdbcurl(String orcljdbcurl) {
        this.orcljdbcurl = orcljdbcurl;
    }

    public String getOrcluser() {
        return orcluser;
    }

    public void setOrcluser(String orcluser) {
        this.orcluser = orcluser;
    }

    public String getOrclpassword() {
        return orclpassword;
    }

    public void setOrclpassword(String orclpassword) {
        this.orclpassword = orclpassword;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDatasetGSObject() {
        return datasetGSObject;
    }

    public void setDatasetGSObject(String datasetGSObject) {
        this.datasetGSObject = datasetGSObject;
    }

    public String getDatasetGDObject() {
        return datasetGDObject;
    }

    public void setDatasetGDObject(String datasetGDObject) {
        this.datasetGDObject = datasetGDObject;
    }

    public String getDatasetSDObject() {
        return datasetSDObject;
    }

    public void setDatasetSDObject(String datasetSDObject) {
        this.datasetSDObject = datasetSDObject;
    }

    public String getDatasetRouteGSObject() {
        return datasetRouteGSObject;
    }

    public void setDatasetRouteGSObject(String datasetRouteGSObject) {
        this.datasetRouteGSObject = datasetRouteGSObject;
    }

    public String getDatasetRouteGDObject() {
        return datasetRouteGDObject;
    }

    public void setDatasetRouteGDObject(String datasetRouteGDObject) {
        this.datasetRouteGDObject = datasetRouteGDObject;
    }

    public String getDatasetRouteSDObject() {
        return datasetRouteSDObject;
    }

    public void setDatasetRouteSDObject(String datasetRouteSDObject) {
        this.datasetRouteSDObject = datasetRouteSDObject;
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
