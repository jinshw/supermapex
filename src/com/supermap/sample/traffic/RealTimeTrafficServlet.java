package com.supermap.sample.traffic;

import com.alibaba.fastjson.JSONObject;
import com.supermap.sample.positiontraffic.MileagePosition;
import com.supermap.services.InterfaceContext;
import com.supermap.services.InterfaceContextAware;
import com.supermap.services.rest.util.JsonConverter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import static com.supermap.sample.traffic.CommodityServer.webSocketSet;

//@Interface(componentTypes = {MileagePosition.class,RealTimeTraffic.class}, optional = false, multiple = true)
public class RealTimeTrafficServlet extends HttpServlet implements InterfaceContextAware {
    //更新频率（秒）
//    private int m_period = 1000 * 30;
    private int m_period = 1000 * 60 * 5;
    private int count = 0;
    private boolean isExeCron = false;

    private String id = null;
    private RealTimeTraffic realTimeTraffic = null;
    private MileagePosition mileagePosition = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out;

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out = response.getWriter();

        String title = "实时路况定时服务";
        out.println("<html>");
        out.println("<head>");
        out.println("<TITLE>" + title + "</TITLE>");
        out.println("</head>");

        out.println("<body>");
        out.println("<p align=\"center\">" + "实时路况定时服务" + "</p>");
        out.println("<hr>");
        out.println("<form method=\"POST\" action=>");
        out.println("<p align=\"center\">");
        out.println("<select name=\"op\" id=\"op\">\n" +
                "        <option value=\"start\">执行定时器</option>\n" +
                "        <option value=\"end\">停止定时器</option>\n" +
                "    </select>");
        out.println("</p>");
        out.println("<p align=\"center\">");
        out.println("<label>");
        out.println("<input type=\"submit\" name=\"submit\" value=\"提交\">");
        out.println("</label>");
        out.println("<label>");
        out.println("<input type=\"reset\" name=\"reset\" value=\"重置\">");
        out.println("</label>");
        out.println("</p>");

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resultStr = "";
        String selectedVal = request.getParameter("op");

        List list = null;
        if (selectedVal.equals("start") && !isExeCron) {
            isExeCron = true;
            TimerManager.IS_EXE = false;
            exeCron();
            resultStr = "开始定时器执行";
        } else if (selectedVal.equals("end")) {
            isExeCron = false;
            TimerManager.IS_EXE = true;
            resultStr = "停止定时器执行";
        }

        JsonConverter js = new JsonConverter();
//        resultStr = js.toFormatedObject(list).toString();
        String cb = request.getParameter("callback");
        if (cb != null) {
            //jsonp格式处理，应对js跨域问题
            resultStr = cb + "(" + resultStr + ")";
            response.setContentType("text/javascript");
        } else {
            response.setContentType("application/x-json");
        }
        PrintWriter out = response.getWriter();
        out.write(resultStr);
        out.close();
    }

    @Override
    public void setInterfaceContext(InterfaceContext context) {
        this.getServletContext().setAttribute(this.id + "InterfaceContext", context);
        List<Object> components = context.getComponents(Object.class);
        if (components != null) {
            for (Object component : components) {
                if (component instanceof RealTimeTraffic) {
                    this.realTimeTraffic = (RealTimeTraffic) component;
                }
                if (component instanceof MileagePosition) {
                    this.mileagePosition = (MileagePosition) component;
                }
            }
        }
        // 执行定时器
        if(TimerManager.IS_EXE){
            TimerManager.IS_EXE = false;
            isExeCron = true;
            exeCron();
        }

    }

    public List<XmlBean> getTrafficList() {
        System.out.println("mileagePosition----" + mileagePosition);
        return mileagePosition.getZHObjectList(realTimeTraffic.getTrafficList());
    }

    // 定时器
    public void exeCron() {
        System.out.println("实时路况定时器开始执行......");
        new TimerManager(0, 0, 0, m_period, false, true, new TimerTask() {
            @Override
            public void run() {
//                try {
                count = count + 1;
                LogHelper.logByIserver("路况服务定时执行开始：" + new Date());
                System.out.println("路况服务定时执行" + count + "次开始：" + new Date());
                List<XmlBean> list = getTrafficList();
                if (list != null) {
                    System.out.println("insert list size = " + list.size());
                } else {
                    System.out.println("insert list size  is null ");
                }

                //websocket
                try {
                    for (CommodityServer webSocket : webSocketSet) {
                        com.supermap.services.rest.util.JsonConverter js = new JsonConverter();
                        String result = js.toFormatedObject(list).toString();
                        webSocket.sendMessage(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //保存数据库中
                realTimeTraffic.insertDatas(list);


                System.out.println("--路况服务定时执行" + count + "次结束：" + new Date());

                System.out.println("---IS_EXE---:" + TimerManager.IS_EXE);
                if (TimerManager.IS_EXE) {//关闭
                    this.cancel();
                }
//                    timerExe();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    timerExe();
//                }finally {
//                    System.out.println("---IS_EXE---:" + TimerManager.IS_EXE);
//                    if (!TimerManager.IS_EXE) {//关闭
//                        this.cancel();
//                    }
//                }

            }
        });
    }

    public void timerExe() {
        count = count + 1;
        LogHelper.logByIserver("路况服务定时执行开始：" + new Date());
        System.out.println("路况服务定时执行" + count + "次开始：" + new Date());
        List<XmlBean> list = getTrafficList();
        if (list != null) {
            realTimeTraffic.insertDatas(list);
        }
        System.out.println("--路况服务定时执行" + count + "次结束：" + new Date());
    }


    /**
     * 使用 Map按value进行排序
     */
    public static Map sortMapByValue(Map oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map sortedMap = new LinkedHashMap();
        List<Map.Entry> entryList = new ArrayList<Map.Entry>(
                oriMap.entrySet());
        Collections.sort(entryList, new MapValueComparator());

        Iterator<Map.Entry> iter = entryList.iterator();
        Map.Entry tmpEntry = null;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

    public static List<TrafficInfoBean> toTrafficInfoBean(Map map) {
        List<TrafficInfoBean> rlist = new ArrayList<TrafficInfoBean>();
        // 获取排序前N条记录
        Iterator entries = map.entrySet().iterator();
        TrafficInfoBean trafficInfoBean = null;
        Map tmap = null;
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            tmap = (Map) entry.getValue();
            String los = (String) tmap.get("los");
            String traveltime = (String) tmap.get("traveltime");
            String startlen = (String) tmap.get("startlen");
            trafficInfoBean = new TrafficInfoBean();
            trafficInfoBean.setTid(key);
            trafficInfoBean.setLos(los);
            trafficInfoBean.setTraveltime(traveltime);
            trafficInfoBean.setStartlen(Integer.valueOf(startlen));
            rlist.add(trafficInfoBean);
        }
        return rlist;
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
   /*
   public static int[] insertDatas(List<XmlBean> list) {
        int[] results = new int[50];
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
            psql = con.prepareStatement("insert into traffic_info(id,tid,startlen,affectlen,los,traveltime,nid,roadcode,roadname,startzh,endzh,startpoint,endpoinrt,batch,createtime,include) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            con.setAutoCommit(false);// 设置为不自动提交
            java.util.Date date = new java.util.Date();
            Timestamp tt = null;
            for (XmlBean xmlBean : list) {
                psql.setString(1, UUIDUtil.create32UpperUUID());
                psql.setString(2, xmlBean.getRticId());
                psql.setInt(3, Integer.valueOf(xmlBean.getFlowStartlen()));
                psql.setInt(4, Integer.valueOf(xmlBean.getFlowStartlen()));
                psql.setString(5, xmlBean.getFlowLos());
                psql.setString(6, xmlBean.getFlowTraveltime());
                psql.setString(7, xmlBean.getNid());
                psql.setString(8, xmlBean.getRoadCode());
                psql.setString(9, xmlBean.getRoadName());
                psql.setDouble(10, xmlBean.getStartzh());
                psql.setDouble(11, xmlBean.getEndzh());
                psql.setString(12, xmlBean.getStartPoint());
                psql.setString(13, xmlBean.getEndPoint());
                psql.setString(14, xmlBean.getBatch());
                tt = new Timestamp(date.getTime());
                psql.setTimestamp(15, tt);
                psql.setString(16, xmlBean.getInclude().toString());
                psql.addBatch();//添加对象批处理中
            }
            // 将一批参数提交给数据库来执行，如果全部命令执行成功，则返回更新计数组成的数组。
            results = psql.executeBatch();
            con.commit();
            System.out.println("提交成功!");
            con.setAutoCommit(true);//提交完成后回复现场将Auto commit,还原为true,


            System.out.println("flag===" + flag);
        } catch (Exception e) {
            try {
                // 若出现异常，对数据库中所有已完成的操作全部撤销，则回滚到事务开始状态
                if (!con.isClosed()) {
                    con.rollback();//当异常发生执行catch中SQLException时，记得要rollback(回滚)；
                    System.out.println("插入失败，回滚！");
                    con.setAutoCommit(true);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

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
        return results;
    }
    */
}

class MapValueComparator implements Comparator<Map.Entry> {

    @Override
    public int compare(Map.Entry me1, Map.Entry me2) {
        JSONObject jsonObject1 = (JSONObject) me1.getValue();
        JSONObject jsonObject2 = (JSONObject) me2.getValue();
        int s1 = Integer.valueOf(jsonObject1.getString("startlen"));
        int s2 = Integer.valueOf(jsonObject2.getString("startlen"));
        int returnInt = 0;
        if (s1 < s2) {
            returnInt = 1;
        } else if (s1 > s2) {
            returnInt = -1;
        } else {
            returnInt = 0;
        }

        return returnInt;
    }
}