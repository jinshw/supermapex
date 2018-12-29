package com.supermap.sample;

import com.supermap.sample.traffic.UUIDUtil;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySqlTest {

    public static void main(String[] args) {
//        querySQL();
        for (int i = 0; i < 100; i++) {
            insertSQL();
        }

    }

    public static void querySQL() {
        //声明Connection对象
        Connection con = null;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://localhost:3306/xjone";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "root";

        //加载驱动程序
        ResultSet rs = null;
        PreparedStatement psql = null;
        try {
            Class.forName(driver);

            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();

            //要执行的SQL语句
            String sql = "select * from traffic_info";
            //3.ResultSet类，用来存放获取的结果集！！

            rs = statement.executeQuery(sql);
            System.out.println("-----------------");
            System.out.println("执行结果如下所示:");
            System.out.println("-----------------");
            System.out.println("姓名" + "\t" + "职称");
            System.out.println("-----------------");

            String tid = null;
            String roadcode = null;
            while (rs.next()) {
                //获取stuname这列数据
                tid = rs.getString("tid");
                //获取stuid这列数据
                roadcode = rs.getString("roadcode");

                //输出结果
                System.out.println(tid + "\t" + roadcode);
            }

        } catch (Exception e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertSQL() {
        int flag = 0;
        //声明Connection对象
        Connection con = null;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://localhost:3306/xjone";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "root";

        //加载驱动程序
        PreparedStatement psql = null;
        try {
            Class.forName(driver);

            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
//            Statement statement = con.createStatement();
            psql = con.prepareStatement("insert into traffic_info(id,tid,startlen,affectlen,los,traveltime,nid,roadcode,roadname,startzh,endzh,startpoint,endpoinrt,batch,createtime) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            psql.setString(1, UUIDUtil.create32UpperUUID());
            psql.setString(2, "2222");
            psql.setInt(3, 12345);
            psql.setInt(4, 56789);
            psql.setString(5, "拥堵");
            psql.setString(6, "路段旅行时间");
            psql.setString(7, "年报矢量数据ID");
            psql.setString(8, "G30");
            psql.setString(9, "年报道路名称");
            psql.setDouble(10, 1234.098);
            psql.setDouble(11, 3242.243);
            psql.setString(12,"89.21432423423423,49.234234234234234");
            psql.setString(13,"90.21432423423423,56.234234234234234");
            psql.setString(14, "2018112012");
            java.util.Date date = new java.util.Date();
            Timestamp tt = new Timestamp(date.getTime());
            psql.setTimestamp(15, tt);

            flag = psql.executeUpdate();
            con.commit();
            System.out.println("flag===" + flag);
        } catch (Exception e) {
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
    }
}
