package com.supermap.sample.traffic;

import com.supermap.services.components.commontypes.GeometrySpatialAnalystResult;

import java.util.List;
import java.util.Map;

public interface RealTimeTraffic {
    /**
     * 获取实时路况前N条数据（包括线段点）
     *
     * @param list
     * @return
     */
    public List<TrafficInfoBean> getTrafficTop(List<TrafficInfoBean> list, int max);

    public List getFeaturesOfMileagePosition(Map map);

    public GeometrySpatialAnalystResult locatePoint(Map<String, String> map);

    /**
     * 获取实时路况数据（老版本已经废弃）
     *
     * @param rticUrl   实时路况接口
     * @param areaCode  六位城市代码
     * @param kindLevel
     */
    public Map<String, Object> getRttByAdcodeJson(String rticUrl, String areaCode, String kindLevel);

    /**
     * 获取路网属性数据
     * @param list
     * @return
     */
    public List<XmlBean> getRAttributes(List<XmlBean> list);

    /**
     * 获取实时路况接口数据
     * @return
     */
    public List<XmlBean> getTrafficList();

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public  int[] insertDatas(List<XmlBean> list);
}
