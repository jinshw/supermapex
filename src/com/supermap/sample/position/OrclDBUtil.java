package com.supermap.sample.position;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrclDBUtil {

    private static final String driverClass = "oracle.jdbc.driver.OracleDriver";
    private static final String jdbcUrl = "jdbc:oracle:thin:@192.168.47.128:1521:helowin";
    private static final String user = "SJZX_ODS";
    private static final String password = "SJZX_ODS";

    public static Connection getConn() {
        // 1.注册驱动
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 2.创建Connection(数据库连接对象)
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, user, password);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
         * Connection是Statement的工厂，一个Connection可以生产多个Statement。
         * Statement是ResultSet的工厂，一个Statement却只能对应一个ResultSet（它们是一一对应的关系）。
         * 所以在一段程序里要用多个ResultSet的时候，必须再Connection中获得多个Statement，然后一个Statement对应一个ResultSet。
         */
        return null;
    }

    public static Connection getConn(String driverClass,String jdbcUrl,String user,String password) {
        // 1.注册驱动
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 2.创建Connection(数据库连接对象)
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, user, password);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*
         * Connection是Statement的工厂，一个Connection可以生产多个Statement。
         * Statement是ResultSet的工厂，一个Statement却只能对应一个ResultSet（它们是一一对应的关系）。
         * 所以在一段程序里要用多个ResultSet的时候，必须再Connection中获得多个Statement，然后一个Statement对应一个ResultSet。
         */
        return null;
    }

    /**
     * 关闭连接(数据库连接对象)
     *
     * @param conn
     */
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭编译的 SQL 语句的对象
     *
     * @param stmt
     */
    public static void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭结果集
     *
     * @param rs
     */
    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     *
     * @param conn
     */
    public static void commit(Connection conn) {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回滚事务
     *
     * @param conn
     */
    public static void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
