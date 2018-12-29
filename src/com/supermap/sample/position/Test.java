package com.supermap.sample.position;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        String roadcode = "G217650200";
        int roadcodeLen = roadcode.toString().length();
        System.out.println(roadcodeLen);

        /*List<RoadBlockedBean> list = new ArrayList<>();
        Connection conn = OrclDBUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        String sql = "select * from XJCX.ROADBLOCKED";
        try {
            pstmt = conn.prepareStatement(sql);
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
        System.out.println(list.size());*/
    }


}
