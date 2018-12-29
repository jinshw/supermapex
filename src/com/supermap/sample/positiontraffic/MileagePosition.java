package com.supermap.sample.positiontraffic;

import com.supermap.data.Geometry;
import com.supermap.sample.traffic.XmlBean;
import com.supermap.services.components.commontypes.GeometrySpatialAnalystResult;
import com.supermap.services.components.commontypes.Point;

import java.util.List;
import java.util.Map;

public interface MileagePosition {
    /**
     * 里程桩定线
     * @param map
     * @return
     */
    public List getFeaturesOfMileagePosition(Map<String, String> map);

    public GeometrySpatialAnalystResult locatePoint(Map<String, String> map);

    /**
     * 获取垂直点(组件实现)
     * @return
     */
    public List<XmlBean> getNearPointByiObjects(Geometry[] geometries);

    /**
     * 获取垂直点（iserver实现）
     * @return
     */
    public Point getNearPointByiServer();

    /**
     *  获取桩号对象列表
     * @param list
     * @return
     */
    public List getZHObjectList(List<XmlBean> list);
}
